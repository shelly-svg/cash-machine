package com.my.web.exception;

import java.sql.SQLException;

/**
 * An exception that throws from db entities & DAO classes
 *
 */
public class DBException extends SQLException {

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
