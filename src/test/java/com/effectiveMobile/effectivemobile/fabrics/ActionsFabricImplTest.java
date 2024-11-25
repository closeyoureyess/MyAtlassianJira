package com.effectiveMobile.effectivemobile.fabrics;

import com.effectiveMobile.effectivemobile.auxiliaryclasses.DefaultSettingsActions;
import com.effectiveMobile.effectivemobile.auxiliaryclasses.TasksActions;
import com.effectiveMobile.effectivemobile.auxiliaryclasses.UserActions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class ActionsFabricImplTest {

    private ActionsFabricImpl actionsFabric;
    private TasksActions tasksActions;
    private UserActions userActions;
    private DefaultSettingsActions defaultSettingsActions;

    @BeforeEach
    void setUp() {
        tasksActions = Mockito.mock(TasksActions.class);
        userActions = Mockito.mock(UserActions.class);
        defaultSettingsActions = Mockito.mock(DefaultSettingsActions.class);

        actionsFabric = new ActionsFabricImpl(tasksActions, userActions, defaultSettingsActions);
    }

    @Test
    @DisplayName("Тест: Проверка создания TasksActions")
    void testCreateTasksActions() {
        TasksActions result = actionsFabric.createTasksActions();
        Assertions.assertNotNull(result, "TasksActions не должен быть null");
    }

    @Test
    @DisplayName("Тест: Проверка создания UserActions")
    void testCreateUserActions() {
        UserActions result = actionsFabric.createUserActions();
        Assertions.assertNotNull(result, "UserActions не должен быть null");
    }

    @Test
    @DisplayName("Тест: Проверка создания DefaultSettingsActions")
    void testCreateDefaultSettingsActions() {
        DefaultSettingsActions result = actionsFabric.createDefaultSettingsActions();
        Assertions.assertNotNull(result, "DefaultSettingsActions не должен быть null");
    }
}
