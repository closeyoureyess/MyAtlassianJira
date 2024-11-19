package com.effectiveMobile.effectivemobile.exeptions;

import jakarta.servlet.ServletException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;
import java.util.Arrays;

/**
 * <pre>
 *     Контроллер, обрабатывающий все эксепшены, котоыре могут быть выброшены в процессе работы приложения
 * </pre>
 */
@ControllerAdvice
@Slf4j
public class HandlerException {

    @ExceptionHandler(MainException.class)
    protected ResponseEntity<Response> handleUserNameException(MainException e){
        log.error("Возникла ошибка: " + e.getClass() + "\n" + e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
        return new ResponseEntity<>(new Response(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IOException.class)
    protected ResponseEntity<Response> handleUserNameException(IOException e){
        log.error("Возникла ошибка: " + e.getClass() + "\n" + e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
        return new ResponseEntity<>(new Response(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ServletException.class)
    protected ResponseEntity<Response> handleUserNameException(ServletException e){
        log.error("Возникла ошибка: " + e.getClass() + "\n" + e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
        return new ResponseEntity<>(new Response(e.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
