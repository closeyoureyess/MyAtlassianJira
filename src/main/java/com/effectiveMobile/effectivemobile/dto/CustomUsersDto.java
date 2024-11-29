package com.effectiveMobile.effectivemobile.dto;

import com.effectiveMobile.effectivemobile.other.UserRoles;
import com.fasterxml.jackson.annotation.JsonFilter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import java.io.Serializable;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Validated
@EqualsAndHashCode
@Builder
@Schema(description = "Сущность пользователь")
@JsonFilter("CustomUsersDtoFilter")
public class CustomUsersDto implements Serializable {

    @Schema(description = "Уникальный идентификатор пользователя", example = "1")
    @Min(value = 1, message = "Идентификатор настройки не должен быть меньше единицы")
    @Max(value = 2147483647, message = "Идентификатор задачи не должен быть больше 2147483647")
    private Integer id;

    @Schema(description = "Пароль пользователя", example = "12345")
    @Size(min = 1, message = "Длина пароля не должна быть меньше 1 символа")
    @Size(max = 120, message = "Длина пароля не должна больше 120 символов")
    private String passwordKey;

    @Schema(description = "E-mail пользователя", example = "example@gmail.com")
    @Size(min = 3, message = "Длина e-mail не должна быть меньше 3 символов")
    @Size(max = 500, message = "Длина e-mail не должна быть больше 500 символов")
    private String email;

    @NotNull(message = "Роль не может быть пустой")
    @Schema(description = "Роль пользователя", example = "USER")
    private UserRoles role;

}
