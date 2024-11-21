package com.effectiveMobile.effectivemobile.services;

import com.effectiveMobile.effectivemobile.dto.DefaultSettingsDto;
import com.effectiveMobile.effectivemobile.entities.DefaultSettings;
import com.effectiveMobile.effectivemobile.fabrics.MappersFabric;
import com.effectiveMobile.effectivemobile.other.DefaultSettingsFieldNameEnum;
import com.effectiveMobile.effectivemobile.repository.DefaultSettingsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.effectiveMobile.effectivemobile.constants.ConstantsClass.EMPTY_SPACE;

@Service
@Slf4j
public class DefaultSettingsServiceImpl implements DefaultSettingsService {

    @Autowired
    private DefaultSettingsRepository defaultSettingsRepository;

    @Autowired
    private MappersFabric mappersFabric;

    @CachePut(cacheNames = "defaultSettingsCache", key = "#defaultSettingsDto.fieldName")
    @Override
    public Optional<DefaultSettingsDto> changeDefaultSettings(DefaultSettingsDto defaultSettingsDto) {
        log.info("Метод changeDefaultSettings()" + defaultSettingsDto.getDefaultTaskPriority() + EMPTY_SPACE +
                defaultSettingsDto.getDefaultTaskStatus());
        Optional<DefaultSettings> defaultSettingsFromDB = defaultSettingsRepository.findByFieldName(defaultSettingsDto.getFieldName());
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
        log.info("Метод changeDefaultSettings()" + fieldName.getFieldName());
        Optional<DefaultSettings> defaultSettingsFromDB = defaultSettingsRepository.findByFieldName(fieldName);
        if (defaultSettingsFromDB.isEmpty()) {
            return Optional.empty();
        }
        DefaultSettings defaultSettings = defaultSettingsFromDB.get();
        return Optional.of(mappersFabric.createDefaultSettingsMapper().convertDefaultSettingsToDto(defaultSettings));
    }
}
