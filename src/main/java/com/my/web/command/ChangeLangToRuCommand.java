package com.my.web.command;

import com.my.web.LocalizationUtils;
import com.my.web.exception.ApplicationException;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class ChangeLangToRuCommand extends Command {

    private static final long serialVersionUID = 2349283972186473273L;
    private static final Logger logger = Logger.getLogger(ChangeLangToRuCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, ApplicationException {
        logger.debug("Change lang to russian command is started");
        HttpSession session = request.getSession();
        session.setAttribute("lang", "ru");
        logger.trace("Set session attribute lang => ru");
        String forward = LocalizationUtils.getAction(session, request);
        logger.debug("Change lang to russian command is finished");
        return forward;
    }

}
