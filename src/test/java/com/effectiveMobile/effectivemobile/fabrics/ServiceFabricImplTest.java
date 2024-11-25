package com.effectiveMobile.effectivemobile.fabrics;

import com.effectiveMobile.effectivemobile.services.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetailsService;

class ServiceFabricImplTest {

    private ServiceFabricImpl serviceFabric;
    private JwtService jwtService;
    private TaskService taskService;
    private MyUserDetailService myUserDetailService;
    private NotesService notesService;
    private DefaultSettingsService defaultSettingsService;

    @BeforeEach
    void setUp() {
        jwtService = Mockito.mock(JwtService.class);
        taskService = Mockito.mock(TaskService.class);
        myUserDetailService = Mockito.mock(MyUserDetailService.class);
        notesService = Mockito.mock(NotesService.class);
        defaultSettingsService = Mockito.mock(DefaultSettingsService.class);

        serviceFabric = new ServiceFabricImpl(jwtService, taskService, myUserDetailService, notesService, defaultSettingsService);
    }

    @Test
    @DisplayName("Тест: Проверка создания JwtService")
    void testCreateJwtService() {
        JwtService result = serviceFabric.createJwtService();
        Assertions.assertNotNull(result, "JwtService не должен быть null");
    }

    @Test
    @DisplayName("Тест: Проверка создания TaskService")
    void testCreateTaskService() {
        TaskService result = serviceFabric.createTaskService();
        Assertions.assertNotNull(result, "TaskService не должен быть null");
    }

    @Test
    @DisplayName("Тест: Проверка создания UserDetailsService")
    void testCreateUserDetailsService() {
        UserDetailsService result = serviceFabric.createUserDetailsService();
        Assertions.assertNotNull(result, "UserDetailsService не должен быть null");
    }

    @Test
    @DisplayName("Тест: Проверка создания NotesService")
    void testCreateNotesService() {
        NotesService result = serviceFabric.createNotesService();
        Assertions.assertNotNull(result, "NotesService не должен быть null");
    }

    @Test
    @DisplayName("Тест: Проверка создания DefaultSettingsService")
    void testCreateDefaultSettingsService() {
        DefaultSettingsService result = serviceFabric.createDefaultSettingsService();
        Assertions.assertNotNull(result, "DefaultSettingsService не должен быть null");
    }
}
