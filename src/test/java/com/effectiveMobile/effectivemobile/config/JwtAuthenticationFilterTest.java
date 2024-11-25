package com.effectiveMobile.effectivemobile.config;

import com.effectiveMobile.effectivemobile.fabrics.ServiceFabric;
import com.effectiveMobile.effectivemobile.services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.io.IOException;

class JwtAuthenticationFilterTest {

    private JwtAuthenticationFilter jwtAuthenticationFilter;
    private ServiceFabric mockServiceFabric;
    private JwtService mockJwtService;
    private UserDetailsService mockUserDetailsService;
    private FilterChain mockFilterChain;

    @BeforeEach
    void setUp() {
        mockServiceFabric = Mockito.mock(ServiceFabric.class);
        mockJwtService = Mockito.mock(JwtService.class);
        mockUserDetailsService = Mockito.mock(UserDetailsService.class);
        mockFilterChain = Mockito.mock(FilterChain.class);

        jwtAuthenticationFilter = new JwtAuthenticationFilter();
        jwtAuthenticationFilter.setServiceFabric(mockServiceFabric);

        Mockito.when(mockServiceFabric.createJwtService()).thenReturn(mockJwtService);
        Mockito.when(mockServiceFabric.createUserDetailsService()).thenReturn(mockUserDetailsService);

        // Очистка SecurityContext перед каждым тестом
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("Тест: Без заголовка Authorization фильтр пропускает запрос")
    void testNoAuthHeader() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        jwtAuthenticationFilter.doFilterInternal(request, response, mockFilterChain);

        Authentication authResult = SecurityContextHolder.getContext().getAuthentication();
        Assertions.assertNull(authResult, "Аутентификация должна быть null");
        Mockito.verify(mockFilterChain, Mockito.times(1)).doFilter(request, response);
        Mockito.verifyNoInteractions(mockJwtService, mockUserDetailsService);
    }

    @Test
    @DisplayName("Тест: Невалидный JWT токен")
    void testInvalidJwtToken() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.addHeader("Authorization", "Bearer invalid.jwt.token");

        Mockito.when(mockJwtService.extractEmailUser("invalid.jwt.token")).thenReturn(null);

        jwtAuthenticationFilter.doFilterInternal(request, response, mockFilterChain);

        Authentication authResult = SecurityContextHolder.getContext().getAuthentication();
        Assertions.assertNull(authResult, "Аутентификация должна быть null");
        Mockito.verify(mockJwtService, Mockito.times(1)).extractEmailUser("invalid.jwt.token");
        Mockito.verify(mockFilterChain, Mockito.times(1)).doFilter(request, response);
    }

    @Test
    @DisplayName("Тест: Валидный JWT токен")
    void testValidJwtToken() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.addHeader("Authorization", "Bearer valid.jwt.token");

        UserDetails mockUserDetails = Mockito.mock(UserDetails.class);
        Mockito.when(mockJwtService.extractEmailUser("valid.jwt.token")).thenReturn("test@example.com");
        Mockito.when(mockUserDetailsService.loadUserByUsername("test@example.com")).thenReturn(mockUserDetails);
        Mockito.when(mockJwtService.isTokenValid("valid.jwt.token")).thenReturn(true);
        Mockito.when(mockUserDetails.getPassword()).thenReturn("password");
        Mockito.when(mockUserDetails.getAuthorities()).thenReturn(null);

        jwtAuthenticationFilter.doFilterInternal(request, response, mockFilterChain);

        Authentication authResult = SecurityContextHolder.getContext().getAuthentication();
        Assertions.assertNotNull(authResult, "Аутентификация должна быть установлена");
        Mockito.verify(mockJwtService, Mockito.times(1)).extractEmailUser("valid.jwt.token");
        Mockito.verify(mockJwtService, Mockito.times(1)).isTokenValid("valid.jwt.token");
        Mockito.verify(mockUserDetailsService, Mockito.times(1)).loadUserByUsername("test@example.com");
        Mockito.verify(mockFilterChain, Mockito.times(1)).doFilter(request, response);
    }

    @Test
    @DisplayName("Тест: Пользователь уже аутентифицирован")
    void testUserAlreadyAuthenticated() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.addHeader("Authorization", "Bearer valid.jwt.token");

        // Устанавливаем уже существующую аутентификацию
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("user", "password"));

        jwtAuthenticationFilter.doFilterInternal(request, response, mockFilterChain);

        // Убеждаемся, что вызовы моков для JWT-логики не происходят
        Mockito.verify(mockJwtService).extractEmailUser(Mockito.anyString());
        Mockito.verify(mockJwtService, Mockito.never()).isTokenValid(Mockito.anyString());
        Mockito.verify(mockUserDetailsService, Mockito.never()).loadUserByUsername(Mockito.anyString());
        Mockito.verify(mockFilterChain, Mockito.times(1)).doFilter(request, response);
    }

}
