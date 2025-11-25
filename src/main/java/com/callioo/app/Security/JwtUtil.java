package com.callioo.app.Security;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import com.callioo.app.Model.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class JwtUtil {

    private static final long EXPIRATION_DURATION_HOURS = 24;
    private final SecretKey secretKey;
    private final String TOKEN_HEADER = "Authorization";
    private final String TOKEN_PREFIX = "Bearer ";

    public JwtUtil(@Value("${jwt.secret}") String secret) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(User user) {
        LocalDateTime tokenCreateTime = LocalDateTime.now();
        LocalDateTime tokenValidity = tokenCreateTime.plusHours(EXPIRATION_DURATION_HOURS);
        Date tokenCreatedTimeDate = Date.from(tokenCreateTime.atZone(ZoneId.systemDefault()).toInstant());
        Date tokenValidityDate = Date.from(tokenValidity.atZone(ZoneId.systemDefault()).toInstant());

        return Jwts.builder()
                .subject(user.getEmail())
                .claim("fullName", user.getFullName())
                .issuedAt(tokenCreatedTimeDate)
                .expiration(tokenValidityDate)
                .signWith(secretKey)
                .compact();
    }

    public Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(TOKEN_HEADER);
        if (bearerToken != null && bearerToken.startsWith(TOKEN_PREFIX)) {
            return bearerToken.substring(TOKEN_PREFIX.length());
        }
        return null;
    }

    public Claims resolveClaims(HttpServletRequest request) {
        try {
            String token = resolveToken(request);
            if (token != null)
                return extractClaims(token);
            else
                return null;
        } catch (ExpiredJwtException ex) {
            request.setAttribute("expired", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            request.setAttribute("invalid", ex.getMessage());
            throw ex;
        }
    }

    public boolean validateClaims(Claims claims) throws AuthenticationException {
        try {
            return claims.getExpiration()
                    .after(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
        } catch (Exception e) {
            throw e;
        }
    }

    public boolean validateToken(String token) throws AuthenticationException {
        try {
            return extractClaims(token).getExpiration()
                    .after(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
        } catch (Exception e) {
            throw e;
        }
    }

    public String extractEmail(String token) {
        return extractClaims(token).getSubject();
    }

}
