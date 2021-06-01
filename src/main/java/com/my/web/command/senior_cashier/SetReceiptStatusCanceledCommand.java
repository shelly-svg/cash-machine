package com.my.web.command.senior_cashier;

import com.my.db.entities.*;
import com.my.web.Commands;
import com.my.web.command.Command;
import com.my.web.exception.DBException;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class SetReceiptStatusCanceledCommand extends Command {

    private static final long serialVersionUID = 2391093230348598231L;
    private static final Logger logger = Logger.getLogger(SetReceiptStatusCanceledCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        logger.debug("Set receipt status canceled command is started");

        HttpSession session = request.getSession();
        ReceiptDAO receiptDAO = new ReceiptDAO();
        ProductDAO productDAO = new ProductDAO();
        String errorMessage;
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


        Receipt currentReceipt = (Receipt) session.getAttribute("currentReceipt");
        if (currentReceipt != null) {
            try {
                currentReceipt = receiptDAO.findReceipt(currentReceipt.getId());
            } catch (DBException exception) {
                errorMessage = "An error has occurred while updating receipt, please try again later";
                session.setAttribute("errorMessage", errorMessage);
                logger.error("errorMessage --> " + exception.getMessage());
                return Commands.ERROR_PAGE_COMMAND;
            }

            if (currentReceipt.getReceiptStatus().name().equals(ReceiptStatus.NEW_RECEIPT.name())) {
                try {
                    receiptDAO.cancelReceipt(currentReceipt);
                } catch (DBException exception) {
                    errorMessage = "An error has occurred while canceling this report, please try again later";
                    session.setAttribute("errorMessage", errorMessage);
                    logger.error("errorMessage --> " + exception.getMessage());
                    return Commands.ERROR_PAGE_COMMAND;
                }
                currentReceipt.setReceiptStatus(ReceiptStatus.CANCELED);
                session.setAttribute("currentReceipt", currentReceipt);
            } else {
                errorMessage = rb.getString("set.receipt.status.closed.command.invalid.status");
                session.setAttribute("errorMessage", errorMessage);
                logger.error("errorMessage --> " + errorMessage);
                return Commands.ERROR_PAGE_COMMAND;
            }
        } else {
            errorMessage = rb.getString("set.receipt.status.closed.command.receipt.null");
            request.setAttribute("errorMessage", errorMessage);
            logger.error("errorMessage --> " + errorMessage);
            return Commands.ERROR_PAGE_COMMAND;
        }

        logger.debug("Set receipt status canceled command is finished");
        return "controller?command=viewCurrentReceipt";
    }

}
