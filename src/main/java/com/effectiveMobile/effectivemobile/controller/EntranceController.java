package com.effectiveMobile.effectivemobile.controller;

import com.effectiveMobile.effectivemobile.dto.CustomUsersDto;
import com.effectiveMobile.effectivemobile.fabrics.ServiceFabric;
import com.effectiveMobile.effectivemobile.auxiliaryclasses.RegistrationUsers;
import com.effectiveMobile.effectivemobile.other.Views;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <pre>
 *     Контроллер регистрации, авторизации пользователей
 * </pre>
 */
@Tag(name = "Вход", description = "Позволяет зарегистрировать нового юзера, авторизоваться в системе")
@RestController
@RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class EntranceController {

    @Autowired
    private ServiceFabric serviceFabric;

    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * Эндпоинт регистрации пользователя
     *
     * @param customUsersDto
     * @return {@link ResponseEntity<CustomUsersDto>}
     */
    @Operation(summary = "Регистрация пользователя", description = "Позволяет зарегистрировать нового пользователя")
    @PostMapping(value = "/entrance/registration", produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonView(Views.Internal.class)
    public ResponseEntity<CustomUsersDto> createUsers(@RequestBody @Parameter(description = "Пользователь") CustomUsersDto customUsersDto) {
        log.info("Метод регистрации, POST " + customUsersDto.getEmail());
        CustomUsersDto customUsersDtoLocal = serviceFabric
                .createUserService()
                .createUser(customUsersDto);
        if (customUsersDtoLocal != null) {
            return ResponseEntity.ok(customUsersDtoLocal);
        }
        return ResponseEntity.internalServerError().build();
    }

    /**
     * Эндпоинт авторизации пользователя
     *
     * @param registrationUsers
     * @return {@link ResponseEntity<String>}
     * @throws UsernameNotFoundException
     */
    @Operation(summary = "Авторизация пользователя", description = "Позволяет пользователю авторизоваться в системе")
    @PostMapping(value = "/entrance/authorization", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> authorizationUser(@RequestBody @Parameter(description = "Форма авторизации") RegistrationUsers registrationUsers)
            throws UsernameNotFoundException {
        log.info("Метод авторизации, POST " + registrationUsers.getEmail());
        String jwtToken = serviceFabric
                .createUserService()
                .authorizationUser(registrationUsers);
        if (jwtToken != null) {
            return ResponseEntity.ok(jwtToken);
        }
        return ResponseEntity.badRequest().build();
    }
}
