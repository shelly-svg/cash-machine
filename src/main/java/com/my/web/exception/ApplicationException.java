package com.my.web.exception;

/**
 * An application exception that provides information about cause to the user
 */
public class ApplicationException extends Exception{

    public ApplicationException() {
    }

    public ApplicationException(String message) {
        super(message);
    }

    public ApplicationException(String message, Throwable cause) {
        super(message, cause);
    }

}
