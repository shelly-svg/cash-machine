package com.my.web.command.senior_cashier;

import com.my.db.entities.*;
import com.my.db.entities.dao.ReceiptDAO;
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
 * Set receipt status canceled command
 */
public class SetReceiptStatusCanceledCommand extends Command {

    private static final long serialVersionUID = 2391093230348598231L;
    private static final Logger logger = Logger.getLogger(SetReceiptStatusCanceledCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, ApplicationException {
        logger.debug("Set receipt status canceled command is started");

        HttpSession session = request.getSession();
        ReceiptDAO receiptDAO = new ReceiptDAO();
        String errorMessage;

        Receipt currentReceipt = (Receipt) session.getAttribute("currentReceipt");
        if (currentReceipt != null) {
            try {
                currentReceipt = receiptDAO.findReceipt(currentReceipt.getId());
            } catch (DBException exception) {
                errorMessage = "receipt.dao.find.receipt";
                logger.error("errorMessage --> " + exception);
                throw new ApplicationException(errorMessage);
            }

            if (currentReceipt.getReceiptStatus().name().equals(ReceiptStatus.NEW_RECEIPT.name())) {
                try {
                    receiptDAO.cancelReceipt(currentReceipt);
                } catch (DBException exception) {
                    errorMessage = "receipt.dao.cancel.report";
                    logger.error("errorMessage --> " + exception);
                    throw new ApplicationException(errorMessage);
                }
                currentReceipt.setReceiptStatus(ReceiptStatus.CANCELED);
                session.setAttribute("currentReceipt", currentReceipt);
            } else {
                errorMessage = "set.receipt.status.closed.command.invalid.status";
                logger.error("errorMessage --> invalid receipt status");
                throw new ApplicationException(errorMessage);
            }
        } else {
            errorMessage = "set.receipt.status.closed.command.receipt.null";
            logger.error("errorMessage --> receipt isn`t chosen");
            throw new ApplicationException(errorMessage);
        }

        logger.debug("Set receipt status canceled command is finished");
        return Commands.VIEW_CURRENT_RECEIPT_COMMAND;
    }

}
