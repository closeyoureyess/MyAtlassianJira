package com.effectiveMobile.effectivemobile.exeptions;

/**
 * <pre>
 *     Ошибка, сообщающая о том, что сущность не найдена
 * </pre>
 */
public class EntityNotFoundExeption extends MainException{

    public EntityNotFoundExeption(String message){
        super(message);
    }

}
