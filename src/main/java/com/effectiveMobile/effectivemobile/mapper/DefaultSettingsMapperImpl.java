package com.effectiveMobile.effectivemobile.mapper;

import com.effectiveMobile.effectivemobile.dto.DefaultSettingsDto;
import com.effectiveMobile.effectivemobile.entities.DefaultSettings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.effectiveMobile.effectivemobile.constants.ConstantsClass.EMPTY_SPACE;

@Component
@Slf4j
public class DefaultSettingsMapperImpl implements DefaultSettingsMapper {

    @Override
    public DefaultSettings convertDtoToDefaultSettings(DefaultSettingsDto defaultSettingsDto) {
        log.info("Метод convertDtoToDefaultSettings()");
        DefaultSettings defaultSettings = new DefaultSettings();
        if (defaultSettingsDto != null) {
            defaultSettings.setId(defaultSettingsDto.getId());
            defaultSettings.setFieldName(defaultSettingsDto.getFieldName());
            defaultSettings.setDefaultTaskStatus(defaultSettingsDto.getDefaultTaskStatus());
            defaultSettings.setDefaultTaskPriority(defaultSettingsDto.getDefaultTaskPriority());
        }
        return defaultSettings;
    }

    @Override
    public DefaultSettingsDto convertDefaultSettingsToDto(DefaultSettings defaultSettings) {
        log.info("Метод convertDefaultSettingsToDto()");
        DefaultSettingsDto defaultSettingsDto = new DefaultSettingsDto();
        if (defaultSettings != null) {
            defaultSettingsDto.setId(defaultSettings.getId());
            defaultSettingsDto.setFieldName(defaultSettings.getFieldName());
            defaultSettingsDto.setDefaultTaskPriority(defaultSettings.getDefaultTaskPriority());
            defaultSettingsDto.setDefaultTaskStatus(defaultSettings.getDefaultTaskStatus());
        }
        return defaultSettingsDto;
    }

    @Override
    public DefaultSettings compareDefaultSettingsAndDto(DefaultSettingsDto defaultSettingsDto, DefaultSettings defaultSettings) {
        log.info("Метод compareDefaultSettingsAndDto()" + defaultSettingsDto.getFieldName() + EMPTY_SPACE
                + defaultSettingsDto.getDefaultTaskPriority());
        defaultSettings.setId(defaultSettingsDto.getId());
        if (defaultSettingsDto.getFieldName() != null) {
            defaultSettings.setFieldName(defaultSettingsDto.getFieldName());
        }
        if (defaultSettingsDto.getDefaultTaskPriority() != null) {
            defaultSettings.setDefaultTaskPriority(defaultSettingsDto.getDefaultTaskPriority());
        }
        if (defaultSettingsDto.getDefaultTaskStatus() != null) {
            defaultSettings.setDefaultTaskStatus(defaultSettingsDto.getDefaultTaskStatus());
        }
        return defaultSettings;
    }
}
