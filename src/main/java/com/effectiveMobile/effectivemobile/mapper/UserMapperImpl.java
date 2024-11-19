package com.effectiveMobile.effectivemobile.mapper;

import com.effectiveMobile.effectivemobile.dto.CustomUsersDto;
import com.effectiveMobile.effectivemobile.entities.CustomUsers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserMapperImpl implements UserMapper{

    @Override
    public CustomUsers convertDtoToUser(CustomUsersDto userDto) {
        log.info("Метод convertDtoToUser()");
        CustomUsers usersLocalObject = new CustomUsers(); // проверка на null
        if (userDto != null) {
            usersLocalObject.setEmail(userDto.getEmail());
            usersLocalObject.setPasswordKey(userDto.getPasswordKey());
            usersLocalObject.setId(userDto.getId());
            usersLocalObject.setRole(userDto.getRole());
        }
        return usersLocalObject;
    }

    @Override
    public CustomUsersDto convertUserToDto(CustomUsers users) {
        log.info("Метод convertUserToDto()");
        CustomUsersDto userDtoLocalObject = new CustomUsersDto();
        if (users != null) {
            userDtoLocalObject.setEmail(users.getEmail());
            userDtoLocalObject.setPasswordKey(users.getPasswordKey());
            userDtoLocalObject.setId(users.getId());
            userDtoLocalObject.setRole(users.getRole());
        }
        return userDtoLocalObject;
    }
}
