package com.effectiveMobile.effectivemobile.services;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * <pre>
 *     Интерфейс для работы c jwt-токенами
 * </pre>
 */
public interface JwtService {

    /**
     * Генерирует JWT-токен для заданного пользователя.
     *
     * @param userDetails детали пользователя
     * @return JWT-токен в виде строки
     */
    String generateToken(UserDetails userDetails);

    /**
     * Извлекает email пользователя из JWT-токена.
     *
     * @param jwt JWT-токен
     * @return email пользователя
     */
    String extractEmailUser(String jwt);

    /**
     * Проверяет валидность JWT-токена по сроку действия.
     *
     * @param jwt JWT-токен
     * @return true, если токен валиден; false в противном случае
     */
    boolean isTokenValid(String jwt);

}
