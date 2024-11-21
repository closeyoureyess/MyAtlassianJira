package com.effectiveMobile.effectivemobile.mapper;

import com.effectiveMobile.effectivemobile.dto.CustomUsersDto;
import com.effectiveMobile.effectivemobile.dto.TasksDto;
import com.effectiveMobile.effectivemobile.entities.CustomUsers;
import com.effectiveMobile.effectivemobile.entities.Tasks;

/**
 * <pre>
 *     Маппер для {@link CustomUsers}, {@link CustomUsersDto}
 * </pre>
 */
public interface UserMapper {

    /**
     * Метод, конвертирующий CustomUsersDto в CustomUsers
     * @param userDto
     * @return сконвертированный {@link CustomUsers}
     */
    CustomUsers convertDtoToUser(CustomUsersDto userDto);

    /**
     * Метод, конвертирующий CustomUsers в CustomUsersDto
     * @param users
     * @return сконвертированный {@link CustomUsersDto}
     */
    CustomUsersDto convertUserToDto(CustomUsers users);
}
