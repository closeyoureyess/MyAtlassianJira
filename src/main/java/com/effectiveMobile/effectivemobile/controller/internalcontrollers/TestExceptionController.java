package com.effectiveMobile.effectivemobile.controller.internalcontrollers;

import com.effectiveMobile.effectivemobile.dto.CustomUsersDto;
import com.effectiveMobile.effectivemobile.exeptions.*;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.ServletException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.hibernate.exception.SQLGrammarException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;

@Hidden
@RestController
@RequestMapping("/test-exceptions")
class TestExceptionController {

    @GetMapping("/executor-not-found")
    public void throwExecutorNotFoundException() throws ExecutorNotFoundExeption {
        throw new ExecutorNotFoundExeption("Executor not found");
    }

    @GetMapping("/bad-credentials")
    public void throwBadCredentialsException() {
        throw new BadCredentialsException("Bad credentials");
    }

    @GetMapping("/not-enough-rules")
    public void throwNotEnoughRulesForEntity() throws NotEnoughRulesForEntity {
        throw new NotEnoughRulesForEntity("Not enough rules for entity");
    }

    @GetMapping("/entity-not-found")
    public void throwEntityNotFoundException() throws EntityNotFoundExeption {
        throw new EntityNotFoundExeption("Entity not found");
    }

    @GetMapping("/io-exception")
    public void throwIOException() throws IOException {
        throw new IOException("IO error occurred");
    }

    @GetMapping("/servlet-exception")
    public void throwServletException() throws ServletException {
        throw new ServletException("Servlet error occurred");
    }

    @GetMapping("/constraint-violation")
    public void throwConstraintViolationException() {
        Set<ConstraintViolation<?>> violations = Collections.emptySet();
        throw new ConstraintViolationException("Constraint violation", violations);
    }

    @PostMapping("/method-argument-not-valid")
    public void throwMethodArgumentNotValidException(@RequestBody @Valid @NotNull CustomUsersDto customUsersDto) {
        // Если валидация не пройдет, выбросится MethodArgumentNotValidException автоматически
    }

    @GetMapping("/user-not-found")
    public void throwUserNotFoundException() {
        throw new UserNotFoundException("User not found");
    }

    @GetMapping("/access-denied")
    public void throwAccessDeniedException() {
        throw new AccessDeniedException("Access denied");
    }

    @GetMapping("/http-message-not-readable")
    public void throwHttpMessageNotReadableException() {
        throw new HttpMessageNotReadableException("Message not readable");
    }

    @GetMapping("/entity-not-be-null")
    public void throwEntityNotBeNullException() throws EntityNotBeNull {
        throw new EntityNotBeNull("Entity must not be null");
    }

    @GetMapping("/field-not-be-null")
    public void throwFieldNotBeNullException() throws FieldNotBeNull {
        throw new FieldNotBeNull("Field must not be null");
    }

    @GetMapping("/role-not-found")
    public void throwRoleNotFoundException() throws RoleNotFoundException {
        throw new RoleNotFoundException("Role not found");
    }

    @GetMapping("/illegal-state")
    public void throwIllegalStateException() {
        throw new IllegalStateException("Illegal state");
    }

    @GetMapping("/sql-grammar")
    public void throwSQLGrammarException() {
        throw new SQLGrammarException("Syntax error", null, "SQL error");
    }

    @GetMapping("/data-integrity-violation")
    public void throwDataIntegrityViolationException() {
        throw new DataIntegrityViolationException("Data integrity violation");
    }

    @GetMapping("/incorrect-type-parameter")
    public void throwIncorrectTypeParameterException() throws IncorrectTypeParameterException {
        throw new IncorrectTypeParameterException("Incorrect type parameter");
    }

    @GetMapping("/runtime-exception")
    public void throwRuntimeException() {
        throw new RuntimeException("Runtime exception occurred");
    }

    @GetMapping("/general-exception")
    public void throwGeneralException() {
        throw new RuntimeException("General exception occurred");
    }

}
