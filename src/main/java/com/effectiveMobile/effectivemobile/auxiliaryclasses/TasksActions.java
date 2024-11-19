package com.effectiveMobile.effectivemobile.auxiliaryclasses;

import com.effectiveMobile.effectivemobile.dto.CustomUsersDto;
import com.effectiveMobile.effectivemobile.entities.CustomUsers;
import com.effectiveMobile.effectivemobile.entities.Tasks;

/**
 * <pre>
 *     Интерфейс с методами, связанными с {@link Tasks}
 * </pre>
 */
public interface TasksActions {

    /**
     * @param objectInt
     * @param constantsInt
     * @return
     */
    boolean compareIntWithConstants(Integer objectInt, Integer constantsInt);

    /**
     * Метод, сравнивающий, совпадает ли переданный пользователь с авторизованным
     * @param customUsersDto
     * @return
     */
    boolean isPrivilegeTasks(CustomUsersDto customUsersDto);
}
