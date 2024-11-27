package com.effectiveMobile.effectivemobile.controller;

import com.effectiveMobile.effectivemobile.dto.DefaultSettingsDto;
import com.effectiveMobile.effectivemobile.exeptions.IncorrectTypeParameterException;
import com.effectiveMobile.effectivemobile.fabrics.ServiceFabric;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import static com.effectiveMobile.effectivemobile.constants.ConstantsClass.EMPTY_SPACE;

/**
 * <pre>
 *     Контроллер редактирования настроек по умолчанию
 * </pre>
 */
@Tag(name = "Настройки по умолчанию", description = "Позволяет отредактировать настройки по умолчанию")
@RestController
@RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@Validated
public class DefaultSettingsController {

    @Autowired
    private ServiceFabric serviceFabric;

    /**
     * Эндпоинт, позволяющий изменить настройки по умолчанию
     *
     * @param defaultSettingsDto - объект с наименованием поля, обновленными настройками по умолчанию для поля
     * @return {@link ResponseEntity<DefaultSettingsDto>}
     * @throws IncorrectTypeParameterException - исключение, выбрасываемое при неверном типе параметра
     */
    @Operation(summary = "Отредактировать настройки по умолчанию для задач",
            description = "Позволяет отредактировать настройки по умолчанию для задач")
    @SecurityRequirement(name = "JWT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Задача успешно создана", content = @Content(examples = @ExampleObject(name = "Редактирование настроек по умолчанию", value = "{\n  \"fieldName\": \"TASK_PRIORITY\",\n  \"defaultTaskStatus\": \"BACKLOG\",\n  \"defaultTaskPriority\": \"MEDIUM\"\n}"))),
            @ApiResponse(responseCode = "403", description = "Не авторизован/Недостаточно прав", content = @Content),
            @ApiResponse(responseCode = "500", description = "отредактировать настройки не удалось", content = @Content)
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Пример тела изменения настройки по умолчанию",
            content = @Content(mediaType = "application/json", examples = @ExampleObject(name = "Редактирование настроек по умолчанию", value = "{\n  \"fieldName\": \"TASK_PRIORITY\",\n  \"defaultTaskStatus\": \"BACKLOG\",\n  \"defaultTaskPriority\": \"MEDIUM\"\n}")))

    @PutMapping(value = "/defaultsettings/update-settings")
    public ResponseEntity<DefaultSettingsDto> changeDefaultSettings(@Valid @RequestBody @NotNull(message = "Необходимо указать настройки по умолчанию")
                                                                        DefaultSettingsDto defaultSettingsDto) throws IncorrectTypeParameterException {
        log.info("Создание комментария, POST " + defaultSettingsDto.getDefaultTaskPriority() + EMPTY_SPACE +
                defaultSettingsDto.getDefaultTaskStatus());

        Optional<DefaultSettingsDto> optionalDefaultSettingsDto = serviceFabric
                .createDefaultSettingsService()
                .changeDefaultSettings(defaultSettingsDto);

        if (optionalDefaultSettingsDto.isPresent()) {
            DefaultSettingsDto localDefaultSettingsDto = optionalDefaultSettingsDto.get();
            return ResponseEntity.ok(localDefaultSettingsDto);
        }

        return ResponseEntity.badRequest().build();
    }
}
