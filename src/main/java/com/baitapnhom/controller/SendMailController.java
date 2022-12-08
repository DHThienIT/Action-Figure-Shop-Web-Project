package com.baitapnhom.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;

import com.baitapnhom.entity.KhuyenMai;
import com.baitapnhom.entity.User;
import com.baitapnhom.renpository.KhuyenmaiRepository;
import com.baitapnhom.renpository.UserRepository;
import com.baitapnhom.service.EmailService;

public class SendMailController extends Thread{
    
    private Thread t;
    private  String id;
    private List<User> listuser;
    @Autowired 
    KhuyenmaiRepository khuyenmaiRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private JavaMailSender mailSender;
  
    public SendMailController(String id, List<User> listuser) {
        super();
        this.id = id;
        this.listuser = listuser;
    }


    @Override
    public void  run() {
      
      
            // Let the thread sleep for a while.
            try {
               String [] a={"thuy","jkdjkd","dkdkđ"};
              
              for(User users : listuser) {
                 send(users.getEmail());
                Thread.sleep(10);
                }
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            

    }
    public void send(String users) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("fromemail@gmail.com");
        message.setTo(users);
        message.setText("Xác thực email");
        message.setSubject("Xin chào quý khách rất vui được phu mụ quý khách . Mã xác nhận :");
        mailSender.send(message);
        System.out.println("Mail Send...");
    }
 
   
    public void start() {
       
        if (t == null) {
            t = new Thread(this,id);
            t.start();
        }
    }
}
