package com.my.web.command.common.setting;

import com.my.db.entities.User;
import com.my.db.entities.dao.UserDAO;
import com.my.web.Commands;
import com.my.web.command.Command;
import com.my.web.exception.ApplicationException;
import com.my.web.exception.DBException;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Change user locale command
 */
public class ChangeUserLocaleCommand extends Command {

    private static final long serialVersionUID = -923812039234091203L;
    private static final Logger logger = Logger.getLogger(ChangeUserLocaleCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, ApplicationException {
        logger.debug("Change user locale command is started");
        HttpSession session = request.getSession();
        UserDAO userDAO = new UserDAO();

        User currentUser = (User) session.getAttribute("user");

        try {
            currentUser = userDAO.findUser(currentUser.getId());
        } catch (DBException exception) {
            String errorMessage = "user.dao.find.user.error";
            logger.error("errorMessage --> " + exception);
            throw new ApplicationException(errorMessage);
        }

        String newLocaleName;
        if ("ru".equals(currentUser.getLocaleName())) {
            newLocaleName = "en";
        } else {
            newLocaleName = "ru";
        }

        try {
            userDAO.updateUserLanguage(currentUser.getId(), newLocaleName);
        } catch (DBException exception) {
            String errorMessage = "user.dao.change.lang";
            logger.error("errorMessage --> " + exception);
            throw new ApplicationException(errorMessage);
        }
        currentUser.setLocaleName(newLocaleName);

        session.setAttribute("user", currentUser);
        session.setAttribute("lang", newLocaleName);
        session.setAttribute("userMessage", "change.locale.command.message");

        logger.debug("Change user locale command is finished");
        return Commands.VIEW_SETTINGS_COMMAND;
    }
}
