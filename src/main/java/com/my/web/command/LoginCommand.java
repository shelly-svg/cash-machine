package com.my.web.command;

import com.my.Path;
import com.my.db.entities.Role;
import com.my.db.entities.User;
import com.my.db.entities.UserDAO;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginCommand extends Command {

    private static final long serialVersionUID = -3256219372648163538L;
    private static final Logger logger = Logger.getLogger(LoginCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        logger.debug("Login command is start");

        HttpSession session = request.getSession();
        String login = request.getParameter("login");
        logger.trace("Request parameter : logging -> " + login);

        String password = request.getParameter("password");

        String errorMessage;
        String forward = Path.ERROR_PAGE;

        if (login == null || password == null || login.isEmpty() || password.isEmpty()) {
            errorMessage = "Login/Password cannot be empty";
            request.setAttribute("errorMessage", errorMessage);
            logger.error("errorMessage --> " + errorMessage);
            return forward;
        }

        User user = new UserDAO().findUserByLogin(login);
        logger.trace("Found user at DB: user-> " + user);

        if (user == null || !password.equals(user.getPassword())) {
            errorMessage = "Cannot find user with such login/password";
            request.setAttribute("errorMessage", errorMessage);
            logger.error("errorMessage --> " + errorMessage);
            return forward;
        } else {
            Role userRole = Role.getRole(user);
            logger.debug("User role --> " + userRole);

                    /*if (userRole == Role.ADMIN)
            forward = Path.COMMAND__LIST_ORDERS;

        if (userRole == Role.CLIENT)
            forward = Path.COMMAND__LIST_MENU;*/
            session.setAttribute("user", user);
            logger.trace("Set the session attribute: user --> " + user);
            session.setAttribute("userRole", userRole);
            logger.info("User " + user + " logged as " + userRole.toString().toLowerCase());

            forward = "controller?command=viewMenu";

            //work with i18n
            String userLocaleName = user.getLocaleName();
            logger.debug("userLocalName --> " + userLocaleName);
            if (userLocaleName != null && !userLocaleName.isEmpty()) {
                session.setAttribute("lang", userLocaleName);
                logger.debug("Set the session attribute: user locale Name -> " + userLocaleName);
            } else {
                session.setAttribute("lang", "ru");
                logger.debug("Set the session attribute: default lang - ru ");
            }

        }

        logger.debug("Login command is finished");
        return forward;
    }

}
