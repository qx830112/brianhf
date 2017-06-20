package com.qingxin.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.qingxin.db.DummyDB;
import com.qingxin.user.bean.User;
import com.qingxin.user.exception.CreateConflictException;
import com.qingxin.user.exception.UserNotFoundException;

public class UserService {
	
	Map<String, User> users = DummyDB.getUsers();
	
	static UserService userService = new UserService();
	
	private UserService(){	
	}
	
	public static synchronized UserService getInstance(){
		return userService;
	}
	
	public boolean create(User user1, User user2) throws UserNotFoundException, CreateConflictException {
		
		User u1 = users.get(user1.getMailAddress());
		User u2 = users.get(user2.getMailAddress());
		
		if (u1 == null || u2 == null) {
			throw new UserNotFoundException();
		}
		
		List<String> blockerList1 = u1.getBlockers().stream().map(User::getMailAddress).collect(Collectors.toList());
		List<String> blockerList2 = u2.getBlockers().stream().map(User::getMailAddress).collect(Collectors.toList());
		if (blockerList1.contains(u2.getMailAddress()) || blockerList2.contains(u1.getMailAddress())) {
			throw new CreateConflictException();
		}
		
		List<String> mailAddressList1 = u1.getFriends().stream().map(User::getMailAddress).collect(Collectors.toList());
		if (!mailAddressList1.contains(u2.getMailAddress())) {
			u1.getFriends().add(u2);
		}
		List<String> mailAddressList2 = u2.getFriends().stream().map(User::getMailAddress).collect(Collectors.toList());
		if (!mailAddressList2.contains(u1.getMailAddress())) {
			u2.getFriends().add(u1);
		}
		return true;
	}
	
	
	public List<String> getFriends(User user) throws UserNotFoundException {
		User u = users.get(user.getMailAddress());
		if (u == null) {
			throw new UserNotFoundException();
		}
		return u.getFriends().stream().map(User::getMailAddress).collect(Collectors.toList());
	}
	
	public List<String> getCommonFriends(List<String> friends1, List<String> friends2) {
		friends1.retainAll(friends2);
		return friends1;
	}
	
	public boolean subscribe(User requestor, User target) throws UserNotFoundException {

		User u1 = users.get(requestor.getMailAddress());
		User u2 = users.get(target.getMailAddress());

		if (u1 == null || u2 == null) {
			throw new UserNotFoundException();
		}

		List<String> mailAddressList = u2.getObservers().stream().map(User::getMailAddress).collect(Collectors.toList());
		if (!mailAddressList.contains(u1.getMailAddress())) {
			u2.getObservers().add(u1);
		}
		return true;
	}
	
	public boolean block(User requestor, User target) throws UserNotFoundException {
		User req = users.get(requestor.getMailAddress());
		User tar = users.get(target.getMailAddress());

		if (req == null || tar == null) {
			throw new UserNotFoundException();
		}
		List<String> reqFriendList = req.getFriends().stream().map(User::getMailAddress).collect(Collectors.toList());
		List<String> tarFriendList = tar.getFriends().stream().map(User::getMailAddress).collect(Collectors.toList());
		
		if (reqFriendList.contains(tar.getMailAddress()) || 
				tarFriendList.contains(req.getMailAddress())) {

			for (int i = 0; i < tar.getObservers().size(); i++) {
				User u = tar.getObservers().get(i);
				if (u.getMailAddress().equals(req.getMailAddress())) {
					tar.getObservers().remove(i);
					break;
				}
			}
		}else{
			List<String> blokerList = req.getBlockers().stream().map(User::getMailAddress).collect(Collectors.toList());
			if (!blokerList.contains(tar.getMailAddress())) {
				req.getBlockers().add(tar);
			}
		}
		return true;
	}

	
	public Set<String> getRecipients(User user, String text) throws UserNotFoundException {
		User u = users.get(user.getMailAddress());
		if (u == null) {
			throw new UserNotFoundException();
		}
		Set<String> recipients = new TreeSet<>();
		
		List<String> observers = u.getObservers().stream().map(User::getMailAddress).collect(Collectors.toList());
		List<String> friends = u.getFriends().stream().map(User::getMailAddress).collect(Collectors.toList());
		List<String> mentions = getMentionedInfo(text);
		List<String> blockersList = u.getBlockers().stream().map(User::getMailAddress).collect(Collectors.toList());
		
		recipients.addAll(observers);
		recipients.addAll(friends);
		recipients.addAll(mentions);
		recipients.removeAll(blockersList);
		
		return recipients;
	}
	
	private List<String> getMentionedInfo(String text){
		List<String> list = new ArrayList<>();
		String reg1 = "[a-zA-Z0-9_]+@[a-zA-Z0-9]+(\\.[a-zA-Z]+){1,3}";
		Pattern p=Pattern.compile(reg1);
		Matcher m=p.matcher(text);
		while(m.find()){
			list.add(m.group());
		}
		return list;
	}
}
