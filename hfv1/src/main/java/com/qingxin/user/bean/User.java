package com.qingxin.user.bean;

import java.util.ArrayList;
import java.util.List;

public class User {
	private String mailAddress;
	private List<User> friends = new ArrayList<User>();
	
	public User(){
		
	}
	public User(String mailAddress) {
		this.mailAddress = mailAddress;
	}
	
	public String getMailAddress() {
		return mailAddress;
	}
	public void setMailAddress(String mailAddress) {
		this.mailAddress = mailAddress;
	}
	public List<User> getFriends() {
		return friends;
	}
	public void setFriends(List<User> friends) {
		this.friends = friends;
	}
	
	
}
