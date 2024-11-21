package com.effectiveMobile.effectivemobile.controller;

import com.effectiveMobile.effectivemobile.dto.NotesDto;
import com.effectiveMobile.effectivemobile.dto.TasksDto;
import com.effectiveMobile.effectivemobile.exeptions.MainException;
import com.effectiveMobile.effectivemobile.fabrics.ServiceFabric;
import com.effectiveMobile.effectivemobile.other.Views;
import com.effectiveMobile.effectivemobile.services.NotesService;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Комментарии", description = "Позволяет создать комментарий для задачи")
@RestController
@RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class NotesController {

    @Autowired
    private ServiceFabric serviceFabric;

    @Operation(summary = "Создание комментария", description = "Позволяет создать комментарий и привязать его к задаче")
    @SecurityRequirement(name = "JWT")
    @PostMapping(value = "/notes/create")
    @JsonView(Views.Public.class)
    public ResponseEntity<NotesDto> createTask(@RequestBody NotesDto notesDto) {
        log.info("Создание комментария, POST " + notesDto.getComments());
        NotesDto localNotesDto = serviceFabric.createNotesService().createNotes(notesDto);
        if (localNotesDto != null) {
            return ResponseEntity.ok(localNotesDto);
        }
        return ResponseEntity.badRequest().build();
    }

}
