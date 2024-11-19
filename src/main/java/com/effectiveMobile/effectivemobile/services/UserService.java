package com.effectiveMobile.effectivemobile.services;

import com.effectiveMobile.effectivemobile.auxiliaryclasses.LoginForm;
import com.effectiveMobile.effectivemobile.dto.CustomUsersDto;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * <pre>
 *     Интерфейс для управления пользователями и их аутентификацией.
 * </pre>
 */
public interface UserService {

    /**
     * Метод для создания нового пользователя
     *
     * @param customUsersDto данные пользователя
     * @return созданный пользователь в виде {@link CustomUsersDto}
     */
    CustomUsersDto createUser (CustomUsersDto customUsersDto);

    /**
     * Авторизует пользователя и возвращает JWT-токен.
     *
     * @param loginForm данные для входа
     * @return JWT-токен
     * @throws UsernameNotFoundException если пользователь не найден
     */
    String authorizationUser(LoginForm loginForm) throws UsernameNotFoundException;

}
