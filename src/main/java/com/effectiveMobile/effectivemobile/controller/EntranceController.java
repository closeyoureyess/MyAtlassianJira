package com.effectiveMobile.effectivemobile.controller;

import com.effectiveMobile.effectivemobile.annotations.FilterResponse;
import com.effectiveMobile.effectivemobile.dto.CustomUsersDto;
import com.effectiveMobile.effectivemobile.dto.RegistrationUsers;
import com.effectiveMobile.effectivemobile.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.effectiveMobile.effectivemobile.constants.ConstantsClass.POST_CREATE_USER;

/**
 * <pre>
 *     Контроллер регистрации, авторизации пользователей
 * </pre>
 */
@Tag(name = "Вход", description = "Позволяет зарегистрировать нового юзера, авторизоваться в системе")
@RestController
@RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@Validated
public class EntranceController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * Эндпоинт регистрации пользователя
     *
     * @param customUsersDto - объект с полями для регистрации пользователя
     * @return {@link ResponseEntity<CustomUsersDto>} - созданный пользователь
     */
    @Operation(summary = "Регистрация пользователя", description = "Позволяет зарегистрировать нового пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь зарегистрирован"),
    })
    @FilterResponse(filterName = POST_CREATE_USER)
    @PostMapping(value = "/entrance/registration", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomUsersDto> createUser(@Valid @RequestBody @Parameter(description = "Пользователь") @NotNull(message = "Пользователь не может быть пустым")
                                                         CustomUsersDto customUsersDto) {
        log.info("Метод регистрации, POST " + customUsersDto.getEmail());
        CustomUsersDto customUsersDtoLocal = userService
                .createUser(customUsersDto);
        if (customUsersDtoLocal != null) {
            return ResponseEntity.ok(customUsersDtoLocal);
        }
        return ResponseEntity.internalServerError().build();
    }

    /**
     * Эндпоинт авторизации пользователя
     *
     * @param registrationUsers - форма авторизации в системе
     * @return {@link ResponseEntity<String>}
     * @throws UsernameNotFoundException - исключение, выбрасываемое, если пользователь не найден
     */
    @Operation(summary = "Авторизация пользователя", description = "Позволяет пользователю авторизоваться в системе")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь авторизован"),
            @ApiResponse(responseCode = "403", description = "Не авторизован", content = @Content),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден", content = @Content)
    })
    @PostMapping(value = "/entrance/authorization", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> authorizationUser(@Valid@RequestBody @Parameter(description = "Форма авторизации")
                                                        @NotNull(message = "Форма авторизации не может быть пустой")
                                                        RegistrationUsers registrationUsers)
            throws UsernameNotFoundException {
        log.info("Метод авторизации, POST " + registrationUsers.getEmail());
        String jwtToken = userService
                .authorizationUser(registrationUsers);
        if (jwtToken != null) {
            return ResponseEntity.ok(jwtToken);
        }
        return ResponseEntity.badRequest().build();
    }
}
