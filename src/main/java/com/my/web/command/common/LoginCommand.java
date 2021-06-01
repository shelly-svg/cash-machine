package com.my.web.command.common;

import com.my.db.entities.Role;
import com.my.db.entities.User;
import com.my.db.entities.UserDAO;
import com.my.web.Commands;
import com.my.web.command.Command;
import com.my.web.encryption.PasswordUtility;
import com.my.web.exception.ApplicationException;
import com.my.web.recaptcha.VerifyUtils;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class LoginCommand extends Command {

    private static final long serialVersionUID = -3256219372648163538L;
    private static final Logger logger = Logger.getLogger(LoginCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        logger.debug("Login command is started");

        HttpSession session = request.getSession();

        String localeName = "en";
        Object localeObj = session.getAttribute("lang");
        if (localeObj != null) {
            localeName = localeObj.toString();
        }
        Locale locale;
        if ("ru".equals(localeName)) {
            locale = new Locale("ru", "RU");
        } else {
            locale = new Locale("en", "EN");
        }
        ResourceBundle rb = ResourceBundle.getBundle("resources", locale);
        String login = request.getParameter("login");
        logger.trace("Request parameter : logging -> " + login);

        String password = request.getParameter("password");

        String errorMessage;
        String forward;
        boolean valid;

        if (login == null || password == null || login.isEmpty() || password.isEmpty()) {
            errorMessage = rb.getString("login.command.values.empty");
            session.setAttribute("errorMessage", errorMessage);
            logger.error("errorMessage --> " + errorMessage);
            return Commands.ERROR_PAGE_COMMAND;
        }

        String gCaptchaResponse = request.getParameter("g-recaptcha-response");

        valid = VerifyUtils.verify(gCaptchaResponse);

        if (!valid) {
            errorMessage = rb.getString("login.command.captcha.invalid");
            session.setAttribute("errorMessage", errorMessage);
            logger.error("errorMessage --> " + errorMessage);
            return Commands.ERROR_PAGE_COMMAND;
        }
        logger.debug("captcha is valid");

        User user;
        try {
            user = new UserDAO().findUserByLogin(login);
        } catch (ApplicationException exception) {
            errorMessage = "An error has occurred while searching user, please try again later";
            session.setAttribute("errorMessage", errorMessage);
            logger.error("errorMessage --> " + errorMessage);
            return Commands.ERROR_PAGE_COMMAND;
        }

        logger.trace("Found user at DB: user-> " + user);

        if (user == null || !PasswordUtility.verifyUserPassword(password, user.getPassword(), user.getSalt())) {
            errorMessage = rb.getString("login.command.credentials.invalid");
            session.setAttribute("errorMessage", errorMessage);
            logger.error("errorMessage --> " + errorMessage);
            return Commands.ERROR_PAGE_COMMAND;
        } else {
            Role userRole = Role.getRole(user);
            logger.debug("User role --> " + userRole);

            session.setAttribute("user", user);
            logger.trace("Set the session attribute: user --> " + user);
            session.setAttribute("userRole", userRole);
            logger.info("User " + user + " logged as " + userRole.toString().toLowerCase());

            forward = Commands.VIEW_MENU_COMMAND;

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
