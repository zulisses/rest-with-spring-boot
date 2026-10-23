package com.github.zulisses.res_with_spring_boot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.github.zulisses.res_with_spring_boot.data.dto.v1.security.AccountCredentialsDTO;
import com.github.zulisses.res_with_spring_boot.data.dto.v1.security.TokenDTO;
import com.github.zulisses.res_with_spring_boot.repository.UserRepository;
import com.github.zulisses.res_with_spring_boot.security.jwt.JwtTokenProvider;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserRepository repository;


    public ResponseEntity<TokenDTO> signIn(AccountCredentialsDTO credentials) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(credentials.getUsername(), credentials.getPassword()));

        var user = repository.findByUserName(credentials.getUsername());
        if(user == null) throw new UsernameNotFoundException("Username " + credentials.getUsername() + " not found");

        var tokenResponse = tokenProvider.createAccessToken(
            credentials.getUsername(),
            user.getRoles()
        );

        return ResponseEntity.ok(tokenResponse);
    }
}
