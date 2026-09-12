package com.example.messenger.JwtTest;

import com.example.messenger.JWT.JwtProperties;
import com.example.messenger.JWT.JwtService;
import com.example.messenger.USER.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;


public class JwtServiceTest {

    @Test
    void extractUserId_ShouldReturnUserId_WhenRightAndActualToken_FromGenerateToken() {

        JwtProperties jwtProperties = new JwtProperties(
                "1secret8secret15secret22secret29secret36",
                3600000
        );

        JwtService jwtService = new JwtService(jwtProperties);

        User testUser = new User(10);

        assertEquals(
                testUser.getId(),
                jwtService.extractUserId( jwtService.generateToken(testUser) )
        );

    }

    @Test
    void extractUserId_ShouldThrow_WhenExpiredToken_FromGenerateToken() {

        JwtProperties jwtProperties = new JwtProperties(
                "1secret8secret15secret22secret29secret36",
                -1
        );

        JwtService jwtService = new JwtService(jwtProperties);

        User testUser = new User(10);

        assertThrowsExactly(
                ExpiredJwtException.class,

                () -> jwtService.extractUserId( jwtService.generateToken(testUser) )
        );

    }

    @Test
    void extractUserId_ShouldThrow_WhenMalformedToken_FromGenerateToken() {

        JwtProperties jwtProperties = new JwtProperties(
                "1secret8secret15secret22secret29secret36",
                3600000
        );

        JwtService jwtService = new JwtService(jwtProperties);

        assertThrowsExactly(
                MalformedJwtException.class,

                () -> jwtService.extractUserId( "malformedToken" )
        );

    }

    @Test
    void extractUserId_ShouldThrow_WhenSignatureWasTampered() {

        JwtProperties jwtProperties = new JwtProperties(
                "1secret8secret15secret22secret29secret36",
                3600000
        );

        JwtService jwtService = new JwtService(jwtProperties);

        User testUser = new User(10);

        String token = jwtService.generateToken(testUser);

        String[] parts = token.split("\\.");

        String header = parts[0];
        String payload = parts[1];
        String signature = parts[2];

        String originalPayload = new String(
                Base64.getUrlDecoder().decode(payload),
                StandardCharsets.UTF_8
        );

        String tamperedPayload = originalPayload.replace(
                "\"sub\":\"10\"",
                "\"sub\":\"999\""
        );

        String encodedTamperedPayload = Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(
                        tamperedPayload.getBytes(StandardCharsets.UTF_8)
                );

        String tamperedToken =
                header + "." +
                        encodedTamperedPayload + "." +
                        signature;

        assertThrowsExactly(
                SignatureException.class,
                () -> jwtService.extractUserId(tamperedToken)
        );
    }
}
