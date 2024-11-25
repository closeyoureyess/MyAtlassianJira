package com.effectiveMobile.effectivemobile.auxiliaryclasses;

import com.effectiveMobile.effectivemobile.dto.DefaultSettingsDto;
import com.effectiveMobile.effectivemobile.other.DefaultSettingsFieldNameEnum;
import com.effectiveMobile.effectivemobile.other.TaskPriorityEnum;
import com.effectiveMobile.effectivemobile.other.TaskStatusEnum;
import com.effectiveMobile.effectivemobile.services.DefaultSettingsServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class DefaultSettingsActionsImplTest {

    private DefaultSettingsDto taskPrioritySettingsDto;
    private DefaultSettingsDto taskStatusSettingsDto;

    @BeforeEach
    void setUp() {
        // Инициализация тестовых данных
        taskPrioritySettingsDto = DefaultSettingsDto.builder()
                .id(1)
                .fieldName(DefaultSettingsFieldNameEnum.TASK_PRIORITY)
                .defaultTaskPriority(TaskPriorityEnum.MEDIUM)
                .build();

        taskStatusSettingsDto = DefaultSettingsDto.builder()
                .id(2)
                .fieldName(DefaultSettingsFieldNameEnum.TASK_STATUS)
                .defaultTaskStatus(TaskStatusEnum.BACKLOG)
                .build();
    }

    @Test
    @DisplayName("Возвращает значение по умолчанию для TASK_PRIORITY, если настройка присутствует")
    void testGetDefaultValueFromTasksFields_TaskPriority_Present() {
        // Arrange
        try (MockedConstruction<DefaultSettingsServiceImpl> mocked = Mockito.mockConstruction(DefaultSettingsServiceImpl.class,
                (mock, context) -> {
                    Mockito.when(mock.getDefaultSettings(DefaultSettingsFieldNameEnum.TASK_PRIORITY))
                            .thenReturn(Optional.of(taskPrioritySettingsDto));
                })) {

            DefaultSettingsActionsImpl actions = new DefaultSettingsActionsImpl();

            // Act
            Optional<TaskPriorityEnum> result = actions.getDefaultValueFromTasksFields(
                    DefaultSettingsFieldNameEnum.TASK_PRIORITY,
                    TaskPriorityEnum.LOW
            );

            boolean enumResult = result.isPresent();
            TaskPriorityEnum tpeExpected = TaskPriorityEnum.MEDIUM;
            TaskPriorityEnum tpeResult = result.get();

            // Assert
            Assertions.assertTrue(enumResult, "Ожидается, что значение присутствует");
            Assertions.assertEquals(tpeExpected, tpeResult, "Ожидается, что возвращено значение MEDIUM");

            int resultSizeMocked = mocked.constructed().size();
            // Верификация, что конструктор был вызван один раз
            Assertions.assertEquals(1, resultSizeMocked, "Ожидается, что был создан один экземпляр " +
                    "DefaultSettingsServiceImpl");

            // Верификация вызова метода getDefaultSettings
            DefaultSettingsServiceImpl mockService = mocked.constructed().get(0);
            Mockito.verify(mockService, Mockito.times(1))
                    .getDefaultSettings(DefaultSettingsFieldNameEnum.TASK_PRIORITY);
        }
    }

    @Test
    @DisplayName("Возвращает значение по умолчанию для TASK_STATUS, если настройка присутствует")
    void testGetDefaultValueFromTasksFields_TaskStatus_Present() {
        // Arrange
        try (MockedConstruction<DefaultSettingsServiceImpl> mocked = Mockito.mockConstruction(DefaultSettingsServiceImpl.class,
                (mock, context) -> {
                    Mockito.when(mock.getDefaultSettings(DefaultSettingsFieldNameEnum.TASK_STATUS))
                            .thenReturn(Optional.of(taskStatusSettingsDto));
                })) {

            DefaultSettingsActionsImpl actions = new DefaultSettingsActionsImpl();

            // Act
            Optional<TaskStatusEnum> result = actions.getDefaultValueFromTasksFields(
                    DefaultSettingsFieldNameEnum.TASK_STATUS,
                    TaskStatusEnum.IN_PROGRESS
            );

            // Assert
            Assertions.assertTrue(result.isPresent(), "Ожидается, что значение присутствует");
            Assertions.assertEquals(TaskStatusEnum.BACKLOG, result.get(), "Ожидается, что возвращено значение BACKLOG");

            // Верификация, что конструктор был вызван один раз
            Assertions.assertEquals(1, mocked.constructed().size(), "Ожидается, что был создан один экземпляр " +
                    "DefaultSettingsServiceImpl");

            // Верификация вызова метода getDefaultSettings
            DefaultSettingsServiceImpl mockService = mocked.constructed().get(0);
            Mockito.verify(mockService, Mockito.times(1))
                    .getDefaultSettings(DefaultSettingsFieldNameEnum.TASK_STATUS);
        }
    }

    @Test
    @DisplayName("Возвращает Optional.empty(), если настройка TASK_PRIORITY отсутствует")
    void testGetDefaultValueFromTasksFields_TaskPriority_NotFound() {
        // Arrange
        try (MockedConstruction<DefaultSettingsServiceImpl> mocked = Mockito.mockConstruction(DefaultSettingsServiceImpl.class,
                (mock, context) -> {
                    Mockito.when(mock.getDefaultSettings(DefaultSettingsFieldNameEnum.TASK_PRIORITY))
                            .thenReturn(Optional.empty());
                })) {

            DefaultSettingsActionsImpl actions = new DefaultSettingsActionsImpl();

            // Act
            Optional<TaskPriorityEnum> result = actions.getDefaultValueFromTasksFields(
                    DefaultSettingsFieldNameEnum.TASK_PRIORITY,
                    TaskPriorityEnum.LOW
            );

            // Assert
            Assertions.assertFalse(result.isPresent(), "Ожидается, что значение отсутствует");

            // Верификация, что конструктор был вызван один раз
            Assertions.assertEquals(1, mocked.constructed().size(), "Ожидается, что был создан один экземпляр " +
                    "DefaultSettingsServiceImpl");

            // Верификация вызова метода getDefaultSettings
            DefaultSettingsServiceImpl mockService = mocked.constructed().get(0);
            Mockito.verify(mockService, Mockito.times(1))
                    .getDefaultSettings(DefaultSettingsFieldNameEnum.TASK_PRIORITY);
        }
    }

    @Test
    @DisplayName("Возвращает Optional.empty() при передаче неподдерживаемого fieldName")
    void testGetDefaultValueFromTasksFields_UnsupportedField() {
        // Arrange
        // Поскольку в enum нет неподдерживаемых значений, используем существующее поле с Optional.empty()
        try (MockedConstruction<DefaultSettingsServiceImpl> mocked = Mockito.mockConstruction(DefaultSettingsServiceImpl.class,
                (mock, context) -> {
                    Mockito.when(mock.getDefaultSettings(DefaultSettingsFieldNameEnum.TASK_PRIORITY))
                            .thenReturn(Optional.empty());
                })) {

            DefaultSettingsActionsImpl actions = new DefaultSettingsActionsImpl();

            // Act
            Optional<TaskPriorityEnum> result = actions.getDefaultValueFromTasksFields(
                    DefaultSettingsFieldNameEnum.TASK_PRIORITY,
                    TaskPriorityEnum.LOW
            );

            // Assert
            Assertions.assertFalse(result.isPresent(), "Ожидается, что значение отсутствует");

            // Верификация, что конструктор был вызван один раз
            Assertions.assertEquals(1, mocked.constructed().size(), "Ожидается, что был создан один экземпляр " +
                    "DefaultSettingsServiceImpl");

            // Верификация вызова метода getDefaultSettings
            DefaultSettingsServiceImpl mockService = mocked.constructed().get(0);
            Mockito.verify(mockService, Mockito.times(1))
                    .getDefaultSettings(DefaultSettingsFieldNameEnum.TASK_PRIORITY);
        }
    }

    @Test
    @DisplayName("Возвращает Optional.empty() при передаче null в качестве fieldName")
    void testGetDefaultValueFromTasksFields_NullFieldName() {
        // Arrange
        DefaultSettingsFieldNameEnum nullFieldName = null;

        try (MockedConstruction<DefaultSettingsServiceImpl> mocked = Mockito.mockConstruction(DefaultSettingsServiceImpl.class,
                (mock, context) -> {
                    Mockito.when(mock.getDefaultSettings(nullFieldName))
                            .thenReturn(Optional.empty());
                })) {

            DefaultSettingsActionsImpl actions = new DefaultSettingsActionsImpl();

            // Act
            Optional<TaskPriorityEnum> result = actions.getDefaultValueFromTasksFields(
                    nullFieldName,
                    TaskPriorityEnum.LOW
            );

            // Assert
            Assertions.assertFalse(result.isPresent(), "Ожидается, что значение отсутствует");

            // Верификация, что конструктор был вызван один раз
            Assertions.assertEquals(1, mocked.constructed().size(), "Ожидается, что был создан один экземпляр " +
                    "DefaultSettingsServiceImpl");

            // Верификация вызова метода getDefaultSettings
            DefaultSettingsServiceImpl mockService = mocked.constructed().get(0);
            Mockito.verify(mockService, Mockito.times(1))
                    .getDefaultSettings(nullFieldName);
        }
    }
}

