package com.effectiveMobile.effectivemobile.auxiliaryclasses;

import java.util.Optional;

/**
 * <pre>
 *     Интерфейс для проверки валидности объекта
 * </pre>
 */
public interface ValidationClass {

    /**
     * Метод, проверящий, является ли переданная строка email или id
     *
     * @param line Строка для проверки, может содержать email или числовой идентификатор.
     * @return {@link Optional<ValidationClassImpl>} с результатами проверки: email или ID.
     */
    Optional<ValidationClassImpl> validEmailOrId(String line);
}
