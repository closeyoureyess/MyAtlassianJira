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
     * Метод для получения объекта CustomersUsers по емейлу текущего авторизрванного пользователя
     *
     * @return Возвращает CustomersUsers из БД по e-mail текущего авторизованного пользователя
     */
    Optional<CustomUsers> getCurrentUser();

    /**
     * Метод, позволяющий узнать роль текущего авторизованного пользователя без запроса в БД
     *
     * @return {@link String} роль текущего авторизованного пользователя
     */
    Optional<String> getRoleCurrentAuthorizedUser(String roleToMatch);

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
