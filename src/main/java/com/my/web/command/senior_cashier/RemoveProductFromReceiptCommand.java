package com.my.web.command.senior_cashier;

import com.my.db.entities.*;
import com.my.web.Commands;
import com.my.web.command.Command;
import com.my.web.exception.ApplicationException;
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
        Receipt updatedReceipt = receiptDAO.findReceipt(currentReceipt.getId());

        if (!updatedReceipt.getReceiptStatus().name().equals(ReceiptStatus.NEW_RECEIPT.name())) {
            String errorMessage = rb.getString("remove.product.from.receipt.command.invalid.status");
            session.setAttribute("errorMessage", errorMessage);
            logger.error("errorMessage --> " + errorMessage);
            return "controller?command=noCommand";
        }

        int productId = Integer.parseInt(request.getParameter("product_id"));
        int receiptId = Integer.parseInt(request.getParameter("receipt_id"));
        int amount = Integer.parseInt(request.getParameter("amount"));

        Product currentProduct = productDAO.findProduct(productId);

        try {
            productDAO.updateProductsAmount(productId, currentProduct.getAmount() + amount);
        } catch (ApplicationException exception) {
            String errorMessage = rb.getString("product.dao.error.update.amount");
            session.setAttribute("errorMessage", errorMessage);
            logger.error("errorMessage -> " + errorMessage);
            return Commands.ERROR_PAGE_COMMAND;
        }

        receiptDAO.deleteProductFromReceipt(receiptId, productId);

        logger.debug("Remove product from receipt command is finished");
        return "controller?command=viewReceiptProducts";
    }
}
