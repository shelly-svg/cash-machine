package com.my.web.command.cashier;

import com.my.db.entities.*;
import com.my.db.entities.dao.ProductDAO;
import com.my.db.entities.dao.ReceiptDAO;
import com.my.web.Commands;
import com.my.web.command.Command;
import com.my.web.command.commodity_expert.EditProductCommand;
import com.my.web.exception.ApplicationException;
import com.my.web.exception.DBException;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, ApplicationException {
        logger.debug("Add products into current receipt command is started");

        int id;
        try {
            id = Integer.parseInt(request.getParameter("id"));
        } catch (NumberFormatException exception) {
            String errorMessage = "product.doesnt.exist";
            logger.error("errorMessage --> " + exception);
            throw new ApplicationException(errorMessage);
        }
        logger.debug("Received product ID => " + id);

        Receipt currentReceipt = (Receipt) request.getSession().getAttribute("currentReceipt");
        try {
            currentReceipt = receiptDAO.findReceipt(currentReceipt.getId());
        } catch (DBException exception) {
            String errorMessage = "receipt.dao.find.receipt";
            logger.error("errorMessage --> " + exception);
            throw new ApplicationException(errorMessage);
        }
        logger.debug("Updated current receipt => " + currentReceipt);

        Product product;
        try {
            product = productDAO.findProduct(id);
        } catch (DBException exception) {
            String errorMessage = "product.dao.find.product";
            logger.error("errorMessage --> " + exception);
            throw new ApplicationException(errorMessage);
        }

        if (product.getAmount() == 0) {
            String errorMessage = "add.product.out.of.stock";
            logger.error("errorMessage --> Product out of stock");
            throw new ApplicationException(errorMessage);
        }

        if (currentReceipt.getReceiptStatus().equals(ReceiptStatus.CLOSED) || currentReceipt.getReceiptStatus().equals(ReceiptStatus.CANCELED)) {
            String errorMessage = "add.product.cannot.add.invalid.status";
            logger.error("errorMessage --> cannot add products into canceled and closed receipts");
            throw new ApplicationException(errorMessage);
        }

        try {
            receiptDAO.addProductIntoReceipt(product, currentReceipt.getId());
        } catch (DBException exception) {
            String errorMessage = "add.product.already.added.error";
            logger.error("errorMessage --> " + exception);
            throw new ApplicationException(errorMessage);
        }

        logger.debug("Add products into current receipt command is finished, forwarding to search for products page");
        return Commands.VIEW_SEARCH_PRODUCT_COMMAND;
    }

}
