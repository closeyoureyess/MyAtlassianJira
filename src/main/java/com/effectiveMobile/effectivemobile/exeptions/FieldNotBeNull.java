package com.effectiveMobile.effectivemobile.exeptions;

public class FieldNotBeNull extends MainException{
    public FieldNotBeNull(String message) {
        super(message);
    }

    public FieldNotBeNull(String message, Throwable cause) {
        super(message, cause);
    }
}
