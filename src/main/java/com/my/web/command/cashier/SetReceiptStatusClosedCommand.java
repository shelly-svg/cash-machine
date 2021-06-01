package com.my.web.command.cashier;

import com.my.Path;
import com.my.db.entities.Receipt;
import com.my.db.entities.ReceiptDAO;
import com.my.db.entities.ReceiptStatus;
import com.my.web.Commands;
import com.my.web.command.Command;
import com.my.web.exception.ApplicationException;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class SetReceiptStatusClosedCommand extends Command {

    private static final long serialVersionUID = -8372367112394320123L;
    private static final Logger logger = Logger.getLogger(SetReceiptStatusClosedCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        logger.debug("Set receipt status closed command is started");

        HttpSession session = request.getSession();
        ReceiptDAO receiptDAO = new ReceiptDAO();

        Receipt currentReceipt = (Receipt) session.getAttribute("currentReceipt");
        if (currentReceipt != null) {
            try {
                currentReceipt = receiptDAO.findReceipt(currentReceipt.getId());
            } catch (ApplicationException exception) {
                String errorMessage = "An error has occurred while updating receipt, please try again later";
                session.setAttribute("errorMessage", errorMessage);
                logger.error("errorMessage --> " + exception.getMessage());
                return Commands.ERROR_PAGE_COMMAND;
            }
            try {
                if (receiptDAO.getAllProductsFromReceipt(currentReceipt.getId()).isEmpty()) {
                    String errorMessage = "You cannot close empty receipts";
                    session.setAttribute("errorMessage", errorMessage);
                    logger.error("errorMessage --> " + errorMessage);
                    return Commands.ERROR_PAGE_COMMAND;
                }
            } catch (ApplicationException exception) {
                String errorMessage = "An error has occurred, please try again later";
                session.setAttribute("errorMessage", errorMessage);
                logger.error("errorMessage --> " + exception.getMessage());
                return Commands.ERROR_PAGE_COMMAND;
            }
            if (currentReceipt.getReceiptStatus().name().equals(ReceiptStatus.NEW_RECEIPT.name())) {
                try {
                    receiptDAO.setReceiptStatus(currentReceipt.getId(), ReceiptStatus.CLOSED);
                } catch (ApplicationException exception) {
                    String errorMessage = "Something went wrong while closing this receipt, please try again later";
                    session.setAttribute("errorMessage", errorMessage);
                    logger.error("errorMessage --> " + exception.getMessage());
                    return Commands.ERROR_PAGE_COMMAND;
                }
                currentReceipt.setReceiptStatus(ReceiptStatus.CLOSED);
            } else {
                String errorMessage = "This receipt is canceled, you cannot close";
                session.setAttribute("errorMessage", errorMessage);
                logger.error("errorMessage --> " + errorMessage);
                return Commands.ERROR_PAGE_COMMAND;
            }
            session.setAttribute("currentReceipt", currentReceipt);
        } else {
            String errorMessage = "You didnt chose receipt";
            session.setAttribute("errorMessage", errorMessage);
            logger.error("errorMessage --> " + errorMessage);
            return Commands.ERROR_PAGE_COMMAND;
        }

        logger.debug("Set receipt status closed command is finished");
        return "controller?command=viewCurrentReceipt";
    }
}
