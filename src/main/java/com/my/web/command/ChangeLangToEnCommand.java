package com.my.web.command;

import com.my.Path;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class ChangeLangToEnCommand extends Command {

    private static final long serialVersionUID = -3402394287548203940L;
    private static final Logger logger = Logger.getLogger(ChangeLangToEnCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Object forward;
        //forward = Path.MENU_PAGE;
        HttpSession session = request.getSession();
        System.out.println("SESSION ATR" + session.getAttribute("lastAction"));
        forward = session.getAttribute("lastAction");
        if (forward == null) {
            session.setAttribute("lang", "en");
            return Path.LOGIN_PAGE;
        }
        logger.trace("CHANGE LANG TO EN COMMAND");
        session.setAttribute("lang", "en");
        return (String) forward;
    }
}
