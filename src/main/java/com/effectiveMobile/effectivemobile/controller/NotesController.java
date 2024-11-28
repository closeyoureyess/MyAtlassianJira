package com.effectiveMobile.effectivemobile.controller;

import com.effectiveMobile.effectivemobile.annotations.FilterResponse;
import com.effectiveMobile.effectivemobile.dto.NotesDto;
import com.effectiveMobile.effectivemobile.exeptions.MainException;
import com.effectiveMobile.effectivemobile.fabrics.ServiceFabric;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.effectiveMobile.effectivemobile.constants.ConstantsClass.POST_CREATE_NOTES;

/**
 * <pre>
 *     Контроллер, обрабатывающий все эксепшены, котоыре могут быть выброшены в процессе работы приложения
 * </pre>
 */
@Tag(name = "Комментарии", description = "Позволяет создать комментарий для задачи")
@RestController
@RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@Validated
public class NotesController {

    @Autowired
    private ServiceFabric serviceFabric;

    @Operation(summary = "Создание комментария", description = "Позволяет создать комментарий и привязать его к задаче")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Комментарий успешно создан", content = @Content(examples = @ExampleObject(value = "\"{\\n  \\\"id\\\": 1,\\n  \\\"usersDto\\\": {\\n    \\\"id\\\": 2,\\n    \\\"email\\\": \\\"example2@gmail.com\\\"\\n  },\\n  \\\"comments\\\": \\\"Обсуждение\\\",\\n  \\\"task\\\": {\\n    \\\"id\\\": 1\\n  }\\n}\""))),
            @ApiResponse(responseCode = "400", description = "Передаваемый идентификатор задачи является null/Передаваемый объект Tasks является null", content = @Content),
            @ApiResponse(responseCode = "403", description = "Не авторизован/Недостаточно прав", content = @Content),
            @ApiResponse(responseCode = "404", description = "Задача по заданному идентификатору не найдена", content = @Content)
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Пример тела запроса для создания комментария",
            content = @Content(mediaType = "application/json", examples = @ExampleObject(name = "Создание комментария", value = "\"{\\n  \\\"id\\\": 1,\\n \\\"comments\\\": \\\"Обсуждение\\\",\\n  \\\"task\\\": {\\n    \\\"id\\\": 1\\n  }\\n}\"")))
    @FilterResponse(filterName = POST_CREATE_NOTES)
    @SecurityRequirement(name = "JWT")
    @PostMapping(value = "/notes/create")
    public ResponseEntity<NotesDto> createNotes(@Valid @RequestBody @NotNull(message = "Комментарий не может быть null")
                                                    NotesDto notesDto) throws MainException {
        log.info("Создание комментария, POST " + notesDto.getComments());
        NotesDto localNotesDto = serviceFabric.createNotesService().createNotes(notesDto);
        if (localNotesDto != null) {
            return ResponseEntity.ok(localNotesDto);
        }
        return ResponseEntity.badRequest().build();
    }
}