package com.effectiveMobile.effectivemobile.dto;

import com.effectiveMobile.effectivemobile.other.TaskPriorityEnum;
import com.effectiveMobile.effectivemobile.other.TaskStatusEnum;
import com.effectiveMobile.effectivemobile.other.Views;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.boot.jackson.JsonMixin;

import java.io.Serializable;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Schema(description = "Сущность задача")
@JsonFilter("TasksDtoFilter")
public class TasksDto implements Serializable {

    @Schema(description = "Уникальный идентификатор задачи", example = "1")
    private Integer id;

    @Schema(description = "Заголовок задачи", example = "Тестовая задача")
    private String header;

    private CustomUsersDto taskAuthor;

    private CustomUsersDto taskExecutor;

    @Schema(description = "Описание задачи", example = "Тестовое описание задачи")
    private String description;

    @Schema(description = "Приоритет по задаче", example = "MEDIUM")
    private TaskPriorityEnum taskPriority;

    @Schema(description = "Статус задачи", example = "BACKLOG")
    private TaskStatusEnum taskStatus;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<NotesDto> notesDto;
}
