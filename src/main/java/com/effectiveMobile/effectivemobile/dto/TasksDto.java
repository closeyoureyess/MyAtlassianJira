package com.effectiveMobile.effectivemobile.dto;

import com.effectiveMobile.effectivemobile.other.TaskPriorityEnum;
import com.effectiveMobile.effectivemobile.other.TaskStatusEnum;
import com.effectiveMobile.effectivemobile.other.Views;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Schema(description = "Сущность задача")
public class TasksDto implements Serializable {

    @Schema(description = "Уникальный идентификатор задачи", example = "1")
    @JsonView(Views.Public.class)
    private Integer id;

    @Schema(description = "Заголовок задачи", example = "Тестовая задача")
    @JsonView(Views.Public.class)
    private String header;

    @JsonView(Views.Public.class)
    private CustomUsersDto taskAuthor;

    @JsonView(Views.Public.class)
    private CustomUsersDto taskExecutor;

    @Schema(description = "Описание задачи", example = "Тестовое описание задачи")
    @JsonView(Views.Public.class)
    private String description;

    @Schema(description = "Приоритет по задаче", example = "MEDIUM")
    @JsonView(Views.Public.class)
    private TaskPriorityEnum taskPriority;

    @Schema(description = "Статус задачи", example = "BACKLOG")
    @JsonView(Views.Public.class)
    private TaskStatusEnum taskStatus;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonView(Views.Internal.class)
    private List<NotesDto> notesDto;
}
