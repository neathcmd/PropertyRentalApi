package com.rental.PropertyRentalApi.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rental.PropertyRentalApi.Entity.UserEntity;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import static com.rental.PropertyRentalApi.Exception.ErrorsExceptionFactory.unauthorized;


@Service
@RequiredArgsConstructor
public class JwtService {

    /*
     * =========================
     * 🔒 PRODUCTION RULE
     * =========================
     * - NEVER hardcode secrets
     * - Value MUST come from ENV variable in prod
     *
     * PROD:
     * export SPRING_JWT_SECRET=super-long-random-secret
     */
    @Value("${spring.jwt-secret}")
    private String jwtSecret;

    /*
     * =========================
     * ⏱ TOKEN EXPIRATION
     * =========================
     * DEV example:
     *  access = 15 minutes
     *  refresh = 7 days
     *
     * PROD:
     * - Access: 5–15 minutes
     * - Refresh: 7–30 days
     */
    @Value("${spring.access-token-expire}")
    private long accessTokenExpired; // milliseconds

    @Value("${spring.refresh-token-expire}")
    private long refreshTokenExpired;

    /*
     * =========================
     * 🔑 SIGNING KEY
     * =========================
     * PROD REQUIREMENT:
     * - Secret length >= 64 bytes for HS512
     */
    private SecretKey getSignKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    // =================
    // ACCESS TOKEN
    // =================
    public String generateAccessToken(
            String userId,
            String email,
            String username,
            List<String> roles   // ⬅️ PROD: enable role-based authorization
    ) {

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("email", email);
        claims.put("type", "access");

        /*
         * =========================
         * 🔐 PROD FEATURE
         * =========================
         * Uncomment when you add RBAC (role-based access control)
         */
        if (roles != null && !roles.isEmpty()) {
            claims.put("roles", roles);
        }

        return buildToken(claims, username, accessTokenExpired);
    }

    // =================
    // REFRESH TOKEN
    // =================
    public String generateRefreshToken(String userId) {

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("type", "refresh");

        /*
         * =========================
         * 🛡 PROD NOTE
         * =========================
         * - Refresh token should be stored in DB / Redis
         * - Allow rotation & revocation
         */
        return buildToken(claims, userId, refreshTokenExpired);
    }

    // =================
    // JWT BUILDER
    // =================
    private String buildToken(
            Map<String, Object> claims,
            String subject,
            long expiration
    ) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(now)
                .expiration(expiryDate)

                /*
                 * =========================
                 * 🔐 PROD SECURITY
                 * =========================
                 * HS512 is good for prod IF secret is long enough
                 */
                .signWith(getSignKey(), Jwts.SIG.HS512)
                .compact();
    }

    // =================
    // USERNAME EXTRACTOR METHOD
    // =================
    public String extractUsername(String token) {
        return Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }


    // =================
    // VALIDATE TOKEN
    // =================
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        Date expiration = Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration();
        return expiration.before(new Date());
    }

    // =================
    // GET CURRENT USER
    // =================
    public UserEntity getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw unauthorized("User not Authenticated");
        }

        // Store entity as principal in jwt auth filter
        return (UserEntity) authentication.getPrincipal();
    }

}

