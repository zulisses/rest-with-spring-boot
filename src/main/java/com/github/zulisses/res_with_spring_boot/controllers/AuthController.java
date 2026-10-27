package com.github.zulisses.res_with_spring_boot.controllers;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
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

    @Operation(summary = "Refresh a token for a authenticated user and returns a new token")
    @PutMapping("/refresh/{username}")
    public ResponseEntity<TokenDTO> refreshToken(@PathVariable("username") String username, @RequestHeader("Authorization") String refreshToken) {
        if(StringUtils.isBlank(username) || StringUtils.isBlank(refreshToken)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        var token = service.refreshToken(username, refreshToken);
        if(token == null) ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        return ResponseEntity.ok().body(token);
    }
}
