package com.effectiveMobile.effectivemobile.controllers;

import com.effectiveMobile.effectivemobile.dto.CustomUsersDto;
import com.effectiveMobile.effectivemobile.dto.NotesDto;
import com.effectiveMobile.effectivemobile.dto.RegistrationUsers;
import com.effectiveMobile.effectivemobile.dto.TasksDto;
import com.effectiveMobile.effectivemobile.other.UserRoles;
import com.effectiveMobile.effectivemobile.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * Тесты для контроллера EntranceController.
 */
@SpringBootTest
@AutoConfigureMockMvc
class UniversalResponseBodyAdviceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserService userService;

    private String jwtToken;

    /**
     * Регистрация и авторизация пользователя перед каждым тестом.
     */
    @BeforeEach
    public void setup() throws Exception {
        RegistrationUsers registrationUsers = new RegistrationUsers("example2@gmail.com", "12345");
        jwtToken = userService.authorizationUser(registrationUsers);
    }

    @DisplayName("Проверка эндпоинта регистрации пользователя POST /entrance/registration")
    @Test
    @Order(1)
    void testCreateUser() throws Exception {
        CustomUsersDto customUsersDto = new CustomUsersDto();
        customUsersDto.setEmail("newuser@example.com");
        customUsersDto.setPasswordKey("password123"); // Используйте правильное поле
        customUsersDto.setRole(UserRoles.ADMIN);

        mockMvc.perform(MockMvcRequestBuilders.post("/entrance/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customUsersDto)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("newuser@example.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.passwordKey").doesNotExist()) // Убедитесь, что пароль не возвращается
                .andExpect(MockMvcResultMatchers.jsonPath("$.role").value("ADMIN"));
    }

    @Test
    void testPostCreateNotesFilter() throws Exception {
        NotesDto notesDto = new NotesDto();
        notesDto.setComments("Тестовый комментарий");
        notesDto.setTask(new TasksDto(1, null, null, null, null,
                null, null, null)); // ID задачи, к которой привязываем комментарий

        mockMvc.perform(MockMvcRequestBuilders.post("/notes/create")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(notesDto)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.notesAuthor.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.notesAuthor.email").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.comments").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.task.id").exists())
                // Ensure other fields are not present
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").doesNotExist());
    }

    @Test
    void testPostCreateUserFilter() throws Exception {
        CustomUsersDto customUsersDto = new CustomUsersDto();
        customUsersDto.setEmail("example2@gmail.com");
        customUsersDto.setPasswordKey("12345"); // Добавляем пароль
        customUsersDto.setRole(UserRoles.ADMIN);

        mockMvc.perform(MockMvcRequestBuilders.post("/entrance/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customUsersDto)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.role").exists())
                // Ensure other fields are not present
                .andExpect(MockMvcResultMatchers.jsonPath("$.passwordKey").doesNotExist());
    }
}