package com.patil.eventmanagement.security.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;
import java.util.function.Function;

@Component
public class JwtService {

    public static final String SECRET_KEY = "b4a2ee5f74b0b6558d4d73844173535859a767801494dccf6bdf37d860270630";
    private static final long EXPIRATION_TIME = 1000 * 60 * 30; // 30 minutes

    private static final Set<String> blacklistedTokens = new HashSet<>();

    public String generateToken(String username) {
        Map<String, String> claims = new HashMap<>();
        return createToken(username, claims);
    }

    private String createToken(String username, Map<String, String> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSignKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse JWT token: " + e.getMessage());
        }
    }

    public <T> T extractClaims(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String extractUsername(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaims(token, Claims::getExpiration);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        // Validate the token against username, expiration, and blacklist
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token) && !isTokenInvalid(token));
    }

    public void invalidateToken(String token) {
        blacklistedTokens.add(token); // Add the token to the blacklist
    }

    public boolean isTokenInvalid(String token) {
        return blacklistedTokens.contains(token); // Check if the token is in the blacklist
    }
}