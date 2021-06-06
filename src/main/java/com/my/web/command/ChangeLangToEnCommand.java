package com.my.web.command;

import com.my.web.localization.LocalizationUtils;
import com.my.web.exception.ApplicationException;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Change language to english command
 */
public class ChangeLangToEnCommand extends Command {

    private static final long serialVersionUID = -3402394287548203940L;
    private static final Logger logger = Logger.getLogger(ChangeLangToEnCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, ApplicationException {
        logger.debug("Change lang to english command is started");
        HttpSession session = request.getSession();
        session.setAttribute("lang", "en");
        logger.trace("Set session attribute lang => en");
        String forward = LocalizationUtils.getAction(session, request);
        logger.debug("Change lang to english command is finished");
        return forward;
    }

}
