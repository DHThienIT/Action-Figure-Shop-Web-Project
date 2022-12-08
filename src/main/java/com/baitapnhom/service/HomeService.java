package com.baitapnhom.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baitapnhom.entity.User;
import com.baitapnhom.renpository.HomeRepository;


@Service
public class HomeService {
	@Autowired
	private HomeRepository homeRepository;
	
	public User saveUser(User user) {
		return homeRepository.save(user);
	}
	public List<User> getAllUsers() {
		return homeRepository.findAll();
	}
}
