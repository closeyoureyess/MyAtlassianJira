package com.effectiveMobile.effectivemobile.mapper;

import com.effectiveMobile.effectivemobile.entities.CustomUsers;
import com.effectiveMobile.effectivemobile.fabrics.MappersFabric;
import com.effectiveMobile.effectivemobile.other.TaskPriorityEnum;
import com.effectiveMobile.effectivemobile.repository.AuthorizationRepository;
import com.effectiveMobile.effectivemobile.constants.ConstantsClass;
import com.effectiveMobile.effectivemobile.exeptions.DescriptionUserExeption;
import com.effectiveMobile.effectivemobile.exeptions.ExecutorNotFoundExeption;
import com.effectiveMobile.effectivemobile.fabrics.ActionsFabric;
import com.effectiveMobile.effectivemobile.dto.TasksDto;
import com.effectiveMobile.effectivemobile.entities.Tasks;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
@Slf4j
public class TaskMapperImpl implements TaskMapper {

    @Autowired
    private MappersFabric mappersFabric;

    @Autowired
    private AuthorizationRepository authorizationRepository;

    @Autowired
    private ActionsFabric actionsFabric;

    @Override
    public Tasks convertDtoToTasks(TasksDto tasksDto, Integer... method) throws ExecutorNotFoundExeption {
        log.info("Метод convertDtoToTasks()");
        Tasks taskLocalObject = new Tasks();
        if (tasksDto != null) {
            taskLocalObject.setId(tasksDto.getId());
            if (tasksDto.getTaskExecutor() != null || method != null) {
                taskLocalObject.setTaskExecutor(mappersFabric.createUserMapper().convertDtoToUser(tasksDto.getTaskExecutor()));
            } else if (tasksDto.getTaskExecutor() == null && method == null) {
                throw new ExecutorNotFoundExeption(DescriptionUserExeption.EXECUTOR_NOT_SPECIFIED.getEnumDescription());
            }
            taskLocalObject.setTaskAuthor(mappersFabric.createUserMapper().convertDtoToUser(tasksDto.getTaskAuthor()));
            if (tasksDto.getTaskPriority() != null) {
                taskLocalObject.setTaskPriority(tasksDto.getTaskPriority());
            } else {
                taskLocalObject.setTaskPriority(TaskPriorityEnum.MEDIUM);
            }
            taskLocalObject.setTaskStatus(tasksDto.getTaskStatus());
            taskLocalObject.setDescription(tasksDto.getDescription());
            taskLocalObject.setHeader(tasksDto.getHeader());
            if (tasksDto.getNotesDto() != null) {
                taskLocalObject.setNotes(mappersFabric.createNotesMapper().transferDtoToListNotes(tasksDto.getNotesDto()));
            }
        }
        return taskLocalObject;
    }

    @Override
    public TasksDto convertTasksToDto(Tasks tasks) {
        log.info("Метод convertTasksToDto()");
        TasksDto tasksDtoLocalObject = new TasksDto();
        if (tasks != null) {
            tasksDtoLocalObject.setId(tasks.getId());
            tasksDtoLocalObject.setTaskExecutor(mappersFabric.createUserMapper().convertUserToDto(tasks.getTaskExecutor()));
            tasksDtoLocalObject.setTaskAuthor(mappersFabric.createUserMapper().convertUserToDto(tasks.getTaskAuthor()));
            tasksDtoLocalObject.setTaskPriority(tasks.getTaskPriority());
            tasksDtoLocalObject.setTaskStatus(tasks.getTaskStatus());
            tasksDtoLocalObject.setDescription(tasks.getDescription());
            tasksDtoLocalObject.setHeader(tasks.getHeader());
            if (tasks.getNotes() != null) {
                tasksDtoLocalObject.setNotesDto(mappersFabric.createNotesMapper().transferListNotesToDto(tasks.getNotes()));
            }
        }
        return tasksDtoLocalObject;
    }

    @Override
    public List<TasksDto> transferListTasksToDto(List<Tasks> tasksList) {
        log.info("Метод transferListTasksToDto()");
        List<TasksDto> tasksDtoList = new LinkedList<>();
        if (tasksList != null) {
            for (int i = 0; i < tasksList.size(); i++) {
                tasksDtoList.add(new TasksDto(tasksList.get(i).getId(), tasksList.get(i).getHeader(),
                        mappersFabric.createUserMapper().convertUserToDto(tasksList.get(i).getTaskAuthor()),
                        mappersFabric.createUserMapper().convertUserToDto(tasksList.get(i).getTaskExecutor()), tasksList.get(i).getDescription(),
                        tasksList.get(i).getTaskPriority(),
                        tasksList.get(i).getTaskStatus(),
                        mappersFabric.createNotesMapper().transferListNotesToDto(tasksList.get(i).getNotes())));
            }
        }
        return tasksDtoList;
    }

    @Override
    public Tasks compareTaskAndDto(TasksDto tasksDto, Tasks tasks) throws UsernameNotFoundException {
        log.info("Метод compareTaskAndDto()");
        //author
        tasks = compareTaskAndDtoAuthor(tasksDto, tasks);
        //executor
        tasks = compareTasksAndDtoExecutor(tasksDto, tasks);
        //desctiption
        tasks = compareTasksAndDtoDescription(tasksDto, tasks);
        //priority
        tasks = compareTasksAndDtoPriority(tasksDto, tasks);
        //header
        tasks = compareTasksAndDtoHeader(tasksDto, tasks);
        //status
        tasks = compareTasksAndDtoStatus(tasksDto, tasks);
        //notes
        return tasks;
    }

    /**
     * Метод, сравнивающий Автора из БД и из ДТО
     *
     * @param tasksDto
     * @param tasks
     */
    private Tasks compareTaskAndDtoAuthor(TasksDto tasksDto, Tasks tasks) {
        log.info("Метод compareTaskAndDtoAuthor()");
        Tasks newTasks;
        if ((tasksDto.getTaskAuthor() != null && tasks.getTaskAuthor() != null) // Есть автор
                && (!tasksDto.getTaskAuthor().getEmail().equals(tasks.getTaskAuthor().getEmail()) // Емейл не совпадает
                ||
                !tasksDto.getTaskAuthor().getId().equals(tasks.getTaskAuthor().getId()))) { // ID не совпадает

            newTasks = actionsFabric
                    .createUserActions()
                    .checkFindUser(mappersFabric.createUserMapper().convertDtoToUser(tasksDto.getTaskAuthor()), tasks, ConstantsClass.ZERO_FLAG);
            if (newTasks != null) {
                tasks.setTaskAuthor(newTasks.getTaskAuthor());
            }
        } else if ((tasksDto.getTaskAuthor() != null && tasks.getTaskAuthor() == null)
                && (tasksDto.getTaskAuthor().getId() != null || tasksDto.getTaskAuthor().getEmail() != null)) {

            newTasks = actionsFabric
                    .createUserActions()
                    .checkFindUser(mappersFabric.createUserMapper().convertDtoToUser(tasksDto.getTaskAuthor()), tasks, ConstantsClass.ZERO_FLAG);
            if (newTasks != null) {
                tasks.setTaskAuthor(newTasks.getTaskAuthor());
            }
        }
        return tasks;
    }

    /**
     * Метод, сравнивающий Исполнителя из БД и из ДТО
     *
     * @param tasksDto
     * @param tasks
     */
    private Tasks compareTasksAndDtoExecutor(TasksDto tasksDto, Tasks tasks) {
        log.info("Метод compareTasksAndDtoExecutor()");
        if (
                (
                        ((tasksDto.getTaskExecutor() != null && tasks.getTaskExecutor() != null)) // a
                                &&
                                (!tasksDto.getTaskExecutor().getEmail().equals(tasks.getTaskExecutor().getEmail()) // 1
                                        ||
                                        !tasksDto.getTaskExecutor().getId().equals(tasks.getTaskExecutor().getId())) // b
                )

                        || (tasksDto.getTaskExecutor() != null && tasks.getTaskExecutor() == null) // 2
        ) {
            CustomUsers customUsers = mappersFabric
                    .createUserMapper()
                    .convertDtoToUser(tasksDto.getTaskExecutor());
            Tasks newTasks = actionsFabric
                    .createUserActions()
                    .checkFindUser(customUsers, tasks, ConstantsClass.ONE_FLAG);
            if (newTasks != null) {
                tasks.setTaskExecutor(newTasks.getTaskExecutor());
            }
        }
        return tasks;
    }

    /**
     * Метод, сравнивающий описание задачи из БД и из ДТО
     *
     * @param tasksDto
     * @param tasks
     */
    private Tasks compareTasksAndDtoDescription(TasksDto tasksDto, Tasks tasks) {
        log.info("Метод compareTasksAndDtoDescription()");
        if (tasksDto.getDescription() != null && !tasksDto.getDescription()
                .equals(tasks.getDescription())) {

            tasks.setDescription(tasksDto.getDescription());
        }
        return tasks;
    }

    /**
     * Метод, сравнивающий приоритет у задачи из БД и из ДТО
     *
     * @param tasksDto
     * @param tasks
     */
    private Tasks compareTasksAndDtoPriority(TasksDto tasksDto, Tasks tasks) {
        log.info("Метод compareTasksAndDtoPriority()");
        if (tasksDto.getTaskPriority() != null && !tasksDto.getTaskPriority()
                .equals(tasks.getTaskPriority())) {

            tasks.setTaskPriority(tasksDto.getTaskPriority());
        }
        return tasks;
    }

    /**
     * Метод, сравнивающий заголовок задачи из БД и из ДТО
     *
     * @param tasksDto
     * @param tasks
     */
    private Tasks compareTasksAndDtoHeader(TasksDto tasksDto, Tasks tasks) {
        log.info("Метод compareTasksAndDtoHeader()");
        if (/*tasksDto.getHeader() != null && */!tasksDto.getHeader().equals(tasks.getHeader())) {

            tasks.setHeader(tasksDto.getHeader());
        }
        return tasks;
    }

    /**
     * Метод, сравнивающий статус задачи из БД и из ДТО
     *
     * @param tasksDto
     * @param tasks
     */
    private Tasks compareTasksAndDtoStatus(TasksDto tasksDto, Tasks tasks) {
        log.info("Метод compareTasksAndDtoStatus()");
        if ((tasks.getTaskExecutor().getEmail().equals(actionsFabric.createUserActions().getEmailCurrentUser()))
                && (tasksDto.getTaskStatus() != null && !tasksDto.getTaskStatus().equals(tasks.getTaskStatus()))) {

            tasks.setTaskStatus(tasksDto.getTaskStatus());
        }
        return tasks;
    }

}
