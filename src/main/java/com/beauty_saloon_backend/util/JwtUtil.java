package com.beauty_saloon_backend.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.function.Function;
import java.util.Base64;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    private final SecretKey key;

    public JwtUtil(@Value("${JWT_SECRET_KEY}") String secretKey) {
        // Dekódoljuk a Base64 titkos kulcsot
        byte[] decodedKey = Base64.getDecoder().decode(secretKey);
        // Létrehozzuk a SecretKey objektumot a dekódolt kulccsal
        this.key = Keys.hmacShaKeyFor(decodedKey);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token){
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims,T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(key) // Használjuk a biztonságos kulcsot
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    // Token generálása UserDetails alapján, beleértve a szerepköröket
    public String generateToken(UserDetails userDetails){
        Map<String, Object> claims = new HashMap<>();
        // Szerepkörök hozzáadása a claims részhez
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        claims.put("roles", roles);
        return createToken(claims, userDetails.getUsername());
    }

    private String createToken(Map<String, Object> claims, String subject){
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 óra
                .signWith(key, SignatureAlgorithm.HS256) // Aláírás biztonságos kulccsal és algoritmussal
                .compact();
    }

    // Token validálása UserDetails alapján
    public Boolean validateToken(String token, UserDetails userDetails){
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // Szerepkörök kinyerése a tokenből
    public List<String> extractRoles(String token){
        Claims claims = extractAllClaims(token);
        return claims.get("roles", List.class);
    }
}
