package com.qingxin.db;

import java.util.HashMap;
import java.util.Map;

import com.qingxin.user.bean.User;

public class DummyDB {
	
	private DummyDB(){
	}
	
	public static Map<String,User> getUsers(){
		Map<String,User> users = new HashMap<>();
		
		User andy = new User("andy@example.com");
		User john = new User("john@example.com");
		User tony = new User("tony@example.com");
		User lisa = new User("lisa@example.com");
		
		users.put("andy@example.com", andy);
		users.put("john@example.com", john);
		users.put("tony@example.com", tony);
		users.put("lisa@example.com", lisa);
		
		
		andy.getFriends().add(john);
		andy.getFriends().add(tony);
		john.getFriends().add(tony);
		
		return users;
	}
}
