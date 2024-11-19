package com.effectiveMobile.effectivemobile.mapper;

import com.effectiveMobile.effectivemobile.repository.AuthorizationRepository;
import com.effectiveMobile.effectivemobile.entities.CustomUsers;
import com.effectiveMobile.effectivemobile.constants.ConstantsClass;
import com.effectiveMobile.effectivemobile.exeptions.DescriptionUserExeption;
import com.effectiveMobile.effectivemobile.exeptions.ExecutorNotFoundExeption;
import com.effectiveMobile.effectivemobile.fabrics.ActionsFabric;
import com.effectiveMobile.effectivemobile.dto.TasksDto;
import com.effectiveMobile.effectivemobile.entities.Tasks;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class TaskMapperImpl implements TaskMapper {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private NotesMapper notesMapper;

    @Autowired
    private AuthorizationRepository authorizationRepository;

    @Autowired
    private ActionsFabric actionsFabric;

    public Tasks convertDtoToTasks(TasksDto tasksDto, Integer... method) throws ExecutorNotFoundExeption {
        Tasks taskLocalObject = new Tasks();
        if (tasksDto != null) {
            taskLocalObject.setId(tasksDto.getId());
            if (tasksDto.getTaskExecutor() != null || method != null) {
                taskLocalObject.setTaskExecutor(userMapper.convertDtoToUser(tasksDto.getTaskExecutor()));
            } else if (tasksDto.getTaskExecutor() == null && method == null) {
                throw new ExecutorNotFoundExeption(DescriptionUserExeption.EXECUTOR_NOT_SPECIFIED.getEnumDescription());
            }
            taskLocalObject.setTaskAuthor(userMapper.convertDtoToUser(tasksDto.getTaskAuthor()));
            taskLocalObject.setTaskPriority(tasksDto.getTaskPriority());
            taskLocalObject.setTaskStatus(tasksDto.getTaskStatus());
            taskLocalObject.setDescription(tasksDto.getDescription());
            taskLocalObject.setHeader(tasksDto.getHeader());
            if (tasksDto.getNotesDto() != null) {
                taskLocalObject.setNotes(notesMapper.convertDtoToNotes(tasksDto.getNotesDto()));
            }
        }
        return taskLocalObject;
    }

    public TasksDto convertTasksToDto(Tasks tasks) {
        TasksDto tasksDtoLocalObject = new TasksDto();
        if (tasks != null) {
            tasksDtoLocalObject.setId(tasks.getId());
            tasksDtoLocalObject.setTaskExecutor(userMapper.convertUserToDto(tasks.getTaskExecutor()));
            tasksDtoLocalObject.setTaskAuthor(userMapper.convertUserToDto(tasks.getTaskAuthor()));
            tasksDtoLocalObject.setTaskPriority(tasks.getTaskPriority());
            tasksDtoLocalObject.setTaskStatus(tasks.getTaskStatus());
            tasksDtoLocalObject.setDescription(tasks.getDescription());
            tasksDtoLocalObject.setHeader(tasks.getHeader());
            tasksDtoLocalObject.setNotesDto(notesMapper.convertNotesToDto(tasks.getNotes()));
        }
        return tasksDtoLocalObject;
    }

    public List<TasksDto> transferListTasksToDto(List<Tasks> tasksList) {
        List<TasksDto> tasksDtoList = new LinkedList<>();
        if (tasksList != null) {
            for (int i = 0; i < tasksList.size(); i++) {
                tasksDtoList.add(new TasksDto(tasksList.get(i).getId(), tasksList.get(i).getHeader(),
                        userMapper.convertUserToDto(tasksList.get(i).getTaskAuthor()),
                        userMapper.convertUserToDto(tasksList.get(i).getTaskExecutor()), tasksList.get(i).getDescription(),
                        tasksList.get(i).getTaskPriority(),
                        tasksList.get(i).getTaskStatus(), notesMapper.convertNotesToDto(tasksList.get(i).getNotes())));
            }
        }
        return tasksDtoList;
    }

    public Tasks compareTaskAndDto(TasksDto tasksDto, Tasks tasks) throws UsernameNotFoundException {
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
        tasks = compareTasksAndDtoNotes(tasksDto, tasks);
        return tasks;
    }

    /**
     * Метод, сравнивающий Автора из БД и из ДТО
     * @param tasksDto
     * @param tasks
     */
    private Tasks compareTaskAndDtoAuthor(TasksDto tasksDto, Tasks tasks) {
        Tasks newTasks;
        if ((tasksDto.getTaskAuthor() != null && tasks.getTaskAuthor() != null) // Есть автор
                && (!tasksDto.getTaskAuthor().getEmail().equals(tasks.getTaskAuthor().getEmail()) // Емейл не совпадает
                ||
                !tasksDto.getTaskAuthor().getId().equals(tasks.getTaskAuthor().getId()))) { // ID не совпадает

            newTasks = actionsFabric
                    .createUserActions()
                    .checkFindUser(userMapper.convertDtoToUser(tasksDto.getTaskAuthor()), tasks, ConstantsClass.REGIME_OVERWRITING);
            if (newTasks != null) {
                tasks.setTaskAuthor(newTasks.getTaskAuthor());
            }
        } else if ((tasksDto.getTaskAuthor() != null && tasks.getTaskAuthor() == null)
                && (tasksDto.getTaskAuthor().getId() != null || tasksDto.getTaskAuthor().getEmail() != null)) {

            newTasks = actionsFabric
                    .createUserActions()
                    .checkFindUser(userMapper.convertDtoToUser(tasksDto.getTaskAuthor()), tasks, ConstantsClass.REGIME_OVERWRITING);
            if (newTasks != null) {
                tasks.setTaskAuthor(newTasks.getTaskAuthor());
            }
        }
        return tasks;
    }

    /**
     * Метод, сравнивающий Исполнителя из БД и из ДТО
     * @param tasksDto
     * @param tasks
     */
    private Tasks compareTasksAndDtoExecutor(TasksDto tasksDto, Tasks tasks) {
        if (
                (
                        ((tasksDto.getTaskExecutor() != null && tasks.getTaskExecutor() != null))
                                &&
                                (!tasksDto.getTaskExecutor().getEmail().equals(tasks.getTaskExecutor().getEmail())
                                        ||
                                        !tasksDto.getTaskExecutor().getId().equals(tasks.getTaskExecutor().getId()))
                )

                        || (tasksDto.getTaskExecutor() != null && tasks.getTaskExecutor() == null)
        ) {

            Tasks newTasks = actionsFabric
                    .createUserActions()
                    .checkFindUser(userMapper.convertDtoToUser(tasksDto.getTaskExecutor()), tasks, ConstantsClass.REGIME_RECORD);
            if (newTasks != null) {
                tasks.setTaskExecutor(newTasks.getTaskExecutor());
            }
        }
        return tasks;
    }

    /**
     * Метод, сравнивающий описание задачи из БД и из ДТО
     * @param tasksDto
     * @param tasks
     */
    private Tasks compareTasksAndDtoDescription(TasksDto tasksDto, Tasks tasks) {
        if (tasksDto.getDescription() != null && !tasksDto.getDescription()
                .equals(tasks.getDescription())) {

            tasks.setDescription(tasksDto.getDescription());
        }
        return tasks;
    }

    /**
     * Метод, сравнивающий приоритет у задачи из БД и из ДТО
     * @param tasksDto
     * @param tasks
     */
    private Tasks compareTasksAndDtoPriority(TasksDto tasksDto, Tasks tasks) {
        if (/*tasksDto.getTaskPriority() != null && */!tasksDto.getTaskPriority()
                .equals(tasks.getTaskPriority())) {

            tasks.setTaskPriority(tasksDto.getTaskPriority());
        }
        return tasks;
    }

    /**
     * Метод, сравнивающий заголовок задачи из БД и из ДТО
     * @param tasksDto
     * @param tasks
     */
    private Tasks compareTasksAndDtoHeader(TasksDto tasksDto, Tasks tasks) {
        if (/*tasksDto.getHeader() != null && */!tasksDto.getHeader().equals(tasks.getHeader())) {

            tasks.setHeader(tasksDto.getHeader());
        }
        return tasks;
    }

    /**
     * Метод, сравнивающий статус задачи из БД и из ДТО
     * @param tasksDto
     * @param tasks
     */
    private Tasks compareTasksAndDtoStatus(TasksDto tasksDto, Tasks tasks) {
        if ((tasks.getTaskExecutor().getEmail().equals(actionsFabric.createUserActions().getEmailCurrentUser()))
                && (tasksDto.getTaskStatus() != null && !tasksDto.getTaskStatus().equals(tasks.getTaskStatus()))) {

            tasks.setTaskStatus(tasksDto.getTaskStatus());
        }
        return tasks;
    }

    /**
     * Метод, сравнивающий комментарии задачи из БД и из ДТО
     * @param tasksDto
     * @param tasks
     */
    private Tasks compareTasksAndDtoNotes(TasksDto tasksDto, Tasks tasks) {
        Optional<CustomUsers> optionalCurrentUser = actionsFabric.createUserActions().getCurrentUser();
        if ((tasksDto.getNotesDto() != null && tasks.getNotes() != null) && optionalCurrentUser.isPresent()) {
            String emailCurrentUser = optionalCurrentUser.get().getEmail();
            if (emailCurrentUser.equals(tasks.getNotes().getUsers().getEmail())) {
                tasks.getNotes().setComments(tasksDto.getNotesDto().getComments());
                tasks.getNotes().setUsers(actionsFabric.createUserActions().getCurrentUser().get());
            }
        }
        return tasks;
    }
}
