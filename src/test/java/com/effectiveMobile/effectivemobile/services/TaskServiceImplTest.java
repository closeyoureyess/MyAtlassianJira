package com.effectiveMobile.effectivemobile.services;

import com.effectiveMobile.effectivemobile.auxiliaryclasses.TasksActions;
import com.effectiveMobile.effectivemobile.auxiliaryclasses.UserActions;
import com.effectiveMobile.effectivemobile.dto.CustomUsersDto;
import com.effectiveMobile.effectivemobile.dto.TasksDto;
import com.effectiveMobile.effectivemobile.entities.CustomUsers;
import com.effectiveMobile.effectivemobile.entities.Tasks;
import com.effectiveMobile.effectivemobile.exeptions.*;
import com.effectiveMobile.effectivemobile.fabrics.ActionsFabric;
import com.effectiveMobile.effectivemobile.fabrics.MappersFabric;
import com.effectiveMobile.effectivemobile.mapper.TaskMapper;
import com.effectiveMobile.effectivemobile.mapper.UserMapper;
import com.effectiveMobile.effectivemobile.other.TaskPriorityEnum;
import com.effectiveMobile.effectivemobile.other.TaskStatusEnum;
import com.effectiveMobile.effectivemobile.other.UserRoles;
import com.effectiveMobile.effectivemobile.repository.AuthorizationRepository;
import com.effectiveMobile.effectivemobile.repository.TasksRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.UndeclaredThrowableException;
import java.util.List;
import java.util.Optional;

import static com.effectiveMobile.effectivemobile.exeptions.DescriptionUserExeption.NOT_ENOUGH_RULES_MUST_BE_ADMIN;

class TaskServiceImplTest {

    private TaskServiceImpl taskService;
    private MappersFabric mockMappersFabric;
    private ActionsFabric mockActionsFabric;
    private TasksRepository mockTasksRepository;
    private AuthorizationRepository mockAuthorizationRepository;
    private TaskMapper mockTaskMapper;
    private UserActions userActions;
    private UserMapper userMapper;
    private TasksActions tasksActions;

    @BeforeEach
    void setUp() {
        mockMappersFabric = Mockito.mock(MappersFabric.class);
        mockActionsFabric = Mockito.mock(ActionsFabric.class);
        mockTasksRepository = Mockito.mock(TasksRepository.class);
        mockAuthorizationRepository = Mockito.mock(AuthorizationRepository.class);
        mockTaskMapper = Mockito.mock(TaskMapper.class);
        userActions = Mockito.mock(UserActions.class);
        userMapper = Mockito.mock(UserMapper.class);
        tasksActions = Mockito.mock(TasksActions.class);

        taskService = new TaskServiceImpl();
        taskService.setMappersFabric(mockMappersFabric);
        taskService.setActionsFabric(mockActionsFabric);
        taskService.setTasksRepository(mockTasksRepository);
        taskService.setAuthorizationRepository(mockAuthorizationRepository);

        Mockito.when(mockMappersFabric.createTaskMapper()).thenReturn(mockTaskMapper);
    }

    @Test
    @DisplayName("Тест: Успешное создание задачи")
    void testCreateTasksSuccess() throws MainException {
        TasksDto tasksDto = new TasksDto();
        tasksDto.setHeader("Test Task");
        tasksDto.setTaskExecutor(new CustomUsersDto());
        tasksDto.setTaskPriority(null);
        tasksDto.setTaskStatus(null);

        TasksDto tasksDtoNew = tasksDto;
        tasksDtoNew.setTaskPriority(TaskPriorityEnum.MEDIUM);
        tasksDtoNew.setTaskStatus(TaskStatusEnum.BACKLOG);

        Tasks tasks = new Tasks();
        tasks.setId(1);
        tasks.setHeader("Test Task");
        tasks.setTaskExecutor(new CustomUsers());
        tasks.setTaskPriority(TaskPriorityEnum.HIGH);
        tasks.setTaskStatus(TaskStatusEnum.IN_PROGRESS);

        Mockito.when(mockActionsFabric.createTasksActions()).thenReturn(tasksActions);
        Mockito.when(tasksActions.fillTaskPriorityAndTaskStatusFields(tasksDto)).thenReturn(tasksDtoNew);
        Mockito.when(mockMappersFabric.createUserMapper()).thenReturn(userMapper);
        Mockito.when(mockActionsFabric.createUserActions()).thenReturn(userActions);
        Mockito.when(userActions.checkFindUser(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(tasks);
        Mockito.when(userActions.getCurrentUser()).thenReturn(Optional.of(new CustomUsers()));
        Mockito.when(mockTaskMapper.convertDtoToTasks(tasksDtoNew)).thenReturn(tasks);
        Mockito.when(userMapper.convertUserToDto(Mockito.any())).thenReturn(new CustomUsersDto());
        Mockito.when(mockTasksRepository.save(tasks)).thenReturn(tasks);
        Mockito.when(mockTaskMapper.convertTasksToDto(tasks)).thenReturn(tasksDtoNew);

        TasksDto result = taskService.createTasks(tasksDto);

        Assertions.assertNotNull(result, "Результат не должен быть null");
       Assertions.assertEquals(tasksDtoNew, result);
        Mockito.verify(mockTasksRepository,  Mockito.times(1)).save(tasks);
    }

    @Test
    @DisplayName("Тест: Успешное изменение задачи")
    void testChangeTasksSuccess() throws MainException {
        TasksDto tasksDto = new TasksDto();
        tasksDto.setId(1);
        tasksDto.setHeader("Updated Task");
        tasksDto.setTaskExecutor(new CustomUsersDto());
        tasksDto.setTaskPriority(TaskPriorityEnum.MEDIUM);
        tasksDto.setTaskStatus(TaskStatusEnum.BACKLOG);

        Tasks tasks = new Tasks();
        tasks.setId(1);
        tasks.setHeader("Test Task");
        tasks.setTaskExecutor(new CustomUsers());
        tasks.setTaskPriority(TaskPriorityEnum.HIGH);
        tasks.setTaskStatus(TaskStatusEnum.IN_PROGRESS);

        Mockito.when(mockTaskMapper.convertDtoToTasks(tasksDto)).thenReturn(tasks);
        Mockito.when(mockActionsFabric.createUserActions()).thenReturn(userActions);
        Mockito.when(mockTasksRepository.findById(1)).thenReturn(Optional.of(tasks));
        Mockito.when(mockTaskMapper.compareTaskAndDto(tasksDto, tasks)).thenReturn(tasks);
        Mockito.when(mockTasksRepository.save(tasks)).thenReturn(tasks);
        Mockito.when(mockTaskMapper.convertTasksToDto(tasks)).thenReturn(tasksDto);

        // Устанавливаем права пользователя
        Mockito.when(userActions.comparisonEmailTasksFromDBAndEmailCurrentAuthUser( Mockito.any())).thenReturn(true);
        Mockito.when(userActions.currentUserAdminOrUserRole(UserRoles.ADMIN.getUserRoles())).thenReturn(true);

        Optional<TasksDto> result = taskService.changeTasks(tasksDto);

        Assertions.assertTrue(result.isPresent(), "Результат должен быть непустым");
        Assertions.assertEquals(tasksDto, result.get(), "Результат должен совпадать с DTO");
        Mockito.verify(mockTasksRepository,  Mockito.times(1)).save(tasks);
    }

    @Test
    @DisplayName("Тест: Получение задач по автору")
    void testGetTasksOfAuthor() {
        String authorEmail = "author@example.com";
        Integer offset = 0;
        Integer limit = 10;
        Integer flag = 1;

        CustomUsers author = new CustomUsers();
        author.setId(1);
        author.setEmail(authorEmail);

        Tasks task1 = new Tasks();
        task1.setId(1);
        task1.setTaskAuthor(author);

        Tasks task2 = new Tasks();
        task2.setId(2);
        task2.setTaskAuthor(author);

        Page<Tasks> tasksPage = new PageImpl<>(List.of(task1, task2));

        Mockito.when(mockAuthorizationRepository.findByEmail(authorEmail)).thenReturn(Optional.of(author));
        Mockito.when(mockTasksRepository.findAllByTaskAuthorId(author.getId(), PageRequest.of(offset, limit))).thenReturn(tasksPage);
        Mockito.when(mockTaskMapper.transferListTasksToDto(tasksPage.getContent())).thenReturn(List.of(new TasksDto(), new TasksDto()));

        Optional<List<TasksDto>> result = taskService.getTasksOfAuthorOrExecutor(authorEmail, offset, limit, flag);

        Assertions.assertTrue(result.isPresent(), "Результат должен быть непустым");
        Assertions.assertEquals(2, result.get().size(), "Размер списка должен быть 2");
    }

    @Test
    @DisplayName("Тест: Успешное удаление задачи")
    void testDeleteTasksSuccess() {
        Integer taskId = 1;

        Mockito.when(mockTasksRepository.existsById(taskId)).thenReturn(true);

        boolean result = taskService.deleteTasks(taskId);

        Assertions.assertTrue(result, "Результат должен быть true");
        Mockito.verify(mockTasksRepository,  Mockito.times(1)).deleteById(taskId);
    }
    @Test
    @DisplayName("Тест: Выброс исключения при изменении задачи, если задача не найдена")
    void testChangeTasksTaskNotFound() throws ExecutorNotFoundExeption {
        TasksDto tasksDto = new TasksDto();
        tasksDto.setId(1);
        tasksDto.setHeader("Updated Task");
        tasksDto.setTaskExecutor(new CustomUsersDto());
        tasksDto.setTaskPriority(TaskPriorityEnum.MEDIUM);
        tasksDto.setTaskStatus(TaskStatusEnum.BACKLOG);

        Mockito.when(mockTaskMapper.convertDtoToTasks(tasksDto)).thenReturn(new Tasks());
        Mockito. when(mockTasksRepository.findById(1)).thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundExeption.class, () -> {
            taskService.changeTasks(tasksDto);
        });
    }

    @Test
    @DisplayName("Тест: Возврат Optional.empty(), если у пользователя недостаточно прав")
    void testChangeTasksNotEnoughPrivileges() throws MainException {
        TasksDto tasksDto = new TasksDto();
        tasksDto.setId(1);
        tasksDto.setHeader("Updated Task");
        tasksDto.setTaskExecutor(new CustomUsersDto());
        tasksDto.setTaskPriority(TaskPriorityEnum.MEDIUM);
        tasksDto.setTaskStatus(TaskStatusEnum.BACKLOG);

        Tasks tasks = new Tasks();
        tasks.setId(1);
        tasks.setHeader("Test Task");
        tasks.setTaskExecutor(new CustomUsers());
        tasks.setTaskPriority(TaskPriorityEnum.HIGH);
        tasks.setTaskStatus(TaskStatusEnum.IN_PROGRESS);

        Mockito.when(mockTaskMapper.convertDtoToTasks(tasksDto)).thenReturn(tasks);
        Mockito.when(mockTaskMapper.convertTasksToDto(tasks)).thenReturn(tasksDto);
        Mockito.when(mockActionsFabric.createUserActions()).thenReturn(userActions);
        Mockito.when(mockTasksRepository.findById(1)).thenReturn(Optional.of(tasks));
        Mockito.when(userActions.comparisonEmailTasksFromDBAndEmailCurrentAuthUser(Mockito.any())).thenReturn(true);
        Mockito.when(userActions.currentUserAdminOrUserRole(UserRoles.ADMIN.getUserRoles())).thenReturn(false);
        Mockito.when(userActions.currentUserAdminOrUserRole(UserRoles.USER.getUserRoles())).thenReturn(false);

        Optional<TasksDto> result = taskService.changeTasks(tasksDto);

        Assertions.assertFalse(result.isPresent(), "Результат должен быть пустым");
    }

    @Test
    @DisplayName("Тест: Получение задач по исполнителю")
    void testGetTasksOfExecutor() {
        String executorEmail = "executor@example.com";
        Integer offset = 0;
        Integer limit = 10;
        Integer flag = 0;

        CustomUsers executor = new CustomUsers();
        executor.setId(1);
        executor.setEmail(executorEmail);

        Tasks task1 = new Tasks();
        task1.setId(1);
        task1.setTaskExecutor(executor);

        Tasks task2 = new Tasks();
        task2.setId(2);
        task2.setTaskExecutor(executor);

        Page<Tasks> tasksPage = new PageImpl<>(List.of(task1, task2));

        Mockito.when(mockAuthorizationRepository.findByEmail(executorEmail)).thenReturn(Optional.of(executor));
        Mockito.when(mockTasksRepository.findAllByTaskExecutorId(executor.getId(), PageRequest.of(offset, limit))).thenReturn(tasksPage);
        Mockito.when(mockTaskMapper.transferListTasksToDto(tasksPage.getContent())).thenReturn(List.of(new TasksDto(), new TasksDto()));

        Optional<List<TasksDto>> result = taskService.getTasksOfAuthorOrExecutor(executorEmail, offset, limit, flag);

        Assertions.assertTrue(result.isPresent(), "Результат должен быть непустым");
        Assertions.assertEquals(2, result.get().size(), "Размер списка должен быть 2");
    }

    @Test
    @DisplayName("Тест: Возврат Optional.empty(), если флаг не соответствует ни одному из значений")
    void testGetTasksOfAuthorOrExecutorInvalidFlag() {
        String authorOrExecutor = "author@example.com";
        Integer offset = 0;
        Integer limit = 10;
        Integer flag = 2; // Некорректный флаг

        Optional<List<TasksDto>> result = taskService.getTasksOfAuthorOrExecutor(authorOrExecutor, offset, limit, flag);

        Assertions.assertFalse(result.isPresent(), "Результат должен быть пустым");
    }

    @Test
    @DisplayName("Тест: Возврат false, если задача не найдена")
    void testDeleteTasksNotFound() {
        Integer taskId = 1;

        Mockito.when(mockTasksRepository.existsById(taskId)).thenReturn(false);

        boolean result = taskService.deleteTasks(taskId);

        Assertions.assertFalse(result, "Результат должен быть false");
    }

    @Test
    @DisplayName("Тест: Получение задач по ID пользователя")
    void testReceiveAllTasksAuthorOrExecutorDataBaseByUserId() {
        String authorOrExecutor = "1"; // Предположим, что это ID пользователя
        Integer offset = 0;
        Integer limit = 10;
        Integer flag = 1;

        CustomUsers user = new CustomUsers();
        user.setId(1);

        Tasks task1 = new Tasks();
        task1.setId(1);
        task1.setTaskAuthor(user);

        Tasks task2 = new Tasks();
        task2.setId(2);
        task2.setTaskAuthor(user);

        Page<Tasks> tasksPage = new PageImpl<>(List.of(task1, task2));

        Mockito.when(mockAuthorizationRepository.findByEmail( Mockito.anyString())).thenReturn(Optional.empty());
        Mockito.when(mockTasksRepository.findAllByTaskAuthorId(user.getId(), PageRequest.of(offset, limit))).thenReturn(tasksPage);
        Mockito.when(mockTaskMapper.transferListTasksToDto(tasksPage.getContent())).thenReturn(List.of(new TasksDto(), new TasksDto()));

        // Используем ReflectionTestUtils для вызова приватного метода с правильными параметрами
        List<TasksDto> result = ReflectionTestUtils.invokeMethod(taskService,
                "receiveAllTasksAuthorOrExecutorDataBase", authorOrExecutor, offset, limit, flag);

        Assertions.assertEquals(2, result.size(), "Размер списка должен быть 2");
    }

    @Test
    @DisplayName("Тест: Получение задач по исполнителю")
    void testMethodFindAllTasksAuthorOrExecutorByExecutor() {
        Pageable pageable = PageRequest.of(0, 10);
        Integer userId = 1;
        Integer flag = 0;

        CustomUsers executor = new CustomUsers();
        executor.setId(1);

        Tasks task1 = new Tasks();
        task1.setId(1);
        task1.setTaskExecutor(executor);

        Tasks task2 = new Tasks();
        task2.setId(2);
        task2.setTaskExecutor(executor);

        Page<Tasks> tasksPage = new PageImpl<>(List.of(task1, task2));

        Mockito.when(mockTasksRepository.findAllByTaskExecutorId(userId, pageable)).thenReturn(tasksPage);

        // Используем ReflectionTestUtils для вызова приватного метода с правильными параметрами
        Optional<Page<Tasks>> result = ReflectionTestUtils.invokeMethod(taskService, "methodFindAllTasksAuthorOrExecutor", pageable, userId, flag);

        Assertions.assertTrue(result.isPresent(), "Результат должен быть непустым");
        Assertions.assertEquals(2, result.get().getContent().size(), "Размер списка должен быть 2");
    }

    @Test
    @DisplayName("Тест: Возврат Optional.empty(), если флаг не соответствует ни одному из значений")
    void testMethodFindAllTasksAuthorOrExecutorInvalidFlag() {
        Pageable pageable = PageRequest.of(0, 10);
        Integer userId = 1;
        Integer flag = 2; // Некорректный флаг

        // Используем ReflectionTestUtils для вызова приватного метода с правильными параметрами
        Optional<Page<Tasks>> result = ReflectionTestUtils.invokeMethod(taskService, "methodFindAllTasksAuthorOrExecutor", pageable, userId, flag);

        Assertions.assertFalse(result.isPresent(), "Результат должен быть пустым");
    }

    @Test
    @DisplayName("Тест: Проверка прав на редактирование задачи, если емейлы равны")
    void testCheckingRightsToEditTaskEmailsEqual() throws NotEnoughRulesForEntity, RoleNotFoundException {
        TasksDto tasksDtoFromDB = new TasksDto();
        tasksDtoFromDB.setTaskExecutor(new CustomUsersDto());

        Mockito.when(userActions.comparisonEmailTasksFromDBAndEmailCurrentAuthUser(Mockito.any())).thenReturn(true);
        Mockito.when(userActions.currentUserAdminOrUserRole(UserRoles.USER.getUserRoles())).thenReturn(true);
        Mockito.when(mockActionsFabric.createUserActions()).thenReturn(userActions);
        Mockito.when(mockActionsFabric.createTasksActions()).thenReturn(tasksActions);
        Mockito.when(tasksActions.fieldsTasksAllowedForEditing(tasksDtoFromDB)).thenReturn(true);

        // Используем ReflectionTestUtils для вызова приватного метода с правильными параметрами
        boolean result = ReflectionTestUtils.invokeMethod(taskService, "checkingRightsToEditTask", tasksDtoFromDB);

        Assertions.assertTrue(result, "Результат должен быть true");
    }

    @Test
    @DisplayName("Тест: Проверка прав на редактирование задачи, если емейлы не равны")
    void testCheckingRightsToEditTaskEmailsNotEqual() {
        // Создание TasksDto с taskExecutor
        TasksDto tasksDtoFromDB = new TasksDto();
        CustomUsersDto customUsersDto = new CustomUsersDto();
        customUsersDto.setId(1);
        customUsersDto.setEmail("executor@example.com");
        tasksDtoFromDB.setTaskExecutor(customUsersDto);

        // Настройка мока для ActionsFabric
        Mockito.when(mockActionsFabric.createUserActions()).thenReturn(userActions);

        // Настройка мока для UserActions
        Mockito.when(userActions.comparisonEmailTasksFromDBAndEmailCurrentAuthUser(Mockito.any(CustomUsersDto.class))).thenReturn(false);

        // Ожидание выброса UndeclaredThrowableException и проверка причины
        UndeclaredThrowableException exception = Assertions.assertThrows(UndeclaredThrowableException.class, () -> {
            ReflectionTestUtils.invokeMethod(taskService, "checkingRightsToEditTask", tasksDtoFromDB);
        });

        // Проверка, что причина исключения - NotEnoughRulesForEntity
        Throwable cause = exception.getCause();
        Assertions.assertInstanceOf(NotEnoughRulesForEntity.class, cause, "Причина должна быть NotEnoughRulesForEntity");
        Assertions.assertEquals(NOT_ENOUGH_RULES_MUST_BE_ADMIN.getEnumDescription(), cause.getMessage(),
                "Сообщение исключения должно совпадать");
    }
}
