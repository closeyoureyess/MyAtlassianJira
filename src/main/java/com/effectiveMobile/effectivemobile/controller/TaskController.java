package com.effectiveMobile.effectivemobile.controller;

import com.effectiveMobile.effectivemobile.annotations.FilterResponse;
import com.effectiveMobile.effectivemobile.constants.ConstantsClass;
import com.effectiveMobile.effectivemobile.dto.TasksDto;
import com.effectiveMobile.effectivemobile.exeptions.MainException;
import com.effectiveMobile.effectivemobile.fabrics.ServiceFabric;
import com.effectiveMobile.effectivemobile.services.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static com.effectiveMobile.effectivemobile.constants.ConstantsClass.*;

/**
 * <pre>
 *     Контроллер создания, редактирования, удаления, получения информации о задачах
 * </pre>
 */
@Tag(name = "Задачи", description = "Позволяет создать, редактировать, удалить, получить информацию о задачах")
@RestController
@RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@Validated
public class TaskController {

    @Autowired
    private ServiceFabric serviceFabric;

    /**
     * Эндпоинд POST для создания задачи
     *
     * @param tasksDto DTO объект задачи для создания
     * @return {@link ResponseEntity} с созданной задачей
     * @throws MainException Если произошла ошибка при создании задачи
     */
    @Operation(summary = "Создание задачи", description = "Позволяет создать задачу", responses = {})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Задача успешно создана", content = @Content(examples = @ExampleObject(value = "\"{\\n  \\\"id\\\": 1,\\n  \\\"header\\\": \\\"Тестовая задача\\\",\\n  \\\"taskAuthor\\\": {\\n    \\\"id\\\": 1,\\n    \\\"email\\\": \\\"author@example.com\\\"\\n  },\\n  \\\"taskExecutor\\\": {\\n    \\\"id\\\": 2,\\n    \\\"email\\\": \\\"executor@example.com\\\"\\n  },\\n  \\\"description\\\": \\\"Тестовое описание задачи\\\",\\n  \\\"taskPriority\\\": \\\"MEDIUM\\\",\\n  \\\"taskStatus\\\": \\\"BACKLOG\\\"\\n}\""))),
            @ApiResponse(responseCode = "400", description = "Не удалось создать задачу", content = @Content),
            @ApiResponse(responseCode = "403", description = "Не авторизован/Недостаточно прав", content = @Content),
            @ApiResponse(responseCode = "404", description = "У задачи отсутствует исполнитель", content = @Content)
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Пример тела запроса для создания задачи", content = @Content(mediaType = "application/json", examples = @ExampleObject(name = "Создание задачи", value = "\"{\\n  \\\"header\\\": \\\"Тестовая задача\\\",\\n  \\\"taskExecutor\\\": {\\n  \\\"email\\\": \\\"example2@gmail.com\\\"\\n  },\\n  \\\"description\\\": \\\"Тестовое описание задачи\\\",\\n  \\\"taskPriority\\\": \\\"MEDIUM\\\",\\n  \\\"taskStatus\\\": \\\"BACKLOG\\\"\\n}\"")))
    @FilterResponse(filterName = POST_CREATE_TASKS)
    @SecurityRequirement(name = "JWT")
    @PostMapping(value = "/task/create")
    public ResponseEntity<TasksDto> createTask(@Valid @RequestBody @NotNull(message = "TasksDto не может быть null")
                                               TasksDto tasksDto) throws MainException {
        log.info("Создание задачи, POST " + tasksDto.getHeader());
        TaskService taskService = serviceFabric.createTaskService();
        TasksDto localTasksDto = taskService.createTasks(tasksDto);
        if (localTasksDto != null) {
            return ResponseEntity.ok(localTasksDto);
        }
        return ResponseEntity.badRequest().build();
    }

    /**
     * Эндпоинд GET для получения информации о задачах по автору.
     *
     * @param author Автор задачи (email)
     * @param offset Номер страницы для пагинации (0 - по умолчанию)
     * @param limit  Количество сущностей на странице (10 - по умолчанию)
     * @return {@link ResponseEntity} с задачами по автору
     */
    @Operation(summary = "Получить задачу по автору")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Возвращены задачи с комментариями по автору", content = @Content(examples = @ExampleObject(value = "\"[{\\\"id\\\":1,\\\"header\\\":\\\"Test task header\\\",\\\"taskAuthor\\\":{\\\"id\\\":2,\\\"email\\\":\\\"example2@gmail.com\\\"},\\\"taskExecutor\\\":{\\\"id\\\":1,\\\"email\\\":\\\"example@gmail.com\\\"},\\\"description\\\":\\\"This is a test task description\\\",\\\"taskPriority\\\":\\\"MEDIUM\\\",\\\"taskStatus\\\":\\\"BACKLOG\\\",\\\"notesDto\\\":[{\\\"id\\\":1,\\\"notesAuthor\\\":{\\\"id\\\":2,\\\"email\\\":\\\"example2@gmail.com\\\"},\\\"comments\\\":\\\"This is a test comment for Task 1\\\"}]},{\\\"id\\\":2,\\\"header\\\":\\\"Test Task 2\\\",\\\"taskAuthor\\\":{\\\"id\\\":2,\\\"email\\\":\\\"example2@gmail.com\\\"},\\\"taskExecutor\\\":{\\\"id\\\":2,\\\"email\\\":\\\"example2@gmail.com\\\"},\\\"description\\\":\\\"Another test task description\\\",\\\"taskPriority\\\":\\\"HIGH\\\",\\\"taskStatus\\\":\\\"IN_PROGRESS\\\",\\\"notesDto\\\":[{\\\"id\\\":2,\\\"notesAuthor\\\":{\\\"id\\\":1,\\\"email\\\":\\\"example@gmail.com\\\"},\\\"comments\\\":\\\"Another comment for Task 2\\\"}]}]\""))),
            @ApiResponse(responseCode = "400", description = "Не удалось получить информацию о задачах", content = @Content),
            @ApiResponse(responseCode = "403", description = "Не авторизован/Недостаточно прав", content = @Content),
    })
    @FilterResponse(filterName = GET_TASKAUTHOR_TASKS)
    @SecurityRequirement(name = "JWT")
    @GetMapping(value = "/task/gen-info/author/{author}", consumes = MediaType.ALL_VALUE)
    public ResponseEntity<List<TasksDto>> getTaskAuthor(
            @PathVariable("author") @Parameter(description = "Автор задачи", example = "example@gmail.com")
            @NotBlank(message = "Автор не может быть пустым") String author,
            @RequestParam(value = "offset", defaultValue = "0") @Min(0) @Parameter(description = "Номер страницы", example = "0")
            @NotNull(message = "Страница не может быть пустой")
            Integer offset,
            @RequestParam(value = "limit", defaultValue = "10") @Min(10) @Parameter(description = "Количество сущностей на странице",
                    example = "10") @NotNull(message = "Кол-во сущностей не может быть пустым") Integer limit
    ) {
        log.info("Получение задачи по автору, метод GET " + author);
        Optional<List<TasksDto>> optionalAuthorsTasksDtoList = serviceFabric
                .createTaskService()
                .getTasksOfAuthorOrExecutor(author, offset, limit,
                        ConstantsClass.ONE_FLAG);
        if (optionalAuthorsTasksDtoList.isPresent()) {
            return ResponseEntity.ok(optionalAuthorsTasksDtoList.get());
        }
        return ResponseEntity.badRequest().build();
    }

    /**
     * Эндпоинд GET для получения информации о задачах по исполнителю
     *
     * @param executorEmail - емейл пользователя, по которому будут найдены задачи, комментарии
     * @param offset        - номер страницы
     * @param limit         - кол-во сущностей на странице
     * @return {@link ResponseEntity<List<TasksDto>>}
     */
    @Operation(summary = "Получить задачу по исполнителю")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Возвращены задачи с комментариями по исполнителю", content = @Content(examples = @ExampleObject(value = "\"[{\\\"id\\\":2,\\\"header\\\":\\\"Test Task 2\\\",\\\"taskAuthor\\\":{\\\"id\\\":2,\\\"email\\\":\\\"example2@gmail.com\\\"},\\\"taskExecutor\\\":{\\\"id\\\":2,\\\"email\\\":\\\"example2@gmail.com\\\"},\\\"description\\\":\\\"Another test task description\\\",\\\"taskPriority\\\":\\\"HIGH\\\",\\\"taskStatus\\\":\\\"IN_PROGRESS\\\",\\\"notesDto\\\":[{\\\"id\\\":2,\\\"notesAuthor\\\":{\\\"id\\\":1,\\\"email\\\":\\\"example@gmail.com\\\"},\\\"comments\\\":\\\"Another comment for Task 2\\\"}]}]\""))),
            @ApiResponse(responseCode = "400", description = "Не удалось получить информацию о задачах", content = @Content),
            @ApiResponse(responseCode = "403", description = "Не авторизован/Недостаточно прав", content = @Content),
    })
    @FilterResponse(filterName = GET_TASKEXECUTOR_TASKS)
    @SecurityRequirement(name = "JWT")
    @GetMapping(value = "/task/gen-info/executor/{executorEmail}", consumes = MediaType.ALL_VALUE)
    public ResponseEntity<List<TasksDto>> getTaskExecutor(
            @PathVariable("executorEmail") @Parameter(description = "Исполнитель задачи") @NotBlank(message = "Исполнитель не может быть пустым") String executorEmail,
            @RequestParam(value = "offset", defaultValue = "0") @Min(0) @Parameter(description = "Номер страницы", example = "0") @NotNull(message = "Страница не может быть пустой")
            Integer offset,
            @RequestParam(value = "limit", defaultValue = "10") @Min(10) @Parameter(description = "Количество сущностей на странице",
                    example = "10") @NotNull(message = "Кол-во сущностей не может быть пустым") Integer limit
    ) {
        log.info("Получение задачи по исполнителю, метод GET " + executorEmail);
        Optional<List<TasksDto>> optionalExecutorTasksDtoList = serviceFabric
                .createTaskService()
                .getTasksOfAuthorOrExecutor(executorEmail, offset, limit, ConstantsClass.ZERO_FLAG);
        if (optionalExecutorTasksDtoList.isPresent()) {
            return ResponseEntity.ok(optionalExecutorTasksDtoList.get());
        }
        return ResponseEntity.badRequest().build();
    }

    /**
     * Эндпоинд PUT для редактирования задач
     *
     * @param tasksDto объект {@link TasksDto}
     * @return {@link ResponseEntity<TasksDto>}
     * @throws MainException
     */
    @Operation(summary = "Отредактировать задачу", description = "Отредактировать задачу, в т.ч добавить комментарий")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Задача успешно отредактирована", content = @Content(examples = @ExampleObject(value = "{\n  \"header\": \"Тестовая задача\",\n  \"taskAuthor\": {\n    \"id\": 1,\n    \"email\": \"author@example.com\"\n  },\n  \"taskExecutor\": {\n    \"id\": 2,\n    \"email\": \"executor@example.com\"\n  },\n  \"description\": \"Тестовое описание задачи\",\n  \"taskPriority\": \"MEDIUM\",\n  \"taskStatus\": \"BACKLOG\",\n  \"notesDto\": [\n    {\n      \"id\": 1,\n      \"notesAuthor\": {\n        \"id\": 3,\n        \"email\": \"commenter@example.com\"\n      },\n      \"comments\": \"Обсуждение\"\n    }\n  ]\n}"))),
            @ApiResponse(responseCode = "403", description = "Для редактирования недостаточно прав на сущность/Не авторизован/Недостаточно прав", content = @Content),
            @ApiResponse(responseCode = "400", description = "Не удалось отредактировать задачу", content = @Content),
            @ApiResponse(responseCode = "404", description = "У задачи отсутствует исполнитель", content = @Content)
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Пример тела запроса для редактирования задачи", content = @Content(mediaType = "application/json", examples = @ExampleObject(name = "Редактирование задачи", value = "\"{\\n  \\\"id\\\": 1,\\n  \\\"header\\\": \\\"Тестовая задача\\\",\\n  \\\"taskAuthor\\\": {\\n    \\\"id\\\": 1,\\n    \\\"email\\\": \\\"example@gmail.com\\\"\\n  },\\n  \\\"taskExecutor\\\": {\\n    \\\"id\\\": 1,\\n    \\\"email\\\": \\\"example@gmail.com\\\"\\n  },\\n  \\\"description\\\": \\\"Тестовое описание задачи\\\",\\n  \\\"taskPriority\\\": \\\"MEDIUM\\\",\\n  \\\"taskStatus\\\": \\\"BACKLOG\\\"\\n}\"")))
    @FilterResponse(filterName = PUT_EDIT_TASKS)
    @SecurityRequirement(name = "JWT")
    @PutMapping("/task/update-tasks")
    public ResponseEntity<TasksDto> editTasks(@Valid @RequestBody @Parameter(description = "Объект TasksDto с полями, требующими редактирования")
                                              @NotNull(message = "TasksDto не может быть null") TasksDto tasksDto) throws MainException {
        Optional<TasksDto> newTasksDto = serviceFabric
                .createTaskService()
                .changeTasks(tasksDto);
        if (newTasksDto.isPresent()) {
            return ResponseEntity.ok(newTasksDto.get());
        }
        return ResponseEntity.badRequest().build();
    }

    /**
     * Эндпоинд DELETE для удаления задач
     *
     * @param idTasks - id задачи для удаления
     * @return {@link ResponseEntity<String>}
     */
    @Operation(summary = "Удалить задачу")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Не удалось произвести удаление задачи", content = @Content),
            @ApiResponse(responseCode = "403", description = "Не авторизован/Недостаточно прав"),
    })
    @SecurityRequirement(name = "JWT")
    @DeleteMapping(value = "/task/delete/{id}", consumes = MediaType.ALL_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> deleteTasks(@PathVariable("id") @Parameter(description = "ID задачи") @NotNull(message = "ID задачи не может быть null")
                                                  Integer idTasks) {
        log.info("Удаление задачи по id, метод DELETE" + idTasks);
        boolean resultDeleteTasks = serviceFabric
                .createTaskService()
                .deleteTasks(idTasks);
        if (resultDeleteTasks) {
            return ResponseEntity.ok(ConstantsClass.IS_DELETE);
        }
        return ResponseEntity.notFound().build();
    }
}
