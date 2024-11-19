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
     * @param line
     * @return {@link ValidationClassImpl} с email, либо id
     */
    Optional<ValidationClassImpl> validEmailOrId(String line);
}
