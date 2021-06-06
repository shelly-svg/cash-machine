package com.my.web.command.common;

import com.my.db.entities.Role;
import com.my.db.entities.User;
import com.my.db.entities.dao.UserDAO;
import com.my.web.Commands;
import com.my.web.command.Command;
import com.my.web.encryption.PasswordUtility;
import com.my.web.exception.ApplicationException;
import com.my.web.exception.DBException;
import com.my.web.recaptcha.VerifyUtils;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Login command
 */
public class LoginCommand extends Command {

    private static final long serialVersionUID = -3256219372648163538L;
    private static final Logger logger = Logger.getLogger(LoginCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, ApplicationException {
        logger.debug("Login command is started");

        HttpSession session = request.getSession();

        String login = request.getParameter("login");
        logger.trace("Request parameter : login -> " + login);

        String password = request.getParameter("password");
        logger.trace("Request parameter : password -> " + password);

        String errorMessage;
        String forward;
        boolean valid;

        if (login == null || password == null || login.isEmpty() || password.isEmpty()) {
            errorMessage = "login.command.values.empty";
            logger.error("errorMessage --> " + "Login/password cannot be empty");
            throw new ApplicationException(errorMessage);
        }

        String gCaptchaResponse = request.getParameter("g-recaptcha-response");

        valid = VerifyUtils.verify(gCaptchaResponse);

        if (!valid) {
            errorMessage = "login.command.captcha.invalid";
            logger.error("errorMessage --> " + "Captcha isn`t valid");
            throw new ApplicationException(errorMessage);
        }

        logger.debug("captcha is valid");

        User user;
        try {
            user = new UserDAO().findUserByLogin(login);
        } catch (DBException exception) {
            errorMessage = "user.dao.find.user.error";
            logger.error("errorMessage --> " + exception);
            throw new ApplicationException(errorMessage);
        }

        logger.trace("Found user at DB: user-> " + user);

        if (user == null || !PasswordUtility.verifyUserPassword(password, user.getPassword(), user.getSalt())) {
            errorMessage = "login.command.credentials.invalid";
            logger.error("errorMessage --> " + "Login/password isn`t valid");
            throw new ApplicationException(errorMessage);
        } else {
            Role userRole = Role.getRole(user);
            logger.debug("User role --> " + userRole);
            session.setAttribute("user", user);
            logger.trace("Set the session attribute: user --> " + user);
            session.setAttribute("userRole", userRole);
            logger.info("User " + user + " logged as " + userRole.toString().toLowerCase());
            forward = Commands.VIEW_MENU_COMMAND;

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
