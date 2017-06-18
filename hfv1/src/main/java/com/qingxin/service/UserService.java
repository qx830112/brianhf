package com.qingxin.service;

import java.util.List;
import java.util.stream.Collectors;

import com.qingxin.user.bean.User;

public class UserService {
	
	static UserService userService = new UserService();
	
	private UserService(){
		
	}
	
	public static synchronized UserService getInstance(){
		return userService;
	}
	
	public boolean create(User u1, User u2) {
		List<String> mailAddressList = u1.getFriends().stream().map(User::getMailAddress).collect(Collectors.toList());
		if(!mailAddressList.contains(u2.getMailAddress())){
			u1.getFriends().add(u2);
		}
		return true;
	}
}
