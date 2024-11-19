package com.effectiveMobile.effectivemobile.exeptions;

/**
 * <pre>
 *     Ошибка, сообщающая о том, что для работы с сущностью недостаточно прав
 * </pre>
 */
public class NotEnoughRulesEntity extends MainException {

    public NotEnoughRulesEntity(String message){
        super(message);
    }
}
