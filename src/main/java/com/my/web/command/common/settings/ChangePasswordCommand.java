package com.my.web.command.common.settings;

import com.my.web.Commands;
import com.my.web.command.Command;
import com.my.web.exception.ApplicationException;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class ChangePasswordCommand extends Command {

    private static final long serialVersionUID = 7292203901100002330L;
    private static final Logger logger = Logger.getLogger(ChangePasswordCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, ApplicationException {
        HttpSession session = request.getSession();

        String parameter = request.getParameter("code");
        System.out.println(parameter);

        session.setAttribute("userMessage", parameter);
        logger.debug("Change user locale command is finished");
        return Commands.VIEW_SETTINGS_COMMAND;
    }
}
