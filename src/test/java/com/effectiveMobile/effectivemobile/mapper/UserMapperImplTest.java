package com.effectiveMobile.effectivemobile.mapper;

import com.effectiveMobile.effectivemobile.dto.CustomUsersDto;
import com.effectiveMobile.effectivemobile.entities.CustomUsers;
import com.effectiveMobile.effectivemobile.other.UserRoles;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserMapperImplTest {

    private UserMapperImpl mapper;

    @BeforeEach
    void setUp() {
        mapper = new UserMapperImpl();
    }

    @Test
    @DisplayName("Тест: Конвертация DTO в CustomUsers")
    void testConvertDtoToUser() {
        CustomUsersDto dto = new CustomUsersDto(1, "test@example.com", "password", UserRoles.USER);
        CustomUsers entity = mapper.convertDtoToUser(dto);

        int expected = dto.getId();
        int result = entity.getId();
        Assertions.assertEquals(expected, result);

        String emailExpected = dto.getEmail();
        String emailResult = entity.getEmail();
        Assertions.assertEquals(emailExpected, emailResult);

        String passwordKeyExpected = dto.getPasswordKey();
        String passwordKeyResult = entity.getPasswordKey();
        Assertions.assertEquals(passwordKeyExpected, passwordKeyResult);

        UserRoles roleExpected = dto.getRole();
        UserRoles roleResult = entity.getRole();
        Assertions.assertEquals(roleExpected, roleResult);
    }

    @Test
    @DisplayName("Тест: Конвертация CustomUsers в DTO")
    void testConvertUserToDto() {
        CustomUsers entity = CustomUsers.builder().id(1)
                .email("test@example.com").passwordKey("password").role(UserRoles.USER).build(); //new CustomUsers(1, "test@example.com", "password", UserRoles.USER);
        CustomUsersDto dto = mapper.convertUserToDto(entity);

        Assertions.assertEquals(entity.getId(), dto.getId());
        Assertions.assertEquals(entity.getEmail(), dto.getEmail());
        Assertions.assertEquals(entity.getPasswordKey(), dto.getPasswordKey());
        Assertions.assertEquals(entity.getRole(), dto.getRole());
    }
}
