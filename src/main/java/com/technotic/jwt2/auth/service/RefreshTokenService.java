package com.technotic.jwt2.auth.service;

import com.technotic.jwt2.auth.entity.RefreshToken;
import com.technotic.jwt2.auth.entity.User;
import com.technotic.jwt2.auth.repository.RefreshTokenRepository;
import com.technotic.jwt2.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    public RefreshToken createrefreshToken(String userName) {
        User user = userRepository.findByEmail(userName)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + userName));

        log.info("RefreshTokenService::-> createrefreshToken ::-> "+user.getUsername());
        log.info("RefreshTokenService::-> createrefreshToken ::-> "+user.getRefreshToken());

        RefreshToken refreshToken = user.getRefreshToken();

        if (refreshToken == null) {
            long refreshTokenValidity = 30*10000;

            refreshToken = RefreshToken.builder()
                    .refreshToken(UUID.randomUUID().toString())
                    .expirationTime(Instant.now().plusMillis(refreshTokenValidity))
                    .user(user)
                    .build();
           // log.info("RefreshTokenService::-> createrefreshToken ::-> new rt "+refreshToken);
           // log.info("RefreshTokenService::-> createrefreshToken ::-> old  "+refreshToken.getUser());
            refreshTokenRepository.save(refreshToken);
        }
        log.info("RefreshTokenService::-> createrefreshToken ::-> old "+refreshToken);
        //log.info("RefreshTokenService::-> createrefreshToken ::-> old  "+refreshToken.getUser());
        return refreshToken;
    }

    public RefreshToken verifyRefreshToken(String refreshToken) {

        RefreshToken refToken = refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("Refresh token not found..."));

        if (refToken.getExpirationTime().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(refToken);

            throw new RuntimeException("Reefresh token expired...");

        }
        return refToken;


    }

}
