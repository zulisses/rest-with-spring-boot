package com.github.zulisses.res_with_spring_boot.controllers;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.zulisses.res_with_spring_boot.data.dto.v1.security.AccountCredentialsDTO;
import com.github.zulisses.res_with_spring_boot.data.dto.v1.security.TokenDTO;
import com.github.zulisses.res_with_spring_boot.services.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController 
@RequestMapping("/auth")
@Tag(name = "Authentication Endpoint", description = "Authentication API")
public class AuthController {

    @Autowired
    private AuthService service;

    @Operation(summary = "Authenticates a user and returns a token")
    @PostMapping("/signin")
    public ResponseEntity<TokenDTO> signIn(@RequestBody AccountCredentialsDTO credentials) {
        if(credentials == null || StringUtils.isBlank(credentials.getUsername()) || StringUtils.isBlank(credentials.getPassword())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        var token = service.signIn(credentials);
        if(token == null) ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        return ResponseEntity.ok().body(token);
    }
}
