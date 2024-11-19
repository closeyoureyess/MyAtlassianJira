package com.effectiveMobile.effectivemobile.auxiliaryclasses;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.Objects;

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
@Schema(description = "Форма авторизации пользователя")
public class LoginForm {

    @Schema(description = "E-mail адрес пользователя", example = "example@example.com")
    private String email;

    @Schema(description = "Пароль пользователя", example = "12345")
    private String passwordKey;

}
