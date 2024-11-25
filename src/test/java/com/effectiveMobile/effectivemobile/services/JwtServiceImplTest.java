package com.effectiveMobile.effectivemobile.services;

import com.effectiveMobile.effectivemobile.constants.ConstantsClass;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.concurrent.TimeUnit;

class JwtServiceImplTest {

    private JwtServiceImpl jwtService;
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        jwtService = new JwtServiceImpl();
        userDetails = new User("test@example.com", "password", Collections.emptyList());
    }

    @Test
    @DisplayName("Тест: Генерация токена")
    void testGenerateToken() {
        String token = jwtService.generateToken(userDetails);
        Assertions.assertNotNull(token, "Токен не должен быть null");
    }

    @Test
    @DisplayName("Тест: Извлечение email пользователя из токена")
    void testExtractEmailUser() {
        String token = jwtService.generateToken(userDetails);
        String email = jwtService.extractEmailUser(token);
        Assertions.assertEquals(userDetails.getUsername(), email, "Email должен совпадать с email пользователя");
    }

    @Test
    @DisplayName("Тест: Валидация валидного токена")
    void testIsTokenValid() {
        String token = jwtService.generateToken(userDetails);
        Assertions.assertTrue(jwtService.isTokenValid(token), "Токен должен быть валидным");
    }

    @Test
    @DisplayName("Тест: Валидация невалидного токена")
    void testIsTokenInvalid() {
        // Создаем истекший токен
        SecretKey key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(ConstantsClass.SECRETKEY));
        String expiredToken = Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis() - TimeUnit.HOURS.toMillis(1)))
                .expiration(new Date(System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(1)))
                .signWith(key)
                .compact();

        // Перехватываем исключение ExpiredJwtException
        Exception exception = Assertions.assertThrows(ExpiredJwtException.class, () -> {
            jwtService.isTokenValid(expiredToken);
        });

        // Проверяем, что исключение было выброшено
        Assertions.assertNotNull(exception, "Должно быть выброшено исключение ExpiredJwtException");
    }

    @Test
    @DisplayName("Тест: Генерация секретного ключа")
    void testGenerateKey() {
        SecretKey result = ReflectionTestUtils.invokeMethod(jwtService,"generateKey");
        Assertions.assertNotNull(result, "Ключ не должен быть null");
    }
}