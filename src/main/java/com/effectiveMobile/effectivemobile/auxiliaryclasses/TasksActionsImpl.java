package com.effectiveMobile.effectivemobile.auxiliaryclasses;

import com.effectiveMobile.effectivemobile.dto.TasksDto;
import com.effectiveMobile.effectivemobile.exeptions.MainException;
import com.effectiveMobile.effectivemobile.exeptions.NotEnoughRulesForEntity;
import com.effectiveMobile.effectivemobile.other.DefaultSettingsFieldNameEnum;
import com.effectiveMobile.effectivemobile.other.TaskPriorityEnum;
import com.effectiveMobile.effectivemobile.other.TaskStatusEnum;
import com.effectiveMobile.effectivemobile.repository.AuthorizationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.effectiveMobile.effectivemobile.exeptions.DescriptionUserExeption.NOT_ENOUGH_RULES_MUST_BE_EXECUTOR;

@Component
@Slf4j
public class TasksActionsImpl implements TasksActions {

    @Autowired
    private AuthorizationRepository authorizationRepository;

    @Autowired
    private DefaultSettingsActions defaultSettingsActions;

    @Override
    public TasksDto fillTaskPriorityAndTaskStatusFields(TasksDto tasksDto) throws MainException {
        if (tasksDto.getTaskPriority() == null) {
            Optional<TaskStatusEnum> optionalTaskStatus = defaultSettingsActions
                    .getDefaultValueFromTasksFields(DefaultSettingsFieldNameEnum.TASK_PRIORITY, TaskStatusEnum.BACKLOG);
            if (optionalTaskStatus.isPresent()) {
                TaskStatusEnum taskStatusEnum = optionalTaskStatus.get();
                tasksDto.setTaskStatus(taskStatusEnum);
            }
        }
        if (tasksDto.getTaskStatus() == null) {
            Optional<TaskPriorityEnum> optionalTaskPriorityEnum = defaultSettingsActions
                    .getDefaultValueFromTasksFields(DefaultSettingsFieldNameEnum.TASK_STATUS, TaskPriorityEnum.MEDIUM);
            if (optionalTaskPriorityEnum.isPresent()) {
                TaskPriorityEnum taskPriorityEnum = optionalTaskPriorityEnum.get();
                tasksDto.setTaskPriority(taskPriorityEnum);
            }
        }
        return tasksDto;
    }

    @Override
    public boolean fieldsTasksAllowedForEditing(TasksDto tasksDto) throws NotEnoughRulesForEntity {
        log.info("Метод fieldsAllowedForEditing()");
        if (tasksDto != null &&
                (
                  (tasksDto.getNotesDto() == null
                   && tasksDto.getTaskPriority() == null
                   && tasksDto.getTaskAuthor() == null
                   && tasksDto.getTaskExecutor() == null
                   && tasksDto.getDescription() == null
                   && tasksDto.getHeader() == null)
                       &&
                   (tasksDto.getId() != null && tasksDto.getTaskStatus() != null)
                )
        ) {
            log.info("Метод fieldsAllowedForEditing(), редактирование разрешено");
            return true;
        } else {
            log.info("Метод fieldsAllowedForEditing(), выброшен NotEnoughRulesForEntity");
            throw new NotEnoughRulesForEntity(NOT_ENOUGH_RULES_MUST_BE_EXECUTOR.getEnumDescription());
        }
    }
}
