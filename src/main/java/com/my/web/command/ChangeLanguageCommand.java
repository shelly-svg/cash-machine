package com.my.web.command;

import com.my.web.exception.ApplicationException;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class ChangeLanguageCommand extends Command {

    private static final long serialVersionUID = 9120493091209031223L;
    private static final Logger logger = Logger.getLogger(ChangeLanguageCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, ApplicationException {
        logger.debug("Change language command is started");

        HttpSession session = request.getSession();

        String queryString = request.getParameter("queryString");
        String language = request.getParameter("language");

        session.setAttribute("lang", language);
        logger.trace("Set session attribute lang =>" + language);

        String forward = request.getContextPath() + request.getServletPath() + "?" + queryString;
        logger.debug("Change language command is finished");
        return forward;
    }

}
