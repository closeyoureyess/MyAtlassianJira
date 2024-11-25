package com.effectiveMobile.effectivemobile.mapper;

import com.effectiveMobile.effectivemobile.auxiliaryclasses.UserActions;
import com.effectiveMobile.effectivemobile.dto.CustomUsersDto;
import com.effectiveMobile.effectivemobile.dto.NotesDto;
import com.effectiveMobile.effectivemobile.dto.TasksDto;
import com.effectiveMobile.effectivemobile.entities.CustomUsers;
import com.effectiveMobile.effectivemobile.entities.Notes;
import com.effectiveMobile.effectivemobile.entities.Tasks;
import com.effectiveMobile.effectivemobile.exeptions.ExecutorNotFoundExeption;
import com.effectiveMobile.effectivemobile.fabrics.ActionsFabric;
import com.effectiveMobile.effectivemobile.other.TaskPriorityEnum;
import com.effectiveMobile.effectivemobile.other.TaskStatusEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.Method;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskMapperImplTest {

    private TaskMapperImpl taskMapper;
    private UserMapper mockUserMapper;
    private NotesMapper mockNotesMapper;
    private ActionsFabric mockActionsFabric;

    @BeforeEach
    void setUp() {
        mockUserMapper = mock(UserMapper.class);
        mockNotesMapper = mock(NotesMapper.class);
        mockActionsFabric = mock(ActionsFabric.class);

        taskMapper = new TaskMapperImpl();
        taskMapper.setUserMapper(mockUserMapper);
        taskMapper.setNotesMapper(mockNotesMapper);
        taskMapper.setActionsFabric(mockActionsFabric);
    }

    @Test
    @DisplayName("Тест: Конвертация DTO в Tasks")
    void testConvertDtoToTasks() throws ExecutorNotFoundExeption {
        TasksDto tasksDto = new TasksDto();
        tasksDto.setId(1);
        tasksDto.setTaskPriority(TaskPriorityEnum.HIGH);
        tasksDto.setNotesDto(List.of(new NotesDto()));

        when(mockNotesMapper.transferDtoToListNotes(anyList())).thenReturn(List.of());

        Tasks result = taskMapper.convertDtoToTasks(tasksDto);

        assertNotNull(result, "Результат не должен быть null");
        assertEquals(tasksDto.getId(), result.getId(), "Идентификаторы должны совпадать");
        assertEquals(TaskPriorityEnum.HIGH, result.getTaskPriority(), "Приоритет должен быть установлен");
        verify(mockNotesMapper, times(1)).transferDtoToListNotes(anyList());
    }

    @Test
    @DisplayName("Тест: Конвертация Tasks в DTO")
    void testConvertTasksToDto() {
        Tasks tasks = new Tasks();
        tasks.setId(1);
        tasks.setTaskPriority(TaskPriorityEnum.MEDIUM);
        tasks.setNotes(List.of(new Notes()));

        when(mockNotesMapper.transferListNotesToDto(anyList())).thenReturn(List.of());

        TasksDto result = taskMapper.convertTasksToDto(tasks);

        assertNotNull(result, "Результат не должен быть null");
        assertEquals(tasks.getId(), result.getId(), "Идентификаторы должны совпадать");
        assertEquals(TaskPriorityEnum.MEDIUM, result.getTaskPriority(), "Приоритет должен быть установлен");
        verify(mockNotesMapper, times(1)).transferListNotesToDto(anyList());
    }

    @Test
    @DisplayName("Тест: Сравнение Tasks и TasksDto")
    void testCompareTaskAndDto() {
        TasksDto tasksDto = new TasksDto();
        tasksDto.setTaskPriority(TaskPriorityEnum.HIGH);

        Tasks tasks = new Tasks();
        tasks.setTaskPriority(TaskPriorityEnum.LOW);

        Tasks result = taskMapper.compareTaskAndDto(tasksDto, tasks);

        assertNotNull(result, "Результат не должен быть null");
        assertEquals(TaskPriorityEnum.HIGH, result.getTaskPriority(), "Приоритет должен быть обновлен");
    }

    @Test
    @DisplayName("Тест: Приватный метод compareTaskAndDtoAuthor")
    void testCompareTaskAndDtoAuthor() throws Exception {
        Method method = TaskMapperImpl.class.getDeclaredMethod("compareTaskAndDtoAuthor", TasksDto.class, Tasks.class);
        method.setAccessible(true);

        TasksDto tasksDto = new TasksDto();
        CustomUsersDto customUsersDto = new CustomUsersDto();
        customUsersDto.setId(1);
        tasksDto.setTaskAuthor(customUsersDto);

        Tasks tasks = new Tasks();
        CustomUsers customUsers = new CustomUsers();
        customUsers.setId(2);
        tasks.setTaskAuthor(customUsers);

        CustomUsers updatedCustomUsers = new CustomUsers();
        updatedCustomUsers.setId(1);

        when(mockUserMapper.convertDtoToUser(customUsersDto)).thenReturn(updatedCustomUsers);
        UserActions userActions = Mockito.mock(UserActions.class);
        when(mockActionsFabric.createUserActions()).thenReturn(userActions);

        // Создаем новый объект Tasks с обновленным автором
        Tasks updatedTasks = new Tasks();
        updatedTasks.setTaskAuthor(updatedCustomUsers);

        when(userActions.checkFindUser(updatedCustomUsers, tasks, 0)).thenReturn(updatedTasks);

        Tasks result = (Tasks) method.invoke(taskMapper, tasksDto, tasks);

        assertEquals(1, result.getTaskAuthor().getId(), "Автор должен быть обновлен");
        verify(mockUserMapper, times(1)).convertDtoToUser(customUsersDto);
        verify(userActions, times(1)).checkFindUser(updatedCustomUsers, tasks, 0);
    }

    @Test
    @DisplayName("Тест: Приватный метод compareTasksAndDtoPriority")
    void testCompareTasksAndDtoPriority() throws Exception {
        Method method = TaskMapperImpl.class.getDeclaredMethod("compareTasksAndDtoPriority", TasksDto.class, Tasks.class);
        method.setAccessible(true);

        TasksDto tasksDto = new TasksDto();
        tasksDto.setTaskPriority(TaskPriorityEnum.HIGH);

        Tasks tasks = new Tasks();

        Tasks result = (Tasks) method.invoke(taskMapper, tasksDto, tasks);

        assertEquals(TaskPriorityEnum.HIGH, result.getTaskPriority(), "Приоритет должен быть обновлен");
    }

    @Test
    @DisplayName("Тест: Приватный метод compareTasksAndDtoDescription")
    void testCompareTasksAndDtoDescription() throws Exception {
        Method method = TaskMapperImpl.class.getDeclaredMethod("compareTasksAndDtoDescription", TasksDto.class, Tasks.class);
        method.setAccessible(true);

        TasksDto tasksDto = new TasksDto();
        tasksDto.setDescription("Updated Description");

        Tasks tasks = new Tasks();

        Tasks result = (Tasks) method.invoke(taskMapper, tasksDto, tasks);

        assertEquals("Updated Description", result.getDescription(), "Описание должно быть обновлено");
    }

    @Test
    @DisplayName("Тест: Приватный метод compareTasksAndDtoHeader")
    void testCompareTasksAndDtoHeader() throws Exception {
        Method method = TaskMapperImpl.class.getDeclaredMethod("compareTasksAndDtoHeader", TasksDto.class, Tasks.class);
        method.setAccessible(true);

        TasksDto tasksDto = new TasksDto();
        tasksDto.setHeader("New Header");

        Tasks tasks = new Tasks();

        Tasks result = (Tasks) method.invoke(taskMapper, tasksDto, tasks);

        assertEquals("New Header", result.getHeader(), "Заголовок должен быть обновлен");
    }

    @Test
    @DisplayName("Тест: Приватный метод compareTasksAndDtoStatus")
    void testCompareTasksAndDtoStatus() throws Exception {
        Method method = TaskMapperImpl.class.getDeclaredMethod("compareTasksAndDtoStatus", TasksDto.class, Tasks.class);
        method.setAccessible(true);

        TasksDto tasksDto = new TasksDto();
        tasksDto.setTaskStatus(TaskStatusEnum.IN_PROGRESS);

        Tasks tasks = new Tasks();
        tasks.setTaskStatus(TaskStatusEnum.BACKLOG);

        Tasks result = (Tasks) method.invoke(taskMapper, tasksDto, tasks);

        assertEquals(TaskStatusEnum.IN_PROGRESS, result.getTaskStatus(), "Статус должен быть обновлен");
    }

    @Test
    @DisplayName("Тест: Конвертация DTO в Tasks с исключением при отсутствии исполнителя")
    void testConvertDtoToTasksWithExecutorNotFoundExeption() {
        TasksDto tasksDto = new TasksDto();
        tasksDto.setTaskExecutor(null);

        when(mockNotesMapper.transferDtoToListNotes(anyList())).thenReturn(List.of());

        assertThrows(ExecutorNotFoundExeption.class, () -> {
            taskMapper.convertDtoToTasks(tasksDto, null);
        }, "Должно быть выброшено исключение ExecutorNotFoundExeption");
    }

    @Test
    @DisplayName("Тест: Конвертация DTO в Tasks с установкой приоритета по умолчанию")
    void testConvertDtoToTasksWithDefaultPriority() throws ExecutorNotFoundExeption {
        TasksDto tasksDto = new TasksDto();
        tasksDto.setId(1);
        tasksDto.setTaskExecutor(new CustomUsersDto());
        tasksDto.setNotesDto(List.of(new NotesDto()));

        when(mockNotesMapper.transferDtoToListNotes(anyList())).thenReturn(List.of());

        Tasks result = taskMapper.convertDtoToTasks(tasksDto);

        assertNotNull(result, "Результат не должен быть null");
        assertEquals(tasksDto.getId(), result.getId(), "Идентификаторы должны совпадать");
        assertEquals(TaskPriorityEnum.MEDIUM, result.getTaskPriority(), "Приоритет должен быть установлен по умолчанию");
        verify(mockNotesMapper, times(1)).transferDtoToListNotes(anyList());
    }

    @Test
    @DisplayName("Тест: Конвертация Tasks в DTO с инициализацией userMapper и notesMapper")
    void testConvertTasksToDtoWithNullMappers() {
        Tasks tasks = new Tasks();
        tasks.setId(1);
        tasks.setTaskPriority(TaskPriorityEnum.MEDIUM);
        tasks.setNotes(List.of(new Notes(1, new CustomUsers(), "test", new Tasks())));

        // Устанавливаем mockUserMapper и mockNotesMapper в null
        ReflectionTestUtils.setField(taskMapper, "userMapper", null);
        ReflectionTestUtils.setField(taskMapper, "notesMapper", null);

        TasksDto result = taskMapper.convertTasksToDto(tasks);

        assertNotNull(result, "Результат не должен быть null");
        assertEquals(tasks.getId(), result.getId(), "Идентификаторы должны совпадать");
        assertEquals(TaskPriorityEnum.MEDIUM, result.getTaskPriority(), "Приоритет должен быть установлен");
    }

    @Test
    @DisplayName("Тест: Конвертация списка Tasks в список DTO")
    void testTransferListTasksToDto() {
        Tasks task1 = new Tasks();
        task1.setId(1);
        task1.setHeader("Task 1");
        task1.setTaskAuthor(new CustomUsers());
        task1.setTaskExecutor(new CustomUsers());
        task1.setDescription("Description 1");
        task1.setTaskPriority(TaskPriorityEnum.HIGH);
        task1.setTaskStatus(TaskStatusEnum.IN_PROGRESS);
        task1.setNotes(List.of(new Notes()));

        Tasks task2 = new Tasks();
        task2.setId(2);
        task2.setHeader("Task 2");
        task2.setTaskAuthor(new CustomUsers());
        task2.setTaskExecutor(new CustomUsers());
        task2.setDescription("Description 2");
        task2.setTaskPriority(TaskPriorityEnum.LOW);
        task2.setTaskStatus(TaskStatusEnum.BACKLOG);
        task2.setNotes(List.of(new Notes()));

        List<Tasks> tasksList = List.of(task1, task2);

        when(mockUserMapper.convertUserToDto(any(CustomUsers.class))).thenReturn(new CustomUsersDto());
        when(mockNotesMapper.transferListNotesToDto(anyList())).thenReturn(List.of());

        List<TasksDto> result = taskMapper.transferListTasksToDto(tasksList);

        assertNotNull(result, "Результат не должен быть null");
        assertEquals(2, result.size(), "Размер списка должен быть 2");
        verify(mockUserMapper, times(4)).convertUserToDto(any(CustomUsers.class));
        verify(mockNotesMapper, times(2)).transferListNotesToDto(anyList());
    }

    @Test
    @DisplayName("Тест: Приватный метод compareTaskAndDtoAuthor с проверкой всех сценариев в первом if")
    void testCompareTaskAndDtoAuthorWithAllScenarios() throws Exception {
        Method method = TaskMapperImpl.class.getDeclaredMethod("compareTaskAndDtoAuthor", TasksDto.class, Tasks.class);
        method.setAccessible(true);

        TasksDto tasksDto = new TasksDto();
        CustomUsersDto customUsersDto = new CustomUsersDto();
        customUsersDto.setId(1);
        tasksDto.setTaskAuthor(customUsersDto);

        Tasks tasks = new Tasks();
        CustomUsers customUsers = new CustomUsers();
        customUsers.setId(2);
        tasks.setTaskAuthor(customUsers);

        CustomUsers updatedCustomUsers = new CustomUsers();
        updatedCustomUsers.setId(1);

        when(mockUserMapper.convertDtoToUser(customUsersDto)).thenReturn(updatedCustomUsers);
        UserActions userActions = Mockito.mock(UserActions.class);
        when(mockActionsFabric.createUserActions()).thenReturn(userActions);

        // Создаем новый объект Tasks с обновленным автором
        Tasks updatedTasks = new Tasks();
        updatedTasks.setTaskAuthor(updatedCustomUsers);

        when(userActions.checkFindUser(updatedCustomUsers, tasks, 0)).thenReturn(updatedTasks);

        // Тест для сценария, когда tasksDto.getTaskAuthor() != null && tasks.getTaskAuthor() != null
        Tasks result = (Tasks) method.invoke(taskMapper, tasksDto, tasks);
        assertEquals(1, result.getTaskAuthor().getId(), "Автор должен быть обновлен");

        // Тест для сценария, когда tasksDto.getTaskAuthor() != null && tasks.getTaskAuthor() == null
        tasks.setTaskAuthor(null);
        result = (Tasks) method.invoke(taskMapper, tasksDto, tasks);
        assertEquals(1, result.getTaskAuthor().getId(), "Автор должен быть обновлен");

        // Тест для сценария, когда tasksDto.getTaskAuthor() == null && tasks.getTaskAuthor() != null
        tasksDto.setTaskAuthor(null);
        tasks.setTaskAuthor(customUsers);
        result = (Tasks) method.invoke(taskMapper, tasksDto, tasks);
        assertEquals(2, result.getTaskAuthor().getId(), "Автор не должен быть обновлен");

        // Тест для сценария, когда tasksDto.getTaskAuthor() == null && tasks.getTaskAuthor() == null
        tasks.setTaskAuthor(null);
        result = (Tasks) method.invoke(taskMapper, tasksDto, tasks);
        assertNull(result.getTaskAuthor(), "Автор должен быть null");

        verify(mockUserMapper, times(2)).convertDtoToUser(customUsersDto);
        verify(userActions, times(2)).checkFindUser(updatedCustomUsers, tasks, 0);
    }

    @Test
    @DisplayName("Тест: Приватный метод compareTasksAndDtoExecutor с проверкой всех сценариев в if")
    void testCompareTasksAndDtoExecutorWithAllScenarios() throws Exception {
        Method method = TaskMapperImpl.class.getDeclaredMethod("compareTasksAndDtoExecutor", TasksDto.class, Tasks.class);
        method.setAccessible(true);

        TasksDto tasksDto = new TasksDto();
        CustomUsersDto customUsersDto = new CustomUsersDto();
        customUsersDto.setId(1);
        tasksDto.setTaskExecutor(customUsersDto);

        Tasks tasks = new Tasks();
        CustomUsers customUsers = new CustomUsers();
        customUsers.setId(2);
        tasks.setTaskExecutor(customUsers);

        CustomUsers updatedCustomUsers = new CustomUsers();
        updatedCustomUsers.setId(1);

        when(mockUserMapper.convertDtoToUser(customUsersDto)).thenReturn(updatedCustomUsers);
        UserActions userActions = Mockito.mock(UserActions.class);
        when(mockActionsFabric.createUserActions()).thenReturn(userActions);

        // Создаем новый объект Tasks с обновленным исполнителем
        Tasks updatedTasks = new Tasks();
        updatedTasks.setTaskExecutor(updatedCustomUsers);

        when(userActions.checkFindUser(updatedCustomUsers, tasks, 1)).thenReturn(updatedTasks);

        // Тест для сценария, когда tasksDto.getTaskExecutor() != null && tasks.getTaskExecutor() != null
        Tasks result = (Tasks) method.invoke(taskMapper, tasksDto, tasks);
        assertEquals(1, result.getTaskExecutor().getId(), "Исполнитель должен быть обновлен");

        // Тест для сценария, когда tasksDto.getTaskExecutor() != null && tasks.getTaskExecutor() == null
        tasks.setTaskExecutor(null);
        result = (Tasks) method.invoke(taskMapper, tasksDto, tasks);
        assertEquals(1, result.getTaskExecutor().getId(), "Исполнитель должен быть обновлен");

        // Тест для сценария, когда tasksDto.getTaskExecutor() == null && tasks.getTaskExecutor() != null
        tasksDto.setTaskExecutor(null);
        tasks.setTaskExecutor(customUsers);
        result = (Tasks) method.invoke(taskMapper, tasksDto, tasks);
        assertEquals(2, result.getTaskExecutor().getId(), "Исполнитель не должен быть обновлен");

        // Тест для сценария, когда tasksDto.getTaskExecutor() == null && tasks.getTaskExecutor() == null
        tasks.setTaskExecutor(null);
        result = (Tasks) method.invoke(taskMapper, tasksDto, tasks);
        assertNull(result.getTaskExecutor(), "Исполнитель должен быть null");

        verify(mockUserMapper, times(2)).convertDtoToUser(customUsersDto);
        verify(userActions, times(2)).checkFindUser(updatedCustomUsers, tasks, 1);
    }
}
