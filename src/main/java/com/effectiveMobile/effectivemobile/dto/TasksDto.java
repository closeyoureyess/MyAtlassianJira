package com.effectiveMobile.effectivemobile.dto;

import com.effectiveMobile.effectivemobile.other.TaskPriorityEnum;
import com.effectiveMobile.effectivemobile.other.TaskStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Schema(description = "Сущность задача")
public class TasksDto implements Serializable {

    @Schema(description = "Уникальный идентификатор пользователя", example = "1")
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

    /*@Schema(description = "Комментарий к задаче", example = "Обсуждение")*/
    private NotesDto notesDto;
}
