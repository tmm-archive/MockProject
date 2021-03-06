package com.java.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.java.dao.UserDao;
import com.java.pojo.User;

@Component
public class UserManager {

	@Autowired
	UserDao userdao;
	
	public User getUserDetails(String userId) {
		return userdao.getUserDetails(userId);
	}

	public void setUserDetails(User user) {
		userdao.saveDetails(user);
	}
	
}
