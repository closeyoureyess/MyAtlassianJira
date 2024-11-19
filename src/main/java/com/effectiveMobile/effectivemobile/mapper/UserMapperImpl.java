package com.effectiveMobile.effectivemobile.mapper;

import com.effectiveMobile.effectivemobile.dto.CustomUsersDto;
import com.effectiveMobile.effectivemobile.entities.CustomUsers;
import org.springframework.stereotype.Component;

@Component
public class UserMapperImpl implements UserMapper{

    @Override
    public CustomUsers convertDtoToUser(CustomUsersDto userDto) {
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
