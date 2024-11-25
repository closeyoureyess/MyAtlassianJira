package com.effectiveMobile.effectivemobile.dto;

import com.effectiveMobile.effectivemobile.other.TaskPriorityEnum;
import com.effectiveMobile.effectivemobile.other.TaskStatusEnum;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import java.io.Serializable;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
@Schema(description = "Сущность задача")
@JsonFilter("TasksDtoFilter")
@Validated
public class TasksDto implements Serializable {

    @Schema(description = "Уникальный идентификатор задачи", example = "1")
    @Min(value = 1, message = "Идентификатор задачи не должен быть меньше единицы")
    @Max(value = 2147483647, message = "Идентификатор задачи не должен быть больше 2147483647")
    private Integer id;

    @Schema(description = "Заголовок задачи", example = "Тестовая задача")
    @Size(min = 1, message = "Длина заголовка не должна быть меньше 1 символа")
    @Size(max = 1000, message = "Длина заголовка должна быть не более 1000 символов")
    private String header;

    private CustomUsersDto taskAuthor;

    private CustomUsersDto taskExecutor;

    @Schema(description = "Описание задачи", example = "Тестовое описание задачи")
    @Size(max = 10000, message = "Длина описания не должна превышать 10000 символов")
    private String description;

    @Schema(description = "Приоритет по задаче", example = "MEDIUM")
    private TaskPriorityEnum taskPriority;

    @Schema(description = "Статус задачи", example = "BACKLOG")
    private TaskStatusEnum taskStatus;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<NotesDto> notesDto;
}
