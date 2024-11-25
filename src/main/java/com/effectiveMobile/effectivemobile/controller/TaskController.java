package com.effectiveMobile.effectivemobile.controller;

import com.effectiveMobile.effectivemobile.annotations.FilterResponse;
import com.effectiveMobile.effectivemobile.constants.ConstantsClass;
import com.effectiveMobile.effectivemobile.dto.TasksDto;
import com.effectiveMobile.effectivemobile.exeptions.MainException;
import com.effectiveMobile.effectivemobile.fabrics.ServiceFabric;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Пример тела запроса для создания задачи", content = @Content(mediaType = "application/json", examples = @ExampleObject(name = "Создание задачи", value = "\"{\\n  \\\"id\\\": 1,\\n  \\\"header\\\": \\\"Тестовая задача\\\",\\n  \\\"taskExecutor\\\": {\\n  \\\"email\\\": \\\"example2@gmail.com\\\"\\n  },\\n  \\\"description\\\": \\\"Тестовое описание задачи\\\",\\n  \\\"taskPriority\\\": \\\"MEDIUM\\\",\\n  \\\"taskStatus\\\": \\\"BACKLOG\\\"\\n}\"")))
    @FilterResponse(filterName = POST_CREATE_TASKS)
    @SecurityRequirement(name = "JWT")
    @PostMapping(value = "/task/create")
    public ResponseEntity<TasksDto> createTask(@RequestBody TasksDto tasksDto) throws MainException {
        log.info("Создание задачи, POST " + tasksDto.getHeader());
        TasksDto localTasksDto = serviceFabric.createTaskService().createTasks(tasksDto);
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
     * @param limit Количество сущностей на странице (10 - по умолчанию)
     * @return {@link ResponseEntity} с задачами по автору
     */
    @Operation(summary = "Получить задачу по автору")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Возвращены задачи с комментариями по автору"),
            @ApiResponse(responseCode = "400", description = "Не удалось получить информацию о задачах", content = @Content),
            @ApiResponse(responseCode = "403", description = "Не авторизован/Недостаточно прав", content = @Content),
    })
    @FilterResponse(filterName = GET_TASKAUTHOR_TASKS)
    @SecurityRequirement(name = "JWT")
    @GetMapping(value = "/task/gen-info/author/{author}", consumes = MediaType.ALL_VALUE)
    public ResponseEntity<List<TasksDto>> getTaskAuthor(
            @PathVariable("author") @Parameter(description = "Автор задачи", example = "example@gmail.com") String author,
            @RequestParam(value = "offset", defaultValue = "0") @Min(0) @Parameter(description = "Номер страницы", example = "0")
            Integer offset,
            @RequestParam(value = "limit", defaultValue = "10") @Min(10) @Parameter(description = "Количество сущностей на странице",
                    example = "10") Integer limit
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
     * @param offset - номер страницы
     * @param limit - кол-во сущностей на странице
     * @return {@link ResponseEntity<List<TasksDto>>}
     */
    @Operation(summary = "Получить задачу по исполнителю")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Возвращены задачи с комментариями по исполнителю"),
            @ApiResponse(responseCode = "400", description = "Не удалось получить информацию о задачах", content = @Content),
            @ApiResponse(responseCode = "403", description = "Не авторизован/Недостаточно прав", content = @Content),
    })
    @FilterResponse(filterName = GET_TASKEXECUTOR_TASKS)
    @SecurityRequirement(name = "JWT")
    @GetMapping("/task/gen-info/executor/{executorEmail}")
    public ResponseEntity<List<TasksDto>> getTaskExecutor(
            @PathVariable("executorEmail") @Parameter(description = "Исполнитель задачи") String executorEmail,
            @RequestParam(value = "offset", defaultValue = "0") @Min(0) @Parameter(description = "Номер страницы", example = "0")
            Integer offset,
            @RequestParam(value = "limit", defaultValue = "10") @Min(10) @Parameter(description = "Количество сущностей на странице",
                    example = "10") Integer limit
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
            @ApiResponse(responseCode = "200", description = "Задача успешно отредактирована", content = @Content(examples = @ExampleObject(value = "\"{\\n  \\\"id\\\": 1,\\n  \\\"header\\\": \\\"Тестовая задача\\\",\\n  \\\"taskAuthor\\\": {\\n    \\\"id\\\": 1,\\n    \\\"email\\\": \\\"example@gmail.com\\\"\\n  },\\n  \\\"taskExecutor\\\": {\\n    \\\"id\\\": 1,\\n    \\\"email\\\": \\\"example@gmail.com\\\"\\n  },\\n  \\\"description\\\": \\\"Тестовое описание задачи\\\",\\n  \\\"taskPriority\\\": \\\"MEDIUM\\\",\\n  \\\"taskStatus\\\": \\\"BACKLOG\\\",\\n  \\\"notesDto\\\": [\\n    {\\n      \\\"id\\\": 1,\\n      \\\"header\\\": \\\"Тестовая задача\\\",\\n      \\\"taskAuthor\\\": {\\n        \\\"id\\\": 1,\\n        \\\"email\\\": \\\"example@gmail.com\\\"\\n      }\\n    }\\n  ]\\n}\""))),
            @ApiResponse(responseCode = "403", description = "Для редактирования недостаточно прав на сущность/Не авторизован/Недостаточно прав", content = @Content),
            @ApiResponse(responseCode = "400", description = "Не удалось отредактировать задачу", content = @Content),
            @ApiResponse(responseCode = "404", description = "У задачи отсутствует исполнитель", content = @Content)
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Пример тела запроса для редактирования задачи", content = @Content(mediaType = "application/json", examples = @ExampleObject(name = "Редактирование задачи", value = "\"{\\n  \\\"id\\\": 1,\\n  \\\"header\\\": \\\"Тестовая задача\\\",\\n  \\\"taskAuthor\\\": {\\n    \\\"id\\\": 1,\\n    \\\"email\\\": \\\"example@gmail.com\\\"\\n  },\\n  \\\"taskExecutor\\\": {\\n    \\\"id\\\": 1,\\n    \\\"email\\\": \\\"example@gmail.com\\\"\\n  },\\n  \\\"description\\\": \\\"Тестовое описание задачи\\\",\\n  \\\"taskPriority\\\": \\\"MEDIUM\\\",\\n  \\\"taskStatus\\\": \\\"BACKLOG\\\"\\n}\"")))
    @FilterResponse(filterName = PUT_EDIT_TASKS)
    @SecurityRequirement(name = "JWT")
    @PutMapping("/task/update-tasks")
    public ResponseEntity<TasksDto> editTasks(@RequestBody @Parameter(description = "Объект TasksDto с полями, требующими редактирования")
                                              TasksDto tasksDto) throws MainException {
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
    @DeleteMapping("/task/delete/{id}")
    public ResponseEntity<String> deleteTasks(@PathVariable("id") @Parameter(description = "ID задачи") Integer idTasks) {
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
