package com.effectiveMobile.effectivemobile.config;

import com.effectiveMobile.effectivemobile.constants.ConstantsClass;
import com.effectiveMobile.effectivemobile.services.MyUserDetailService;
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
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    private MyUserDetailService myUserDetailService;
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * Метод, настраивающий цепочку фильтров безопасности приложения
     * @param httpSecurity
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(registry -> {
                    registry.requestMatchers("/entrance/**",
                            "/swagger-ui/**", "/v3/api-docs/**").permitAll();
                    registry.requestMatchers("/task/**").hasRole(ConstantsClass.USERROLE);
                    registry.requestMatchers("/task/delete/**").hasRole(ConstantsClass.ADMINROLE);
                    registry.anyRequest().authenticated(); // Любой запрос должен быть аутентифицирован
                })
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return myUserDetailService;
    }

    /**
     * Настраивает менеджер аутентификации с использованием указанного провайдера аутентификации.
     * @return настроенный {@link AuthenticationManager}
     */
    @Bean
    public AuthenticationManager authenticationManager(){
        return new ProviderManager(authenticationProvider());
    }

    /**
     * Определяет провайдер аутентификации с использованием DAO и шифрования паролей.
     * @return настроенный {@link AuthenticationProvider}
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(myUserDetailService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;

    }

    /**
     * Определяет механизм шифрования паролей с использованием BCrypt.
     * @return экземпляр {@link PasswordEncoder}
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
