package com.effectiveMobile.effectivemobile.controller.internalcontrollers;

import com.effectiveMobile.effectivemobile.exeptions.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.SQLGrammarException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;
import java.time.LocalDateTime;
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

    /**
     * Обработчик ExecutorNotFoundException
     */
    @ExceptionHandler(ExecutorNotFoundExeption.class)
    protected ResponseEntity<ApiErrorResponse> handleExecutorNotFoundExeption(ExecutorNotFoundExeption e, HttpServletRequest request) {
        log.error(GENERATION_ERROR.getEnumDescription() + e.getClass() + LINE_FEED + e.getMessage() + LINE_FEED +
                e);

        ApiErrorResponse apiErrorResponse = buildApiErrorResponse(
                HttpStatus.NOT_FOUND,
                e.getMessage(),
                request.getRequestURI(),
                null
        );

        return new ResponseEntity<>(apiErrorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * Обработчик NotEnoughRulesForEntity
     */
    @ExceptionHandler(NotEnoughRulesForEntity.class)
    protected ResponseEntity<ApiErrorResponse> handleNotEnoughRulesForEntity(NotEnoughRulesForEntity e, HttpServletRequest request) {
        log.error(GENERATION_ERROR.getEnumDescription() + e.getClass() + LINE_FEED + e.getMessage() + LINE_FEED +
                e);

        ApiErrorResponse apiErrorResponse = buildApiErrorResponse(
                HttpStatus.FORBIDDEN,
                e.getMessage(),
                request.getRequestURI(),
                null
        );

        return new ResponseEntity<>(apiErrorResponse, HttpStatus.FORBIDDEN);
    }

    /**
     * Обработчик EntityNotFoundException
     */
    @ExceptionHandler(EntityNotFoundExeption.class)
    protected ResponseEntity<ApiErrorResponse> handleEntityNotFoundExeption(EntityNotFoundExeption e, HttpServletRequest request) {
        log.error(GENERATION_ERROR.getEnumDescription() + e.getClass() + LINE_FEED + e.getMessage() + LINE_FEED +
                e);

        ApiErrorResponse apiErrorResponse = buildApiErrorResponse(
                HttpStatus.NOT_FOUND,
                e.getMessage(),
                request.getRequestURI(),
                null
        );

        return new ResponseEntity<>(apiErrorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * Обработчик IOException
     */
    @ExceptionHandler(IOException.class)
    protected ResponseEntity<ApiErrorResponse> handleIOException(IOException e, HttpServletRequest request) {
        log.error(GENERATION_ERROR.getEnumDescription() + e.getClass() + LINE_FEED + e.getMessage() + LINE_FEED +
                e);

        ApiErrorResponse apiErrorResponse = buildApiErrorResponse(
                HttpStatus.BAD_REQUEST,
                e.getMessage(),
                request.getRequestURI(),
                null
        );

        return new ResponseEntity<>(apiErrorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Обработчик ServletException
     */
    @ExceptionHandler(ServletException.class)
    protected ResponseEntity<ApiErrorResponse> handleServletException(ServletException e, HttpServletRequest request) {
        log.error(GENERATION_ERROR.getEnumDescription() + e.getClass() + LINE_FEED + e.getMessage() + LINE_FEED +
                e);

        ApiErrorResponse apiErrorResponse = buildApiErrorResponse(
                HttpStatus.BAD_REQUEST,
                e.getMessage(),
                request.getRequestURI(),
                null
        );

        return new ResponseEntity<>(apiErrorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Обработчик ConstraintViolationException
     */
    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<ApiErrorResponse> handleConstraintViolationException(ConstraintViolationException e,
                                                                                  HttpServletRequest request) {
        log.error(GENERATION_ERROR.getEnumDescription() + e.getClass() + LINE_FEED + e.getMessage() + LINE_FEED +
                e);

        List<Violation> violations = e.getConstraintViolations().stream()
                .map(violation -> new Violation(
                        violation.getPropertyPath().toString(),
                        violation.getMessage()
                ))
                .collect(Collectors.toList());

        ApiErrorResponse apiErrorResponse = buildApiErrorResponse(
                HttpStatus.BAD_REQUEST,
                e.getMessage(),
                request.getRequestURI(),
                violations
        );

        return new ResponseEntity<>(apiErrorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Обработчик MethodArgumentNotValidException
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        log.error(GENERATION_ERROR.getEnumDescription() + e.getClass() + LINE_FEED + e.getMessage() + LINE_FEED +
                e);

        List<Violation> violations = e.getBindingResult().getFieldErrors().stream()
                .map(error -> new Violation(error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());

        ApiErrorResponse apiErrorResponse = buildApiErrorResponse(
                HttpStatus.BAD_REQUEST,
                e.getMessage(),
                request.getRequestURI(),
                violations
        );

        return new ResponseEntity<>(apiErrorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Обработчик UsernameNotFoundException
     */
    @ExceptionHandler(UserNotFoundException.class)
    protected ResponseEntity<ApiErrorResponse> handleUsernameNotFoundException(UserNotFoundException e, HttpServletRequest request) {
        log.error(GENERATION_ERROR.getEnumDescription() + e.getClass() + LINE_FEED + e.getMessage() + LINE_FEED +
                e);

        ApiErrorResponse apiErrorResponse = buildApiErrorResponse(
                HttpStatus.NOT_FOUND,
                e.getMessage(),
                request.getRequestURI(),
                null
        );

        return new ResponseEntity<>(apiErrorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * Обработчик AccessDeniedException
     */
    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<ApiErrorResponse> handleAccessDeniedException(AccessDeniedException e, HttpServletRequest request) {
        log.error(GENERATION_ERROR.getEnumDescription() + e.getClass() + LINE_FEED + e.getMessage() + LINE_FEED +
                e);

        ApiErrorResponse apiErrorResponse = buildApiErrorResponse(
                HttpStatus.FORBIDDEN,
                e.getMessage(),
                request.getRequestURI(),
                null
        );

        return new ResponseEntity<>(apiErrorResponse, HttpStatus.FORBIDDEN);
    }

    /**
     * Обработчик HttpMessageNotReadableException
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<ApiErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException e, HttpServletRequest request) {
        log.error(GENERATION_ERROR.getEnumDescription() + e.getClass() + LINE_FEED + e.getMessage() + LINE_FEED +
                e);

        ApiErrorResponse apiErrorResponse = buildApiErrorResponse(
                HttpStatus.BAD_REQUEST,
                e.getMessage(),
                request.getRequestURI(),
                null
        );

        return new ResponseEntity<>(apiErrorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Обработчик EntityNotBeNull
     */
    @ExceptionHandler(EntityNotBeNull.class)
    protected ResponseEntity<ApiErrorResponse> handleEntityNotBeNull(EntityNotBeNull e, HttpServletRequest request) {
        log.error(GENERATION_ERROR.getEnumDescription() + e.getClass() + LINE_FEED + e.getMessage() + LINE_FEED +
                e);

        ApiErrorResponse apiErrorResponse = buildApiErrorResponse(
                HttpStatus.BAD_REQUEST,
                e.getMessage(),
                request.getRequestURI(),
                null
        );

        return new ResponseEntity<>(apiErrorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Обработчик FieldNotBeNull
     */
    @ExceptionHandler(FieldNotBeNull.class)
    protected ResponseEntity<ApiErrorResponse> handleFieldNotBeNull(FieldNotBeNull e, HttpServletRequest request) {
        log.error(GENERATION_ERROR.getEnumDescription() + e.getClass() + LINE_FEED + e.getMessage() + LINE_FEED +
                e);

        ApiErrorResponse apiErrorResponse = buildApiErrorResponse(
                HttpStatus.BAD_REQUEST,
                e.getMessage(),
                request.getRequestURI(),
                null
        );

        return new ResponseEntity<>(apiErrorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Обработчик RoleNotFoundException
     */
    @ExceptionHandler(RoleNotFoundException.class)
    protected ResponseEntity<ApiErrorResponse> handleRoleNotFoundException(RoleNotFoundException e, HttpServletRequest request) {
        log.error(GENERATION_ERROR.getEnumDescription() + e.getClass() + LINE_FEED + e.getMessage() + LINE_FEED +
                e);

        ApiErrorResponse apiErrorResponse = buildApiErrorResponse(
                HttpStatus.NOT_FOUND,
                e.getMessage(),
                request.getRequestURI(),
                null
        );

        return new ResponseEntity<>(apiErrorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * Обработчик IllegalStateException
     */
    @ExceptionHandler(IllegalStateException.class)
    protected ResponseEntity<ApiErrorResponse> handleIllegalStateException(IllegalStateException e, HttpServletRequest request) {
        log.error(GENERATION_ERROR.getEnumDescription() + e.getClass() + LINE_FEED + e.getMessage() + LINE_FEED +
                e);

        ApiErrorResponse apiErrorResponse = buildApiErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                e.getMessage(),
                request.getRequestURI(),
                null
        );

        return new ResponseEntity<>(apiErrorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Обработчик SQLGrammarException
     */
    @ExceptionHandler(SQLGrammarException.class)
    protected ResponseEntity<ApiErrorResponse> handleSQLGrammarException(SQLGrammarException e, HttpServletRequest request) {
        log.error(GENERATION_ERROR.getEnumDescription() + e.getClass() + LINE_FEED + e.getMessage() + LINE_FEED +
                e);

        ApiErrorResponse apiErrorResponse = buildApiErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                e.getMessage(),
                request.getRequestURI(),
                null
        );

        return new ResponseEntity<>(apiErrorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Обработчик DataIntegrityViolationException
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    protected ResponseEntity<ApiErrorResponse> handleDataIntegrityViolationException(DataIntegrityViolationException e,
                                                                                     HttpServletRequest request) {
        log.error(GENERATION_ERROR.getEnumDescription() + e.getClass() + LINE_FEED + e.getMessage() + LINE_FEED +
                e);

        ApiErrorResponse apiErrorResponse = buildApiErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                e.getMessage(),
                request.getRequestURI(),
                null
        );

        return new ResponseEntity<>(apiErrorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Общий обработчик для всех остальных исключений
     */
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ApiErrorResponse> handleAllExceptions(Exception e, HttpServletRequest request) {
        log.error("Возникла непредвиденная ошибка " + e.getClass() + LINE_FEED + e.getMessage() + LINE_FEED +
                e);

        ApiErrorResponse apiErrorResponse = buildApiErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                e.getMessage(),
                request.getRequestURI(),
                null
        );

        return new ResponseEntity<>(apiErrorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RuntimeException.class)
    protected ResponseEntity<ApiErrorResponse> handleAllRunTimeException(RuntimeException e, HttpServletRequest request) {
        log.error("Возникла непредвиденная ошибка " + e.getClass() + LINE_FEED + e.getMessage() + LINE_FEED +
                e);

        ApiErrorResponse apiErrorResponse = buildApiErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                e.getMessage(),
                request.getRequestURI(),
                null
        );

        return new ResponseEntity<>(apiErrorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Обработчик исключения, выбрасываемого, если передаются некорректные параметры в JSON, либо в параметры метода
     */
    @ExceptionHandler(IncorrectTypeParameterException.class)
    protected ResponseEntity<ApiErrorResponse> handleIncorrectTypeParameterException(IncorrectTypeParameterException e, HttpServletRequest request) {
        log.error(GENERATION_ERROR.getEnumDescription() + e.getClass() + LINE_FEED + e.getMessage() + LINE_FEED +
                e);

        ApiErrorResponse apiErrorResponse = buildApiErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                e.getMessage(),
                request.getRequestURI(),
                null
        );

        return new ResponseEntity<>(apiErrorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ApiErrorResponse buildApiErrorResponse(HttpStatus status, String message, String path, List<Violation> violations) {
        return ApiErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .path(path)
                .violations(violations)
                .build();
    }
}
