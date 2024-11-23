package com.effectiveMobile.effectivemobile.controller.internalcontrollers;

import com.effectiveMobile.effectivemobile.annotations.FilterResponse;
import com.effectiveMobile.effectivemobile.controller.NotesController;
import com.effectiveMobile.effectivemobile.controller.TaskController;
import com.effectiveMobile.effectivemobile.dto.NotesDto;
import com.effectiveMobile.effectivemobile.dto.TasksDto;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.core.ResolvableType;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import static com.effectiveMobile.effectivemobile.constants.ConstantsClass.POST_CREATE_NOTES;

@ControllerAdvice(assignableTypes = {NotesController.class, TaskController.class})
@Slf4j
public class UniversalResponseBodyAdvice implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        log.info("@ControllerAdvice класса UniversalResponseBodyAdvice, метод supports");
        ResolvableType resolvableType = ResolvableType.forMethodParameter(returnType);
        if (    resolvableType != null
                && resolvableType.getRawClass() != null
                && resolvableType.hasGenerics()
                && ResponseEntity.class.isAssignableFrom(resolvableType.getRawClass()
        )) {
            Class<?> innerType = resolvableType.getGeneric(0).resolve();
            if (innerType != null) {
                return NotesDto.class.isAssignableFrom(innerType) ||
                        TasksDto.class.isAssignableFrom(innerType);
            }
        }
        return false;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  org.springframework.http.server.ServerHttpRequest request,
                                  org.springframework.http.server.ServerHttpResponse response) {
        log.info("Метод beforeBodyWrite() класса UniversalResponseBodyAdvice");

        // Проверяем, является ли тело ответа экземпляром NotesDto
        if (!(body instanceof NotesDto)) {
            log.info("Тело ответа не является экземпляром NotesDto, метод beforeBodyWrite() класса UniversalResponseBodyAdvice");
            return body;
        }

        // Проверяем, что запрос является ServletServerHttpRequest
        if (!(request instanceof ServletServerHttpRequest)) {
            log.info("Запрос является ServletServerHttpRequest, метод beforeBodyWrite() класса UniversalResponseBodyAdvice");
            return body;
        }

        ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
        HttpServletRequest httpServletRequest = servletRequest.getServletRequest();

        // Получаем HandlerMethod из атрибутов запроса
        Object handler = httpServletRequest.getAttribute("org.springframework.web.servlet.HandlerMapping.bestMatchingHandler");

        if (!(handler instanceof HandlerMethod)) {
            log.info("Запрос является HandlerMethod, метод beforeBodyWrite() класса UniversalResponseBodyAdvice");
            return body;
        }

        //Получить HandlerMethod, который представляет метод контроллера, обрабатывающий текущий запрос.
        HandlerMethod handlerMethod = (HandlerMethod) handler;

        // Получаем аннотацию @FilterResponse
        FilterResponse filterResponse = handlerMethod.getMethodAnnotation(FilterResponse.class);

        if (filterResponse == null) {
            log.info("Метод не аннотирован, не применяем фильтры, метод beforeBodyWrite() класса UniversalResponseBodyAdvice");
            return body;
        }

        String filterName = filterResponse.filterName();
        MappingJacksonValue mapping = new MappingJacksonValue(body);

        if (filterName.equals(POST_CREATE_NOTES)) {
            log.info("Значение: " + POST_CREATE_NOTES + " Эндпоинт POST Notes, метод сreateNotes(), метод beforeBodyWrite() " +
                    "класса UniversalResponseBodyAdvice");
            // Определяем, какие поля включать в NotesDto
            SimpleBeanPropertyFilter notesFilter = SimpleBeanPropertyFilter.filterOutAllExcept("id", "usersDto",
                    "comments", "task");

            // Определяем, какие поля включать в TasksDto
            SimpleBeanPropertyFilter taskFilter = SimpleBeanPropertyFilter.filterOutAllExcept("id");

            // Определяем, какие поля включать в CustomDto
            SimpleBeanPropertyFilter customDtoFilter = SimpleBeanPropertyFilter.filterOutAllExcept("id", "email");

            // Создаём провайдер фильтров
            FilterProvider filters = new SimpleFilterProvider()
                    .addFilter("CustomUsersDtoFilter", customDtoFilter)
                    .addFilter("NotesDtoFilter", notesFilter)
                    .addFilter("TasksDtoFilter", taskFilter);

            mapping.setFilters(filters);
        }
        return mapping;
    }
}
