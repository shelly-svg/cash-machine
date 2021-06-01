package com.my.web.command.cashier;

import com.my.db.entities.Receipt;
import com.my.db.entities.ReceiptDAO;
import com.my.db.entities.ReceiptStatus;
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

public class SetReceiptStatusClosedCommand extends Command {

    private static final long serialVersionUID = -8372367112394320123L;
    private static final Logger logger = Logger.getLogger(SetReceiptStatusClosedCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, ApplicationException {
        logger.debug("Set receipt status closed command is started");

        HttpSession session = request.getSession();
        ReceiptDAO receiptDAO = new ReceiptDAO();

        Receipt currentReceipt = (Receipt) session.getAttribute("currentReceipt");
        if (currentReceipt != null) {
            try {
                currentReceipt = receiptDAO.findReceipt(currentReceipt.getId());
            } catch (DBException exception) {
                String errorMessage = "receipt.dao.find.receipt";
                logger.error("errorMessage --> " + exception);
                throw new ApplicationException(errorMessage);
            }
            try {
                if (receiptDAO.getAllProductsFromReceipt(currentReceipt.getId()).isEmpty()) {
                    String errorMessage = "close.receipt.empty.error";
                    logger.error("errorMessage --> invalid, receipt has no products");
                    throw new ApplicationException(errorMessage);
                }
            } catch (DBException exception) {
                String errorMessage = "error.occurred";
                logger.error("errorMessage --> " + exception);
                throw new ApplicationException(errorMessage);
            }
            if (currentReceipt.getReceiptStatus().name().equals(ReceiptStatus.NEW_RECEIPT.name())) {
                try {
                    receiptDAO.setReceiptStatus(currentReceipt.getId(), ReceiptStatus.CLOSED);
                } catch (DBException exception) {
                    String errorMessage = "error.occurred";
                    logger.error("errorMessage --> " + exception);
                    throw new ApplicationException(errorMessage);
                }
                currentReceipt.setReceiptStatus(ReceiptStatus.CLOSED);
            } else {
                String errorMessage = "close.receipt.canceled.error";
                logger.error("errorMessage -- Cannot close canceled receipts");
                throw new ApplicationException(errorMessage);
            }
            session.setAttribute("currentReceipt", currentReceipt);
        } else {
            String errorMessage = "has.not.chosen.receipt.error";
            logger.error("errorMessage --> has not chosen receipt");
            throw new ApplicationException(errorMessage);
        }

        logger.debug("Set receipt status closed command is finished");
        return Commands.VIEW_CURRENT_RECEIPT_COMMAND;
    }
}
