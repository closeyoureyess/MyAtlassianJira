package com.effectiveMobile.effectivemobile.exeptions;

public class RoleNotFoundException extends MainException{
    public RoleNotFoundException(String message) {
        super(message);
    }

    public RoleNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
