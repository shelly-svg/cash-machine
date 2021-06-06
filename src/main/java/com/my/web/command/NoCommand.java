package com.my.web.command;

import com.my.Path;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * No command
 */
public class NoCommand extends Command {

    private static final long serialVersionUID = -1244523817302849368L;
    private static final Logger logger = Logger.getLogger(NoCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        logger.debug("NoCommand is started");

        HttpSession session = request.getSession();
        String errorMessage;

        Object o = session.getAttribute("errorMessage");
        if (o != null) {
            errorMessage = o.toString();
        } else {
            errorMessage = "Something went wrong";
        }

        request.setAttribute("errorMessage", errorMessage);
        logger.error("Set the request attribute: errorMessage -> " + errorMessage);
        logger.debug("NoCommand is finished");
        return Path.ERROR_PAGE;
    }

}
