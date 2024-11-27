package com.effectiveMobile.effectivemobile.controllers;

import com.effectiveMobile.effectivemobile.dto.DefaultSettingsDto;
import com.effectiveMobile.effectivemobile.dto.RegistrationUsers;
import com.effectiveMobile.effectivemobile.other.DefaultSettingsFieldNameEnum;
import com.effectiveMobile.effectivemobile.other.TaskPriorityEnum;
import com.effectiveMobile.effectivemobile.other.TaskStatusEnum;
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

@SpringBootTest
@AutoConfigureMockMvc
class DefaultSettingsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserService userService;

    private String jwtToken;

    @BeforeEach
    void setUp() throws Exception {
        // Авторизация и получение JWT-токена
        RegistrationUsers registrationUsers = new RegistrationUsers("example2@gmail.com", "12345");
        jwtToken = userService.authorizationUser(registrationUsers);
    }

    @DisplayName("Проверка эндпоинта смены настроек по умолчанию PUT /defaultsettings/update-settings")
    @Test
    @Order(4)
    void testChangeDefaultSettings() throws Exception {
        // Подготовка данных для успешной смены настроек
        DefaultSettingsDto defaultSettingsDto = new DefaultSettingsDto();
        defaultSettingsDto.setFieldName(DefaultSettingsFieldNameEnum.TASK_PRIORITY);
        defaultSettingsDto.setDefaultTaskPriority(TaskPriorityEnum.HIGH);

        // Тест успешной смены настроек
        mockMvc.perform(MockMvcRequestBuilders.put("/defaultsettings/update-settings")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(defaultSettingsDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.fieldName").value("TASK_PRIORITY"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.defaultTaskPriority").value("HIGH"));
    }

    @DisplayName("Проверка эндпоинта смены настроек по умолчанию PUT /defaultsettings/update-settings")
    @Test
    @Order(3)
    void testChangeDefaultSettings2() throws Exception {
        // Подготовка данных для успешной смены настроек
        DefaultSettingsDto defaultSettingsDto = new DefaultSettingsDto();
        defaultSettingsDto.setFieldName(DefaultSettingsFieldNameEnum.TASK_STATUS);
        defaultSettingsDto.setDefaultTaskStatus(TaskStatusEnum.BACKLOG);

        // Тест успешной смены настроек
        mockMvc.perform(MockMvcRequestBuilders.put("/defaultsettings/update-settings")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(defaultSettingsDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.fieldName").value("TASK_STATUS"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.defaultTaskStatus").value("BACKLOG"));
    }

    @DisplayName("Проверка эндпоинта смены настроек по умолчанию PUT /defaultsettings/update-settings, если fieldName null")
    @Test
    @Order(2)
    void testChangeDefaultSettings3() throws Exception {
        DefaultSettingsDto defaultSettingsDto = new DefaultSettingsDto();
        defaultSettingsDto.setDefaultTaskStatus(TaskStatusEnum.IN_PROGRESS);

        // Тест смены настроек для несуществующего поля
        mockMvc.perform(MockMvcRequestBuilders.put("/defaultsettings/update-settings")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(defaultSettingsDto)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @DisplayName("Проверка эндпоинта смены настроек по умолчанию PUT /defaultsettings/update-settings, если поля заполнены не в соответветствии" +
            "с тем, какие необходимо настроить")
    @Test
    @Order(1)
    void testChangeDefaultSettings4() throws Exception {
        DefaultSettingsDto defaultSettingsDto = new DefaultSettingsDto();
        defaultSettingsDto.setFieldName(DefaultSettingsFieldNameEnum.TASK_PRIORITY);
        defaultSettingsDto.setDefaultTaskStatus(TaskStatusEnum.IN_PROGRESS);

        // Тест смены настроек для несуществующего поля
        mockMvc.perform(MockMvcRequestBuilders.put("/defaultsettings/update-settings")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(defaultSettingsDto)))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError());
    }
}
