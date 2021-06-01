package com.my.web.exception;

import java.sql.SQLException;

public class DBException extends SQLException {

    public DBException(String message) {
        super(message);
    }

}
