package com.baitapnhom.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baitapnhom.entity.User;
import com.baitapnhom.renpository.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    
    public void save(User user) {
        userRepository.save(user);
    }
    public Optional<User> finByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    public boolean checkLogin(String username, String password) {
        Optional<User> userOptional = finByUsername(username);
        if(userOptional.isPresent() && userOptional.get().getPassword().equals(password)) {
            System.out.println("Bạn đã đăng nhập thành công");
            return true;
        }
        return false;
    } 
    
    public boolean ktraTaiKhoan(String username) {
        Optional<User> userOptional = finByUsername(username);
        if(userOptional.isPresent())
            return true;
        return false;
    }



   }
