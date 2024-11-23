package com.effectiveMobile.effectivemobile.controller.internalcontrollers;

import com.effectiveMobile.effectivemobile.exeptions.*;
import jakarta.servlet.ServletException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.api.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
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
import static com.effectiveMobile.effectivemobile.exeptions.DescriptionUserExeption.GENERATION_ERROR;

/**
 * <pre>
 *     Контроллер, обрабатывающий все эксепшены, котоыре могут быть выброшены в процессе работы приложения
 * </pre>
 */
@ControllerAdvice
@Slf4j
public class HandlerExceptionController {

    @ExceptionHandler(ExecutorNotFoundExeption.class)
    protected ResponseEntity<ErrorMessage> errorExecutorNotFoundExeption(ExecutorNotFoundExeption e){
        log.error(GENERATION_ERROR.getEnumDescription() + e.getClass() + LINE_FEED + e.getMessage() + LINE_FEED +
                Arrays.toString(e.getStackTrace()));
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorMessage(e.getMessage()));
    }

    @ExceptionHandler(NotEnoughRulesForEntity.class)
    protected ResponseEntity<ErrorMessage> errorNotEnoughRulesEntity(NotEnoughRulesForEntity e){
        log.error(GENERATION_ERROR.getEnumDescription() + e.getClass() + LINE_FEED + e.getMessage() + LINE_FEED + Arrays.toString(e.getStackTrace()));
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new ErrorMessage(e.getMessage()));
    }

    @ExceptionHandler(EntityNotFoundExeption.class)
    protected ResponseEntity<ErrorMessage> errorEntityNotFoundExeption(EntityNotFoundExeption e){
        log.error(GENERATION_ERROR.getEnumDescription() + e.getClass() + LINE_FEED + e.getMessage() + LINE_FEED + Arrays.toString(e.getStackTrace()));
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorMessage(e.getMessage()));
    }

    @ExceptionHandler(IOException.class)
    protected ResponseEntity<ErrorMessage> errorIOException(IOException e){
        log.error(GENERATION_ERROR.getEnumDescription() + e.getClass() + LINE_FEED + e.getMessage() + LINE_FEED + Arrays.toString(e.getStackTrace()));
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorMessage(e.getMessage()));
    }

    @ExceptionHandler(ServletException.class)
    protected ResponseEntity<ErrorMessage> errorServletException(ServletException e){
        log.error(GENERATION_ERROR.getEnumDescription() + e.getClass() + LINE_FEED + e.getMessage() + LINE_FEED + Arrays.toString(e.getStackTrace()));
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorMessage(e.getMessage()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<ValidationErrorResponse> errorConstraintViolationException(ConstraintViolationException e) {
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
    protected ResponseEntity<ValidationErrorResponse> errorMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error(GENERATION_ERROR.getEnumDescription() + e.getClass() + LINE_FEED + e.getMessage() + LINE_FEED +
                Arrays.toString(e.getStackTrace()));
        final List<Violation> violations = e.getBindingResult().getFieldErrors().stream()
                .map(error -> new Violation(error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());
        ValidationErrorResponse errorResponse = new ValidationErrorResponse(violations);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorResponse); //
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    protected ResponseEntity<ErrorMessage> errorUsernameNotFoundException(UsernameNotFoundException e) {
        log.error(GENERATION_ERROR.getEnumDescription() + e.getClass() + LINE_FEED + e.getMessage() + LINE_FEED +
                Arrays.toString(e.getStackTrace()));
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorMessage(e.getMessage()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<ErrorMessage> errorAccessDeniedException(AccessDeniedException e) {
        log.error(GENERATION_ERROR.getEnumDescription() + e.getClass() + LINE_FEED + e.getMessage() + LINE_FEED +
                Arrays.toString(e.getStackTrace()));
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new ErrorMessage(e.getMessage()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<ErrorMessage> errorHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.error(GENERATION_ERROR.getEnumDescription() + e.getClass() + LINE_FEED + e.getMessage() + LINE_FEED +
                Arrays.toString(e.getStackTrace()));
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorMessage(e.getMessage()));
    }

    @ExceptionHandler(EntityNotBeNull.class)
    protected ResponseEntity<ErrorMessage> errorEntityNotBeNull(EntityNotBeNull e) {
        log.error(GENERATION_ERROR.getEnumDescription() + e.getClass() + LINE_FEED + e.getMessage() + LINE_FEED +
                Arrays.toString(e.getStackTrace()));
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorMessage(e.getMessage()));
    }

    @ExceptionHandler(FieldNotBeNull.class)
    protected ResponseEntity<ErrorMessage> errorFieldNotBeNull(FieldNotBeNull e) {
        log.error(GENERATION_ERROR.getEnumDescription() + e.getClass() + LINE_FEED + e.getMessage() + LINE_FEED +
                Arrays.toString(e.getStackTrace()));
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorMessage(e.getMessage()));
    }
}
