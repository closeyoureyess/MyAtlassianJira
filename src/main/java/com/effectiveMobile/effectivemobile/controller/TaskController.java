package com.effectiveMobile.effectivemobile.controller;

import com.effectiveMobile.effectivemobile.constants.ConstantsClass;
import com.effectiveMobile.effectivemobile.exeptions.MainException;
import com.effectiveMobile.effectivemobile.dto.TasksDto;
import com.effectiveMobile.effectivemobile.other.Views;
import com.effectiveMobile.effectivemobile.services.TaskService;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
    private TaskService taskService;

    /**
     * Эндпоинд POST для создания задачи
     * @param tasksDto
     * @throws MainException
     * @return {@link ResponseEntity<TasksDto>}
     */
    @Operation(summary = "Создание задачи", description = "Позволяет создать задачу")
    @PostMapping(value = "/task/create")
    @JsonView(Views.Public.class)
    public ResponseEntity<TasksDto> createTask(@RequestBody TasksDto tasksDto) throws MainException {
        log.info("Создание задачи, POST " + tasksDto.getHeader());
        TasksDto localTasksDto = taskService.createTasks(tasksDto);
        if (localTasksDto != null) {
            return ResponseEntity.ok(localTasksDto);
        }
        return ResponseEntity.badRequest().build();
    }

    /**
     * Эндпоинд GET для получения информации о задачах по автору
     * @param author
     * @param offset
     * @param limit
     * @return {@link ResponseEntity<List<TasksDto>>}
     */
    @Operation(summary = "Получить задачу по автору")
    @GetMapping("/task/gen-info/author/{author}")
    @JsonView(Views.Public.class)
    public ResponseEntity<List<TasksDto>> getTaskAuthor(
            @PathVariable("author") @Parameter(description = "Автор задачи", example = "Иван") String author,
            @RequestParam(value = "offset", defaultValue = "0") @Min(0) @Parameter(description = "Номер страницы", example = "0")
            Integer offset,
            @RequestParam(value = "limit", defaultValue = "10") @Min(10) @Parameter(description = "Количество сущностей на странице",
                    example = "10") Integer limit
    ) {
        log.info("Получение задачи по автору, метод GET " + author);
        Optional<List<TasksDto>> optionalAuthorsTasksDtoList = taskService.getTasksOfAuthorOrExecutor(author, offset, limit,
                ConstantsClass.REGIME_RECORD);
        if (optionalAuthorsTasksDtoList.isPresent()) {
            return ResponseEntity.ok(optionalAuthorsTasksDtoList.get());
        }
        return ResponseEntity.badRequest().build();
    }

    /**
     * Эндпоинд GET для получения информации о задачах по исполнителю
     * @param executorEmail
     * @param offset
     * @param limit
     * @return {@link ResponseEntity<List<TasksDto>>}
     */
    @Operation(summary = "Получить задачу по исполнителю")
    @GetMapping("/task/gen-info/executor/{executorEmail}")
    @JsonView(Views.Public.class)
    public ResponseEntity<List<TasksDto>> getTaskExecutor(
            @PathVariable("executorEmail") @Parameter(description = "Исполнитель задачи") String executorEmail,
            @RequestParam(value = "offset", defaultValue = "0") @Min(0) @Parameter(description = "Номер страницы", example = "0")
            Integer offset,
            @RequestParam(value = "limit", defaultValue = "10") @Min(10) @Parameter(description = "Количество сущностей на странице",
                    example = "10") Integer limit
    ) {
        log.info("Получение задачи по исполнителю, метод GET " + executorEmail);
        Optional<List<TasksDto>> optionalExecutorTasksDtoList = taskService.getTasksOfAuthorOrExecutor(executorEmail, offset, limit,
                ConstantsClass.REGIME_OVERWRITING);
        if (optionalExecutorTasksDtoList.isPresent()) {
            return ResponseEntity.ok(optionalExecutorTasksDtoList.get());
        }
        return ResponseEntity.badRequest().build();
    }

    /**
     * Эндпоинд PUT для редактирования задач
     * @param tasksDto
     * @return {@link ResponseEntity<TasksDto>}
     * @throws MainException
     */
    @Operation(summary = "Отредактировать задачу", description = "Отредактировать задачу, в т.ч добавить комментарий")
    @PutMapping("/task/update-tasks")
    @JsonView(Views.Public.class)
    public ResponseEntity<TasksDto> editTasks(@RequestBody @Parameter(description = "Объект TasksDto с полями, требующими редактирования")
                                                  TasksDto tasksDto) throws MainException {
        Optional<TasksDto> newTasksDto = taskService.changeTasks(tasksDto);
        if (newTasksDto.isPresent()) {
            return ResponseEntity.ok(newTasksDto.get());
        }
        return ResponseEntity.badRequest().build();
    }

    /**
     * Эндпоинд DELETE для удаления задач
     * @param idTasks
     * @return {@link ResponseEntity<String>}
     */
    @Operation(summary = "Удалить задачу")
    @DeleteMapping("/task/delete/{id}")
    public ResponseEntity<String> deleteCase(@PathVariable("id") @Parameter(description = "ID задачи")
                                                 Integer idTasks) {
        log.info("Удаление задачи по id, метод DELETE" + idTasks);
        boolean resultDeleteTasks = taskService.deleteTasks(idTasks);
        if (resultDeleteTasks) {
            return ResponseEntity.ok(ConstantsClass.IS_DELETE);
        }
        return ResponseEntity.notFound().build();
    }
}
