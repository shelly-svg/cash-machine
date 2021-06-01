package com.my.web.command.senior_cashier;

import com.my.db.entities.*;
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

public class RemoveProductFromReceiptCommand extends Command {

    private static final long serialVersionUID = -7291892230120392931L;
    private static final Logger logger = Logger.getLogger(RemoveProductFromReceiptCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, ApplicationException {
        logger.debug("Remove product from receipt command is started");

        HttpSession session = request.getSession();
        ReceiptDAO receiptDAO = new ReceiptDAO();
        ProductDAO productDAO = new ProductDAO();

        Receipt currentReceipt = (Receipt) session.getAttribute("currentReceipt");
        Receipt updatedReceipt;
        try {
            updatedReceipt = receiptDAO.findReceipt(currentReceipt.getId());
        } catch (DBException exception) {
            String errorMessage = "receipt.dao.find.receipt";
            logger.error("errorMessage --> " + exception);
            throw new ApplicationException(errorMessage);
        }

        if (!updatedReceipt.getReceiptStatus().name().equals(ReceiptStatus.NEW_RECEIPT.name())) {
            String errorMessage = "remove.product.from.receipt.command.invalid.status";
            logger.error("errorMessage --> cannot remove products from closed receipts");
            throw new ApplicationException(errorMessage);
        }

        int productId;
        int receiptId;
        int amount;
        try {
            productId = Integer.parseInt(request.getParameter("product_id"));
            receiptId = Integer.parseInt(request.getParameter("receipt_id"));
            amount = Integer.parseInt(request.getParameter("amount"));
        } catch (NumberFormatException exception) {
            String errorMessage = "error.occurred";
            logger.error("errorMessage --> invalid ids");
            throw new ApplicationException(errorMessage);
        }

        Product currentProduct;
        try {
            currentProduct = productDAO.findProduct(productId);
        } catch (DBException exception) {
            String errorMessage = "product.dao.find.product";
            logger.error("errorMessage --> " + exception);
            throw new ApplicationException(errorMessage);
        }

        try {
            receiptDAO.deleteProductFromReceipt(receiptId, currentProduct, amount);
        } catch (DBException exception) {
            String errorMessage = "receipt.dao.delete.product.from.receipt";
            logger.error("errorMessage --> " + exception);
            throw new ApplicationException(errorMessage);
        }

        logger.debug("Remove product from receipt command is finished");
        return Commands.VIEW_RECEIPT_PRODUCTS_COMMAND;
    }
}
