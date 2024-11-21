package com.effectiveMobile.effectivemobile.auxiliaryclasses;

import com.effectiveMobile.effectivemobile.constants.ConstantsClass;
import com.effectiveMobile.effectivemobile.dto.CustomUsersDto;
import com.effectiveMobile.effectivemobile.dto.DefaultSettingsDto;
import com.effectiveMobile.effectivemobile.dto.TasksDto;
import com.effectiveMobile.effectivemobile.entities.DefaultSettings;
import com.effectiveMobile.effectivemobile.entities.Tasks;
import com.effectiveMobile.effectivemobile.exeptions.IncorrectTypeParameterException;
import com.effectiveMobile.effectivemobile.exeptions.MainException;
import com.effectiveMobile.effectivemobile.fabrics.ActionsFabric;
import com.effectiveMobile.effectivemobile.fabrics.MappersFabric;
import com.effectiveMobile.effectivemobile.fabrics.ServiceFabric;
import com.effectiveMobile.effectivemobile.other.DefaultSettingsFieldNameEnum;
import com.effectiveMobile.effectivemobile.other.TaskPriorityEnum;
import com.effectiveMobile.effectivemobile.other.TaskStatusEnum;
import com.effectiveMobile.effectivemobile.repository.AuthorizationRepository;
import com.effectiveMobile.effectivemobile.entities.CustomUsers;
import com.effectiveMobile.effectivemobile.repository.TasksRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.effectiveMobile.effectivemobile.constants.ConstantsClass.EMPTY_SPACE;
import static com.effectiveMobile.effectivemobile.exeptions.DescriptionUserExeption.INCORRECT_TYPE_PARAMETER;

@Component
@Slf4j
public class TasksActionsImpl implements TasksActions {

    @Autowired
    private AuthorizationRepository authorizationRepository;

    @Autowired
    private TasksRepository tasksRepository;

    @Autowired
    private MappersFabric mappersFabric;

    @Autowired
    private ServiceFabric serviceFabric;

    @Autowired
    private ActionsFabric actionsFabric;

    @Override
    public boolean isPrivilegeTasks(CustomUsersDto customUsersDto) {
        log.info("Метод isPrivilegeTasks()");
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        Optional<CustomUsers> customUsers = authorizationRepository.findByEmail(loggedInUser.getName());
        String emailCurrentUser = customUsers.get().getEmail();
        if (customUsersDto.getEmail().equals(emailCurrentUser)) {
            return true;
        }
        return false;
    }

    @Override
    public TasksDto fillTaskPriorityAndTaskStatusFields(TasksDto tasksDto) throws MainException {
        DefaultSettingsActions defaultSettingsActions = actionsFabric.createDefaultSettingsActions();
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
}
