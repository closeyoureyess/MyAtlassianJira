package com.effectiveMobile.effectivemobile.exeptions;

/**
 * <pre>
 *     Ошибка, сообщающая о том, что для работы с сущностью недостаточно прав
 * </pre>
 */
public class NotEnoughRulesForEntity extends MainException {

    public NotEnoughRulesForEntity(String message){
        super(message);
    }

    public NotEnoughRulesForEntity(String message, Throwable cause) {
        super(message, cause);
    }
}
