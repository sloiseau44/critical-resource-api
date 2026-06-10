package com.criticalresource.criticalresourceapi.auth;

import com.criticalresource.criticalresourceapi.domain.user.Role;
import com.criticalresource.criticalresourceapi.domain.user.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import javax.crypto.SecretKey;

import java.util.Date;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
public class JwtServiceTest {
    @InjectMocks
    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(jwtService, "secret", "criticalresourceapisecretkey1234567890abcdef");
        ReflectionTestUtils.setField(jwtService, "expiration", 86400000L);
    }

    @Test
    void should_return_username_from_token() {
        User user = User.builder()
                .username("jdupont")
                .email("jdupont@test.fr")
                .password("encoded_password")
                .role(Role.OPERATEUR)
                .build();

        String token = jwtService.generateToken(user);

        String username = jwtService.extractUsername(token);

        assertThat(username).isEqualTo("jdupont");
    }

    @Test
    void should_return_true_when_token_is_valid() {
        User user = User.builder()
                .username("jdupont")
                .email("jdupont@test.fr")
                .password("encoded_password")
                .role(Role.OPERATEUR)
                .build();

        String token = jwtService.generateToken(user);

        assertThat(jwtService.isTokenValid(token)).isTrue();
    }

    @Test
    void should_return_false_when_token_has_invalid_signature() {
        SecretKey wrongKey = Keys.hmacShaKeyFor("wrongsecretwrongsecretwrongsecret12".getBytes());
        String tokenWithWrongSignature = Jwts.builder()
                .subject("jdupont")
                .signWith(wrongKey)
                .compact();

        assertThat(jwtService.isTokenValid(tokenWithWrongSignature)).isFalse();
    }

    @Test
    void should_return_false_when_token_is_expired() {
        SecretKey key = Keys.hmacShaKeyFor("criticalresourceapisecretkey1234567890abcdef".getBytes());
        String expiredToken = Jwts.builder()
                .subject("jdupont")
                .expiration(new Date(System.currentTimeMillis() - 1000))
                .signWith(key)
                .compact();

        assertThat(jwtService.isTokenValid(expiredToken)).isFalse();
    }
}
