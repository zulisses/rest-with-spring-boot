package com.github.zulisses.res_with_spring_boot.services;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    public void sendEmailWithAttachment(String emailRequestJson, MultipartFile attachment) {
        File tempFile = null;
        try {
            EmailRequestDTO emailRequest = new ObjectMapper().readValue(emailRequestJson, EmailRequestDTO.class);
            tempFile = File.createTempFile("attachment", attachment.getOriginalFilename());
            attachment.transferTo(tempFile);

            emailSender.to(emailRequest.getTo()).withSubject(emailRequest.getSubject()).withMessage(emailRequest.getMessage()).attach(tempFile.getAbsolutePath()).send(emailConfig);

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error parsing email request", e);
        } catch (IOException e) {
            throw new RuntimeException("Error processing the attachment", e);
        } finally {
            if (tempFile != null && tempFile.exists()) tempFile.delete();
        }
    }
}
