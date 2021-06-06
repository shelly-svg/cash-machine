package com.my.web.command.common.settings;

import com.my.db.entities.User;
import com.my.db.entities.dao.UserDAO;
import com.my.web.Commands;
import com.my.web.LocalizationUtils;
import com.my.web.command.Command;
import com.my.web.email.EmailUtility;
import com.my.web.encryption.PasswordUtility;
import com.my.web.exception.ApplicationException;
import com.my.web.exception.DBException;
import org.apache.log4j.Logger;

import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ResourceBundle;

public class SendConfirmationLinkCommand extends Command {

    private static final long serialVersionUID = -4019392012993292002L;
    private static final Logger logger = Logger.getLogger(SendConfirmationLinkCommand.class);
    private static final int CODE_LENGTH = 20;
    private static final int SALT_LENGTH = 50;

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, ApplicationException {
        logger.debug("Send confirmation link command is started");
        HttpSession session = request.getSession();
        ResourceBundle rb = LocalizationUtils.getCurrentRb(session);

        User currentUser = (User) session.getAttribute("user");

        try {
            currentUser = new UserDAO().findUser(currentUser.getId());
        } catch (DBException exception) {
            String errorMessage = "user.dao.find.user.error";
            logger.error("errorMessage --> " + exception);
            throw new ApplicationException(errorMessage);
        }

        String code = generateCode(currentUser.getId());
        sendConfirmationCode(currentUser, code, rb);

        session.setAttribute("userMessage", "send.confirmation.link.command");
        logger.debug("Send confirmation link command is finished");
        return Commands.VIEW_SETTINGS_COMMAND;
    }

    private void sendConfirmationCode(User user, String link, ResourceBundle rb) throws ApplicationException {
        try {
            EmailUtility.sendPasswordLink(user.getEmail(), link, rb);
        } catch (MessagingException exception) {
            String errorMessage = "send.confirmation.message.error";
            logger.error("An error occurred while sending confirmation message");
            throw new ApplicationException(errorMessage);
        }
    }

    private String generateCode(int userId) throws ApplicationException {
        String randomCode = PasswordUtility.getSalt(CODE_LENGTH);
        String saltRandomCode = PasswordUtility.getSalt(SALT_LENGTH);
        try {
            new UserDAO().addConfirmationCode(userId, saltRandomCode, randomCode);
        } catch (DBException exception) {
            String errorMessage = "send.confirmation.message.get.code.error";
            logger.error("An error has occurred, maybe message is already sent, check your email");
            throw new ApplicationException(errorMessage);
        }
        return PasswordUtility.generateSecurePassword(randomCode, saltRandomCode);
    }

}
