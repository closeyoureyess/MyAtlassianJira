package com.effectiveMobile.effectivemobile.dto;

import com.effectiveMobile.effectivemobile.other.UserRoles;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
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
@Schema(description = "Сущность пользователь")
@JsonFilter("CustomUsersDtoFilter")
public class CustomUsersDto implements Serializable {

    @Schema(description = "Уникальный идентификатор пользователя", example = "1")
    @Size(min = 3, message = "Длина строки не должна быть меньше трех символов")
    @Size(max = 3500, message = "Длина строки не должна превышать 3500 символов.")
    private Integer id;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Schema(description = "Пароль пользователя", example = "12345")
    private String passwordKey;

    @Schema(description = "E-mail пользователя", example = "example@gmail.com")
    private String email;

    @NotBlank(message = "Роль не может быть пустой")
    @Schema(description = "Роль пользователя", example = "USER")
    private UserRoles role;

}
