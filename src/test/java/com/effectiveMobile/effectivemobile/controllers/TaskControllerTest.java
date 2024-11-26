package com.effectiveMobile.effectivemobile.controllers;

import com.effectiveMobile.effectivemobile.constants.ConstantsClass;
import com.effectiveMobile.effectivemobile.dto.CustomUsersDto;
import com.effectiveMobile.effectivemobile.dto.RegistrationUsers;
import com.effectiveMobile.effectivemobile.dto.TasksDto;
import com.effectiveMobile.effectivemobile.other.TaskPriorityEnum;
import com.effectiveMobile.effectivemobile.other.TaskStatusEnum;
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
class TaskControllerTest {

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

    @DisplayName("Проверка эндпоинта редактирования задач PUT /task/update-tasks")
    @Test
    @Order(3)
    void testEditTask() throws Exception {

        TasksDto taskDto = new TasksDto();
        taskDto.setId(1);
        taskDto.setHeader("Заголовок изменен");
        taskDto.setDescription("Описание изменено");
        taskDto.setTaskPriority(TaskPriorityEnum.HIGH);
        taskDto.setTaskStatus(TaskStatusEnum.BACKLOG);

        mockMvc.perform(MockMvcRequestBuilders.put("/task/update-tasks")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskDto)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.header").value("Заголовок изменен"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Описание изменено"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.taskPriority").value("HIGH"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.taskStatus").value("BACKLOG"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.taskAuthor.id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.taskAuthor.email").value("example2@gmail.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.taskExecutor.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.taskExecutor.email").value("example@gmail.com"));
    }

    @DisplayName("Проверка эндпоинта удаления задач DELETE /task/delete/{id}")
    @Test
    @Order(4)
    void testDeleteTasks() throws Exception {
        CustomUsersDto customUsersDto = CustomUsersDto.builder()
                .email("example2@gmail.com")
                .build();
        TasksDto taskDto = TasksDto.builder()
                .header("Test Task For Delete")
                .description("Test For Delete")
                .taskExecutor(customUsersDto)
                .taskPriority(TaskPriorityEnum.MEDIUM)
                .taskStatus(TaskStatusEnum.BACKLOG)
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/task/create")
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskDto)));


        mockMvc.perform(MockMvcRequestBuilders.delete("/task/delete/{id}", 3)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(ConstantsClass.IS_DELETE));

        // Попытка удаления несуществующей задачи (id = 9999)
        mockMvc.perform(MockMvcRequestBuilders.delete("/task/delete/{id}", 9999)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }


    @DisplayName("Проверка эндпоинта создания задачи POST /task/create")
    @Test
    @Order(5)
    void testCreateTask() throws Exception {
        CustomUsersDto customUsersDto = CustomUsersDto.builder()
                .email("example2@gmail.com")
                .build();
        TasksDto taskDto = TasksDto.builder()
                .header("Test Task")
                .description("Test Description")
                .taskExecutor(customUsersDto)
                .taskPriority(TaskPriorityEnum.MEDIUM)
                .taskStatus(TaskStatusEnum.BACKLOG)
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/task/create")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists()) // Проверка наличия поля id
                .andExpect(MockMvcResultMatchers.jsonPath("$.header").value("Test Task"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Test Description"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.taskAuthor.id").exists()) // Проверка наличия поля id у taskAuthor
                .andExpect(MockMvcResultMatchers.jsonPath("$.taskAuthor.email").value("example2@gmail.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.taskExecutor.id").exists()) // Проверка наличия поля id у taskExecutor
                .andExpect(MockMvcResultMatchers.jsonPath("$.taskExecutor.email").value("example2@gmail.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.taskPriority").value("MEDIUM"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.taskStatus").value("BACKLOG"));
    }

    @DisplayName("Проверка эндпоинта получения задач и комментариев к ним по автору GET /task/gen-info/author/{author}")
    @Test
    @Order(2)
    void testGetTaskAuthor() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/task/gen-info/author/example2@gmail.com")
                        .header("Authorization", "Bearer " + jwtToken)
                        .param("offset", "0")
                        .param("limit", "10"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))  // проверка ID первой задачи
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].header").value("Test task header"))  // проверка заголовка первой задачи
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].taskAuthor.id").value(2))  // проверка ID автора первой задачи
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].taskAuthor.email").value("example2@gmail.com"))  // проверка email автора
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].taskExecutor.id").value(1))  // проверка ID исполнителя первой задачи
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].taskExecutor.email").value("example@gmail.com"))  // проверка email исполнителя
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description").value("This is a test task description"))  // проверка описания задачи
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].taskPriority").value("MEDIUM"))  // проверка приоритета задачи
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].taskStatus").value("BACKLOG"))  // проверка статуса задачи
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].notesDto[0].id").value(1))  // проверка ID первой заметки
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].notesDto[0].notesAuthor.id").value(2))  // проверка ID автора заметки
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].notesDto[0].notesAuthor.email").value("example2@gmail.com"))  // проверка email автора заметки
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].notesDto[0].comments").value("This is a test comment for Task 1"))  // проверка комментария в первой заметке

                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2))  // проверка ID второй задачи
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].header").value("Test Task 2"))  // проверка заголовка второй задачи
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].taskAuthor.id").value(2))  // проверка ID автора второй задачи
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].taskAuthor.email").value("example2@gmail.com"))  // проверка email автора второй задачи
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].taskExecutor.id").value(2))  // проверка ID исполнителя второй задачи
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].taskExecutor.email").value("example2@gmail.com"))  // проверка email исполнителя второй задачи
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].description").value("Another test task description"))  // проверка описания второй задачи
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].taskPriority").value("HIGH"))  // проверка приоритета второй задачи
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].taskStatus").value("IN_PROGRESS"))  // проверка статуса второй задачи
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].notesDto[0].id").value(2))  // проверка ID второй заметки
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].notesDto[0].notesAuthor.id").value(1))  // проверка ID автора второй заметки
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].notesDto[0].notesAuthor.email").value("example@gmail.com"))  // проверка email автора второй заметки
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].notesDto[0].comments").value("Another comment for Task 2"));  // проверка комментария во второй заметке

    }

    @DisplayName("Проверка эндпоинта получения задач и комментариев к ним по исполнителю GET /task/gen-info/executor/{executorEmail}")
    @Test
    @Order(1)
    void testGetTaskExecutor() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/task/gen-info/executor/example@gmail.com")
                        .header("Authorization", "Bearer " + jwtToken)
                        .param("offset", "0")
                        .param("limit", "10"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].header").value("Test task header"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].taskAuthor.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].taskAuthor.email").value("example2@gmail.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].taskExecutor.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].taskExecutor.email").value("example@gmail.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description").value("This is a test task description")) //
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].taskPriority").value("MEDIUM"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].taskStatus").value("BACKLOG"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].notesDto[0].id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].notesDto[0].notesAuthor.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].notesDto[0].notesAuthor.email").value("example2@gmail.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].notesDto[0].comments").value(
                        "This is a test comment for Task 1"));
    }
}