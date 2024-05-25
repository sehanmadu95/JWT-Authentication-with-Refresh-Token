package com.technotic.jwt2.auth.controller;

import com.technotic.jwt2.auth.entity.RefreshToken;
import com.technotic.jwt2.auth.entity.User;
import com.technotic.jwt2.auth.service.AuthService;
import com.technotic.jwt2.auth.service.JwtService;
import com.technotic.jwt2.auth.service.RefreshTokenService;
import com.technotic.jwt2.auth.utils.AuthRonsponse;
import com.technotic.jwt2.auth.utils.LogingRequest;
import com.technotic.jwt2.auth.utils.RefreshTokenRequest;
import com.technotic.jwt2.auth.utils.RegisterRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/auth/")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;
    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<AuthRonsponse> resgister(@RequestBody RegisterRequest registerRequest){
      log.info("Hit to Auth controller register method.");
     // return null;
      return ResponseEntity.ok(authService.register(registerRequest));
    }


    @PostMapping("/login")
    public ResponseEntity<AuthRonsponse> login(@RequestBody LogingRequest logingRequest){
        return ResponseEntity.ok(authService.login(logingRequest));
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<AuthRonsponse> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest){
        RefreshToken refreshToken=refreshTokenService
                .verifyRefreshToken(refreshTokenRequest.getRefreshToekn());
        log.info("User details:"+refreshToken.getUser());

        User user=refreshToken.getUser();
        log.info("User details:"+user.getUsername());
        String accsessToken=jwtService.generateToken(user);

        return ResponseEntity.ok(AuthRonsponse.builder()
                .accessToken(accsessToken)
                .refreshToken(refreshToken.getRefreshToken())
                .build());

    }
}
