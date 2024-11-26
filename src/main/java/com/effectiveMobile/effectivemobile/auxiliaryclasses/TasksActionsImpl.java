package com.effectiveMobile.effectivemobile.auxiliaryclasses;

import com.effectiveMobile.effectivemobile.dto.TasksDto;
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
    public TasksDto fillTaskPriorityAndTaskStatusFields(TasksDto tasksDto) {
        if (tasksDto.getTaskPriority() == null) {
            Optional<TaskPriorityEnum> optionalTaskPriority = defaultSettingsActions
                    .getDefaultValueFromTasksFields(DefaultSettingsFieldNameEnum.TASK_PRIORITY, TaskPriorityEnum.MEDIUM);
            if (optionalTaskPriority.isPresent()) {
                TaskPriorityEnum taskPriorityEnum = optionalTaskPriority.get();
                tasksDto.setTaskPriority(taskPriorityEnum);
            }
        }
        if (tasksDto.getTaskStatus() == null) {
            Optional<TaskStatusEnum> optionalTaskStatusEnum = defaultSettingsActions
                    .getDefaultValueFromTasksFields(DefaultSettingsFieldNameEnum.TASK_STATUS, TaskStatusEnum.BACKLOG);
            if (optionalTaskStatusEnum.isPresent()) {
                TaskStatusEnum taskStatusEnum = optionalTaskStatusEnum.get();
                tasksDto.setTaskStatus(taskStatusEnum);
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
