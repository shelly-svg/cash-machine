package com.my.web.exception;

import java.sql.SQLException;

/**
 * An exception that throws from db entities & DAO classes
 *
 */
public class DBException extends SQLException {

    private static final long serialVersionUID = -3289120349923004221L;

    public DBException() {
    }

    public DBException(Throwable cause) {
        super(cause);
    }

    public DBException(String message) {
        super(message);
    }

    public DBException(String reason, Throwable cause) {
        super(reason, cause);
    }
}
