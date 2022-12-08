package com.baitapnhom.service;


import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.baitapnhom.entity.Mail;
 
@Service
public class EmailServices {
    
    private final JavaMailSender mailSender;
    
    @Autowired
    public EmailServices(JavaMailSender javamailSender) {
        this.mailSender = javamailSender;
    }
    
    public void sendHTMLEmail(Mail message) throws MessagingException {
        MimeMessage emailMessage = mailSender.createMimeMessage();
        MimeMessageHelper mailBuilder = new MimeMessageHelper(emailMessage, true);
        mailBuilder.setTo(message.getMailTo());
        mailBuilder.setFrom(message.getMailFrom());         
        mailBuilder.setText(message.getMailContent(), true);
        mailBuilder.setSubject(message.getMailSubject());
        mailSender.send(emailMessage);
    }
}