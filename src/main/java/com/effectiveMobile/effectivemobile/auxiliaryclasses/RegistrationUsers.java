package com.effectiveMobile.effectivemobile.auxiliaryclasses;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

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
public class RegistrationUsers {

    @Schema(description = "E-mail адрес пользователя", example = "example@gmail.com")
    private String email;

    @Schema(description = "Пароль пользователя", example = "12345")
    private String passwordKey;

}
