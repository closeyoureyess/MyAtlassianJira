package com.effectiveMobile.effectivemobile.mapper;

import com.effectiveMobile.effectivemobile.dto.DefaultSettingsDto;
import com.effectiveMobile.effectivemobile.entities.DefaultSettings;


/**
 * <pre>
 *     Маппер для {@link DefaultSettings}, {@link DefaultSettingsDto}
 * </pre>
 */
public interface DefaultSettingsMapper {

    /**
     * Метод для конвертации списка с DefaultSettings в список с DefaultSettingsDto
     *
     * @param defaultSettingsDto
     * @return сконвертированный {@link DefaultSettings}
     */
    DefaultSettings convertDtoToDefaultSettings(DefaultSettingsDto defaultSettingsDto);

    /**
     * Метод для конвертации списка с DefaultSettingsDto в список с DefaultSettings
     *
     * @param defaultSettings
     * @return сконвертированный {@link DefaultSettingsDto}
     */
    DefaultSettingsDto convertDefaultSettingsToDto(DefaultSettings defaultSettings);

    /**
     * Метод, сравнивающий поля DefaultSettings из БД с DefaultSettingsDto
     *
     * @param defaultSettingsDto - объект-дто
     * @param defaultSettings - объект из БД
     * @return {@link DefaultSettings} - итоговый объект после сравнения, готовый для обновления сущности в БД
     */
    DefaultSettings compareDefaultSettingsAndDto(DefaultSettingsDto defaultSettingsDto, DefaultSettings defaultSettings);

}
