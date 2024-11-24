package com.effectiveMobile.effectivemobile.services;

import com.effectiveMobile.effectivemobile.auxiliaryclasses.*;
import com.effectiveMobile.effectivemobile.dto.CustomUsersDto;
import com.effectiveMobile.effectivemobile.exeptions.MainException;
import com.effectiveMobile.effectivemobile.exeptions.RoleNotFoundException;
import com.effectiveMobile.effectivemobile.mapper.TaskMapper;
import com.effectiveMobile.effectivemobile.other.UserRoles;
import com.effectiveMobile.effectivemobile.repository.AuthorizationRepository;
import com.effectiveMobile.effectivemobile.entities.CustomUsers;
import com.effectiveMobile.effectivemobile.constants.ConstantsClass;
import com.effectiveMobile.effectivemobile.exeptions.NotEnoughRulesForEntity;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static com.effectiveMobile.effectivemobile.constants.ConstantsClass.EMPTY_SPACE;
import static com.effectiveMobile.effectivemobile.constants.ConstantsClass.ZERO_FLAG;
import static com.effectiveMobile.effectivemobile.exeptions.DescriptionUserExeption.*;

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
    public TasksDto createTasks(TasksDto tasksDto) throws MainException {
        log.info("Метод createTasks() " + tasksDto.getHeader());
        UserActions userActions = actionsFabric.createUserActions();
        TaskMapper taskMapper = mappersFabric.createTaskMapper();

        /*tasksDto = actionsFabric.createTasksActions().fillTaskPriorityAndTaskStatusFields(tasksDto);*/
        tasksDto = assignAuthorTask(userActions, tasksDto);
        Tasks newTasks = taskMapper.convertDtoToTasks(tasksDto);
        newTasks = userActions
                .checkFindUser(newTasks.getTaskExecutor(), newTasks, ConstantsClass.ONE_FLAG);
        newTasks = userActions
                .checkFindUser(newTasks.getTaskAuthor(), newTasks, ZERO_FLAG);
        newTasks.setId(ZERO_FLAG);
        newTasks = tasksRepository.save(newTasks); // save to PostgreSQL
        return taskMapper.convertTasksToDto(newTasks);
    }

    @Override
    @Transactional
    public Optional<TasksDto> changeTasks(TasksDto tasksDtoFromJson) throws MainException {
        log.info("Метод changeTasks() " + tasksDtoFromJson.getId());
        Optional<Tasks> optionalTaskDatabase = Optional.empty();
        TaskMapper taskMapper = mappersFabric.createTaskMapper();

        if (tasksDtoFromJson.getId() != null) {
            optionalTaskDatabase = tasksRepository.findById(taskMapper.convertDtoToTasks(tasksDtoFromJson).getId());
        }
        if (optionalTaskDatabase.isPresent()) {
            Tasks tasks = optionalTaskDatabase.get();
            TasksDto newTasksDtoFromDB = taskMapper.convertTasksToDto(tasks);

            boolean resultCheckPrivilege = checkingRightsToEditTask(newTasksDtoFromDB, tasksDtoFromJson);
            if (!resultCheckPrivilege) {
                return Optional.empty();
            }
            Tasks newTasks = taskMapper.compareTaskAndDto(tasksDtoFromJson, optionalTaskDatabase.get());
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
        log.info("Метод deleteTasks() " + idTasks);
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
     * Метод, определяющий, может ли пользователь редактировать задачу или нет
     *
     * @param tasksDtoFromDB
     * @throws NotEnoughRulesForEntity
     */
    private boolean checkingRightsToEditTask(TasksDto tasksDtoFromDB, TasksDto tasksDtoFromJson) throws NotEnoughRulesForEntity, RoleNotFoundException {
        log.info("Метод isExecutorOfTaskOrNot() " + tasksDtoFromDB.getId());
        UserActions userActions = actionsFabric.createUserActions();

        CustomUsersDto userDtoAuthorTaskDB = tasksDtoFromDB.getTaskExecutor();
        boolean resultEmailsEqual = userActions.comparisonEmailTasksFromDBAndEmailCurrentAuthUser(userDtoAuthorTaskDB);
        boolean resultHaveAdminRoleOrNot = userActions.currentUserAdminOrUserRole(UserRoles.ADMIN.getUserRoles()); // Check является ли администратором
        if (resultHaveAdminRoleOrNot) { // Является администратором
            return true;
        }
        if (resultEmailsEqual) { // Не является администратором, емейлы исп-ля и авторизованного польз-ля равны
            boolean resultHaveUserRoleOrNot = userActions.currentUserAdminOrUserRole(UserRoles.USER.getUserRoles());
            if (resultHaveUserRoleOrNot) {
                log.error("Метод isExecutorOfTaskOrNot(), выброшен NotEnoughRulesForEntity");
                return actionsFabric
                        .createTasksActions()
                        .fieldsTasksAllowedForEditing(tasksDtoFromDB);
            }
        }
        if (!resultEmailsEqual) { // Не является администратором, емейлы исп-ля и авторизованного польз-ля не равны
            log.error("Метод isExecutorOfTaskOrNot(), выброшен NotEnoughRulesForEntity");
            throw new NotEnoughRulesForEntity(NOT_ENOUGH_RULES_MUST_BE_ADMIN.getEnumDescription());
        }
        return false;
        /*String adminRole = UserRoles.ADMIN.getUserRoles();
        Optional<String> roleCurrentAuthorizedUser = actionsFabric
                .createUserActions()
                .getRoleCurrentAuthorizedUser(adminRole);
        log.info("РООООЛЬ" + roleCurrentAuthorizedUser.map(String::valueOf).orElse("ПУСТО")
        );
        if (roleCurrentAuthorizedUser.isEmpty() || !roleCurrentAuthorizedUser.get().equals(adminRole)) {
            throw new NotEnoughRulesForEntity(NOT_ENOUGH_RULES_MUST_BE_ADMIN.getEnumDescription());
        }*/
        /*if (availabilityRules) {
            fieldsAllowedForEditing(tasksDtoFromDB);
            return true;
        } else {
            log.info("Метод isExecutorOfTaskOrNot() " + tasksDtoFromDB.getId() + " availabilityRules - false");
            return false;
        }*/
    }

    /**
     * Метод, проверяющий поля {@link TasksDto}, которые пытается отредактировать пользователь
     *
     * @param tasksDto
     * @throws NotEnoughRulesForEntity
     */
    private boolean fieldsAllowedForEditing(TasksDto tasksDto) throws NotEnoughRulesForEntity {
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

    private TasksDto assignAuthorTask(UserActions userActions, TasksDto tasksDto) {
        Optional<CustomUsers> optionalAuthorizedUser = userActions.getCurrentUser();
        CustomUsers authorizedUser = optionalAuthorizedUser.get();
        tasksDto.setTaskAuthor(mappersFabric.createUserMapper().convertUserToDto(authorizedUser));
        return tasksDto;
    }

}