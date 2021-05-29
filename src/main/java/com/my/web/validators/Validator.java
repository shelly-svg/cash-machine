package com.my.web.validators;

import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ResourceBundle;

public abstract class Validator<T> {

    public abstract boolean validate(T entity, HttpSession session, ResourceBundle rb) throws IOException, ServletException;

}
