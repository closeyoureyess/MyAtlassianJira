package com.effectiveMobile.effectivemobile.services;

import com.effectiveMobile.effectivemobile.dto.DefaultSettingsDto;
import com.effectiveMobile.effectivemobile.other.DefaultSettingsFieldNameEnum;

import java.util.Optional;

/**
 * <pre>
 *     Интерфейс для работы c настройками по умолчанию
 * </pre>
 */
public interface DefaultSettingsService {

    /**
     * Метод, позволяющий отредактировать значения по умолчанию для нужных полей
     *
     * @param defaultSettingsDto
     * @return {@link Optional<DefaultSettingsDto>} с отредактированными настройками по умолчанию для полей
     */
    Optional<DefaultSettingsDto> changeDefaultSettings(DefaultSettingsDto defaultSettingsDto);

    /**
     * Метод, позволяющий получить выставленное значение по умолчанию для определенного поля
     *
     * @param fieldName - поле, для которого нужно получить значение по умолчанию
     * @return {@link Optional<DefaultSettingsDto>} выставленное значение по умолчанию для поля
     */
    Optional<DefaultSettingsDto> getDefaultSettings(DefaultSettingsFieldNameEnum fieldName);
}
