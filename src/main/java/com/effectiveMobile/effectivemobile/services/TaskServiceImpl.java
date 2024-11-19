package com.effectiveMobile.effectivemobile.services;

import com.effectiveMobile.effectivemobile.dto.CustomUsersDto;
import com.effectiveMobile.effectivemobile.repository.AuthorizationRepository;
import com.effectiveMobile.effectivemobile.entities.CustomUsers;
import com.effectiveMobile.effectivemobile.auxiliaryclasses.ValidationClass;
import com.effectiveMobile.effectivemobile.auxiliaryclasses.ValidationClassImpl;
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

import static com.effectiveMobile.effectivemobile.constants.ConstantsClass.EMPTY;
import static com.effectiveMobile.effectivemobile.constants.ConstantsClass.REGIME_OVERWRITING;

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
    public TasksDto createTasks(TasksDto tasksDto) throws UsernameNotFoundException, ExecutorNotFoundExeption {
        log.info("Метод createTasks() " + tasksDto.getId());
        Optional<CustomUsers> optionalAuthorizedUser = actionsFabric.createUserActions().getCurrentUser();
        tasksDto.setTaskAuthor(mappersFabric.createUserMapper().convertUserToDto(optionalAuthorizedUser.get()));
        Tasks newTasks = mappersFabric.createTaskMapper().convertDtoToTasks(tasksDto);
        newTasks = actionsFabric
                .createUserActions()
                .checkFindUser(newTasks.getTaskExecutor(), newTasks, ConstantsClass.REGIME_RECORD);
        newTasks = actionsFabric
                .createUserActions()
                .checkFindUser(newTasks.getTaskAuthor(), newTasks, REGIME_OVERWRITING);
        newTasks.setId(REGIME_OVERWRITING);
        tasksRepository.save(newTasks); // save to PostgreSQL
        return mappersFabric
                .createTaskMapper()
                .convertTasksToDto(newTasks);
    }

    @Override
    @Transactional
    public Optional<TasksDto> changeTasks(TasksDto tasksDto) throws ExecutorNotFoundExeption, NotEnoughRulesEntity {
        log.info("Метод changeTasks() " + tasksDto.getId());
        Optional<Tasks> optionalTaskDatabase = Optional.empty();
        if (tasksDto.getId() != null) {
            optionalTaskDatabase = tasksRepository.findById(mappersFabric
                    .createTaskMapper()
                    .convertDtoToTasks(tasksDto)
                    .getId());
        }
        if (optionalTaskDatabase.isPresent()) {
            TasksDto newTasksDto = mappersFabric
                    .createTaskMapper()
                    .convertTasksToDto(optionalTaskDatabase.get());

            boolean resultCheckPrivilege = checkPrivilegeTasks(newTasksDto, tasksDto);
            if (!resultCheckPrivilege) {
                return Optional.empty();
            }
            if (tasksDto.getTaskAuthor() == null) {
                tasksDto.setTaskAuthor(newTasksDto.getTaskAuthor());
            }
            Tasks newTasks = mappersFabric
                    .createTaskMapper()
                    .compareTaskAndDto(tasksDto, optionalTaskDatabase.get());
            newTasks = tasksRepository.save(newTasks);
            return Optional.of(mappersFabric
                    .createTaskMapper()
                    .convertTasksToDto(newTasks));
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public Optional<List<TasksDto>> getTasksOfAuthorOrExecutor(String authorOrExecutor, Integer offset, Integer limit, Integer flag) {
        log.info("Метод getTasksOfAuthorOrExecutor() " + authorOrExecutor + EMPTY + flag);
        if (flag.equals(ConstantsClass.REGIME_RECORD)) {
            return Optional.of(receiveAllTasksAuthorOrExecutorDataBase(authorOrExecutor, offset, limit, ConstantsClass.REGIME_RECORD));
        } else if (flag.equals(REGIME_OVERWRITING)) {
            return Optional.of(receiveAllTasksAuthorOrExecutorDataBase(authorOrExecutor, offset, limit, REGIME_OVERWRITING));
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
        log.info("Метод receiveAllTasksAuthorOrExecutorDataBase() " + authorOrExecutor + EMPTY + flag);
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
        log.info("Метод methodFindAllTasksAuthorOrExecutor() " + userId + EMPTY + flag);
        if (flag.equals(ConstantsClass.REGIME_RECORD)) {
            return Optional.of(tasksRepository.findAllByTaskAuthorId(userId, pageble));
        } else if (flag.equals(REGIME_OVERWRITING)) {
            return Optional.of(tasksRepository.findAllByTaskExecutorId(userId, pageble));
        }
        return Optional.empty();
    }

    /**
     * Метод, проверяющий наличие прав на редактирование задачи
     *
     * @param tasksDtoFromDB
     * @param tasksDto
     * @return
     * @throws NotEnoughRulesEntity
     */
    private boolean checkPrivilegeTasks(TasksDto tasksDtoFromDB, TasksDto tasksDto) throws NotEnoughRulesEntity {
        log.info("Метод checkPrivilegeTasks() " + tasksDto.getId() + EMPTY + tasksDtoFromDB.getId());
        CustomUsersDto userDtoAuthorTaskDB = tasksDtoFromDB.getTaskAuthor();
        CustomUsersDto userCurrentDtoExecutorTaskDB = tasksDto.getTaskExecutor();
        boolean availabilityRules = actionsFabric
                .createTasksActions()
                .isPrivilegeTasks(userDtoAuthorTaskDB);
        if (availabilityRules) {
            return true;
        } else {
            if (actionsFabric.createTasksActions().isPrivilegeTasks(userCurrentDtoExecutorTaskDB)) {
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