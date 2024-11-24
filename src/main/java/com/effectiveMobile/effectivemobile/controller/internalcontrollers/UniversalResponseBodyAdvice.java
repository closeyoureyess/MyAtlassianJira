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

import java.util.Collection;
import java.util.List;

import static com.effectiveMobile.effectivemobile.constants.ConstantsClass.*;

@ControllerAdvice(assignableTypes = {NotesController.class, TaskController.class})
@Slf4j
public class UniversalResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        log.info("@ControllerAdvice класса UniversalResponseBodyAdvice, метод supports");
        ResolvableType resolvableType = ResolvableType.forMethodParameter(returnType);

        // Проверяем, что тип не null
        if (resolvableType == null) {
            return false;
        }

        Class<?> rawClass = resolvableType.getRawClass();

        // Проверяем, что это ResponseEntity
        if (rawClass == null || !ResponseEntity.class.isAssignableFrom(rawClass)) {
            return false;
        }

        // Извлекаем внутренний тип T из ResponseEntity<T>
        ResolvableType innerType = resolvableType.getGeneric(0);
        if (innerType == null) {
            return false;
        }

        Class<?> resolvedClass = innerType.resolve();
        if (resolvedClass == null) {
            return false;
        }

        // Проверяем, является ли T непосредственно NotesDto или TasksDto
        if (isSupportedDto(resolvedClass)) {
            return true;
        }

        // Если T - коллекция, проверяем тип элементов
        if (isSupportedCollection(resolvedClass, innerType)) {
            return true;
        }

        return false;
    }

    /**
     * Проверяет, явлется ли классом c тем возвращаемым Dto, который обрабатывается.
     */
    private boolean isSupportedDto(Class<?> clazz) {
        return NotesDto.class.isAssignableFrom(clazz) || TasksDto.class.isAssignableFrom(clazz);
    }

    /**
     * Проверяет, является ли класс коллекцией с поддерживаемыми элементами.
     */
    private boolean isSupportedCollection(Class<?> clazz, ResolvableType innerType) {
        if (!Collection.class.isAssignableFrom(clazz)) {
            return false;
        }

        ResolvableType elementType = innerType.getGeneric(0);
        if (elementType == null) {
            return false;
        }

        Class<?> elementClass = elementType.resolve();
        if (elementClass == null) {
            return false;
        }

        return NotesDto.class.isAssignableFrom(elementClass) || TasksDto.class.isAssignableFrom(elementClass);
    }

    private boolean resolvableTypeGetGenericBlock(ResolvableType innerType) {
        ResolvableType elementType = innerType.getGeneric(0); // E в Collection<E>
        if (elementType != null) {
            Class<?> elementClass = elementType.resolve();
            if (elementClass != null &&
                    (NotesDto.class.isAssignableFrom(elementClass) ||
                            TasksDto.class.isAssignableFrom(elementClass))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request,
                                  ServerHttpResponse response) {
        log.info("Метод beforeBodyWrite() класса UniversalResponseBodyAdvice");
        String customUsersDtoFilter = "CustomUsersDtoFilter";
        String notesDtoFilter = "NotesDtoFilter";
        String tasksDtoFilter = "TasksDtoFilter";

        // Проверяем, является ли тело ответа экземпляром NotesDto
        if (!(body instanceof NotesDto) && !(body instanceof TasksDto) && !(body instanceof List<?>)) {
            log.info("Тело ответа не является экземпляром NotesDto/TasksDto/List, метод beforeBodyWrite() " +
                    "класса UniversalResponseBodyAdvice");
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
        SimpleBeanPropertyFilter notesFilter;
        SimpleBeanPropertyFilter taskFilter;
        SimpleBeanPropertyFilter customDtoFilter;

        FilterProvider filters = switch (filterName) {
            case POST_CREATE_NOTES -> {
                log.info("Значение: " + POST_CREATE_NOTES + " Эндпоинт POST Notes, метод сreateNotes(), метод beforeBodyWrite() " +
                        "класса UniversalResponseBodyAdvice");
                // Определяем, какие поля включать в NotesDto
                notesFilter = SimpleBeanPropertyFilter.filterOutAllExcept(NOTES_AUTHOR_FIELD_NAME,
                        "comments", "task");

                // Определяем, какие поля включать в TasksDto
                taskFilter = SimpleBeanPropertyFilter.filterOutAllExcept(USUAL_ID_FIELD_NAME);

                // Определяем, какие поля включать в CustomDto
                customDtoFilter = SimpleBeanPropertyFilter.filterOutAllExcept(USUAL_ID_FIELD_NAME, USER_EMAIL_FIELD_NAME);

                // Создаём провайдер фильтров
                filters = new SimpleFilterProvider()
                        .addFilter(customUsersDtoFilter, customDtoFilter)
                        .addFilter(notesDtoFilter, notesFilter)
                        .addFilter(tasksDtoFilter, taskFilter);
                yield filters;
            }
            case POST_CREATE_TASKS -> {
                log.info("Значение: " + POST_CREATE_TASKS + " Эндпоинт POST Tasks /task/create, метод сreateTask(), метод beforeBodyWrite() " +
                        "класса UniversalResponseBodyAdvice");
                taskFilter = SimpleBeanPropertyFilter.filterOutAllExcept(USUAL_ID_FIELD_NAME, TASK_HEADER_FIELD_NAME,
                        TASK_AUTHOR_FIELD_NAME, TASK_EXECUTOR_FIELD_NAME, TASK_DESCRIPTION_FIELD_NAME, TASK_PRIORITY_FIELD_NAME,
                        TASK_STATUS_FIELD_NAME);
                customDtoFilter = SimpleBeanPropertyFilter.filterOutAllExcept(USUAL_ID_FIELD_NAME, USER_EMAIL_FIELD_NAME);
                filters = new SimpleFilterProvider()
                        .addFilter(customUsersDtoFilter, customDtoFilter)
                        .addFilter(tasksDtoFilter, taskFilter);
                yield filters;
            }
            case GET_TASKAUTHOR_TASKS -> {
                log.info("Значение: " + GET_TASKAUTHOR_TASKS + " Эндпоинт GET Tasks /task/gen-info/author, метод getTaskExecutor(), " +
                        "метод beforeBodyWrite() класса UniversalResponseBodyAdvice");
                notesFilter = SimpleBeanPropertyFilter.filterOutAllExcept(USUAL_ID_FIELD_NAME, NOTES_AUTHOR_FIELD_NAME,
                        NOTES_COMMENTS_FIELD_NAME);
                taskFilter = SimpleBeanPropertyFilter.filterOutAllExcept(USUAL_ID_FIELD_NAME, TASK_HEADER_FIELD_NAME,
                        TASK_AUTHOR_FIELD_NAME, TASK_EXECUTOR_FIELD_NAME, TASK_DESCRIPTION_FIELD_NAME, TASK_PRIORITY_FIELD_NAME,
                        TASK_STATUS_FIELD_NAME, TASK_NOTES_FIELD_NAME);
                customDtoFilter = SimpleBeanPropertyFilter.filterOutAllExcept(USUAL_ID_FIELD_NAME, USER_EMAIL_FIELD_NAME);
                filters = new SimpleFilterProvider()
                        .addFilter(customUsersDtoFilter, customDtoFilter)
                        .addFilter(notesDtoFilter, notesFilter)
                        .addFilter(tasksDtoFilter, taskFilter);
                yield filters;
            }
            case GET_TASKEXECUTOR_TASKS -> {
                log.info("Значение: " + GET_TASKEXECUTOR_TASKS + " Эндпоинт GET Tasks /task/gen-info/executor/, метод editTasks(), " +
                        "метод beforeBodyWrite() класса UniversalResponseBodyAdvice");
                notesFilter = SimpleBeanPropertyFilter.filterOutAllExcept(USUAL_ID_FIELD_NAME, NOTES_AUTHOR_FIELD_NAME,
                        NOTES_COMMENTS_FIELD_NAME);
                taskFilter = SimpleBeanPropertyFilter.filterOutAllExcept(USUAL_ID_FIELD_NAME, TASK_HEADER_FIELD_NAME,
                        TASK_AUTHOR_FIELD_NAME, TASK_EXECUTOR_FIELD_NAME, TASK_DESCRIPTION_FIELD_NAME, TASK_PRIORITY_FIELD_NAME,
                        TASK_STATUS_FIELD_NAME, TASK_NOTES_FIELD_NAME);
                customDtoFilter = SimpleBeanPropertyFilter.filterOutAllExcept(USUAL_ID_FIELD_NAME, USER_EMAIL_FIELD_NAME);
                filters = new SimpleFilterProvider()
                        .addFilter(customUsersDtoFilter, customDtoFilter)
                        .addFilter(notesDtoFilter, notesFilter)
                        .addFilter(tasksDtoFilter, taskFilter);
                yield filters;
            }
            case PUT_EDIT_TASKS -> {
                log.info("Значение: " + PUT_EDIT_TASKS + " Эндпоинт PUT Tasks /task/update-tasks, метод editTasks(), " +
                        "метод beforeBodyWrite() класса UniversalResponseBodyAdvice");
                notesFilter = SimpleBeanPropertyFilter.filterOutAllExcept(USUAL_ID_FIELD_NAME, NOTES_AUTHOR_FIELD_NAME,
                        NOTES_COMMENTS_FIELD_NAME);
                taskFilter = SimpleBeanPropertyFilter.filterOutAllExcept(USUAL_ID_FIELD_NAME, TASK_HEADER_FIELD_NAME,
                        TASK_AUTHOR_FIELD_NAME, TASK_EXECUTOR_FIELD_NAME, TASK_DESCRIPTION_FIELD_NAME, TASK_PRIORITY_FIELD_NAME,
                        TASK_STATUS_FIELD_NAME, TASK_NOTES_FIELD_NAME);
                customDtoFilter = SimpleBeanPropertyFilter.filterOutAllExcept(USUAL_ID_FIELD_NAME, USER_EMAIL_FIELD_NAME);
                filters = new SimpleFilterProvider()
                        .addFilter(customUsersDtoFilter, customDtoFilter)
                        .addFilter(notesDtoFilter, notesFilter)
                        .addFilter(tasksDtoFilter, taskFilter);
                yield filters;
            }
            default -> null;
        };

        if (filters != null) {
            mapping.setFilters(filters);
        }
        return mapping;
    }
}
