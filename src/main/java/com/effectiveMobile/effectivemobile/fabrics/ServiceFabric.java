package com.effectiveMobile.effectivemobile.fabrics;

import com.effectiveMobile.effectivemobile.services.*;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * <pre>
 *     Фабрика для создания экземпляров классов-Service
 * </pre>
 */
public interface ServiceFabric {

    /**
     * Фабричный метод для получения {@link JwtService}
     *
     * @return интерфейс JwtService, который имплементируется JwtService.class
     */
    JwtService createJwtService();

    /**
     * Фабричный метод для получения {@link TaskService}
     *
     * @return интерфейс TaskService, который имплементируется TaskServiceImpl.class
     */
    TaskService createTaskService();

    /**
     * Фабричный метод для получения {@link UserService}
     *
     * @return интерфейс UserService, который имплементируется UserServiceImpl.class
     */
    UserService createUserService();

    /**
     * Фабричный метод для получения {@link UserDetailsService}
     *
     * @return интерфейс UserDetailsService, который имплементируется MyUserDetailServiceImpl.class
     */
    UserDetailsService createUserDetailsService();

    /**
     * Фабричный метод для получения {@link NotesService}
     *
     * @return интерфейс NotesService, который имплементируется NotesServiceImpl.class
     */
    NotesService createNotesService();

    DefaultSettingsService createDefaultSettingsService();

}
