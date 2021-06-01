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

public class RemoveProductFromReceiptCommand extends Command {

    private static final long serialVersionUID = -7291892230120392931L;
    private static final Logger logger = Logger.getLogger(RemoveProductFromReceiptCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        logger.debug("Remove product from receipt command is started");

        HttpSession session = request.getSession();
        ReceiptDAO receiptDAO = new ReceiptDAO();
        ProductDAO productDAO = new ProductDAO();
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
        Receipt updatedReceipt;
        try {
            updatedReceipt = receiptDAO.findReceipt(currentReceipt.getId());
        } catch (DBException exception) {
            String errorMessage = "An error has occurred while updating receipt, please try again later";
            session.setAttribute("errorMessage", errorMessage);
            logger.error("errorMessage --> " + exception.getMessage());
            return Commands.ERROR_PAGE_COMMAND;
        }

        if (!updatedReceipt.getReceiptStatus().name().equals(ReceiptStatus.NEW_RECEIPT.name())) {
            String errorMessage = rb.getString("remove.product.from.receipt.command.invalid.status");
            session.setAttribute("errorMessage", errorMessage);
            logger.error("errorMessage --> " + errorMessage);
            return "controller?command=noCommand";
        }

        int productId = Integer.parseInt(request.getParameter("product_id"));
        int receiptId = Integer.parseInt(request.getParameter("receipt_id"));
        int amount = Integer.parseInt(request.getParameter("amount"));

        Product currentProduct;
        try {
            currentProduct = productDAO.findProduct(productId);
        } catch (DBException exception) {
            String errorMessage = "An error has occurred while retrieving product, try again later";
            session.setAttribute("errorMessage", errorMessage);
            logger.error("errorMessage -> " + exception.getMessage());
            return Commands.ERROR_PAGE_COMMAND;
        }

        try {
            receiptDAO.deleteProductFromReceipt(receiptId, currentProduct, amount);
        } catch (DBException exception) {
            String errorMessage = "An error has occurred while deleting product, try again later";
            session.setAttribute("errorMessage", errorMessage);
            logger.error("errorMessage -> " + exception.getMessage());
            return Commands.ERROR_PAGE_COMMAND;
        }

        logger.debug("Remove product from receipt command is finished");
        return "controller?command=viewReceiptProducts";
    }
}
