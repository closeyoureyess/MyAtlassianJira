package com.effectiveMobile.effectivemobile.exeptions;

public class IncorrectTypeParameterException extends MainException {
    public IncorrectTypeParameterException(String message) {
        super(message);
    }

    public IncorrectTypeParameterException(String message, Throwable cause) {
        super(message, cause);
    }
}
