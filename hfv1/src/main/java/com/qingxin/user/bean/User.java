package com.qingxin.user.bean;

import java.util.ArrayList;
import java.util.List;

public class User {
	String mailAddress;
	List<User> friends = new ArrayList<User>();
}
