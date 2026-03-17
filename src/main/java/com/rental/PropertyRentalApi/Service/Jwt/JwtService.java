package com.rental.PropertyRentalApi.Service.Jwt;

import com.rental.PropertyRentalApi.Entity.Users;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static com.rental.PropertyRentalApi.Exception.ErrorsExceptionFactory.unauthorized;

@Service
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class JwtService {

    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);

    private static final long TOKEN_EXPIRY_MS = 60L * 24 * 60 * 60 * 1000; // 60 days

    @Value("${spring.jwt-secret}")
    private String jwtSecret;

    /*
     * =========================
     * SIGNING KEY
     * =========================
     */
    private SecretKey getSignKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    /*
     * =========================
     * CLAIMS CORE
     * =========================
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private <T> T extractClaim(String token, Function<Claims, T> resolver) {
        return resolver.apply(extractAllClaims(token));
    }

    /*
     * =========================
     * TOKEN GENERATION
     * =========================
     */
    public String generateToken(String userId, String email, String username, List<String> roles) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("email", email);

        if (roles != null && !roles.isEmpty()) {
            claims.put("roles", roles);
        }

        Date now = new Date();
        Date expiry = new Date(now.getTime() + TOKEN_EXPIRY_MS);

        return Jwts.builder()
                .claims(claims)
                .subject(username)
                .issuedAt(now)
                .expiration(expiry)
                .signWith(getSignKey(), Jwts.SIG.HS512)
                .compact();
    }

    /*
     * =========================
     * CLAIM EXTRACTORS
     * =========================
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String extractUserId(String token) {
        return extractClaim(token, claims -> claims.get("userId", String.class));
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /*
     * =========================
     * VALIDATION
     * =========================
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        return extractUsername(token).equals(userDetails.getUsername())
                && !isTokenExpired(token);
    }

    public boolean validateToken(String token) {
        if (token == null || token.isBlank()) {
            logger.debug("Token validation failed: token is null or blank");
            return false;
        }
        try {
            extractAllClaims(token);
            return !isTokenExpired(token);
        } catch (JwtException | IllegalArgumentException e) {
            logger.debug("Token validation failed: {}", e.getMessage());
            return false;
        }
    }

    /*
     * =========================
     * CURRENT USER
     * =========================
     */
    public Users getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null ||
                !authentication.isAuthenticated() ||
                authentication.getPrincipal().equals("anonymousUser")) {
            throw unauthorized("User not authenticated");
        }

        return (Users) authentication.getPrincipal();
    }
}