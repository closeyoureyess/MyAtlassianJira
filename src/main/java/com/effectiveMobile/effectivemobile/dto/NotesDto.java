package com.effectiveMobile.effectivemobile.dto;

import com.fasterxml.jackson.annotation.JsonFilter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Schema(description = "Сущность комментарий")
@Builder
@Validated
@JsonFilter("NotesDtoFilter")
public class NotesDto implements Serializable {

    @Schema(description = "Уникальный идентификатор комментария", example = "1")
    @Min(value = 1, message = "Идентификатор комментария не должен быть меньше единицы")
    @Max(value = 2147483647, message = "Идентификатор комментария не должен быть больше 2147483647")
    private Integer id;

    private CustomUsersDto notesAuthor;

    @Schema(description = "Комментарий", example = "Обсуждение")
    @NotBlank(message = "Комментарий не может быть пуст")
    @Size(min = 1, message = "Длина комментария не должна быть меньше 1 символа")
    @Size(max = 20000, message = "Длина комментария должна быть не более 20000 символов")
    private String comments;

    @Schema(description = "Комментируемая задача")
    private TasksDto task;

}
