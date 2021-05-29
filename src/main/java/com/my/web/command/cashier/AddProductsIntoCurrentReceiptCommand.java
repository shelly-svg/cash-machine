package com.my.web.command.cashier;

import com.my.db.entities.*;
import com.my.web.Commands;
import com.my.web.command.Command;
import com.my.web.command.commodity_expert.EditProductCommand;
import com.my.web.exception.ApplicationException;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class AddProductsIntoCurrentReceiptCommand extends Command {
    private static final long serialVersionUID = -2348237473492349742L;
    private static final Logger logger = Logger.getLogger(EditProductCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        logger.debug("Add products into current receipt command is started");
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


        int id = Integer.parseInt(request.getParameter("id"));
        Receipt currentReceipt = (Receipt) request.getSession().getAttribute("currentReceipt");
        currentReceipt = receiptDAO.findReceipt(currentReceipt.getId());

        Product product = productDAO.findProduct(id);
        if (product.getAmount() == 0) {
            String errorMessage = rb.getString("add.product.out.of.stock");
            session.setAttribute("errorMessage", errorMessage);
            logger.error("errorMessage --> " + errorMessage);
            return Commands.ERROR_PAGE_COMMAND;
        }

        if (currentReceipt.getReceiptStatus().equals(ReceiptStatus.CLOSED) || currentReceipt.getReceiptStatus().equals(ReceiptStatus.CANCELED)) {
            String errorMessage = rb.getString("add.product.cannot.add.invalid.status");
            session.setAttribute("errorMessage", errorMessage);
            logger.error("errorMessage --> " + errorMessage);
            return Commands.ERROR_PAGE_COMMAND;
        }

        try {
            receiptDAO.addProductIntoReceipt(id, currentReceipt.getId());
        } catch (ApplicationException exception) {
            String errorMessage = rb.getString("add.product.already.added.error");
            session.setAttribute("errorMessage", errorMessage);
            logger.error("errorMessage --> " + errorMessage);
            return Commands.ERROR_PAGE_COMMAND;
        }
        productDAO.updateProductsAmount(id, product.getAmount() - 1);
        logger.debug("RECEIVED PRODUCT ID => " + id);
        logger.debug("Add products into current receipt command is finished, forwarding to search for products page");

        return "controller?command=viewSearchProductPage";
    }

}
