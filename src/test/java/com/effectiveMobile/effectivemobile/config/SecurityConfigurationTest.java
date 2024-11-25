package com.effectiveMobile.effectivemobile.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.lang.reflect.Field;

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
        Assertions.assertNotNull(authenticationManager, "AuthenticationManager должен быть настроен");
        Assertions.assertInstanceOf(AuthenticationManager.class, authenticationManager, "Должен быть возвращен корректный AuthenticationManager");
    }

    @Test
    @DisplayName("Тест: Проверка бина AuthenticationProvider")
    void testAuthenticationProvider() throws NoSuchFieldException, IllegalAccessException {
        DaoAuthenticationProvider provider = (DaoAuthenticationProvider) securityConfiguration.authenticationProvider();
        Assertions.assertNotNull(provider, "AuthenticationProvider должен быть настроен");

        // Получаем поле passwordEncoder через рефлексию
        Field passwordEncoderField = DaoAuthenticationProvider.class.getDeclaredField("passwordEncoder");
        passwordEncoderField.setAccessible(true);

        PasswordEncoder passwordEncoder = (PasswordEncoder) passwordEncoderField.get(provider);
        Assertions.assertNotNull(passwordEncoder, "PasswordEncoder в AuthenticationProvider не должен быть null");
        Assertions.assertInstanceOf(BCryptPasswordEncoder.class, passwordEncoder, "Должен быть использован BCryptPasswordEncoder");
    }

    @Test
    @DisplayName("Тест: Проверка бина PasswordEncoder")
    void testPasswordEncoder() {
        PasswordEncoder passwordEncoder = securityConfiguration.passwordEncoder();
        Assertions.assertNotNull(passwordEncoder, "PasswordEncoder должен быть создан");
        Assertions.assertInstanceOf(PasswordEncoder.class, passwordEncoder, "Должен быть использован BCryptPasswordEncoder");
    }
}