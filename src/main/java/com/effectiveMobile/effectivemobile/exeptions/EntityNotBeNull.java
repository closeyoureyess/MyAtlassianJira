package com.effectiveMobile.effectivemobile.exeptions;

public class EntityNotBeNull extends MainException{
    public EntityNotBeNull(String message) {
        super(message);
    }

    public EntityNotBeNull(String message, Throwable cause) {
        super(message, cause);
    }
}
