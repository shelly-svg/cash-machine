package com.my.web.command.cashier;

import com.my.Path;
import com.my.db.entities.*;
import com.my.web.command.Command;
import com.my.web.command.common.EditProductCommand;
import com.my.web.exception.ApplicationException;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AddProductsIntoCurrentReceiptCommand extends Command {
    private static final long serialVersionUID = -2348237473492349742L;
    private static final Logger logger = Logger.getLogger(EditProductCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        logger.debug("Add products into current receipt command is started");
        int id = Integer.parseInt(request.getParameter("id"));
        Receipt currentReceipt = (Receipt) request.getSession().getAttribute("currentReceipt");

        if (currentReceipt.getReceiptStatus().equals(ReceiptStatus.CLOSED) || currentReceipt.getReceiptStatus().equals(ReceiptStatus.CANCELED)) {
            String errorMessage = "You cannot add products into closed and canceled receipts";
            request.setAttribute("errorMessage", errorMessage);
            logger.error("errorMessage --> " + errorMessage);
            return Path.ERROR_PAGE;
        }

        ReceiptDAO receiptDAO = new ReceiptDAO();
        try {
            receiptDAO.addProductIntoReceipt(id, currentReceipt.getId());
        } catch (ApplicationException exception) {
            String errorMessage = "This product is already exist at this receipt";
            request.setAttribute("errorMessage", errorMessage);
            logger.error("errorMessage --> " + errorMessage);
            return Path.ERROR_PAGE;
        }
        logger.debug("RECEIVED PRODUCT ID => " + id);
        logger.debug("Add products into current receipt command is finished, forwarding to search for products page");

        return Path.VIEW_SEARCH_PRODUCTS_PAGE;
    }

}
