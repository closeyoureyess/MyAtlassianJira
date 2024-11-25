package com.effectiveMobile.effectivemobile.mapper;

import com.effectiveMobile.effectivemobile.dto.CustomUsersDto;
import com.effectiveMobile.effectivemobile.entities.CustomUsers;
import com.effectiveMobile.effectivemobile.other.UserRoles;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

        assertEquals(dto.getId(), entity.getId());
        assertEquals(dto.getEmail(), entity.getEmail());
        assertEquals(dto.getPasswordKey(), entity.getPasswordKey());
        assertEquals(dto.getRole(), entity.getRole());
    }

    @Test
    @DisplayName("Тест: Конвертация CustomUsers в DTO")
    void testConvertUserToDto() {
        CustomUsers entity = CustomUsers.builder().id(1)
                .email("test@example.com").passwordKey("password").role(UserRoles.USER).build(); //new CustomUsers(1, "test@example.com", "password", UserRoles.USER);
        CustomUsersDto dto = mapper.convertUserToDto(entity);

        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getEmail(), dto.getEmail());
        assertEquals(entity.getPasswordKey(), dto.getPasswordKey());
        assertEquals(entity.getRole(), dto.getRole());
    }
}
