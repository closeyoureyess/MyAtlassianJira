package com.effectiveMobile.effectivemobile.services;

import com.effectiveMobile.effectivemobile.auxiliaryclasses.UserActions;
import com.effectiveMobile.effectivemobile.dto.CustomUsersDto;
import com.effectiveMobile.effectivemobile.dto.RegistrationUsers;
import com.effectiveMobile.effectivemobile.entities.CustomUsers;
import com.effectiveMobile.effectivemobile.exeptions.DescriptionUserExeption;
import com.effectiveMobile.effectivemobile.fabrics.ActionsFabric;
import com.effectiveMobile.effectivemobile.fabrics.MappersFabric;
import com.effectiveMobile.effectivemobile.fabrics.ServiceFabric;
import com.effectiveMobile.effectivemobile.mapper.UserMapper;
import com.effectiveMobile.effectivemobile.other.UserRoles;
import com.effectiveMobile.effectivemobile.repository.AuthorizationRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

class UserServiceImplTest {

    @Mock
    private MappersFabric mockMappersFabric;

    @Mock
    private UserDetailsService mockUserDetailsService;

    @Mock
    private ServiceFabric mockServiceFabric;

    @Mock
    private ActionsFabric mockActionsFabric;

    @Mock
    private AuthorizationRepository mockAuthorizationRepository;

    @Mock
    private AuthenticationManager mockAuthenticationManager;

    @Mock
    private UserMapper mockUserMapper;

    @Mock
    private UserActions mockUserActions;

    @Mock
    private JwtService mockJwtService;

    @InjectMocks
    private UserServiceImpl userService;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Тестирование метода createUser при условии, что пользователь уже существует.
     */
    @Test
    @DisplayName("Тест: Создание пользователя, если пользователь уже существует")
    void testCreateUser_UserAlreadyExists() {
        // Подготовка данных
        CustomUsersDto inputDto = new CustomUsersDto();
        inputDto.setId(1);
        inputDto.setEmail("existing@example.com");
        inputDto.setPasswordKey("password123");
        inputDto.setRole(UserRoles.USER);

        CustomUsers existingUser = new CustomUsers();
        existingUser.setId(1);
        existingUser.setEmail("existing@example.com");
        existingUser.setPasswordKey(passwordEncoder.encode("password123"));
        existingUser.setRole(UserRoles.USER);

        CustomUsersDto existingDto = new CustomUsersDto();
        existingDto.setId(1);
        existingDto.setEmail("existing@example.com");
        existingDto.setPasswordKey("password123");
        existingDto.setRole(UserRoles.USER);

        // Настройка моков
        Mockito.when(mockMappersFabric.createUserMapper()).thenReturn(mockUserMapper);
        Mockito.when(mockUserMapper.convertDtoToUser(inputDto)).thenReturn(existingUser);
        Mockito.when(mockActionsFabric.createUserActions()).thenReturn(mockUserActions);
        Mockito.when(mockUserActions.searchUserEmailOrId(existingUser)).thenReturn(Optional.of(existingUser));
        Mockito.when(mockUserMapper.convertUserToDto(existingUser)).thenReturn(existingDto);

        // Вызов метода
        CustomUsersDto result = userService.createUser(inputDto);

        // Проверки
        Assertions.assertNotNull(result, "Результат не должен быть null");
        Assertions.assertEquals(existingDto, result, "Результат должен совпадать с существующим DTO");
        Mockito.verify(mockUserActions, Mockito.times(1)).searchUserEmailOrId(existingUser);
        Mockito.verify(mockAuthorizationRepository, Mockito.never()).save(Mockito.any(CustomUsers.class));
    }

    /**
     * Тестирование метода createUser при условии, что пользователь не существует и успешно создается.
     */
    @Test
    @DisplayName("Тест: Успешное создание нового пользователя")
    void testCreateUser_Success() {
        // Подготовка данных
        CustomUsersDto inputDto = new CustomUsersDto();
        inputDto.setId(null); // Новый пользователь
        inputDto.setEmail("newuser@example.com");
        inputDto.setPasswordKey("password123");
        inputDto.setRole(UserRoles.USER); // Уже корректная роль

        CustomUsers newUser = new CustomUsers();
        newUser.setId(null);
        newUser.setEmail("newuser@example.com");
        newUser.setPasswordKey("password123");
        newUser.setRole(UserRoles.USER);

        CustomUsers savedUser = new CustomUsers();
        savedUser.setId(2);
        savedUser.setEmail("newuser@example.com");
        savedUser.setPasswordKey(passwordEncoder.encode("password123"));
        savedUser.setRole(UserRoles.USER);

        CustomUsersDto savedDto = new CustomUsersDto();
        savedDto.setId(2);
        savedDto.setEmail("newuser@example.com");
        savedDto.setPasswordKey("password123");
        savedDto.setRole(UserRoles.USER);

        // Настройка моков
        Mockito.when(mockMappersFabric.createUserMapper()).thenReturn(mockUserMapper);
        Mockito.when(mockUserMapper.convertDtoToUser(inputDto)).thenReturn(newUser);
        Mockito.when(mockActionsFabric.createUserActions()).thenReturn(mockUserActions);
        Mockito.when(mockUserActions.searchUserEmailOrId(newUser)).thenReturn(Optional.empty());
        Mockito.when(mockAuthorizationRepository.save(newUser)).thenReturn(savedUser);
        Mockito.when(mockUserMapper.convertUserToDto(savedUser)).thenReturn(savedDto);

        // Вызов метода
        CustomUsersDto result = userService.createUser(inputDto);

        // Проверки
        Assertions.assertNotNull(result, "Результат не должен быть null");
        Assertions.assertEquals(savedDto, result, "Результат должен совпадать с сохраненным DTO");
        Mockito.verify(mockUserActions, Mockito.times(1)).searchUserEmailOrId(newUser);
        Mockito.verify(mockAuthorizationRepository, Mockito.times(1)).save(newUser);
    }

    /**
     * Тестирование метода createUser при условии, что роль пользователя некорректна и должна быть установлена USER.
     */
    @Test
    @DisplayName("Тест: Создание пользователя с некорректной ролью, должна быть установлена роль USER")
    void testCreateUser_IncorrectRole() {
        // Подготовка данных
        CustomUsersDto inputDto = new CustomUsersDto();
        inputDto.setId(null);
        inputDto.setEmail("anotheruser@example.com");
        inputDto.setPasswordKey("password123");
        inputDto.setRole(null); // Некорректная роль

        CustomUsers newUser = new CustomUsers();
        newUser.setId(null);
        newUser.setEmail("anotheruser@example.com");
        newUser.setPasswordKey("password123");
        newUser.setRole(UserRoles.USER); // Роль должна быть установлена

        CustomUsers savedUser = new CustomUsers();
        savedUser.setId(3);
        savedUser.setEmail("anotheruser@example.com");
        savedUser.setPasswordKey(passwordEncoder.encode("password123"));
        savedUser.setRole(UserRoles.USER);

        CustomUsersDto savedDto = new CustomUsersDto();
        savedDto.setId(3);
        savedDto.setEmail("anotheruser@example.com");
        savedDto.setPasswordKey("password123");
        savedDto.setRole(UserRoles.USER);

        // Настройка моков
        Mockito.when(mockMappersFabric.createUserMapper()).thenReturn(mockUserMapper);
        Mockito.when(mockUserMapper.convertDtoToUser(inputDto)).thenReturn(newUser);
        Mockito.when(mockActionsFabric.createUserActions()).thenReturn(mockUserActions);
        Mockito.when(mockUserActions.searchUserEmailOrId(newUser)).thenReturn(Optional.empty());
        Mockito.when(mockAuthorizationRepository.save(newUser)).thenReturn(savedUser);
        Mockito.when(mockUserMapper.convertUserToDto(savedUser)).thenReturn(savedDto);

        // Вызов метода
        CustomUsersDto result = userService.createUser(inputDto);

        // Проверки
        Assertions.assertNotNull(result, "Результат не должен быть null");
        Assertions.assertEquals(UserRoles.USER, result.getRole(), "Роль должна быть установлена как USER");
        Mockito.verify(mockUserActions, Mockito.times(1)).searchUserEmailOrId(newUser);
        Mockito.verify(mockAuthorizationRepository, Mockito.times(1)).save(newUser);
    }

    /**
     * Тестирование метода authorizationUser при успешной аутентификации.
     */
    @Test
    @DisplayName("Тест: Успешная авторизация пользователя")
    void testAuthorizationUser_Success() throws Exception {
        // Подготовка данных
        RegistrationUsers registrationUsers = new RegistrationUsers();
        registrationUsers.setEmail("authuser@example.com");
        registrationUsers.setPasswordKey("password123");

        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.isAuthenticated()).thenReturn(true);

        UserDetails mockUserDetails = Mockito.mock(UserDetails.class);
        Mockito.when(mockUserDetailsService.loadUserByUsername("authuser@example.com")).thenReturn(mockUserDetails);

        String mockToken = "mockJwtToken";

        // Настройка моков
        Mockito.when(mockAuthenticationManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        Mockito.when(mockServiceFabric.createJwtService()).thenReturn(mockJwtService);
        Mockito.when(mockJwtService.generateToken(mockUserDetails)).thenReturn(mockToken);

        // Вызов метода
        String result = userService.authorizationUser(registrationUsers);

        // Проверки
        Assertions.assertNotNull(result, "Результат не должен быть null");
        Assertions.assertEquals(mockToken, result, "JWT токен должен совпадать с ожидаемым");
        Mockito.verify(mockAuthenticationManager, Mockito.times(1)).authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class));
        Mockito.verify(mockUserDetailsService, Mockito.times(1)).loadUserByUsername("authuser@example.com");
        Mockito.verify(mockJwtService, Mockito.times(1)).generateToken(mockUserDetails);
    }

    /**
     * Тестирование метода authorizationUser при неудачной аутентификации.
     */
    @Test
    @DisplayName("Тест: Неудачная авторизация пользователя")
    void testAuthorizationUser_Failure() throws Exception {
        // Подготовка данных
        RegistrationUsers registrationUsers = new RegistrationUsers();
        registrationUsers.setEmail("nonexistent@example.com");
        registrationUsers.setPasswordKey("wrongpassword");

        // Настройка моков
        Mockito.when(mockAuthenticationManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new UsernameNotFoundException("User not found"));

        // Вызов метода и проверка исключения
        UsernameNotFoundException exception = Assertions.assertThrows(UsernameNotFoundException.class, () -> {
            userService.authorizationUser(registrationUsers);
        });

        Assertions.assertEquals("User not found", exception.getMessage(), "Сообщение исключения должно " +
                "совпадать");
        Mockito.verify(mockAuthenticationManager, Mockito.times(1)).authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class));
        Mockito.verify(mockUserDetailsService, Mockito.never()).loadUserByUsername(Mockito.anyString());
        Mockito.verify(mockJwtService, Mockito.never()).generateToken(Mockito.any(UserDetails.class));
    }

    /**
     * Тестирование метода authorizationUser при аутентификации, но пользователь не найден.
     */
    @Test
    @DisplayName("Тест: Авторизация пользователя, аутентификация успешна, но пользователь не найден")
    void testAuthorizationUser_AuthenticationSuccessButUserNotFound() throws Exception {
        String email = "authuser@example.com";
        String password = "password123";
        RegistrationUsers registrationUsers = new RegistrationUsers();
        registrationUsers.setEmail(email);
        registrationUsers.setPasswordKey(password);

        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.isAuthenticated()).thenReturn(true);

        // Настройка моков
        Mockito.when(mockAuthenticationManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        Mockito.when(mockUserDetailsService.loadUserByUsername("authuser@example.com")).thenThrow(new UsernameNotFoundException("User not found"));

        // Вызов метода и проверка исключения
        UsernameNotFoundException exception = Assertions.assertThrows(UsernameNotFoundException.class, () -> {
            userService.authorizationUser(registrationUsers);
        });

        Assertions.assertEquals("User not found", exception.getMessage(), "Сообщение исключения должно совпадать");
        Mockito.verify(mockAuthenticationManager, Mockito.times(1)).authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class));
        Mockito.verify(mockUserDetailsService, Mockito.times(1)).loadUserByUsername("authuser@example.com");
        Mockito.verify(mockJwtService, Mockito.never()).generateToken(Mockito.any(UserDetails.class));
    }

    @Test
    @DisplayName("Тест: Неудачная аутентификация пользователя")
    void testAuthorizationUser_AuthenticationFailure() throws Exception {
        String email = "authuser@example.com";
        String password = "wrongpassword";
        RegistrationUsers registrationUsers = new RegistrationUsers();
        registrationUsers.setEmail(email);
        registrationUsers.setPasswordKey(password);

        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.isAuthenticated()).thenReturn(false);

        // Настройка моков
        Mockito.when(mockAuthenticationManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);

        // Вызов метода и проверка исключения
        UsernameNotFoundException exception = Assertions.assertThrows(UsernameNotFoundException.class, () -> {
            userService.authorizationUser(registrationUsers);
        });

        Assertions.assertEquals(DescriptionUserExeption.USER_NOT_FOUND.getEnumDescription() + " " + email, exception.getMessage(),
                "Сообщение исключения должно совпадать");
        Mockito.verify(mockAuthenticationManager, Mockito.times(1)).authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class));
        Mockito.verify(mockUserDetailsService, Mockito.never()).loadUserByUsername(Mockito.anyString());
        Mockito.verify(mockJwtService, Mockito.never()).generateToken(Mockito.any(UserDetails.class));
    }

}

