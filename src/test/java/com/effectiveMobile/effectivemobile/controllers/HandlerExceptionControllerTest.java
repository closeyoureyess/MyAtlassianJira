package com.effectiveMobile.effectivemobile.controllers;

import com.effectiveMobile.effectivemobile.dto.CustomUsersDto;
import com.effectiveMobile.effectivemobile.dto.RegistrationUsers;
import com.effectiveMobile.effectivemobile.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;

@SpringBootTest
@AutoConfigureMockMvc
class HandlerExceptionControllerTest {

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

    /**
     * Тесты для ExecutorNotFoundExeption.
     */
    @Nested
    @DisplayName("ExecutorNotFoundExeption")
    class ExecutorNotFoundExeptionTests {

        @Test
        @DisplayName("Должен возвращать 404 Not Found и сообщение об ошибке")
        void whenExecutorNotFound_thenNotFound() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.get("/test-exceptions/executor-not-found")
                            .header("Authorization", "Bearer " + jwtToken))
                    .andExpect(MockMvcResultMatchers.status().isNotFound())
                    .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(404))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("Not Found"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Executor not found"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.path").value("/test-exceptions/executor-not-found"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.violations").doesNotExist());
        }
    }

    /**
     * Тесты для BadCredentialsException.
     */
    @Nested
    @DisplayName("BadCredentialsException")
    class BadCredentialsExceptionTests {

        @Test
        @DisplayName("Должен возвращать 401 Unauthorized и сообщение об ошибке")
        void whenBadCredentials_thenUnauthorized() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.get("/test-exceptions/bad-credentials")
                            .header("Authorization", "Bearer " + jwtToken))
                    .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                    .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(401))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("Unauthorized"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Bad credentials"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.path").value("/test-exceptions/bad-credentials"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.violations").doesNotExist());
        }
    }

    /**
     * Тесты для NotEnoughRulesForEntity.
     */
    @Nested
    @DisplayName("NotEnoughRulesForEntity")
    class NotEnoughRulesForEntityTests {

        @Test
        @DisplayName("Должен возвращать 403 Forbidden и сообщение об ошибке")
        void whenNotEnoughRulesForEntity_thenForbidden() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.get("/test-exceptions/not-enough-rules")
                            .header("Authorization", "Bearer " + jwtToken))
                    .andExpect(MockMvcResultMatchers.status().isForbidden())
                    .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(403))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("Forbidden"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Not enough rules for entity"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.path").value("/test-exceptions/not-enough-rules"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.violations").doesNotExist());
        }
    }

    /**
     * Тесты для EntityNotFoundExeption.
     */
    @Nested
    @DisplayName("EntityNotFoundExeption")
    class EntityNotFoundExeptionTests {

        @Test
        @DisplayName("Должен возвращать 404 Not Found и сообщение об ошибке")
        void whenEntityNotFound_thenNotFound() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.get("/test-exceptions/entity-not-found")
                            .header("Authorization", "Bearer " + jwtToken))
                    .andExpect(MockMvcResultMatchers.status().isNotFound())
                    .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(404))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("Not Found"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Entity not found"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.path").value("/test-exceptions/entity-not-found"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.violations").doesNotExist());
        }
    }

    /**
     * Тесты для IOException.
     */
    @Nested
    @DisplayName("IOException")
    class IOExceptionTests {

        @Test
        @DisplayName("Должен возвращать 400 Bad Request и сообщение об ошибке")
        void whenIOException_thenBadRequest() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.get("/test-exceptions/io-exception")
                            .header("Authorization", "Bearer " + jwtToken))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("Bad Request"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("IO error occurred"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.path").value("/test-exceptions/io-exception"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.violations").doesNotExist());
        }
    }

    /**
     * Тесты для ServletException.
     */
    @Nested
    @DisplayName("ServletException")
    class ServletExceptionTests {

        @Test
        @DisplayName("Должен возвращать 400 Bad Request и сообщение об ошибке")
        void whenServletException_thenBadRequest() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.get("/test-exceptions/servlet-exception")
                            .header("Authorization", "Bearer " + jwtToken))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("Bad Request"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Servlet error occurred"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.path").value("/test-exceptions/servlet-exception"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.violations").doesNotExist());
        }
    }

    /**
     * Тесты для ConstraintViolationException.
     */
    @Nested
    @DisplayName("ConstraintViolationException")
    class ConstraintViolationExceptionTests {

        @Test
        @DisplayName("Должен возвращать 400 Bad Request и список нарушений")
        void whenConstraintViolation_thenBadRequest() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.get("/test-exceptions/constraint-violation")
                            .header("Authorization", "Bearer " + jwtToken))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("Bad Request"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Constraint violation"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.path").value("/test-exceptions/constraint-violation"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.violations").isArray())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.violations", hasSize(greaterThanOrEqualTo(0))));
        }
    }

    /**
     * Тесты для UserNotFoundException.
     */
    @Nested
    @DisplayName("UserNotFoundException")
    class UserNotFoundExceptionTests {

        @Test
        @DisplayName("Должен возвращать 404 Not Found и сообщение об ошибке")
        void whenUserNotFound_thenNotFound() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.get("/test-exceptions/user-not-found")
                            .header("Authorization", "Bearer " + jwtToken))
                    .andExpect(MockMvcResultMatchers.status().isNotFound())
                    .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(404))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("Not Found"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("User not found"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.path").value("/test-exceptions/user-not-found"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.violations").doesNotExist());
        }
    }

    /**
     * Тесты для AccessDeniedException.
     */
    @Nested
    @DisplayName("AccessDeniedException")
    class AccessDeniedExceptionTests {

        @Test
        @DisplayName("Должен возвращать 403 Forbidden и сообщение об ошибке")
        void whenAccessDenied_thenForbidden() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.get("/test-exceptions/access-denied")
                            .header("Authorization", "Bearer " + jwtToken))
                    .andExpect(MockMvcResultMatchers.status().isForbidden())
                    .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(403))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("Forbidden"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Access denied"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.path").value("/test-exceptions/access-denied"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.violations").doesNotExist());
        }
    }

    /**
     * Тесты для HttpMessageNotReadableException.
     */
    @Nested
    @DisplayName("HttpMessageNotReadableException")
    class HttpMessageNotReadableExceptionTests {

        @Test
        @DisplayName("Должен возвращать 400 Bad Request и сообщение об ошибке")
        void whenHttpMessageNotReadable_thenBadRequest() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.get("/test-exceptions/http-message-not-readable")
                            .header("Authorization", "Bearer " + jwtToken))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("Bad Request"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Message not readable"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.path").value("/test-exceptions/http-message-not-readable"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.violations").doesNotExist());
        }
    }

    /**
     * Тесты для EntityNotBeNull.
     */
    @Nested
    @DisplayName("EntityNotBeNull")
    class EntityNotBeNullTests {

        @Test
        @DisplayName("Должен возвращать 400 Bad Request и сообщение об ошибке")
        void whenEntityNotBeNull_thenBadRequest() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.get("/test-exceptions/entity-not-be-null")
                            .header("Authorization", "Bearer " + jwtToken))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("Bad Request"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Entity must not be null"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.path").value("/test-exceptions/entity-not-be-null"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.violations").doesNotExist());
        }
    }

    /**
     * Тесты для FieldNotBeNull.
     */
    @Nested
    @DisplayName("FieldNotBeNull")
    class FieldNotBeNullTests {

        @Test
        @DisplayName("Должен возвращать 400 Bad Request и сообщение об ошибке")
        void whenFieldNotBeNull_thenBadRequest() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.get("/test-exceptions/field-not-be-null")
                            .header("Authorization", "Bearer " + jwtToken))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("Bad Request"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Field must not be null"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.path").value("/test-exceptions/field-not-be-null"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.violations").doesNotExist());
        }
    }

    /**
     * Тесты для RoleNotFoundException.
     */
    @Nested
    @DisplayName("RoleNotFoundException")
    class RoleNotFoundExceptionTests {

        @Test
        @DisplayName("Должен возвращать 404 Not Found и сообщение об ошибке")
        void whenRoleNotFound_thenNotFound() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.get("/test-exceptions/role-not-found")
                            .header("Authorization", "Bearer " + jwtToken))
                    .andExpect(MockMvcResultMatchers.status().isNotFound())
                    .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(404))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("Not Found"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Role not found"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.path").value("/test-exceptions/role-not-found"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.violations").doesNotExist());
        }
    }

    /**
     * Тесты для IllegalStateException.
     */
    @Nested
    @DisplayName("IllegalStateException")
    class IllegalStateExceptionTests {

        @Test
        @DisplayName("Должен возвращать 500 Internal Server Error и сообщение об ошибке")
        void whenIllegalStateException_thenInternalServerError() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.get("/test-exceptions/illegal-state")
                            .header("Authorization", "Bearer " + jwtToken))
                    .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                    .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(500))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("Internal Server Error"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Illegal state"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.path").value("/test-exceptions/illegal-state"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.violations").doesNotExist());
        }
    }

    /**
     * Тесты для SQLGrammarException.
     */
    @Nested
    @DisplayName("SQLGrammarException")
    class SQLGrammarExceptionTests {

        @Test
        @DisplayName("Должен возвращать 500 Internal Server Error и сообщение об ошибке")
        void whenSQLGrammarException_thenInternalServerError() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.get("/test-exceptions/sql-grammar")
                            .header("Authorization", "Bearer " + jwtToken))
                    .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                    .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(500))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("Internal Server Error"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Syntax error [SQL error]"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.path").value("/test-exceptions/sql-grammar"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.violations").doesNotExist());
        }
    }

    /**
     * Тесты для DataIntegrityViolationException.
     */
    @Nested
    @DisplayName("DataIntegrityViolationException")
    class DataIntegrityViolationExceptionTests {

        @Test
        @DisplayName("Должен возвращать 500 Internal Server Error и сообщение об ошибке")
        void whenDataIntegrityViolationException_thenInternalServerError() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.get("/test-exceptions/data-integrity-violation")
                            .header("Authorization", "Bearer " + jwtToken))
                    .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                    .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(500))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("Internal Server Error"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Data integrity violation"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.path").value("/test-exceptions/data-integrity-violation"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.violations").doesNotExist());
        }
    }

    /**
     * Тесты для IncorrectTypeParameterException.
     */
    @Nested
    @DisplayName("IncorrectTypeParameterException")
    class IncorrectTypeParameterExceptionTests {

        @Test
        @DisplayName("Должен возвращать 500 Internal Server Error и сообщение об ошибке")
        void whenIncorrectTypeParameterException_thenInternalServerError() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.get("/test-exceptions/incorrect-type-parameter")
                            .header("Authorization", "Bearer " + jwtToken))
                    .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                    .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(500))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("Internal Server Error"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Incorrect type parameter"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.path").value("/test-exceptions/incorrect-type-parameter"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.violations").doesNotExist());
        }
    }

    /**
     * Тесты для RuntimeException.
     */
    @Nested
    @DisplayName("RuntimeException")
    class RuntimeExceptionTests {

        @Test
        @DisplayName("Должен возвращать 500 Internal Server Error и сообщение об ошибке")
        void whenRuntimeException_thenInternalServerError() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.get("/test-exceptions/runtime-exception")
                            .header("Authorization", "Bearer " + jwtToken))
                    .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                    .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(500))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("Internal Server Error"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Runtime exception occurred"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.path").value("/test-exceptions/runtime-exception"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.violations").doesNotExist());
        }

        @Test
        @DisplayName("Должен возвращать 500 Internal Server Error и сообщение об ошибке для общего исключения")
        void whenGeneralException_thenInternalServerError() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.get("/test-exceptions/general-exception")
                            .header("Authorization", "Bearer " + jwtToken))
                    .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                    .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(500))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("Internal Server Error"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("General exception occurred"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.path").value("/test-exceptions/general-exception"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.violations").doesNotExist());
        }
    }

}
