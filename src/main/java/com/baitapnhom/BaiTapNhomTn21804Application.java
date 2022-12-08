package com.baitapnhom;


import java.util.List;

import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;

import com.baitapnhom.controller.SendMailController;
import com.baitapnhom.controller.ThreadController;
import com.baitapnhom.renpository.UserRepository;



@SpringBootApplication

public class BaiTapNhomTn21804Application {

	public static void main(String[] args) {
	    
		SpringApplication.run(BaiTapNhomTn21804Application.class, args);
	
		
	}

}
