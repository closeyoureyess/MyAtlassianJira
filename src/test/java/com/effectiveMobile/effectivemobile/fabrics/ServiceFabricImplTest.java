package com.effectiveMobile.effectivemobile.fabrics;

import com.effectiveMobile.effectivemobile.services.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

class ServiceFabricImplTest {

    private ServiceFabricImpl serviceFabric;
    private JwtService jwtService;
    private TaskService taskService;
    private MyUserDetailService myUserDetailService;
    private NotesService notesService;
    private DefaultSettingsService defaultSettingsService;

    @BeforeEach
    void setUp() {
        jwtService = mock(JwtService.class);
        taskService = mock(TaskService.class);
        myUserDetailService = mock(MyUserDetailService.class);
        notesService = mock(NotesService.class);
        defaultSettingsService = mock(DefaultSettingsService.class);

        serviceFabric = new ServiceFabricImpl(jwtService, taskService, myUserDetailService, notesService, defaultSettingsService);
    }

    @Test
    @DisplayName("Тест: Проверка создания JwtService")
    void testCreateJwtService() {
        assertNotNull(serviceFabric.createJwtService(), "JwtService не должен быть null");
    }

    @Test
    @DisplayName("Тест: Проверка создания TaskService")
    void testCreateTaskService() {
        assertNotNull(serviceFabric.createTaskService(), "TaskService не должен быть null");
    }

    @Test
    @DisplayName("Тест: Проверка создания UserDetailsService")
    void testCreateUserDetailsService() {
        assertNotNull(serviceFabric.createUserDetailsService(), "UserDetailsService не должен быть null");
    }

    @Test
    @DisplayName("Тест: Проверка создания NotesService")
    void testCreateNotesService() {
        assertNotNull(serviceFabric.createNotesService(), "NotesService не должен быть null");
    }

    @Test
    @DisplayName("Тест: Проверка создания DefaultSettingsService")
    void testCreateDefaultSettingsService() {
        assertNotNull(serviceFabric.createDefaultSettingsService(), "DefaultSettingsService не должен быть null");
    }
}
