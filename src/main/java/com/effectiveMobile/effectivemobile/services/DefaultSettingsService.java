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

    Optional<DefaultSettingsDto> changeDefaultSettings(DefaultSettingsDto defaultSettingsDto);

    Optional<DefaultSettingsDto> getDefaultSettings(DefaultSettingsFieldNameEnum fieldName);
}
