package com.my.web.command.common;

import com.my.db.entities.Role;
import com.my.db.entities.User;
import com.my.db.entities.UserDAO;
import com.my.web.command.Command;
import com.my.web.recaptcha.VerifyUtils;
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
        logger.debug("Login command is started");

        HttpSession session = request.getSession();
        String login = request.getParameter("login");
        logger.trace("Request parameter : logging -> " + login);

        String password = request.getParameter("password");

        String errorMessage;
        String forward;
        boolean valid;

        if (login == null || password == null || login.isEmpty() || password.isEmpty()) {
            errorMessage = "Login/Password cannot be empty";
            session.setAttribute("errorMessage", errorMessage);
            logger.error("errorMessage --> " + errorMessage);
            return "?command=noCommand";
        }

        String gCaptchaResponse = request.getParameter("g-recaptcha-response");

        logger.debug("gRecaptchaResponse = " + gCaptchaResponse);

        valid = VerifyUtils.verify(gCaptchaResponse);

        if (!valid) {
            errorMessage = "Captcha is entered wrong";
            session.setAttribute("errorMessage", errorMessage);
            logger.error("errorMessage --> " + errorMessage);
            return "?command=noCommand";
        }


        User user = new UserDAO().findUserByLogin(login);
        logger.trace("Found user at DB: user-> " + user);

        if (user == null || !password.equals(user.getPassword())) {
            errorMessage = "Cannot find user with such login/password";
            session.setAttribute("errorMessage", errorMessage);
            logger.error("errorMessage --> " + errorMessage);
            return "?command=noCommand";
        } else {
            Role userRole = Role.getRole(user);
            logger.debug("User role --> " + userRole);

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
