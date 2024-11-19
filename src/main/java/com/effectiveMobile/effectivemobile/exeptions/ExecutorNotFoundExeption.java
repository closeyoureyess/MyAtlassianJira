package com.effectiveMobile.effectivemobile.exeptions;

/**
 * <pre>
 *     Ошибка, сообщающая о том, что исполнитель не найден
 * </pre>
 */
public class ExecutorNotFoundExeption extends MainException{

    public ExecutorNotFoundExeption(String message){
        super(message);
    }
}
