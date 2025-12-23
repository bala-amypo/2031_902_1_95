package com.example.demo.security;

import java.security.Key;
import java.util.Date;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

    private static final String DEFAULT_SECRET =
            "my-super-secret-key-my-super-secret-key";

    private static final long DEFAULT_VALIDITY = 86400000;

    private Key key;
    private long validity;

    public JwtTokenProvider() {
        this.key = Keys.hmacShaKeyFor(DEFAULT_SECRET.getBytes());
        this.validity = DEFAULT_VALIDITY;
    }

    public JwtTokenProvider(String secret, long validity) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.validity = validity;
    }

    public String generateToken(Long userId, String email, String role) {
        return Jwts.builder()
                .setSubject(email)
                .claim("userId", userId)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + validity))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Long getUserIdFromToken(String token) {
        return getClaims(token).get("userId", Long.class);
    }

    public String getEmailFromToken(String token) {
        return getClaims(token).getSubject();
    }

    public String getRoleFromToken(String token) {
        return getClaims(token).get("role", String.class);
    }

    public boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getEmail(String token) {
        return getEmailFromToken(token);
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
