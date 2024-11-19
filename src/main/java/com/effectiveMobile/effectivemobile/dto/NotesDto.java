package com.effectiveMobile.effectivemobile.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Schema(description = "Сущность комментарий")
public class NotesDto implements Serializable {

    @Schema(description = "Уникальный идентификатор комментария", example = "1")
    private int id;

    private CustomUsersDto usersDto;

    @Schema(description = "Комментарий", example = "Обсуждение")
    private String comments;

}
