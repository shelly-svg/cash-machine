package com.my.web.command.common;

import com.my.Path;
import com.my.web.command.Command;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LogoutCommand extends Command {

    private static final long serialVersionUID = -3293284734878343933L;
    private static final Logger logger = Logger.getLogger(LoginCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        logger.debug("Logout command is started");

        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        logger.debug("Logout command is finished");
        return Path.LOGIN_PAGE;
    }

}
