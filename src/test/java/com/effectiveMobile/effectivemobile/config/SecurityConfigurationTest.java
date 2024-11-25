package com.effectiveMobile.effectivemobile.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;

class SecurityConfigurationTest {

    private SecurityConfiguration securityConfiguration;
    private HttpSecurity mockHttpSecurity;
    private JwtAuthenticationFilter mockJwtAuthenticationFilter;
    private PasswordEncoder mockPasswordEncoder;

    @BeforeEach
    void setUp() {
        mockHttpSecurity = Mockito.mock(HttpSecurity.class);
        mockJwtAuthenticationFilter = Mockito.mock(JwtAuthenticationFilter.class);
        mockPasswordEncoder = Mockito.mock(PasswordEncoder.class);

        securityConfiguration = new SecurityConfiguration();
        securityConfiguration.setJwtAuthenticationFilter(mockJwtAuthenticationFilter);
    }

    @Test
    @DisplayName("Тест: Проверка бина AuthenticationManager")
    void testAuthenticationManager() {
        AuthenticationManager authenticationManager = securityConfiguration.authenticationManager();
        assertNotNull(authenticationManager, "AuthenticationManager должен быть настроен");
        assertTrue(authenticationManager instanceof AuthenticationManager, "Должен быть возвращен корректный AuthenticationManager");
    }

    @Test
    @DisplayName("Тест: Проверка бина AuthenticationProvider")
    void testAuthenticationProvider() throws NoSuchFieldException, IllegalAccessException {
        DaoAuthenticationProvider provider = (DaoAuthenticationProvider) securityConfiguration.authenticationProvider();
        assertNotNull(provider, "AuthenticationProvider должен быть настроен");

        // Получаем поле passwordEncoder через рефлексию
        Field passwordEncoderField = DaoAuthenticationProvider.class.getDeclaredField("passwordEncoder");
        passwordEncoderField.setAccessible(true);

        PasswordEncoder passwordEncoder = (PasswordEncoder) passwordEncoderField.get(provider);
        assertNotNull(passwordEncoder, "PasswordEncoder в AuthenticationProvider не должен быть null");
        assertInstanceOf(BCryptPasswordEncoder.class, passwordEncoder, "Должен быть использован BCryptPasswordEncoder");
    }

    @Test
    @DisplayName("Тест: Проверка бина PasswordEncoder")
    void testPasswordEncoder() {
        PasswordEncoder passwordEncoder = securityConfiguration.passwordEncoder();
        assertNotNull(passwordEncoder, "PasswordEncoder должен быть создан");
        assertInstanceOf(PasswordEncoder.class, passwordEncoder, "Должен быть использован BCryptPasswordEncoder");
    }
}