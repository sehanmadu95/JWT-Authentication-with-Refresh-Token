package com.technotic.jwt2.auth.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@Service
@Slf4j
public class JwtService {
    private static final String SECRET = "67f40e2c3c00054a644d854e487089446a0ac4ce399e5c8aefdc7858305ebc6b";

    //Extract Username from Token
    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);

    }

    //extract information from JWT
    public Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    //decode and get the key

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);

    }

    public String generateToken(UserDetails userDetails) {
        log.info("JWT service generate token 1");

        return generateToken(new HashMap<>(), userDetails);
    }

    //generate token using jwt utility class and return token as string
    public String generateToken(Map<String, Object> extractClaims, UserDetails userDetails) {
        log.info("JWT service generate token 2");
        return Jwts
                .builder()
                .setClaims(extractClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 25*1000))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    //if token is valid by chcking if token is expired for current user

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpire(token));

    }

    //if token is expire
    private boolean isTokenExpire(String token) {
        return extractExpiration(token).before(new Date());
    }

    //get expiration date from token
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }


}
