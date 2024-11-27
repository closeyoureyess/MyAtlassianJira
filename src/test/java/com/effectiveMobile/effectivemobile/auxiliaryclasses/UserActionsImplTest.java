package com.effectiveMobile.effectivemobile.auxiliaryclasses;

import com.effectiveMobile.effectivemobile.constants.ConstantsClass;
import com.effectiveMobile.effectivemobile.dto.CustomUsersDto;
import com.effectiveMobile.effectivemobile.entities.CustomUsers;
import com.effectiveMobile.effectivemobile.entities.Tasks;
import com.effectiveMobile.effectivemobile.exeptions.DescriptionUserExeption;
import com.effectiveMobile.effectivemobile.exeptions.RoleNotFoundException;
import com.effectiveMobile.effectivemobile.repository.AuthorizationRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collection;
import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UserActionsImplTest {

    @Mock
    private AuthorizationRepository authorizationRepository;

    @Mock
    private TasksActions tasksActions;

    @InjectMocks
    private UserActionsImpl userActionsImpl;

    private CustomUsers customUserByEmail;
    private CustomUsers customUserById;

    @BeforeEach
    void setUp() {
        // Инициализация тестовых пользователей
        customUserByEmail = new CustomUsers();
        customUserByEmail.setId(1);
        customUserByEmail.setEmail("user@example.com");
        customUserByEmail.setPasswordKey("password");
        customUserByEmail.setRole(com.effectiveMobile.effectivemobile.other.UserRoles.USER);

        customUserById = new CustomUsers();
        customUserById.setId(2);
        customUserById.setEmail("anotheruser@example.com");
        customUserById.setPasswordKey("password123");
        customUserById.setRole(com.effectiveMobile.effectivemobile.other.UserRoles.ADMIN);
    }

    /**
     * Тест для метода checkFindUser
     * Сценарий: customUsers имеет только email, пользователь найден, typeOperations = ONE_FLAG
     */
    @Test
    @DisplayName("checkFindUser: пользователь найден по email, typeOperations = ONE_FLAG")
    void testCheckFindUser_FoundByEmail_TypeOneFlag() throws Exception {
        // Arrange
        CustomUsers inputUser = new CustomUsers();
        inputUser.setEmail("user@example.com");
        inputUser.setId(null);

        Tasks newTasks = new Tasks();
        Integer typeOperations = ConstantsClass.ONE_FLAG;

        Mockito.when(authorizationRepository.findByEmail("user@example.com"))
                .thenReturn(Optional.of(customUserByEmail));

        // Act
        Tasks result = userActionsImpl.checkFindUser(inputUser, newTasks, typeOperations);
        CustomUsers taskExecutorResult = result.getTaskExecutor();
        CustomUsers taskAuthorResult = result.getTaskAuthor();

        // Assert
        Assertions.assertNotNull(result, "Результат не должен быть null");
        Assertions.assertEquals(customUserByEmail, taskExecutorResult, "taskExecutor должен быть установлен");
        Assertions.assertNull(taskAuthorResult, "taskAuthor должен оставаться null");
        Mockito.verify(authorizationRepository, Mockito.times(1)).findByEmail("user@example.com");
    }

    /**
     * Тест для метода checkFindUser
     * Сценарий: customUsers имеет только id, пользователь найден, typeOperations = ZERO_FLAG
     */
    @Test
    @DisplayName("checkFindUser: пользователь найден по id, typeOperations = ZERO_FLAG")
    void testCheckFindUser_FoundById_TypeZeroFlag() throws Exception {
        // Arrange
        CustomUsers inputUser = new CustomUsers();
        inputUser.setEmail(null);
        inputUser.setId(2);

        Tasks newTasks = new Tasks();
        Integer typeOperations = ConstantsClass.ZERO_FLAG;

        Mockito.when(authorizationRepository.findById(2))
                .thenReturn(Optional.of(customUserById));

        // Act
        Tasks result = userActionsImpl.checkFindUser(inputUser, newTasks, typeOperations);

        // Assert
        Assertions.assertNotNull(result, "Результат не должен быть null");
        Assertions.assertEquals(customUserById, result.getTaskAuthor(), "taskAuthor должен быть установлен");
        Assertions.assertNull(result.getTaskExecutor(), "taskExecutor должен оставаться null");
        Mockito.verify(authorizationRepository, Mockito.times(1)).findById(2);
    }

    /**
     * Тест для метода checkFindUser
     * Сценарий: customUsers не найден, ожидается UsernameNotFoundException
     */
    @Test
    @DisplayName("checkFindUser: пользователь не найден, выбрасывается UsernameNotFoundException")
    void testCheckFindUser_UserNotFound() {
        // Arrange
        CustomUsers inputUser = new CustomUsers();
        inputUser.setEmail("nonexistent@example.com");
        inputUser.setId(null);

        Tasks newTasks = new Tasks();
        Integer typeOperations = ConstantsClass.ONE_FLAG;

        Mockito.when(authorizationRepository.findByEmail("nonexistent@example.com"))
                .thenReturn(Optional.empty());

        // Act & Assert
        UsernameNotFoundException exception = Assertions.assertThrows(
                UsernameNotFoundException.class,
                () -> userActionsImpl.checkFindUser(inputUser, newTasks, typeOperations),
                "Должен быть выброшен UsernameNotFoundException"
        );

        Assertions.assertEquals(DescriptionUserExeption.USER_NOT_FOUND.getEnumDescription(), exception.getMessage(),
                "Сообщение об ошибке должно соответствовать ожидаемому");
        Mockito.verify(authorizationRepository, Mockito.times(1)).findByEmail("nonexistent@example.com");
    }

    /**
     * Тест для метода getCurrentUser
     * Сценарий: пользователь найден по email
     */
    @Test
    @DisplayName("getCurrentUser: пользователь найден по email")
    void testGetCurrentUser_UserFound() {
        // Arrange
        Authentication mockAuth = Mockito.mock(Authentication.class);
        Mockito.when(mockAuth.getName()).thenReturn("user@example.com");

        SecurityContext mockContext = Mockito.mock(SecurityContext.class);
        Mockito.when(mockContext.getAuthentication()).thenReturn(mockAuth);

        // Используем MockedStatic для SecurityContextHolder
        try (MockedStatic<SecurityContextHolder> mockedSecurityContextHolder = Mockito.mockStatic(SecurityContextHolder.class)) {
            mockedSecurityContextHolder.when(SecurityContextHolder::getContext).thenReturn(mockContext);

            Mockito.when(authorizationRepository.findByEmail("user@example.com"))
                    .thenReturn(Optional.of(customUserByEmail));

            // Act
            Optional<CustomUsers> result = userActionsImpl.getCurrentUser();

            // Assert
            Assertions.assertTrue(result.isPresent(), "Пользователь должен быть найден");
            Assertions.assertEquals(customUserByEmail, result.get(), "Найденный пользователь должен соответствовать ожидаемому");
            Mockito.verify(authorizationRepository, Mockito.times(1)).findByEmail("user@example.com");
        }
    }

    /**
     * Тест для метода getCurrentUser
     * Сценарий: пользователь не найден по email
     */
    @Test
    @DisplayName("getCurrentUser: пользователь не найден по email")
    void testGetCurrentUser_UserNotFound() {
        // Arrange
        Authentication mockAuth = Mockito.mock(Authentication.class);
        Mockito.when(mockAuth.getName()).thenReturn("unknown@example.com");

        SecurityContext mockContext = Mockito.mock(SecurityContext.class);
        Mockito.when(mockContext.getAuthentication()).thenReturn(mockAuth);

        try (MockedStatic<SecurityContextHolder> mockedSecurityContextHolder = Mockito.mockStatic(SecurityContextHolder.class)) {
            mockedSecurityContextHolder.when(SecurityContextHolder::getContext).thenReturn(mockContext);

            Mockito.when(authorizationRepository.findByEmail("unknown@example.com"))
                    .thenReturn(Optional.empty());

            // Act
            Optional<CustomUsers> result = userActionsImpl.getCurrentUser();

            // Assert
            Assertions.assertFalse(result.isPresent(), "Пользователь не должен быть найден");
            Mockito.verify(authorizationRepository, Mockito.times(1)).findByEmail("unknown@example.com");
        }
    }

    /**
     * Тест для метода getRoleCurrentAuthorizedUser
     * Сценарий: пользователь имеет запрашиваемую роль
     */
    @Test
    @DisplayName("getRoleCurrentAuthorizedUser: пользователь имеет запрашиваемую роль")
    void testGetRoleCurrentAuthorizedUser_HasRole() {
        // Arrange
        String roleToMatch = "ADMIN";
        String fullRole = "ROLE_ADMIN";

        GrantedAuthority mockAuthority = Mockito.mock(GrantedAuthority.class);
        Mockito.when(mockAuthority.getAuthority()).thenReturn(fullRole);

        Authentication mockAuth = Mockito.mock(Authentication.class);
        // Исправление: используем Collections.singletonList вместо Arrays.asList
        Collection<GrantedAuthority> authCollection = Collections.singleton(mockAuthority);
        Mockito.when(mockAuth.getAuthorities()).thenReturn((Collection) authCollection);

        SecurityContext mockContext = Mockito.mock(SecurityContext.class);
        Mockito.when(mockContext.getAuthentication()).thenReturn(mockAuth);

        try (MockedStatic<SecurityContextHolder> mockedSecurityContextHolder = Mockito.mockStatic(SecurityContextHolder.class)) {
            mockedSecurityContextHolder.when(SecurityContextHolder::getContext).thenReturn(mockContext);

            // Act
            Optional<String> result = userActionsImpl.getRoleCurrentAuthorizedUser("ADMIN");

            // Assert
            Assertions.assertTrue(result.isPresent(), "Роль должна быть найдена");
            Assertions.assertEquals(fullRole, result.get(), "Роль должна соответствовать ожидаемой");
        }
    }

    /**
     * Тест для метода getRoleCurrentAuthorizedUser
     * Сценарий: пользователь не имеет запрашиваемой роли
     */
    @Test
    @DisplayName("getRoleCurrentAuthorizedUser: пользователь не имеет запрашиваемой роли")
    void testGetRoleCurrentAuthorizedUser_DoesNotHaveRole() {
        // Arrange
        String roleToMatch = "ADMIN";
        String fullRole = "ROLE_USER";

        GrantedAuthority mockAuthority = Mockito.mock(GrantedAuthority.class);
        Mockito.when(mockAuthority.getAuthority()).thenReturn(fullRole);

        Authentication mockAuth = Mockito.mock(Authentication.class);
        Collection<GrantedAuthority> authCollection = Collections.singleton(mockAuthority);
        Mockito.when(mockAuth.getAuthorities()).thenReturn((Collection) authCollection);

        SecurityContext mockContext = Mockito.mock(SecurityContext.class);
        Mockito.when(mockContext.getAuthentication()).thenReturn(mockAuth);

        try (MockedStatic<SecurityContextHolder> mockedSecurityContextHolder = Mockito.mockStatic(SecurityContextHolder.class)) {
            mockedSecurityContextHolder.when(SecurityContextHolder::getContext).thenReturn(mockContext);

            // Act
            Optional<String> result = userActionsImpl.getRoleCurrentAuthorizedUser("ADMIN");

            // Assert
            Assertions.assertFalse(result.isPresent(), "Роль не должна быть найдена");
        }
    }

    /**
     * Тест для метода getEmailCurrentUser
     * Сценарий: пользователь найден, возвращается email
     */
    @Test
    @DisplayName("getEmailCurrentUser: пользователь найден, возвращается email")
    void testGetEmailCurrentUser_UserFound() {
        // Arrange
        Authentication mockAuth = Mockito.mock(Authentication.class);
        Mockito.when(mockAuth.getName()).thenReturn("user@example.com");

        SecurityContext mockContext = Mockito.mock(SecurityContext.class);
        Mockito.when(mockContext.getAuthentication()).thenReturn(mockAuth);

        try (MockedStatic<SecurityContextHolder> mockedSecurityContextHolder = Mockito.mockStatic(SecurityContextHolder.class)) {
            mockedSecurityContextHolder.when(SecurityContextHolder::getContext).thenReturn(mockContext);

            Mockito.when(authorizationRepository.findByEmail("user@example.com"))
                    .thenReturn(Optional.of(customUserByEmail));

            // Act
            String email = userActionsImpl.getEmailCurrentUser();

            // Assert
            Assertions.assertEquals("user@example.com", email, "Email должен соответствовать ожидаемому");
            Mockito.verify(authorizationRepository, Mockito.times(1)).findByEmail("user@example.com");
        }
    }

    /**
     * Тест для метода getEmailCurrentUser
     * Сценарий: пользователь не найден, ожидается NoSuchElementException
     */
    @Test
    @DisplayName("getEmailCurrentUser: пользователь не найден, выбрасывается NoSuchElementException")
    void testGetEmailCurrentUser_UserNotFound() {
        // Arrange
        Authentication mockAuth = Mockito.mock(Authentication.class);
        Mockito.when(mockAuth.getName()).thenReturn("unknown@example.com");

        SecurityContext mockContext = Mockito.mock(SecurityContext.class);
        Mockito.when(mockContext.getAuthentication()).thenReturn(mockAuth);

        try (MockedStatic<SecurityContextHolder> mockedSecurityContextHolder = Mockito.mockStatic(SecurityContextHolder.class)) {
            mockedSecurityContextHolder.when(SecurityContextHolder::getContext).thenReturn(mockContext);

            Mockito.when(authorizationRepository.findByEmail("unknown@example.com"))
                    .thenReturn(Optional.empty());

            // Act & Assert
            NoSuchElementException exception = Assertions.assertThrows(
                    NoSuchElementException.class,
                    () -> userActionsImpl.getEmailCurrentUser(),
                    "Должен быть выброшен NoSuchElementException"
            );

            Mockito.verify(authorizationRepository, Mockito.times(1)).findByEmail("unknown@example.com");
        }
    }

    /**
     * Тест для метода searchUserEmailOrId
     * Сценарий: customUsers имеет email, пользователь найден
     */
    @Test
    @DisplayName("searchUserEmailOrId: customUsers имеет email, пользователь найден")
    void testSearchUserEmailOrId_ByEmail_UserFound() {
        // Arrange
        CustomUsers inputUser = new CustomUsers();
        inputUser.setEmail("user@example.com");
        inputUser.setId(null);

        Mockito.when(authorizationRepository.findByEmail("user@example.com"))
                .thenReturn(Optional.of(customUserByEmail));

        // Act
        Optional<CustomUsers> result = userActionsImpl.searchUserEmailOrId(inputUser);

        // Assert
        Assertions.assertTrue(result.isPresent(), "Пользователь должен быть найден");
        Assertions.assertEquals(customUserByEmail, result.get(), "Найденный пользователь должен соответствовать ожидаемому");
        Mockito.verify(authorizationRepository, Mockito.times(1)).findByEmail("user@example.com");
        Mockito.verify(authorizationRepository, Mockito.never()).findById(Mockito.anyInt());
    }

    /**
     * Тест для метода searchUserEmailOrId
     * Сценарий: customUsers имеет id, пользователь найден
     */
    @Test
    @DisplayName("searchUserEmailOrId: customUsers имеет id, пользователь найден")
    void testSearchUserEmailOrId_ById_UserFound() {
        // Arrange
        CustomUsers inputUser = new CustomUsers();
        inputUser.setEmail(null);
        inputUser.setId(2);

        Mockito.when(authorizationRepository.findById(2))
                .thenReturn(Optional.of(customUserById));

        // Act
        Optional<CustomUsers> result = userActionsImpl.searchUserEmailOrId(inputUser);

        // Assert
        Assertions.assertTrue(result.isPresent(), "Пользователь должен быть найден");
        Assertions.assertEquals(customUserById, result.get(), "Найденный пользователь должен соответствовать ожидаемому");
        Mockito.verify(authorizationRepository, Mockito.times(1)).findById(2);
        Mockito.verify(authorizationRepository, Mockito.never()).findByEmail(Mockito.anyString());
    }

    /**
     * Тест для метода searchUserEmailOrId
     * Сценарий: customUsers имеет оба поля email и id, поиск по id, пользователь найден
     */
    @Test
    @DisplayName("searchUserEmailOrId: customUsers имеет email и id, поиск по id, пользователь найден")
    void testSearchUserEmailOrId_ByIdAndEmail_UserFoundById() {
        // Arrange
        CustomUsers inputUser = new CustomUsers();
        inputUser.setEmail("user@example.com");
        inputUser.setId(1);

        Mockito.when(authorizationRepository.findById(1))
                .thenReturn(Optional.of(customUserByEmail));

        // Act
        Optional<CustomUsers> result = userActionsImpl.searchUserEmailOrId(inputUser);

        // Assert
        Assertions.assertTrue(result.isPresent(), "Пользователь должен быть найден");
        Assertions.assertEquals(customUserByEmail, result.get(), "Найденный пользователь должен соответствовать ожидаемому");
        Mockito.verify(authorizationRepository, Mockito.times(1)).findById(1);
        Mockito.verify(authorizationRepository, Mockito.never()).findByEmail(Mockito.anyString());
    }

    /**
     * Тест для метода searchUserEmailOrId
     * Сценарий: customUsers имеет email, пользователь не найден
     */
    @Test
    @DisplayName("searchUserEmailOrId: customUsers имеет email, пользователь не найден")
    void testSearchUserEmailOrId_ByEmail_UserNotFound() {
        // Arrange
        CustomUsers inputUser = new CustomUsers();
        inputUser.setEmail("unknown@example.com");
        inputUser.setId(null);

        Mockito.when(authorizationRepository.findByEmail("unknown@example.com"))
                .thenReturn(Optional.empty());

        // Act
        Optional<CustomUsers> result = userActionsImpl.searchUserEmailOrId(inputUser);

        // Assert
        Assertions.assertFalse(result.isPresent(), "Пользователь не должен быть найден");
        Mockito.verify(authorizationRepository, Mockito.times(1)).findByEmail("unknown@example.com");
        Mockito.verify(authorizationRepository, Mockito.never()).findById(Mockito.anyInt());
    }

    /**
     * Тест для метода searchUserEmailOrId
     * Сценарий: customUsers null, возвращается Optional.empty()
     */
    @Test
    @DisplayName("searchUserEmailOrId: customUsers null, возвращается Optional.empty()")
    void testSearchUserEmailOrId_NullInput() {
        // Arrange
        CustomUsers inputUser = null;

        // Act
        Optional<CustomUsers> result = userActionsImpl.searchUserEmailOrId(inputUser);

        // Assert
        Assertions.assertFalse(result.isPresent(), "Результат должен быть Optional.empty()");
        Mockito.verifyNoInteractions(authorizationRepository);
    }

    /**
     * Тест для метода comparisonEmailTasksFromDBAndEmailCurrentAuthUser
     * Сценарий: email совпадают
     */
    @Test
    @DisplayName("comparisonEmailTasksFromDBAndEmailCurrentAuthUser: email совпадают")
    void testComparisonEmailTasksFromDBAndEmailCurrentAuthUser_Match() {
        // Arrange
        CustomUsersDto customUsersDto = new CustomUsersDto();
        customUsersDto.setEmail("user@example.com");

        Authentication mockAuth = Mockito.mock(Authentication.class);
        Mockito.when(mockAuth.getName()).thenReturn("user@example.com");

        SecurityContext mockContext = Mockito.mock(SecurityContext.class);
        Mockito.when(mockContext.getAuthentication()).thenReturn(mockAuth);

        try (MockedStatic<SecurityContextHolder> mockedSecurityContextHolder = Mockito.mockStatic(SecurityContextHolder.class)) {
            mockedSecurityContextHolder.when(SecurityContextHolder::getContext).thenReturn(mockContext);

            // Act
            boolean result = userActionsImpl.comparisonEmailTasksFromDBAndEmailCurrentAuthUser(customUsersDto);

            // Assert
            Assertions.assertTrue(result, "Должно возвращаться true, так как email совпадают");
        }
    }

    /**
     * Тест для метода comparisonEmailTasksFromDBAndEmailCurrentAuthUser
     * Сценарий: email не совпадают
     */
    @Test
    @DisplayName("comparisonEmailTasksFromDBAndEmailCurrentAuthUser: email не совпадают")
    void testComparisonEmailTasksFromDBAndEmailCurrentAuthUser_NoMatch() {
        // Arrange
        CustomUsersDto customUsersDto = new CustomUsersDto();
        customUsersDto.setEmail("different@example.com");

        Authentication mockAuth = Mockito.mock(Authentication.class);
        Mockito.when(mockAuth.getName()).thenReturn("user@example.com");

        SecurityContext mockContext = Mockito.mock(SecurityContext.class);
        Mockito.when(mockContext.getAuthentication()).thenReturn(mockAuth);

        try (MockedStatic<SecurityContextHolder> mockedSecurityContextHolder = Mockito.mockStatic(SecurityContextHolder.class)) {
            mockedSecurityContextHolder.when(SecurityContextHolder::getContext).thenReturn(mockContext);

            // Act
            boolean result = userActionsImpl.comparisonEmailTasksFromDBAndEmailCurrentAuthUser(customUsersDto);

            // Assert
            Assertions.assertFalse(result, "Должно возвращаться false, так как email не совпадают");
        }
    }

    /**
     * Тест для метода currentUserAdminOrUserRole
     * Сценарий: пользователь имеет запрашиваемую роль
     */
    @Test
    @DisplayName("currentUserAdminOrUserRole: пользователь имеет запрашиваемую роль")
    void testCurrentUserAdminOrUserRole_HasRole() throws RoleNotFoundException {
        // Arrange
        String roleToMatch = "ADMIN";
        String fullRole = "ROLE_ADMIN";

        GrantedAuthority mockAuthority = Mockito.mock(GrantedAuthority.class);
        Mockito.when(mockAuthority.getAuthority()).thenReturn(fullRole);

        Authentication mockAuth = Mockito.mock(Authentication.class);
        Collection<GrantedAuthority> authCollection = Collections.singleton(mockAuthority);
        Mockito.when(mockAuth.getAuthorities()).thenReturn((Collection) authCollection);

        SecurityContext mockContext = Mockito.mock(SecurityContext.class);
        Mockito.when(mockContext.getAuthentication()).thenReturn(mockAuth);

        try (MockedStatic<SecurityContextHolder> mockedSecurityContextHolder = Mockito.mockStatic(SecurityContextHolder.class)) {
            mockedSecurityContextHolder.when(SecurityContextHolder::getContext).thenReturn(mockContext);

            // Act
            boolean result = userActionsImpl.currentUserAdminOrUserRole(roleToMatch);

            // Assert
            Assertions.assertTrue(result, "Пользователь должен иметь роль ADMIN");
        }
    }

    /**
     * Тест для метода currentUserAdminOrUserRole
     * Сценарий: пользователь не имеет запрашиваемой роли
     */
    @Test
    @DisplayName("currentUserAdminOrUserRole: пользователь не имеет запрашиваемую роль")
    void testCurrentUserAdminOrUserRole_DoesNotHaveRole() {
        // Arrange
        String roleToMatch = "ADMIN";
        String fullRole = "ROLE_USER";
        Optional<String> roleCurrentAuthorizedUser = Optional.empty();

        UserActionsImpl spyUserActionsImpl = Mockito.spy(userActionsImpl);
        Mockito.doReturn(roleCurrentAuthorizedUser).when(spyUserActionsImpl).getRoleCurrentAuthorizedUser(roleToMatch);
        /*Mockito.when(userActionsImpl.getRoleCurrentAuthorizedUser(roleToMatch)).thenReturn(Optional.empty());*/
        // Act
        boolean result = spyUserActionsImpl.currentUserAdminOrUserRole(roleToMatch);

        // Assert
        Assertions.assertFalse(result, "Пользователь не должен иметь роль ADMIN");
    }

    /**
     * Тест для метода currentUserAdminOrUserRole
     * Сценарий: пользователь не имеет запрашиваемую роль, выбрасывается RoleNotFoundException
     */
    /*@Test
    @DisplayName("currentUserAdminOrUserRole: пользователь не имеет запрашиваемую роль, выбрасывается RoleNotFoundException")
    void testCurrentUserAdminOrUserRole_RoleNotFound() {
        // Arrange
        String roleToMatch = "MANAGER";
        String fullRole = "ROLE_USER";

        GrantedAuthority mockAuthority = Mockito.mock(GrantedAuthority.class);
        Mockito.when(mockAuthority.getAuthority()).thenReturn(fullRole);

        Authentication mockAuth = Mockito.mock(Authentication.class);
        Collection<GrantedAuthority> authCollection = Collections.singleton(mockAuthority);
        Mockito.when(mockAuth.getAuthorities()).thenReturn((Collection) authCollection);

        SecurityContext mockContext = Mockito.mock(SecurityContext.class);
        Mockito.when(mockContext.getAuthentication()).thenReturn(mockAuth);

        try (MockedStatic<SecurityContextHolder> mockedSecurityContextHolder = Mockito.mockStatic(SecurityContextHolder.class)) {
            mockedSecurityContextHolder.when(SecurityContextHolder::getContext).thenReturn(mockContext);

            // Act & Assert
            RoleNotFoundException exception = Assertions.assertDoesNotThrow(
                    RoleNotFoundException.class,
                    () -> userActionsImpl.currentUserAdminOrUserRole(roleToMatch),
                    "Должен быть выброшен RoleNotFoundException"
            );

            Assertions.assertEquals(DescriptionUserExeption.INNER_ERROR.getEnumDescription(), exception.getMessage(),
                    "Сообщение об ошибке должно соответствовать ожидаемому");
        }
    }*/
}