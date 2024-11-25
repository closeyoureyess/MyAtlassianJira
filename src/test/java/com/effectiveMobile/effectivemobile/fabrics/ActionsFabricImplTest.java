package com.effectiveMobile.effectivemobile.fabrics;

import com.effectiveMobile.effectivemobile.auxiliaryclasses.DefaultSettingsActions;
import com.effectiveMobile.effectivemobile.auxiliaryclasses.TasksActions;
import com.effectiveMobile.effectivemobile.auxiliaryclasses.UserActions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

class ActionsFabricImplTest {

    private ActionsFabricImpl actionsFabric;
    private TasksActions tasksActions;
    private UserActions userActions;
    private DefaultSettingsActions defaultSettingsActions;

    @BeforeEach
    void setUp() {
        tasksActions = mock(TasksActions.class);
        userActions = mock(UserActions.class);
        defaultSettingsActions = mock(DefaultSettingsActions.class);

        actionsFabric = new ActionsFabricImpl(tasksActions, userActions, defaultSettingsActions);
    }

    @Test
    @DisplayName("Тест: Проверка создания TasksActions")
    void testCreateTasksActions() {
        assertNotNull(actionsFabric.createTasksActions(), "TasksActions не должен быть null");
    }

    @Test
    @DisplayName("Тест: Проверка создания UserActions")
    void testCreateUserActions() {
        assertNotNull(actionsFabric.createUserActions(), "UserActions не должен быть null");
    }

    @Test
    @DisplayName("Тест: Проверка создания DefaultSettingsActions")
    void testCreateDefaultSettingsActions() {
        assertNotNull(actionsFabric.createDefaultSettingsActions(), "DefaultSettingsActions не должен быть null");
    }
}
