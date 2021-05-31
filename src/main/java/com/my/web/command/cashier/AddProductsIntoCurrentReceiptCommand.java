package com.my.web.command.cashier;

import com.my.db.entities.*;
import com.my.web.Commands;
import com.my.web.LocalizationUtils;
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
    private final ReceiptDAO receiptDAO;
    private final ProductDAO productDAO;

    public AddProductsIntoCurrentReceiptCommand() {
        receiptDAO = new ReceiptDAO();
        productDAO = new ProductDAO();
    }

    public AddProductsIntoCurrentReceiptCommand(ReceiptDAO receiptDAO, ProductDAO productDAO) {
        this.receiptDAO = receiptDAO;
        this.productDAO = productDAO;
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        logger.debug("Add products into current receipt command is started");
        HttpSession session = request.getSession();
        ResourceBundle rb = LocalizationUtils.getCurrentRb(session);
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
        try {
            productDAO.updateProductsAmount(id, product.getAmount() - 1);
        } catch (ApplicationException exception) {
            String errorMessage = rb.getString("product.dao.error.update.amount");
            session.setAttribute("errorMessage", errorMessage);
            logger.error("errorMessage -> " + errorMessage);
            return Commands.ERROR_PAGE_COMMAND;
        }
        logger.debug("Received products ID => " + id);
        logger.debug("Add products into current receipt command is finished, forwarding to search for products page");

        return Commands.VIEW_SEARCH_PRODUCT_COMMAND;
    }

}
