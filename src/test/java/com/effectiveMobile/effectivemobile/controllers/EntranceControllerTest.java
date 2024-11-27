package com.effectiveMobile.effectivemobile.controllers;

import com.effectiveMobile.effectivemobile.dto.CustomUsersDto;
import com.effectiveMobile.effectivemobile.dto.RegistrationUsers;
import com.effectiveMobile.effectivemobile.other.UserRoles;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest
@AutoConfigureMockMvc
class EntranceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("Проверка эндпоинта регистрации пользователя POST /entrance/registration")
    @Test
    @Order(5)
    void testCreateUser() throws Exception {
        CustomUsersDto customUsersDto = new CustomUsersDto();
        customUsersDto.setEmail("newuser@example.com");
        customUsersDto.setPasswordKey("password123"); // Добавляем пароль
        customUsersDto.setRole(UserRoles.ADMIN);

        mockMvc.perform(MockMvcRequestBuilders.post("/entrance/registration")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(customUsersDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("newuser@example.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.passwordKey").doesNotExist()) // Пароль не должен возвращаться в ответе
                .andExpect(MockMvcResultMatchers.jsonPath("$.role").value("ADMIN"));
    }

    @DisplayName("Проверка эндпоинта регистрации пользователя POST /entrance/registration, если не заполнен емейл")
    @Test
    @Order(4)
    void testCreateUser_ifEmailIsNull() throws Exception {
        CustomUsersDto customUsersDto = new CustomUsersDto();
        customUsersDto.setPasswordKey("password123"); // Добавляем пароль
        customUsersDto.setRole(UserRoles.ADMIN);

        mockMvc.perform(MockMvcRequestBuilders.post("/entrance/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customUsersDto)))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError());
    }


    @DisplayName("Проверка эндпоинта авторизации пользователя POST /entrance/authorization")
    @Test
    @Order(3)
    void testAuthorizationUser() throws Exception {
        RegistrationUsers registrationUsers = new RegistrationUsers();
        registrationUsers.setEmail("example2@gmail.com");
        registrationUsers.setPasswordKey("12345");

        mockMvc.perform(MockMvcRequestBuilders.post("/entrance/authorization")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(registrationUsers)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(notNullValue()));
    }

    @DisplayName("Проверка эндпоинта авторизации пользователя POST /entrance/authorization, если пароль неверный")
    @Test
    @Order(2)
    void testAuthorizationUser_ifWrongPassword() throws Exception {
        // Подготовка данных для успешной авторизации
        RegistrationUsers registrationUsers = new RegistrationUsers();
        registrationUsers.setEmail("example2@gmail.com");
        registrationUsers.setPasswordKey("wrongpassword");

        mockMvc.perform(MockMvcRequestBuilders.post("/entrance/authorization")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(registrationUsers)))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @DisplayName("Проверка эндпоинта авторизации пользователя POST /entrance/authorization, если пользователя не существует")
    @Test
    @Order(1)
    void testAuthorizationUser_ifUserNotExists() throws Exception {
        RegistrationUsers registrationUsers = new RegistrationUsers();
        registrationUsers.setEmail("nonexistinguser@example.com");
        registrationUsers.setPasswordKey("correctpassword");

        // Тест авторизации несуществующего пользователя
        mockMvc.perform(MockMvcRequestBuilders.post("/entrance/authorization")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(registrationUsers)))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }
}