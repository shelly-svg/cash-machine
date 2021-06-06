package com.my.web.command.common.settings;

import com.my.Path;
import com.my.db.entities.User;
import com.my.db.entities.dao.UserDAO;
import com.my.web.Commands;
import com.my.web.command.Command;
import com.my.web.encryption.PasswordUtility;
import com.my.web.exception.ApplicationException;
import com.my.web.exception.DBException;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Change password command
 */
public class ChangePasswordCommand extends Command {

    private static final long serialVersionUID = 7292203901100002330L;
    private static final Logger logger = Logger.getLogger(ChangePasswordCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, ApplicationException {
        logger.debug("Change password command is started");

        String forward = null;

        logger.debug("Request method => " + request.getMethod());
        if (request.getMethod().equals("GET")) {
            forward = doGet(request);
        } else {
            if (request.getMethod().equals("POST")) {
                forward = doPost(request);
            }
        }
        if (forward == null) {
            forward = Path.MENU_PAGE;
        }

        logger.debug("Change password command is finished");
        return forward;
    }

    private String doPost(HttpServletRequest request) throws ApplicationException {
        logger.debug("Change password command is started at the post method");

        HttpSession session = request.getSession();

        String oldPass = request.getParameter("oldPassword");
        String newPass = request.getParameter("newPassword");
        String repNewPass = request.getParameter("repPassword");

        UserDAO userDAO = new UserDAO();

        User currentUser = (User) session.getAttribute("user");
        try {
            currentUser = userDAO.findUser(currentUser.getId());
        } catch (DBException exception) {
            String errorMessage = "user.dao.find.user.error";
            logger.error("errorMessage --> " + exception);
            throw new ApplicationException(errorMessage);
        }

        if (currentUser == null || !PasswordUtility.verifyUserPassword(oldPass, currentUser.getPassword(), currentUser.getSalt())) {
            String errorMessage = "change.password.command.old.password.mismatch";
            logger.error("errorMessage => user entered invalid old password ");
            throw new ApplicationException(errorMessage);
        }

        if (newPass == null || !newPass.equals(repNewPass)) {
            String errorMessage = "change.password.command.new.password.rep.isn`t.equal";
            logger.error("errorMessage -> New password is not equal to repeat new password");
            throw new ApplicationException(errorMessage);
        }

        String newSalt = PasswordUtility.getSalt(50);
        String newSecurePassword = PasswordUtility.generateSecurePassword(newPass, newSalt);

        try {
            userDAO.updateUserPassword(newSecurePassword, newSalt, currentUser.getId());
        } catch (DBException exception) {
            String errorMessage = "change.password.update.password.error";
            logger.error("errorMessage --> " + exception);
            throw new ApplicationException(errorMessage);
        }

        try {
            currentUser = userDAO.findUser(currentUser.getId());
        } catch (DBException exception) {
            String errorMessage = "user.dao.find.user.error";
            logger.error("errorMessage --> " + exception);
            throw new ApplicationException(errorMessage);
        }
        session.setAttribute("user", currentUser);


        logger.debug(oldPass + newPass + repNewPass);

        session.setAttribute("userMessage", "Your password successfully changed");
        return Commands.VIEW_SETTINGS_COMMAND;
    }

    private String doGet(HttpServletRequest request) throws ApplicationException {
        logger.debug("Change password command is started at the get method");

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
        String parameter = request.getParameter("code");

        String userCode;
        String userSalt;
        try {
            userCode = userDAO.getCode(currentUser.getId());
            userSalt = userDAO.getSalt(currentUser.getId());
        } catch (DBException exception) {
            String errorMessage = "error.occurred";
            logger.error("An error has occurred while retrieving code data");
            throw new ApplicationException(errorMessage);
        }

        if (userCode == null || userCode.isEmpty() || userSalt == null || userSalt.isEmpty() || parameter == null || parameter.isEmpty()) {
            String errorMessage = "change.password.link.invalid";
            logger.error("Invalid credentials");
            throw new ApplicationException(errorMessage);
        }

        if (!PasswordUtility.verifyUserPassword(userCode, parameter, userSalt)) {
            String errorMessage = "change.password.link.invalid";
            logger.error("Invalid link");
            throw new ApplicationException(errorMessage);
        }

        return Path.CHANGE_PASSWORD_PAGE;
    }

}
