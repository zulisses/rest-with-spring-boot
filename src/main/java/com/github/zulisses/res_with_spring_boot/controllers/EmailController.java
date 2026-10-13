package com.github.zulisses.res_with_spring_boot.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.github.zulisses.res_with_spring_boot.controllers.docs.EmailControllerDocs;
import com.github.zulisses.res_with_spring_boot.data.dto.v1.request.EmailRequestDTO;
import com.github.zulisses.res_with_spring_boot.services.EmailService;

@RestController
@RequestMapping(value = "/api/email/v1")
public class EmailController implements EmailControllerDocs{

    @Autowired
    private EmailService emailService;

    @PostMapping()
    @Override
    public ResponseEntity<String> sendEmail(@RequestBody EmailRequestDTO emailRequest) {
        emailService.sendSimpleEmail(emailRequest);
        return new ResponseEntity<>("Email sent successfully", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> sendEmailWithAttachment(String emailRequestJson, MultipartFile multipartFile) {
        return null;
    }
}
