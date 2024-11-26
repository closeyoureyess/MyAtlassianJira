package com.effectiveMobile.effectivemobile.auxiliaryclasses;

import com.effectiveMobile.effectivemobile.dto.DefaultSettingsDto;
import com.effectiveMobile.effectivemobile.other.DefaultSettingsFieldNameEnum;
import com.effectiveMobile.effectivemobile.services.DefaultSettingsServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
public class DefaultSettingsActionsImpl implements DefaultSettingsActions {

    private final ApplicationContext applicationContext;

    public DefaultSettingsActionsImpl(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> Optional<T> getDefaultValueFromTasksFields(DefaultSettingsFieldNameEnum fieldName, T fieldValue)  {
        log.info("Метод getDefaultValueFromTasksFields()");
        DefaultSettingsServiceImpl defaultSettingsService;
        if (applicationContext != null ) {
            defaultSettingsService = applicationContext.getBean(DefaultSettingsServiceImpl.class);
        } else {
            defaultSettingsService = new DefaultSettingsServiceImpl();
        }
        Optional<DefaultSettingsDto> optionalDefaultSettings = defaultSettingsService.getDefaultSettings(fieldName);
        if (optionalDefaultSettings.isPresent()) {
            DefaultSettingsDto defaultSettingsDto = optionalDefaultSettings.get();
            if (fieldName == DefaultSettingsFieldNameEnum.TASK_PRIORITY)
                return Optional.of((T) defaultSettingsDto.getDefaultTaskPriority());
            if (fieldName == DefaultSettingsFieldNameEnum.TASK_STATUS)
                return Optional.of((T) defaultSettingsDto.getDefaultTaskStatus());
        }
        return Optional.empty();
    }
}
