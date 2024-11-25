package com.effectiveMobile.effectivemobile.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.validation.annotation.Validated;

/**
 * <pre>
 *     Класс, используемый Spring Security для авторизации пользователя
 * </pre>
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Schema(description = "Форма авторизации/регистрации пользователя")
@Validated
public class RegistrationUsers {

    @Schema(description = "E-mail адрес пользователя", example = "example@gmail.com")
    @NotBlank(message = "E-mail не может быть пуст")
    private String email;

    @Schema(description = "Пароль пользователя", example = "12345")
    @NotBlank(message = "Поле с паролем не может быть пустым")
    private String passwordKey;

}
