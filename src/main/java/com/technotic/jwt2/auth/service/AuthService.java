package com.technotic.jwt2.auth.service;

import com.technotic.jwt2.auth.entity.User;
import com.technotic.jwt2.auth.entity.UserRole;
import com.technotic.jwt2.auth.repository.UserRepository;
import com.technotic.jwt2.auth.utils.AuthRonsponse;
import com.technotic.jwt2.auth.utils.LogingRequest;
import com.technotic.jwt2.auth.utils.RegisterRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;

    public AuthRonsponse register(RegisterRequest registerRequest){
        log.info("hit to Auth Service register method.. ");
        var user= User.builder()
                .name(registerRequest.getName())
                .email(registerRequest.getEmail())
                .userName(registerRequest.getUserName())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(registerRequest.getRole())
                .build();

        User saveUser = userRepository.save(user);
        log.info("hit to save");
        log.info("USER details refreshtoken: "+saveUser.getUsername());
        var accessToken=jwtService.generateToken(saveUser);
        var refreshToken=refreshTokenService.createrefreshToken(saveUser.getEmail());

        return AuthRonsponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getRefreshToken())
                .build();

    }

    public AuthRonsponse login(LogingRequest logingRequest){
        log.info("AuthService:: -> login");
         authenticationManager.authenticate(
                 new UsernamePasswordAuthenticationToken(
                         logingRequest.getEmail(),
                         logingRequest.getPassword()));

        log.info("AuthService:: -> login:: authenticationManager: ->ok ");
        log.info("AuthService:: -> login:: authenticationManager: email "+logingRequest.getEmail());

        var user=userRepository.findByEmail(logingRequest.getEmail()).orElseThrow(()-> new UsernameNotFoundException("User not found"));
        log.info("AuthService:: -> login:: User details: -> "+user);

        var accessToken=jwtService.generateToken(user);
        log.info("AuthService:: -> login:: accessToken: -> "+accessToken);

        var refreshToken=refreshTokenService.createrefreshToken(logingRequest.getEmail());
        log.info("AuthService:: -> login:: refreshToken: -> "+refreshToken);

   return AuthRonsponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getRefreshToken())
                .build();
    }
}
