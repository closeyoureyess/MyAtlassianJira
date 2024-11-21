package com.effectiveMobile.effectivemobile.auxiliaryclasses;

import com.effectiveMobile.effectivemobile.entities.CustomUsers;
import com.effectiveMobile.effectivemobile.entities.Tasks;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

/**
 * <pre>
 *     Интерфейс с методами, связанными с {@link CustomUsers}
 * </pre>
 */
public interface UserActions {

    /**
     * Метод для указания {@link CustomUsers} автора/исполнителя задачи
     *
     * @param customUsers
     * @param newTasks
     * @param typeOperations
     * @return {@link Tasks} с проставленным автором/исполнителем задачи
     * @throws UsernameNotFoundException
     */
    Tasks checkFindUser(CustomUsers customUsers, Tasks newTasks, Integer typeOperations) throws UsernameNotFoundException;

    /**
     * Метод для получения текущего авторизованного пользователя
     *
     * @return Возвращает текущего авторизованного пользователя из SecurityContextHolder
     */
    Optional<CustomUsers> getCurrentUser();

    /**
     * Метод для получения емейла текущего пользователя
     *
     * @return Возвращает емейл текущего авторизованного пользователя
     */
    String getEmailCurrentUser();

    /**
     * Метод для поиска пользователя в БД по id или e-mail-адресу
     *
     * @param customUsers
     * @return Возвращает найденного в БД пользователя {@link CustomUsers}
     * @throws UsernameNotFoundException
     */
    Optional<CustomUsers> searchUserEmailOrId(CustomUsers customUsers) throws UsernameNotFoundException;
}
