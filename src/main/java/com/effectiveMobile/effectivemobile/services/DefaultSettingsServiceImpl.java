package com.effectiveMobile.effectivemobile.services;

import com.effectiveMobile.effectivemobile.dto.DefaultSettingsDto;
import com.effectiveMobile.effectivemobile.entities.DefaultSettings;
import com.effectiveMobile.effectivemobile.exeptions.IncorrectTypeParameterException;
import com.effectiveMobile.effectivemobile.fabrics.MappersFabric;
import com.effectiveMobile.effectivemobile.other.DefaultSettingsFieldNameEnum;
import com.effectiveMobile.effectivemobile.repository.DefaultSettingsRepository;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.effectiveMobile.effectivemobile.constants.ConstantsClass.EMPTY_SPACE;
import static com.effectiveMobile.effectivemobile.exeptions.DescriptionUserExeption.*;
import static com.effectiveMobile.effectivemobile.other.DefaultSettingsFieldNameEnum.TASK_PRIORITY;
import static com.effectiveMobile.effectivemobile.other.DefaultSettingsFieldNameEnum.TASK_STATUS;

@Setter
@Service
@Slf4j
public class DefaultSettingsServiceImpl implements DefaultSettingsService {

    @Autowired
    private DefaultSettingsRepository defaultSettingsRepository;

    @Autowired
    private MappersFabric mappersFabric;

    @CachePut(cacheNames = "defaultSettingsCache", key = "#defaultSettingsDto.fieldName")
    @Override
    @Transactional
    public Optional<DefaultSettingsDto> changeDefaultSettings(DefaultSettingsDto defaultSettingsDto) throws IncorrectTypeParameterException {
        log.info("Метод changeDefaultSettings()" + defaultSettingsDto.getDefaultTaskPriority() + EMPTY_SPACE +
                defaultSettingsDto.getDefaultTaskStatus());
        if (defaultSettingsDto.getDefaultTaskPriority() != null && defaultSettingsDto.getDefaultTaskStatus() != null) {
            throw new IncorrectTypeParameterException(TOO_MANY_FIELDS_IN_ONE_REQUEST.getEnumDescription());
        }

        if ( defaultSettingsDto.getFieldName() != null
                &&
            (defaultSettingsDto.getDefaultTaskPriority() == null && defaultSettingsDto.getDefaultTaskStatus() == null)
            ) {
            throw new IncorrectTypeParameterException(VALUES_EDITABLE_FIELDS_NOT_BE_NULL.getEnumDescription());
        }

        if (defaultSettingsDto.getFieldName() == TASK_PRIORITY && defaultSettingsDto.getDefaultTaskPriority() == null) {
            throw new IncorrectTypeParameterException(TASK_PRIORITY_NOT_BE_NULL.getEnumDescription());
        } else if (defaultSettingsDto.getFieldName() == TASK_STATUS && defaultSettingsDto.getDefaultTaskStatus() == null) {
            throw new IncorrectTypeParameterException(SELECTED_TASK_STATUS_IS_NULL.getEnumDescription());
        }

        DefaultSettingsFieldNameEnum fieldName = defaultSettingsDto.getFieldName();
        Optional<DefaultSettings> defaultSettingsFromDB = defaultSettingsRepository.findByFieldName(fieldName);
        if (defaultSettingsFromDB.isEmpty()) {
            return Optional.empty();
        }
        DefaultSettings defaultSettings = defaultSettingsFromDB.get();
        defaultSettings = mappersFabric
                .createDefaultSettingsMapper()
                .compareDefaultSettingsAndDto(defaultSettingsDto, defaultSettings);

        defaultSettings = defaultSettingsRepository.save(defaultSettings);

        defaultSettingsDto = mappersFabric
                .createDefaultSettingsMapper()
                .convertDefaultSettingsToDto(defaultSettings);

        return Optional.of(defaultSettingsDto);
    }

    @Cacheable(cacheNames = "defaultSettingsCache", key = "#fieldName")
    @Override
    public Optional<DefaultSettingsDto> getDefaultSettings(DefaultSettingsFieldNameEnum fieldName) {
        log.info("Метод changeDefaultSettings() " + fieldName.getFieldName());
        Optional<DefaultSettings> defaultSettingsFromDB = defaultSettingsRepository.findByFieldName(fieldName);
        if (defaultSettingsFromDB.isEmpty()) {
            return Optional.empty();
        }
        DefaultSettings defaultSettings = defaultSettingsFromDB.get();
        return Optional.of(mappersFabric.createDefaultSettingsMapper().convertDefaultSettingsToDto(defaultSettings));
    }
}
