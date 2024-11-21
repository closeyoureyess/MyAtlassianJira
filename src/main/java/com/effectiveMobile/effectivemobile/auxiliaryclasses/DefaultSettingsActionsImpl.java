package com.effectiveMobile.effectivemobile.auxiliaryclasses;

import com.effectiveMobile.effectivemobile.dto.DefaultSettingsDto;
import com.effectiveMobile.effectivemobile.exeptions.IncorrectTypeParameterException;
import com.effectiveMobile.effectivemobile.exeptions.MainException;
import com.effectiveMobile.effectivemobile.fabrics.ServiceFabric;
import com.effectiveMobile.effectivemobile.other.DefaultSettingsFieldNameEnum;
import com.effectiveMobile.effectivemobile.other.TaskPriorityEnum;
import com.effectiveMobile.effectivemobile.other.TaskStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.effectiveMobile.effectivemobile.exeptions.DescriptionUserExeption.INCORRECT_TYPE_PARAMETER;

@Component
@Slf4j
public class DefaultSettingsActionsImpl implements DefaultSettingsActions {

    @Autowired
    private ServiceFabric serviceFabric;

    @SuppressWarnings("unchecked")
    @Override
    public <T> Optional<T> getDefaultValueFromTasksFields(DefaultSettingsFieldNameEnum fieldName, T fieldValue) throws IncorrectTypeParameterException {
        log.info("Метод getDefaultValueFromTasksFields()");
        Optional<DefaultSettingsDto> optionalDefaultSettings = serviceFabric
                .createDefaultSettingsService()
                .getDefaultSettings(fieldName);
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
