package com.effectiveMobile.effectivemobile.dto;

import com.effectiveMobile.effectivemobile.other.UserRoles;
import com.effectiveMobile.effectivemobile.other.Views;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import java.io.Serializable;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Validated
@EqualsAndHashCode
@JsonFilter("CustomUsersDtoFilter")
public class CustomUsersDto implements Serializable {

    @Schema(description = "Уникальный идентификатор пользователя", example = "1")
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
