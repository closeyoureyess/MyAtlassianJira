package com.effectiveMobile.effectivemobile.config;

import com.effectiveMobile.effectivemobile.other.UserRoles;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * <pre>
 *     Конфигурационный класс для Spring Security
 * </pre>
 */
@Setter

@Configuration
@EnableWebSecurity
@Slf4j
public class SecurityConfiguration {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * Метод, настраивающий цепочку фильтров безопасности приложения
     *
     * @param httpSecurity Объект {@link HttpSecurity} для настройки политики безопасности.
     * @return Объект {@link SecurityFilterChain} с настроенной цепочкой фильтров.
     * @throws Exception Если произошла ошибка во время настройки безопасности.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        log.info("Метод securityFilterChain()");
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(registry -> {
                    registry.requestMatchers("/entrance/**",
                            "/swagger-ui/**", "/v3/api-docs/**").permitAll();
                    registry.requestMatchers("/task/update-tasks", "/notes/**")
                            .hasAnyRole(UserRoles.ADMIN.getUserRoles(), UserRoles.USER.getUserRoles());
                    registry.requestMatchers("/task/**",
                                    "/defaultsettins/**").hasRole(UserRoles.ADMIN.getUserRoles());
                    registry.anyRequest().authenticated(); // Любой запрос должен быть аутентифицирован
                })
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    /**
     * Метод, создающий бин {@link UserDetailsService}
     *
     * @return {@link UserDetailsService} - дефолтный интерфейс Spring Security Core
     */
    @Bean
    public UserDetailsService userDetailsService() {
        log.info("Метод userDetailsService()");
        return userDetailsService;
    }

    /**
     * Настраивает менеджер аутентификации с использованием указанного провайдера аутентификации.
     *
     * @return настроенный {@link AuthenticationManager} - дефолтный интерфейс Spring Security Core
     */
    @Bean
    public AuthenticationManager authenticationManager() {
        log.info("Метод authenticationManager()");
        return new ProviderManager(authenticationProvider());
    }

    /**
     * Определяет провайдер аутентификации с использованием DAO и шифрования паролей.
     *
     * @return настроенный {@link AuthenticationProvider} - дефолтный интерфейс Spring Security Core
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        log.info("Метод authenticationProvider()");
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;

    }

    /**
     * Определяет механизм шифрования паролей с использованием BCrypt.
     *
     * @return экземпляр {@link PasswordEncoder}
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        log.info("Метод passwordEncoder()");
        return new BCryptPasswordEncoder();
    }

}
