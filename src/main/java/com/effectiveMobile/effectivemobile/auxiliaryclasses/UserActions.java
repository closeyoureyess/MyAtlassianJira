package com.effectiveMobile.effectivemobile.auxiliaryclasses;

import com.effectiveMobile.effectivemobile.dto.CustomUsersDto;
import com.effectiveMobile.effectivemobile.entities.CustomUsers;
import com.effectiveMobile.effectivemobile.entities.Tasks;
import com.effectiveMobile.effectivemobile.exeptions.RoleNotFoundException;
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
     * @param customUsers Объект {@link CustomUsers}, содержащий данные пользователя.
     * @param newTasks Объект задачи {@link Tasks}, в котором устанавливается автор или исполнитель.
     * @param typeOperations Целое число, указывающее тип операции: 0 - автор, 1 - исполнитель.
     * @return {@link Tasks} с установленным автором или исполнителем.
     * @throws UsernameNotFoundException Если пользователь не найден в базе данных.
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
     * @param customUsers Объект {@link CustomUsers}, содержащий ID или email для поиска.
     * @return {@link Optional<CustomUsers>} с найденным пользователем, если он существует.
     * @throws UsernameNotFoundException Если пользователь с заданным ID или email не найден.
     */
    Optional<CustomUsers> searchUserEmailOrId(CustomUsers customUsers) throws UsernameNotFoundException;

    /**
     * Метод, сравнивающий, совпадает ли переданный пользователь с авторизованным
     *
     * @param customUsersDto DTO объекта пользователя задачи для сравнения.
     * @return true, если email совпадает, иначе false.
     */
    boolean comparisonEmailTasksFromDBAndEmailCurrentAuthUser(CustomUsersDto customUsersDto);

    /**
     * Метод, позволяющий сравнить роль авторизованного пользователя с переданной в метод
     *
     * @param roleToMatch
     * @return Возвращает true, если роль текущего авторизованного пользователя соответствует переданной, если наоборот - false
     * @throws RoleNotFoundException
     */
    boolean currentUserAdminOrUserRole(String roleToMatch) throws RoleNotFoundException;

    /**
     * Метод, скрывающий пароль, если пользователь передал дто в сервисные классы, не касающиеся авторизации/регистрации
     * с заполненным паролем
     *
     * @param customUsersDto
     * @return {@link CustomUsersDto} со скрытым паролем
     */
    CustomUsersDto hiddenPassword(CustomUsersDto customUsersDto);
}
