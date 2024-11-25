package com.effectiveMobile.effectivemobile.config;

import com.effectiveMobile.effectivemobile.fabrics.ServiceFabric;
import com.effectiveMobile.effectivemobile.services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

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

        when(mockServiceFabric.createJwtService()).thenReturn(mockJwtService);
        when(mockServiceFabric.createUserDetailsService()).thenReturn(mockUserDetailsService);

        // Очистка SecurityContext перед каждым тестом
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("Тест: Без заголовка Authorization фильтр пропускает запрос")
    void testNoAuthHeader() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        jwtAuthenticationFilter.doFilterInternal(request, response, mockFilterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication(), "Аутентификация должна быть null");
        verify(mockFilterChain, times(1)).doFilter(request, response);
        verifyNoInteractions(mockJwtService, mockUserDetailsService);
    }

    @Test
    @DisplayName("Тест: Невалидный JWT токен")
    void testInvalidJwtToken() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.addHeader("Authorization", "Bearer invalid.jwt.token");

        when(mockJwtService.extractEmailUser("invalid.jwt.token")).thenReturn(null);

        jwtAuthenticationFilter.doFilterInternal(request, response, mockFilterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication(), "Аутентификация должна быть null");
        verify(mockJwtService, times(1)).extractEmailUser("invalid.jwt.token");
        verify(mockFilterChain, times(1)).doFilter(request, response);
    }

    @Test
    @DisplayName("Тест: Валидный JWT токен")
    void testValidJwtToken() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.addHeader("Authorization", "Bearer valid.jwt.token");

        UserDetails mockUserDetails = mock(UserDetails.class);
        when(mockJwtService.extractEmailUser("valid.jwt.token")).thenReturn("test@example.com");
        when(mockUserDetailsService.loadUserByUsername("test@example.com")).thenReturn(mockUserDetails);
        when(mockJwtService.isTokenValid("valid.jwt.token")).thenReturn(true);
        when(mockUserDetails.getPassword()).thenReturn("password");
        when(mockUserDetails.getAuthorities()).thenReturn(null);

        jwtAuthenticationFilter.doFilterInternal(request, response, mockFilterChain);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication(), "Аутентификация должна быть установлена");
        verify(mockJwtService, times(1)).extractEmailUser("valid.jwt.token");
        verify(mockJwtService, times(1)).isTokenValid("valid.jwt.token");
        verify(mockUserDetailsService, times(1)).loadUserByUsername("test@example.com");
        verify(mockFilterChain, times(1)).doFilter(request, response);
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
        verify(mockJwtService).extractEmailUser(anyString());
        verify(mockJwtService, never()).isTokenValid(anyString());
        verify(mockUserDetailsService, never()).loadUserByUsername(anyString());
        verify(mockFilterChain, times(1)).doFilter(request, response);
    }

}
