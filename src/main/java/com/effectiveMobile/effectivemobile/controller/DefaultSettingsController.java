package com.effectiveMobile.effectivemobile.controller;

import com.effectiveMobile.effectivemobile.dto.DefaultSettingsDto;
import com.effectiveMobile.effectivemobile.exeptions.IncorrectTypeParameterException;
import com.effectiveMobile.effectivemobile.fabrics.ServiceFabric;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    @PutMapping(value = "/defaultsettings/update-settings")
    public ResponseEntity<DefaultSettingsDto> changeDefaultSettings(@RequestBody DefaultSettingsDto defaultSettingsDto) throws IncorrectTypeParameterException {
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
