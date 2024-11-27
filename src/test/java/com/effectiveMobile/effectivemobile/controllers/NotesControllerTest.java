package com.effectiveMobile.effectivemobile.controllers;

import com.effectiveMobile.effectivemobile.dto.NotesDto;
import com.effectiveMobile.effectivemobile.dto.RegistrationUsers;
import com.effectiveMobile.effectivemobile.dto.TasksDto;
import com.effectiveMobile.effectivemobile.services.JwtService;
import com.effectiveMobile.effectivemobile.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class NotesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    private String jwtToken;

    @BeforeEach
    void setUp() throws Exception {
        // Авторизация и получение JWT-токена
        RegistrationUsers registrationUsers = new RegistrationUsers("example2@gmail.com", "12345");
        jwtToken = userService.authorizationUser(registrationUsers);
    }

    @DisplayName("Проверка эндпоинта создания комментария POST /notes/create")
    @Test
    @Order(1)
    void testCreateNotes() throws Exception {
        // Создаем комментарий для существующей задачи
        NotesDto notesDto = new NotesDto();
        notesDto.setComments("Тестовый комментарий");
        notesDto.setTask(new TasksDto(1, null, null, null, null,
                null, null, null)); // ID задачи, к которой привязываем комментарий

        mockMvc.perform(MockMvcRequestBuilders.post("/notes/create")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(notesDto)))
                .andExpect(MockMvcResultMatchers.status().isOk());
               /* .andExpect(MockMvcResultMatchers.jsonPath("$.comments").value("Тестовый комментарий")) // Проверка комментария
                .andExpect(MockMvcResultMatchers.jsonPath("$.task.id").value(1)) // Проверка ID задачи
                .andExpect(MockMvcResultMatchers.jsonPath("$.notesAuthor.id").value(2)) // Проверка ID пользователя (автора комментария)
                .andExpect(MockMvcResultMatchers.jsonPath("$.notesAuthor.email").value("example2@gmail.com")); // Проверка email пользователя
   */
    }

    @DisplayName("Проверка эндпоинта создания комментария без ID задачи POST /notes/create")
    @Test
    @Order(2)
    void testCreateNotesWithoutTaskId() throws Exception {

        // Создаем комментарий без указания ID задачи
        NotesDto notesDto = new NotesDto();
        notesDto.setComments("Комментарий без ID задачи");

        mockMvc.perform(MockMvcRequestBuilders.post("/notes/create")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(notesDto)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @DisplayName("Проверка эндпоинта создания комментария с несуществующей задачей POST /notes/create")
    @Test
    @Order(3)
    void testCreateNotesWithNonExistentTask() throws Exception {

        // Создаем комментарий для несуществующей задачи (например, с ID 9999)
        NotesDto notesDto = new NotesDto();
        notesDto.setComments("Комментарий для несуществующей задачи");
        notesDto.setTask(new TasksDto(9999, null, null, null, null,
                null, null, null)); // Несуществующая задача

        mockMvc.perform(MockMvcRequestBuilders.post("/notes/create")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(notesDto)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @DisplayName("Проверка эндпоинта создания комментария без авторизации POST /notes/create")
    @Test
    @Order(4)
    void testCreateNotesWithoutAuthorization() throws Exception {

        // Создаем комментарий без авторизации
        NotesDto notesDto = new NotesDto();
        notesDto.setComments("Комментарий без авторизации");
        notesDto.setTask(new TasksDto(1, null, null, null, null,
                null, null, null)); // ID существующей задачи

        mockMvc.perform(MockMvcRequestBuilders.post("/notes/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(notesDto)))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }
}
