package com.effectiveMobile.effectivemobile.services;

import com.effectiveMobile.effectivemobile.auxiliaryclasses.*;
import com.effectiveMobile.effectivemobile.dto.CustomUsersDto;
import com.effectiveMobile.effectivemobile.exeptions.MainException;
import com.effectiveMobile.effectivemobile.mapper.TaskMapper;
import com.effectiveMobile.effectivemobile.other.DefaultSettingsFieldNameEnum;
import com.effectiveMobile.effectivemobile.other.TaskPriorityEnum;
import com.effectiveMobile.effectivemobile.other.TaskStatusEnum;
import com.effectiveMobile.effectivemobile.repository.AuthorizationRepository;
import com.effectiveMobile.effectivemobile.entities.CustomUsers;
import com.effectiveMobile.effectivemobile.constants.ConstantsClass;
import com.effectiveMobile.effectivemobile.exeptions.DescriptionUserExeption;
import com.effectiveMobile.effectivemobile.exeptions.ExecutorNotFoundExeption;
import com.effectiveMobile.effectivemobile.exeptions.NotEnoughRulesEntity;
import com.effectiveMobile.effectivemobile.fabrics.ActionsFabric;
import com.effectiveMobile.effectivemobile.fabrics.MappersFabric;
import com.effectiveMobile.effectivemobile.dto.TasksDto;
import com.effectiveMobile.effectivemobile.entities.Tasks;
import com.effectiveMobile.effectivemobile.repository.TasksRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static com.effectiveMobile.effectivemobile.constants.ConstantsClass.EMPTY_SPACE;
import static com.effectiveMobile.effectivemobile.constants.ConstantsClass.ZERO_FLAG;

@Service
@Slf4j
public class TaskServiceImpl implements TaskService {

    @Autowired
    private MappersFabric mappersFabric;

    @Autowired
    private ActionsFabric actionsFabric;

    @Autowired
    private TasksRepository tasksRepository;

    @Autowired
    private AuthorizationRepository authorizationRepository;

    @Override
    @Transactional
    public TasksDto createTasks(TasksDto tasksDto) throws UsernameNotFoundException, MainException {
        log.info("Метод createTasks() " + tasksDto.getHeader());
        UserActions userActions = actionsFabric.createUserActions();
        TaskMapper taskMapper = mappersFabric.createTaskMapper();


        Optional<CustomUsers> optionalAuthorizedUser = userActions.getCurrentUser();
        tasksDto = actionsFabric.createTasksActions().fillTaskPriorityAndTaskStatusFields(tasksDto);
        CustomUsers authorizedUser = optionalAuthorizedUser.get();
        tasksDto.setTaskAuthor(mappersFabric.createUserMapper().convertUserToDto(authorizedUser));
        Tasks newTasks = taskMapper.convertDtoToTasks(tasksDto);
        newTasks = userActions
                .checkFindUser(newTasks.getTaskExecutor(), newTasks, ConstantsClass.ONE_FLAG);
        newTasks = userActions.checkFindUser(newTasks.getTaskAuthor(), newTasks, ZERO_FLAG);
        newTasks.setId(ZERO_FLAG);
        tasksRepository.save(newTasks); // save to PostgreSQL
        return taskMapper.convertTasksToDto(newTasks);
    }

    @Override
    @Transactional
    public Optional<TasksDto> changeTasks(TasksDto tasksDto) throws MainException {
        log.info("Метод changeTasks() " + tasksDto.getId());
        Optional<Tasks> optionalTaskDatabase = Optional.empty();
        TaskMapper taskMapper = mappersFabric.createTaskMapper();

        tasksDto = actionsFabric.createTasksActions().fillTaskPriorityAndTaskStatusFields(tasksDto);

        if (tasksDto.getId() != null) {
            optionalTaskDatabase = tasksRepository.findById(taskMapper.convertDtoToTasks(tasksDto).getId());
        }
        if (optionalTaskDatabase.isPresent()) {
            Tasks tasks = optionalTaskDatabase.get();
            TasksDto newTasksDto = taskMapper.convertTasksToDto(tasks);

            boolean resultCheckPrivilege = checkPrivilegeTasks(newTasksDto, tasksDto);
            if (!resultCheckPrivilege) {
                return Optional.empty();
            }
            if (tasksDto.getTaskAuthor() == null) {
                tasksDto.setTaskAuthor(newTasksDto.getTaskAuthor());
            }
            Tasks newTasks = taskMapper.compareTaskAndDto(tasksDto, optionalTaskDatabase.get());
            newTasks = tasksRepository.save(newTasks);
            return Optional.of(taskMapper.convertTasksToDto(newTasks));
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public Optional<List<TasksDto>> getTasksOfAuthorOrExecutor(String authorOrExecutor, Integer offset, Integer limit, Integer flag) {
        log.info("Метод getTasksOfAuthorOrExecutor() " + authorOrExecutor + EMPTY_SPACE + flag);
        if (flag.equals(ConstantsClass.ONE_FLAG)) {
            return Optional.of(receiveAllTasksAuthorOrExecutorDataBase(authorOrExecutor, offset, limit, ConstantsClass.ONE_FLAG));
        } else if (flag.equals(ZERO_FLAG)) {
            return Optional.of(receiveAllTasksAuthorOrExecutorDataBase(authorOrExecutor, offset, limit, ZERO_FLAG));
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public boolean deleteTasks(Integer idTasks) {
        log.info("Метод deleteTasks() " + idTasks );
        boolean resultDeleteTasks = tasksRepository.existsById(idTasks);
        if (resultDeleteTasks) {
            tasksRepository.deleteById(idTasks);
            return true;
        }
        return false;
    }

    /**
     * Метод, ищущий задачи и комментарии к ним по автору/исполнителю
     *
     * @param authorOrExecutor
     * @param offset
     * @param limit
     * @param flag
     * @return Список с задачами по заданному пользователю
     */
    private List<TasksDto> receiveAllTasksAuthorOrExecutorDataBase(String authorOrExecutor, Integer offset, Integer limit,
                                                                   Integer flag) {
        log.info("Метод receiveAllTasksAuthorOrExecutorDataBase() " + authorOrExecutor + EMPTY_SPACE + flag);
        ValidationClass validationClass = new ValidationClassImpl();
        List<Tasks> listAllTasks = new LinkedList<>();

        Optional<ValidationClassImpl> resultValid = validationClass.validEmailOrId(authorOrExecutor);
        if (resultValid.isPresent()) {

            Pageable pageble = PageRequest.of(offset, limit);
            String userEmail = resultValid.get().getValidationString();
            Integer userId = resultValid.get().getValidationInteger();

            if (userEmail != null) {
                Optional<CustomUsers> optionalCustomUsers = authorizationRepository.findByEmail(userEmail);
                if (optionalCustomUsers.isPresent()) {
                    Optional<Page<Tasks>> pageWithTasks = methodFindAllTasksAuthorOrExecutor(pageble, optionalCustomUsers.get()
                            .getId(), flag);
                    if (pageWithTasks.isPresent()) {
                        listAllTasks = pageWithTasks.get().stream().toList();
                    }
                }
            } else if (userId != null) {
                Optional<Page<Tasks>> pageWithTasks = methodFindAllTasksAuthorOrExecutor(pageble, userId, flag);
                if (pageWithTasks.isPresent()) {
                    listAllTasks = pageWithTasks.get().stream().toList();
                }
            }

        }
        return mappersFabric.createTaskMapper().transferListTasksToDto(listAllTasks);
    }

    /**
     * Метод, делающий запросы к БД для поиска всех задач по автору/исполнителю
     *
     * @param pageble
     * @param userId
     * @param flag
     * @return {@link Optional<Page<Tasks>>} Optional со страницей с задачами
     */
    private Optional<Page<Tasks>> methodFindAllTasksAuthorOrExecutor(Pageable pageble, Integer userId, Integer flag) {
        log.info("Метод methodFindAllTasksAuthorOrExecutor() " + userId + EMPTY_SPACE + flag);
        if (flag.equals(ConstantsClass.ONE_FLAG)) {
            return Optional.of(tasksRepository.findAllByTaskAuthorId(userId, pageble));
        } else if (flag.equals(ZERO_FLAG)) {
            return Optional.of(tasksRepository.findAllByTaskExecutorId(userId, pageble));
        }
        return Optional.empty();
    }

    /**
     * Метод, проверяющий наличие прав на редактирование задачи
     *
     * @param tasksDtoFromDB
     * @param tasksDto
     * @throws NotEnoughRulesEntity
     */
    private boolean checkPrivilegeTasks(TasksDto tasksDtoFromDB, TasksDto tasksDto) throws NotEnoughRulesEntity {
        log.info("Метод checkPrivilegeTasks() " + tasksDto.getId() + EMPTY_SPACE + tasksDtoFromDB.getId());
        TasksActions tasksActions = actionsFabric.createTasksActions();

        CustomUsersDto userDtoAuthorTaskDB = tasksDtoFromDB.getTaskAuthor();
        CustomUsersDto userCurrentDtoExecutorTaskDB = tasksDto.getTaskExecutor();
        boolean availabilityRules = tasksActions.isPrivilegeTasks(userDtoAuthorTaskDB);
        if (availabilityRules) {
            return true;
        } else {
            if (tasksActions.isPrivilegeTasks(userCurrentDtoExecutorTaskDB)) {
                return isFieldsTasksDtoIsNullOrNot(tasksDto);
            } else {
                return false;
            }
        }
    }

    /**
     * Метод, проверяющий поля {@link TasksDto} на null
     *
     * @param tasksDto
     * @throws NotEnoughRulesEntity
     */
    private boolean isFieldsTasksDtoIsNullOrNot(TasksDto tasksDto) throws NotEnoughRulesEntity {
        log.info("Метод isFieldsTasksDtoIsNullOrNot() " + tasksDto.getId());
        if ((tasksDto.getNotesDto() == null
                && tasksDto.getTaskPriority() == null
                && tasksDto.getTaskAuthor() == null
                && tasksDto.getTaskExecutor() == null
                && tasksDto.getDescription() == null
                && tasksDto.getHeader() == null)
                ||
                (tasksDto.getId() != null && tasksDto.getTaskStatus() != null)
        ) {
            return true;
        } else {
            throw new NotEnoughRulesEntity(DescriptionUserExeption.NOT_ENOUGH_RULES_EXECUTOR.getEnumDescription());
        }
    }


}