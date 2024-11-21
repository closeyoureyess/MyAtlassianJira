package com.effectiveMobile.effectivemobile.exeptions;

public class MainException extends Exception{

    public MainException(String message){
        super(message);
    }

    public MainException(String message, Throwable cause) {
        super(message, cause);
    }
}
