package com.my.web.command;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

public abstract class Command implements Serializable {

    private static final long serialVersionUID = 7239405036606411743L;

    public abstract String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException;

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

}
