package com.rental.PropertyRentalApi.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


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
            String username
            // String role   // ⬅️ PROD: enable role-based authorization
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
        // if (role != null && !role.isBlank()) {
        //     claims.put("role", role);
        // }

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
}
