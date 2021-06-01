package com.my.web.command.cashier;

import com.my.Path;
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
import java.util.Map;

public class EditReceiptProductsCommand extends Command {

    private static final long serialVersionUID = 4912383489123901021L;
    private static final Logger logger = Logger.getLogger(EditReceiptProductsCommand.class);
    private final ReceiptDAO receiptDAO;
    private final ProductDAO productDAO;

    public EditReceiptProductsCommand() {
        receiptDAO = new ReceiptDAO();
        productDAO = new ProductDAO();
    }

    public EditReceiptProductsCommand(ReceiptDAO receiptDAO, ProductDAO productDAO) {
        this.receiptDAO = receiptDAO;
        this.productDAO = productDAO;
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, ApplicationException {
        logger.debug("Edit receipt products command is started");

        String forward = null;
        logger.debug("Requested method =>  " + request.getMethod());

        if (request.getMethod().equals("GET")) {
            forward = doGet(request);
        } else {
            if (request.getMethod().equals("POST")) {
                forward = doPost(request);
            }
        }
        if (forward == null) {
            forward = Path.MENU_PAGE;
        }

        logger.debug("Edit receipt products command is finished, forwarding to -> " + forward);
        return forward;
    }

    private String doPost(HttpServletRequest request) throws ApplicationException {
        logger.debug("Edit receipt products command is started at the POST method");

        HttpSession session = request.getSession();

        Receipt currentReceipt = (Receipt) session.getAttribute("currentReceipt");
        try {
            currentReceipt = receiptDAO.findReceipt(currentReceipt.getId());
        } catch (DBException exception) {
            String errorMessage = "receipt.dao.find.receipt";
            logger.error("errorMessage --> " + exception);
            throw new ApplicationException(errorMessage);
        }

        if (currentReceipt == null || currentReceipt.getReceiptStatus().name().equals(ReceiptStatus.CLOSED.name())
                || currentReceipt.getReceiptStatus().name().equals(ReceiptStatus.CANCELED.name())) {
            String errorMessage = "edit.receipt.products.command.status.error";
            logger.error("errorMessage --> Cannot edit products at closed & canceled receipts");
            throw new ApplicationException(errorMessage);
        }
        session.setAttribute("currentReceipt", currentReceipt);

        int productId;
        int receiptId;
        int oldAmount;
        try {
            productId = Integer.parseInt(request.getParameter("product_id"));
            receiptId = Integer.parseInt(request.getParameter("receipt_id"));
            oldAmount = Integer.parseInt(request.getParameter("oldAmount"));
        } catch (NumberFormatException exception) {
            String errorMessage = "error.occurred";
            logger.error("errorMessage => " + exception);
            throw new ApplicationException(errorMessage);
        }

        int newAmount;
        try {
            newAmount = Integer.parseInt(request.getParameter("newAmount"));
        } catch (NumberFormatException exception) {
            String errorMessage = "edit.receipt.products.command.amount.nan";
            logger.error("errorMessage --> " + exception);
            throw new ApplicationException(errorMessage);
        }

        if (newAmount <= 0 || newAmount > 999999999) {
            String errorMessage = "edit.receipt.products.command.amount.error.null";
            logger.error("errorMessage --> invalid new amount");
            throw new ApplicationException(errorMessage);
        }

        Map<Product, Integer> receiptProductsMap;
        try {
            receiptProductsMap = receiptDAO.getMapOfAmountsAndProductsFromReceipt(currentReceipt);
        } catch (DBException exception) {
            String errorMessage = "receipt.dao.receipt.products.amounts";
            logger.error("errorMessage --> " + exception);
            throw new ApplicationException(errorMessage);
        }

        Product product;
        try {
            product = productDAO.findProduct(productId);
        } catch (DBException exception) {
            String errorMessage = "product.dao.find.product";
            logger.error("errorMessage --> " + exception);
            throw new ApplicationException(errorMessage);
        }

        if (!receiptProductsMap.containsKey(product)) {
            String errorMessage = "product.already.deleted";
            logger.error("errorMessage --> Product that you are currently updating is already deleted from the receipt");
            throw new ApplicationException(errorMessage);
        }

        int newProductAmount;
        if (oldAmount > newAmount && oldAmount + newAmount <= product.getAmount()) {
            try {
                newProductAmount = product.getAmount() + (oldAmount - newAmount);
                receiptDAO.setAmountOfProductAtTheReceipt(newAmount, newProductAmount, receiptId, productId);
            } catch (DBException exception) {
                String errorMessage = "product.dao.error.update.amount";
                logger.error("errorMessage --> " + exception);
                throw new ApplicationException(errorMessage);
            }
            return Commands.VIEW_RECEIPT_PRODUCTS_COMMAND;
        }

        if (product.getAmount() + oldAmount < newAmount) {
            String errorMessage = "edit.receipt.products.command.amount.error";
            logger.error("errorMessage --> invalid amount");
            throw new ApplicationException(errorMessage);
        }

        try {
            newProductAmount = product.getAmount() + (oldAmount - newAmount);
            receiptDAO.setAmountOfProductAtTheReceipt(newAmount, newProductAmount, receiptId, productId);
        } catch (DBException exception) {
            String errorMessage = "product.dao.error.update.amount";
            logger.error("errorMessage --> " + exception);
            throw new ApplicationException(errorMessage);
        }

        return Commands.VIEW_RECEIPT_PRODUCTS_COMMAND;
    }

    private String doGet(HttpServletRequest request) throws ApplicationException {
        logger.debug("Edit receipt products command is started at the GET method");

        HttpSession session = request.getSession();

        Receipt currentReceipt = (Receipt) session.getAttribute("currentReceipt");
        try {
            currentReceipt = receiptDAO.findReceipt(currentReceipt.getId());
        } catch (DBException exception) {
            String errorMessage = "receipt.dao.find.receipt";
            logger.error("errorMessage --> " + exception);
            throw new ApplicationException(errorMessage);
        }

        if (currentReceipt == null || currentReceipt.getReceiptStatus().name().equals(ReceiptStatus.CLOSED.name())
                || currentReceipt.getReceiptStatus().name().equals(ReceiptStatus.CANCELED.name())) {
            String errorMessage = "edit.receipt.products.command.status.error";
            logger.error("errorMessage --> invalid receipt status");
            throw new ApplicationException(errorMessage);
        }
        logger.debug("Set session attribute: updated current receipt information => " + currentReceipt);
        session.setAttribute("currentReceipt", currentReceipt);

        Map<Product, Integer> productMap;
        try {
            productMap = receiptDAO.getMapOfAmountsAndProductsFromReceipt(currentReceipt);
        } catch (DBException exception) {
            String errorMessage = "receipt.dao.receipt.products.amounts";
            logger.error("errorMessage --> " + exception);
            throw new ApplicationException(errorMessage);
        }
        logger.debug("Set request attribute: receipt products with amounts map => " + productMap);
        request.setAttribute("receiptProductMap", productMap);

        return Path.VIEW_RECEIPT_PRODUCTS_PAGE;
    }

}
