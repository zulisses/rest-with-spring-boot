package com.github.zulisses.res_with_spring_boot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.zulisses.res_with_spring_boot.config.EmailConfig;
import com.github.zulisses.res_with_spring_boot.data.dto.v1.request.EmailRequestDTO;
import com.github.zulisses.res_with_spring_boot.mail.EmailSender;

@Service 
public class EmailService {

    @Autowired
    private EmailSender emailSender;

    @Autowired
    private EmailConfig emailConfig;

    public void sendSimpleEmail(EmailRequestDTO emailRequest) {
        emailSender.to(emailRequest.getTo()).withSubject(emailRequest.getSubject()).withMessage(emailRequest.getMessage()).send(emailConfig);
    }
}
