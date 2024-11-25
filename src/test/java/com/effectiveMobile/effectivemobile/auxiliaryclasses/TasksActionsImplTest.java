package com.effectiveMobile.effectivemobile.auxiliaryclasses;

import com.effectiveMobile.effectivemobile.dto.TasksDto;
import com.effectiveMobile.effectivemobile.exeptions.MainException;
import com.effectiveMobile.effectivemobile.exeptions.NotEnoughRulesForEntity;
import com.effectiveMobile.effectivemobile.other.DefaultSettingsFieldNameEnum;
import com.effectiveMobile.effectivemobile.other.TaskPriorityEnum;
import com.effectiveMobile.effectivemobile.other.TaskStatusEnum;
import com.effectiveMobile.effectivemobile.repository.AuthorizationRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

/**
 * Тестовый класс для TasksActionsImpl
 */
@ExtendWith(MockitoExtension.class)
class TasksActionsImplTest {

    @Mock
    private AuthorizationRepository authorizationRepository;

    @Mock
    private DefaultSettingsActions defaultSettingsActions;

    @InjectMocks
    private TasksActionsImpl tasksActionsImpl;

    private TasksDto tasksDto;

    @BeforeEach
    void setUp() {
        // Инициализация тестовых данных
        tasksDto = TasksDto.builder()
                .id(1)
                .header("Тестовая задача")
                .description("Описание тестовой задачи")
                .taskPriority(TaskPriorityEnum.LOW)
                .taskStatus(TaskStatusEnum.PENDING)
                .build();
    }

    /**
     * Тест для метода fillTaskPriorityAndTaskStatusFields
     * Сценарий: taskPriority == null, настройка присутствует
     */
    @Test
    @DisplayName("fillTaskPriorityAndTaskStatusFields: taskPriority null и настройка присутствует")
    void testFillTaskPriorityAndTaskStatusFields_TaskPriorityNull_SettingsPresent() throws MainException {
        // Arrange
        tasksDto.setTaskPriority(null);
        TaskStatusEnum defaultTaskStatus = TaskStatusEnum.BACKLOG;
        Mockito.when(defaultSettingsActions.getDefaultValueFromTasksFields(DefaultSettingsFieldNameEnum.TASK_PRIORITY, TaskStatusEnum.BACKLOG))
                .thenReturn(Optional.of(defaultTaskStatus));

        // Act
        TasksDto result = tasksActionsImpl.fillTaskPriorityAndTaskStatusFields(tasksDto);
        TaskStatusEnum tseExpected = result.getTaskStatus();

        // Assert
        Assertions.assertNotNull(result, "Результат не должен быть null");
        Assertions.assertEquals(defaultTaskStatus, result.getTaskStatus(), "taskStatus должен быть установлен в BACKLOG");
        Assertions.assertNull(result.getTaskPriority(), "taskPriority должен оставаться null");
        Mockito.verify(defaultSettingsActions, Mockito.times(1))
                .getDefaultValueFromTasksFields(DefaultSettingsFieldNameEnum.TASK_PRIORITY, TaskStatusEnum.BACKLOG);
    }

    /**
     * Тест для метода fillTaskPriorityAndTaskStatusFields
     * Сценарий: taskStatus == null, настройка присутствует
     */
    @Test
    @DisplayName("fillTaskPriorityAndTaskStatusFields: taskStatus null и настройка присутствует")
    void testFillTaskPriorityAndTaskStatusFields_TaskStatusNull_SettingsPresent() throws MainException {
        // Arrange
        tasksDto.setTaskStatus(null);
        TaskPriorityEnum defaultTaskPriority = TaskPriorityEnum.MEDIUM;
        Mockito.when(defaultSettingsActions.getDefaultValueFromTasksFields(DefaultSettingsFieldNameEnum.TASK_STATUS, TaskPriorityEnum.MEDIUM))
                .thenReturn(Optional.of(defaultTaskPriority));

        // Act
        TasksDto result = tasksActionsImpl.fillTaskPriorityAndTaskStatusFields(tasksDto);

        // Assert
        Assertions.assertNotNull(result, "Результат не должен быть null");
        Assertions.assertEquals(defaultTaskPriority, result.getTaskPriority(), "taskPriority должен быть установлен в MEDIUM");
        Assertions.assertNull(result.getTaskStatus(), "taskStatus должен оставаться null");
        Mockito.verify(defaultSettingsActions, Mockito.times(1))
                .getDefaultValueFromTasksFields(DefaultSettingsFieldNameEnum.TASK_STATUS, TaskPriorityEnum.MEDIUM);
    }

    /**
     * Тест для метода fillTaskPriorityAndTaskStatusFields
     * Сценарий: taskPriority == null, настройка отсутствует
     */
    @Test
    @DisplayName("fillTaskPriorityAndTaskStatusFields: taskPriority null и настройка отсутствует")
    void testFillTaskPriorityAndTaskStatusFields_TaskPriorityNull_SettingsAbsent() throws MainException {
        // Arrange
        tasksDto.setTaskPriority(null);
        Mockito.when(defaultSettingsActions.getDefaultValueFromTasksFields(DefaultSettingsFieldNameEnum.TASK_PRIORITY, TaskStatusEnum.BACKLOG))
                .thenReturn(Optional.empty());

        // Act
        TasksDto result = tasksActionsImpl.fillTaskPriorityAndTaskStatusFields(tasksDto);

        // Assert
        Assertions.assertNotNull(result, "Результат не должен быть null");
        Assertions.assertNull(result.getTaskPriority(), "taskPriority должен оставаться null");
        Mockito.verify(defaultSettingsActions, Mockito.times(1))
                .getDefaultValueFromTasksFields(DefaultSettingsFieldNameEnum.TASK_PRIORITY, TaskStatusEnum.BACKLOG);
    }

    /**
     * Тест для метода fillTaskPriorityAndTaskStatusFields
     * Сценарий: taskStatus == null, настройка отсутствует
     */
    @Test
    @DisplayName("fillTaskPriorityAndTaskStatusFields: taskStatus null и настройка отсутствует")
    void testFillTaskPriorityAndTaskStatusFields_TaskStatusNull_SettingsAbsent() throws MainException {
        // Arrange
        tasksDto.setTaskStatus(null);
        Mockito.when(defaultSettingsActions.getDefaultValueFromTasksFields(DefaultSettingsFieldNameEnum.TASK_STATUS, TaskPriorityEnum.MEDIUM))
                .thenReturn(Optional.empty());

        // Act
        TasksDto result = tasksActionsImpl.fillTaskPriorityAndTaskStatusFields(tasksDto);

        // Assertss
        Assertions.assertNotNull(result, "Результат не должен быть null");
        Assertions.assertNull(result.getTaskStatus(), "taskStatus должен оставаться null");
        Mockito.verify(defaultSettingsActions, Mockito.times(1))
                .getDefaultValueFromTasksFields(DefaultSettingsFieldNameEnum.TASK_STATUS, TaskPriorityEnum.MEDIUM);
    }

    /**
     * Тест для метода fieldsTasksAllowedForEditing
     * Сценарий: все условия выполняются, редактирование разрешено
     */
    @Test
    @DisplayName("fieldsTasksAllowedForEditing: условия выполнены, редактирование разрешено")
    void testFieldsTasksAllowedForEditing_Allowed() throws NotEnoughRulesForEntity {
        // Arrange
        TasksDto allowedTasksDto = TasksDto.builder()
                .id(1)
                .taskStatus(TaskStatusEnum.IN_PROGRESS)
                .notesDto(null)
                .taskPriority(null)
                .taskAuthor(null)
                .taskExecutor(null)
                .description(null)
                .header(null)
                .build();

        // Act & Assert
        boolean result = tasksActionsImpl.fieldsTasksAllowedForEditing(allowedTasksDto);
        Assertions.assertTrue(result, "Редактирование должно быть разрешено");
    }

    /**
     * Тест для метода fieldsTasksAllowedForEditing
     * Сценарий: условия не выполнены, выбрасывается NotEnoughRulesForEntity
     */
    @Test
    @DisplayName("fieldsTasksAllowedForEditing: условия не выполнены, выбрасывается NotEnoughRulesForEntity")
    void testFieldsTasksAllowedForEditing_NotAllowed() {
        // Arrange
        TasksDto notAllowedTasksDto = TasksDto.builder()
                .id(1)
                .taskStatus(TaskStatusEnum.IN_PROGRESS)
                .taskPriority(TaskPriorityEnum.HIGH) // Дополнительное поле не null
                .build();

        // Act & Assert
        NotEnoughRulesForEntity exception = Assertions.assertThrows(NotEnoughRulesForEntity.class, () -> {
            tasksActionsImpl.fieldsTasksAllowedForEditing(notAllowedTasksDto);
        }, "Должен быть выброшен NotEnoughRulesForEntity");

        Assertions.assertEquals("Для редактирования недостаточно прав на сущность. Исполнитель по задаче может редактировать только статус задачи и оставлять комментарии",
                exception.getMessage(), "Сообщение об ошибке должно соответствовать ожидаемому");
    }

    /**
     * Тест для метода fieldsTasksAllowedForEditing
     * Сценарий: tasksDto == null, выбрасывается NotEnoughRulesForEntity
     */
    @Test
    @DisplayName("fieldsTasksAllowedForEditing: tasksDto null, выбрасывается NotEnoughRulesForEntity")
    void testFieldsTasksAllowedForEditing_NullTasksDto() {
        // Arrange
        TasksDto nullTasksDto = null;

        // Act & Assert
        NotEnoughRulesForEntity exception = Assertions.assertThrows(NotEnoughRulesForEntity.class, () -> {
            tasksActionsImpl.fieldsTasksAllowedForEditing(nullTasksDto);
        }, "Должен быть выброшен NotEnoughRulesForEntity");

        Assertions.assertEquals("Для редактирования недостаточно прав на сущность. Исполнитель по задаче может редактировать только статус задачи и оставлять комментарии",
                exception.getMessage(), "Сообщение об ошибке должно соответствовать ожидаемому");
    }
}
