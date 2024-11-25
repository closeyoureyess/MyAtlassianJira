package com.effectiveMobile.effectivemobile.dto;

import com.effectiveMobile.effectivemobile.other.DefaultSettingsFieldNameEnum;
import com.effectiveMobile.effectivemobile.other.TaskPriorityEnum;
import com.effectiveMobile.effectivemobile.other.TaskStatusEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Schema(description = "Сущность настроек по умолчанию")
@Validated
public class DefaultSettingsDto implements Serializable {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Уникальный идентификатор настройки", example = "1")
    @Min(value = 1, message = "Идентификатор настройки не должен быть меньше единицы")
    @Max(value = 2147483647, message = "Идентификатор задачи не должен быть больше 2147483647")
    private Integer id;

    @Schema(description = "Наименование поля", example = "TASK_PRIORITY")
    @NotNull(message = "Наименование поля не должно быть пустым")
    private DefaultSettingsFieldNameEnum fieldName;

    @Schema(description = "Статус задачи", example = "BACKLOG")
    private TaskStatusEnum defaultTaskStatus;

    @Schema(description = "Приоритет по задаче", example = "MEDIUM")
    private TaskPriorityEnum defaultTaskPriority;

}
