package com.my.web.validators;

import com.my.web.exception.ApplicationException;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * Root of all validators
 *
 */
public abstract class Validator<T> {

    public abstract boolean validate(T entity) throws IOException, ServletException, ApplicationException;

}
