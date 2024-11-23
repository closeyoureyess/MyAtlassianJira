package com.effectiveMobile.effectivemobile.controller;

import com.effectiveMobile.effectivemobile.dto.DefaultSettingsDto;
import com.effectiveMobile.effectivemobile.fabrics.ServiceFabric;
import com.effectiveMobile.effectivemobile.other.Views;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @Operation(summary = "Отредактировать настройки по умолчанию для задач",
            description = "Позволяет отредактировать настройки по умолчанию для задач")
    @SecurityRequirement(name = "JWT")
    @PutMapping(value = "/defaultsettings/update-settings")
    public ResponseEntity<DefaultSettingsDto> changeDefaultSettings(@RequestBody DefaultSettingsDto defaultSettingsDto) {
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
