package com.springerp.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

class JwtTokenUtilTest {

    private JwtTokenUtil jwtTokenUtil;
    private static final String SECRET = "springerpsecretkeywith512bitsforhs512algorithm12345678901234";
    private static final long EXPIRATION_MS = 3_600_000L; // 1 hour
    private static final String USERNAME = "user@example.com";

    @BeforeEach
    void setUp() {
        jwtTokenUtil = new JwtTokenUtil();
        ReflectionTestUtils.setField(jwtTokenUtil, "secretKeyString", SECRET);
        ReflectionTestUtils.setField(jwtTokenUtil, "jwtExpirationMs", EXPIRATION_MS);
    }

    @Test
    void generateToken_returnsNonNullToken() {
        String token = jwtTokenUtil.generateToken(USERNAME);

        assertThat(token).isNotNull().isNotBlank();
    }

    @Test
    void generateToken_producesTokensThatContainUsername() {
        String token = jwtTokenUtil.generateToken(USERNAME);

        assertThat(jwtTokenUtil.getUsernameFromToken(token)).isEqualTo(USERNAME);
    }

    @Test
    void getUsernameFromToken_returnsCorrectUsername() {
        String token = jwtTokenUtil.generateToken(USERNAME);

        assertThat(jwtTokenUtil.getUsernameFromToken(token)).isEqualTo(USERNAME);
    }

    @Test
    void getExpirationDateFromToken_returnsDateInTheFuture() {
        String token = jwtTokenUtil.generateToken(USERNAME);

        Date expiry = jwtTokenUtil.getExpirationDateFromToken(token);

        assertThat(expiry).isAfter(new Date());
    }

    @Test
    void isTokenExpired_returnsFalseForFreshToken() {
        String token = jwtTokenUtil.generateToken(USERNAME);

        assertThat(jwtTokenUtil.isTokenExpired(token)).isFalse();
    }

    @Test
    void isTokenExpired_returnsTrueForExpiredToken() {
        ReflectionTestUtils.setField(jwtTokenUtil, "jwtExpirationMs", -1000L);
        String expiredToken = jwtTokenUtil.generateToken(USERNAME);
        ReflectionTestUtils.setField(jwtTokenUtil, "jwtExpirationMs", EXPIRATION_MS);

        assertThat(jwtTokenUtil.isTokenExpired(expiredToken)).isTrue();
    }

    @Test
    void validateToken_returnsTrueForValidTokenAndMatchingUser() {
        String token = jwtTokenUtil.generateToken(USERNAME);
        UserDetails userDetails = new User(USERNAME, "password", Collections.emptyList());

        assertThat(jwtTokenUtil.validateToken(token, userDetails)).isTrue();
    }

    @Test
    void validateToken_returnsFalseWhenUsernameDoesNotMatch() {
        String token = jwtTokenUtil.generateToken(USERNAME);
        UserDetails differentUser = new User("other@example.com", "password", Collections.emptyList());

        assertThat(jwtTokenUtil.validateToken(token, differentUser)).isFalse();
    }

    @Test
    void validateToken_returnsFalseForExpiredToken() {
        ReflectionTestUtils.setField(jwtTokenUtil, "jwtExpirationMs", -1000L);
        String expiredToken = jwtTokenUtil.generateToken(USERNAME);
        ReflectionTestUtils.setField(jwtTokenUtil, "jwtExpirationMs", EXPIRATION_MS);

        UserDetails userDetails = new User(USERNAME, "password", Collections.emptyList());

        assertThat(jwtTokenUtil.validateToken(expiredToken, userDetails)).isFalse();
    }

    @Test
    void validateToken_returnsFalseForInvalidatedToken() {
        String token = jwtTokenUtil.generateToken(USERNAME);
        UserDetails userDetails = new User(USERNAME, "password", Collections.emptyList());

        jwtTokenUtil.invalidateToken(token);

        assertThat(jwtTokenUtil.validateToken(token, userDetails)).isFalse();
    }

    @Test
    void isTokenInvalidated_returnsFalseBeforeInvalidation() {
        String token = jwtTokenUtil.generateToken(USERNAME);

        assertThat(jwtTokenUtil.isTokenInvalidated(token)).isFalse();
    }

    @Test
    void isTokenInvalidated_returnsTrueAfterInvalidation() {
        String token = jwtTokenUtil.generateToken(USERNAME);

        jwtTokenUtil.invalidateToken(token);

        assertThat(jwtTokenUtil.isTokenInvalidated(token)).isTrue();
    }

    @Test
    void invalidateToken_canBeCalledMultipleTimesWithoutError() {
        String token = jwtTokenUtil.generateToken(USERNAME);

        jwtTokenUtil.invalidateToken(token);
        jwtTokenUtil.invalidateToken(token);

        assertThat(jwtTokenUtil.isTokenInvalidated(token)).isTrue();
    }

    @Test
    void invalidateToken_onlyInvalidatesTheSpecificToken() {
        String token1 = jwtTokenUtil.generateToken(USERNAME);
        String token2 = jwtTokenUtil.generateToken("other@example.com");

        jwtTokenUtil.invalidateToken(token1);

        assertThat(jwtTokenUtil.isTokenInvalidated(token1)).isTrue();
        assertThat(jwtTokenUtil.isTokenInvalidated(token2)).isFalse();
    }

    @Test
    void generateToken_producesWellFormedJwtWithThreeParts() {
        String token = jwtTokenUtil.generateToken(USERNAME);

        String[] parts = token.split("\\.");
        assertThat(parts).hasSize(3);
        assertThat(parts[0]).isNotBlank();
        assertThat(parts[1]).isNotBlank();
        assertThat(parts[2]).isNotBlank();
    }

    @Test
    void isTokenInvalidated_returnsFalseForNeverSeenToken() {
        assertThat(jwtTokenUtil.isTokenInvalidated("completely.unknown.token")).isFalse();
    }
}

