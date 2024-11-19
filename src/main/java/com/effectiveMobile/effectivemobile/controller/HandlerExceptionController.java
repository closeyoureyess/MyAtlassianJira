package com.effectiveMobile.effectivemobile.controller;

import com.effectiveMobile.effectivemobile.exeptions.*;
import jakarta.servlet.ServletException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.api.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.effectiveMobile.effectivemobile.constants.ConstantsClass.LINE_FEED;

/**
 * <pre>
 *     Контроллер, обрабатывающий все эксепшены, котоыре могут быть выброшены в процессе работы приложения
 * </pre>
 */
@ControllerAdvice
@Slf4j
public class HandlerExceptionController {

    @ExceptionHandler(ExecutorNotFoundExeption.class)
    protected ResponseEntity<ErrorMessage> handleUserNameException(ExecutorNotFoundExeption e){
        log.error("Возникла ошибка: " + e.getClass() + LINE_FEED + e.getMessage() + LINE_FEED + Arrays.toString(e.getStackTrace()));
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorMessage(e.getMessage()));
    }

    @ExceptionHandler(NotEnoughRulesEntity.class)
    protected ResponseEntity<ErrorMessage> handleUserNameException(NotEnoughRulesEntity e){
        log.error("Возникла ошибка: " + e.getClass() + LINE_FEED + e.getMessage() + LINE_FEED + Arrays.toString(e.getStackTrace()));
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new ErrorMessage(e.getMessage()));
    }

    @ExceptionHandler(EntityNotFoundExeption.class)
    protected ResponseEntity<ErrorMessage> handleUserNameException(EntityNotFoundExeption e){
        log.error("Возникла ошибка: " + e.getClass() + LINE_FEED + e.getMessage() + LINE_FEED + Arrays.toString(e.getStackTrace()));
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorMessage(e.getMessage()));
    }

    @ExceptionHandler(IOException.class)
    protected ResponseEntity<ErrorMessage> handleUserNameException(IOException e){
        log.error("Возникла ошибка: " + e.getClass() + LINE_FEED + e.getMessage() + LINE_FEED + Arrays.toString(e.getStackTrace()));
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorMessage(e.getMessage()));
    }

    @ExceptionHandler(ServletException.class)
    protected ResponseEntity<ErrorMessage> handleUserNameException(ServletException e){
        log.error("Возникла ошибка: " + e.getClass() + LINE_FEED + e.getMessage() + LINE_FEED + Arrays.toString(e.getStackTrace()));
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorMessage(e.getMessage()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<ValidationErrorResponse> handleConstraintValidationException(ConstraintViolationException e) {
        log.error("Возникла ошибка валидации: " + e.getMessage(), e);
        List<Violation> violations = e.getConstraintViolations().stream()
                .map(violation -> new Violation(
                        violation.getPropertyPath().toString(),
                        violation.getMessage()
                ))
                .collect(Collectors.toList());
        ValidationErrorResponse errorResponse = new ValidationErrorResponse(violations);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ValidationErrorResponse> onMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        final List<Violation> violations = e.getBindingResult().getFieldErrors().stream()
                .map(error -> new Violation(error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());
        ValidationErrorResponse errorResponse = new ValidationErrorResponse(violations);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    protected ResponseEntity<ErrorMessage> onMethodArgumentNotValidException(UsernameNotFoundException e) {
        log.error("Возникла ошибка: " + e.getClass() + LINE_FEED + e.getMessage() + LINE_FEED + Arrays.toString(e.getStackTrace()));
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorMessage(e.getMessage()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<ErrorMessage> onMethodArgumentNotValidException(AccessDeniedException e) {
        log.error("Возникла ошибка: " + e.getClass() + LINE_FEED + e.getMessage() + LINE_FEED + Arrays.toString(e.getStackTrace()));
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new ErrorMessage(e.getMessage()));
    }
}
