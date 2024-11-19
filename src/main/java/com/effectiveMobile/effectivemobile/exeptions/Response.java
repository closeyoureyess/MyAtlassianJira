package com.effectiveMobile.effectivemobile.exeptions;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <pre>
 *     Класс-обертка для глобального контроллера, обрабатывающего все ошибки в приложении
 * </pre>
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Response {

    private String responseExeption;
}
