package com.my.web.command;

import com.my.web.exception.ApplicationException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

/**
 * Main interface for the Command pattern implementation
 */
public abstract class Command implements Serializable {

    private static final long serialVersionUID = 7239405036606411743L;

    /**
     * Execution method for the command
     *
     * @return Address to go once command is executed
     */
    public abstract String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, ApplicationException;

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

}
