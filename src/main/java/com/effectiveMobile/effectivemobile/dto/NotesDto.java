package com.effectiveMobile.effectivemobile.dto;

import com.effectiveMobile.effectivemobile.entities.Tasks;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Schema(description = "Сущность комментарий")
@Validated
public class NotesDto implements Serializable {

    @Schema(description = "Уникальный идентификатор комментария", example = "1")
    private int id;

    private CustomUsersDto usersDto;

    @Schema(description = "Комментарий", example = "Обсуждение")
    @NotBlank(message = "Комментарий не может быть пуст")
    private String comments;

    private TasksDto task;

}
