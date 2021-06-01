package com.my.web.exception;

/**
 * An application exception that provides information about cause to the user
 */
public class ApplicationException extends Exception{

    private static final long serialVersionUID = -1293492384900123199L;

    public ApplicationException() {
    }

    public ApplicationException(String message) {
        super(message);
    }

    public ApplicationException(String message, Throwable cause) {
        super(message, cause);
    }

}
