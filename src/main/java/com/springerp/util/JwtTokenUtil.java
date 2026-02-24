package com.springerp.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * Utility class for JWT token operations.
 * Uses a thread-safe ConcurrentHashMap with expiry timestamps for the token blacklist.
 */
@Component
public class JwtTokenUtil {

    /**
     * Thread-safe token blacklist: token -> expiry timestamp (epoch ms).
     * Expired entries are lazily evicted on isTokenInvalidated checks.
     */
    private final ConcurrentHashMap<String, Long> invalidatedTokens = new ConcurrentHashMap<>();

    @Value("${app.jwt.expiration-milliseconds}")
    private long jwtExpirationMs;

    @Value("${jwt.secret}")
    private String secretKeyString;

    private SecretKey getSigningKey() {
        byte[] keyBytes = secretKeyString.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(getSigningKey())
                .compact();
    }

    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claimsResolver.apply(claims);
    }

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        try {
            final String username = getUsernameFromToken(token);
            return username.equals(userDetails.getUsername())
                    && !isTokenExpired(token)
                    && !isTokenInvalidated(token);
        } catch (io.jsonwebtoken.JwtException e) {
            return false;
        }
    }

    public boolean isTokenExpired(String token) {
        try {
            return getExpirationDateFromToken(token).before(new Date());
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            return true;
        }
    }

    /**
     * Adds token to the blacklist with its natural expiry time.
     * Tokens are automatically considered clean after their expiry.
     */
    public void invalidateToken(String token) {
        try {
            Date expiry = getExpirationDateFromToken(token);
            invalidatedTokens.put(token, expiry.getTime());
        } catch (Exception e) {
            // If we can't parse the token, store it with current time + expiration window
            invalidatedTokens.put(token, System.currentTimeMillis() + jwtExpirationMs);
        }
        evictExpiredTokens();
    }

    /**
     * Checks if a token has been explicitly invalidated (e.g., logout).
     * Lazily evicts expired entries to prevent unbounded memory growth.
     */
    public boolean isTokenInvalidated(String token) {
        Long expiryMs = invalidatedTokens.get(token);
        if (expiryMs == null) {
            return false;
        }
        if (System.currentTimeMillis() > expiryMs) {
            invalidatedTokens.remove(token);
            return false;
        }
        return true;
    }

    /**
     * Removes all blacklist entries whose natural expiry has passed.
     */
    private void evictExpiredTokens() {
        long now = System.currentTimeMillis();
        invalidatedTokens.entrySet().removeIf(entry -> now > entry.getValue());
    }
}
