package com.effectiveMobile.effectivemobile.services;

import com.effectiveMobile.effectivemobile.entities.CustomUsers;
import com.effectiveMobile.effectivemobile.exeptions.DescriptionUserExeption;
import com.effectiveMobile.effectivemobile.exeptions.UserNotFoundException;
import com.effectiveMobile.effectivemobile.other.UserRoles;
import com.effectiveMobile.effectivemobile.repository.AuthorizationRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static com.effectiveMobile.effectivemobile.constants.ConstantsClass.EMPTY_SPACE;

class MyUserDetailServiceTest {

    private MyUserDetailService userDetailService;
    private AuthorizationRepository mockRepository;

    @BeforeEach
    void setUp() {
        mockRepository = Mockito.mock(AuthorizationRepository.class);
        userDetailService = new MyUserDetailService();
        userDetailService.setAuthorizationRepository(mockRepository);
    }

    @Test
    @DisplayName("Тест: Загрузка данных пользователя по его email")
    void testLoadUserByUsername() throws UserNotFoundException {
        String email = "test@example.com";
        CustomUsers customUser = CustomUsers.builder()
                .id(1)
                .email(email)
                .passwordKey("password")
                .role(UserRoles.USER)
                .build();

        Mockito.when(mockRepository.findByEmail(email)).thenReturn(Optional.of(customUser));

        UserDetails userDetails = userDetailService.loadUserByUsername(email);

        Assertions.assertNotNull(userDetails, "UserDetails не должны быть null");
        Assertions.assertEquals(email, userDetails.getUsername(), "Email должен совпадать");
        Assertions.assertEquals(customUser.getPasswordKey(), userDetails.getPassword(), "Пароль должен совпадать");
        Assertions.assertTrue(userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER")), "Роль должна быть USER");
    }

    @Test
    @DisplayName("Тест: Выброс исключения, если пользователь не найден")
    void testLoadUserByUsernameNotFound() {
        String email = "nonexistent@example.com";

        Mockito.when(mockRepository.findByEmail(email)).thenReturn(Optional.empty());

        UserNotFoundException exception = Assertions.assertThrows(UserNotFoundException.class, () -> {
            userDetailService.loadUserByUsername(email);
        });

        Assertions.assertEquals(DescriptionUserExeption.USER_NOT_FOUND.getEnumDescription() +
                        EMPTY_SPACE + email, exception.getMessage(), "Сообщение об ошибке должно совпадать");
    }

    @Test
    @DisplayName("Тест: Получение роли пользователя по умолчанию")
    void testGetRolesDefault() {
        CustomUsers customUser = CustomUsers.builder()
                .id(1)
                .email("test@example.com")
                .passwordKey("password")
                .role(null)
                .build();

        String role = ReflectionTestUtils.invokeMethod(userDetailService, "getRoles", customUser);

        Assertions.assertEquals(UserRoles.USER.getUserRoles(), role, "Роль по умолчанию должна быть USER");
    }

    @Test
    @DisplayName("Тест: Получение роли пользователя")
    void testGetRoles() {
        CustomUsers customUser = CustomUsers.builder()
                .id(1)
                .email("test@example.com")
                .passwordKey("password")
                .role(UserRoles.ADMIN)
                .build();

        String role = ReflectionTestUtils.invokeMethod(userDetailService, "getRoles", customUser);

        Assertions.assertEquals(UserRoles.ADMIN.getUserRoles(), role, "Роль должна совпадать с указанной");
    }
}