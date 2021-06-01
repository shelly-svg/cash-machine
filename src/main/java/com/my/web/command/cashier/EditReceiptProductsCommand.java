package com.my.web.command.cashier;

import com.my.Path;
import com.my.db.entities.*;
import com.my.web.Commands;
import com.my.web.LocalizationUtils;
import com.my.web.command.Command;
import com.my.web.exception.ApplicationException;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

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
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        logger.debug("Edit receipt products command is started");
        String forward = null;
        logger.debug("REQUEST METHOD =>  " + request.getMethod());
        HttpSession session = request.getSession();

        ResourceBundle rb = LocalizationUtils.getCurrentRb(session);
        if (request.getMethod().equals("GET")) {
            forward = doGet(request, rb);
        } else {
            if (request.getMethod().equals("POST")) {
                forward = doPost(request, rb);
            }
        }
        if (forward == null) {
            forward = Path.MENU_PAGE;
        }
        logger.debug("Edit receipt products command is finished, forwarding to -> " + forward);
        return forward;
    }

    private String doPost(HttpServletRequest request, ResourceBundle rb) {
        logger.debug("Edit receipt products command is started at the POST method");

        HttpSession session = request.getSession();
        Receipt currentReceipt = (Receipt) session.getAttribute("currentReceipt");
        try {
            currentReceipt = receiptDAO.findReceipt(currentReceipt.getId());
        } catch (ApplicationException exception) {
            String errorMessage = "An error has occurred while updating receipt, please try again later";
            session.setAttribute("errorMessage", errorMessage);
            logger.error("errorMessage --> " + exception.getMessage());
            return Commands.ERROR_PAGE_COMMAND;
        }

        if (currentReceipt == null || currentReceipt.getReceiptStatus().name().equals(ReceiptStatus.CLOSED.name())
                || currentReceipt.getReceiptStatus().name().equals(ReceiptStatus.CANCELED.name())) {
            String errorMessage = rb.getString("edit.receipt.products.command.status.error");
            session.setAttribute("errorMessage", errorMessage);
            logger.error("errorMessage --> " + errorMessage);
            return Commands.ERROR_PAGE_COMMAND;
        }
        session.setAttribute("currentReceipt", currentReceipt);

        int productId = Integer.parseInt(request.getParameter("product_id"));
        int receiptId = Integer.parseInt(request.getParameter("receipt_id"));
        int oldAmount = Integer.parseInt(request.getParameter("oldAmount"));
        int newAmount;
        int newProductAmount;
        try {
            newAmount = Integer.parseInt(request.getParameter("newAmount"));
        } catch (NumberFormatException exception) {
            String errorMessage = rb.getString("edit.receipt.products.command.amount.nan");
            session.setAttribute("errorMessage", errorMessage);
            logger.error("errorMessage --> " + errorMessage);
            return Commands.ERROR_PAGE_COMMAND;
        }

        if (newAmount <= 0 || newAmount > 999999999) {
            String errorMessage = rb.getString("edit.receipt.products.command.amount.error.null");
            session.setAttribute("errorMessage", errorMessage);
            logger.error("errorMessage --> " + errorMessage);
            return Commands.ERROR_PAGE_COMMAND;
        }

        Map<Product, Integer> receiptProductsMap;
        try {
            receiptProductsMap = receiptDAO.getMapOfAmountsAndProductsFromReceipt(currentReceipt);
        } catch (ApplicationException exception) {
            String errorMessage = "An error has occurred while retrieving receipt products, please try again later";
            session.setAttribute("errorMessage", errorMessage);
            logger.error("errorMessage --> " + exception.getMessage());
            return Commands.ERROR_PAGE_COMMAND;
        }
        Product product;
        try {
            product = productDAO.findProduct(productId);
        } catch (ApplicationException exception) {
            String errorMessage = "An error has occurred while searching product, please try again later";
            session.setAttribute("errorMessage", errorMessage);
            logger.error("errorMessage --> " + exception.getMessage());
            return Commands.ERROR_PAGE_COMMAND;
        }

        if (!receiptProductsMap.containsKey(product)) {
            String errorMessage = "Product that you are currently updating is already deleted from the receipt";
            session.setAttribute("errorMessage", errorMessage);
            logger.error("errorMessage --> " + errorMessage);
            return Commands.ERROR_PAGE_COMMAND;
        }

        if (oldAmount > newAmount && oldAmount + newAmount <= product.getAmount()) {
            try {
                newProductAmount = product.getAmount() + (oldAmount - newAmount);
                receiptDAO.setAmountOfProductAtTheReceipt(newAmount, newProductAmount, receiptId, productId);
            } catch (ApplicationException exception) {
                String errorMessage = rb.getString("product.dao.error.update.amount");
                session.setAttribute("errorMessage", errorMessage);
                logger.error("errorMessage -> " + errorMessage);
                return Commands.ERROR_PAGE_COMMAND;
            }
            return Commands.VIEW_RECEIPT_PRODUCTS_COMMAND;
        }
        if (product.getAmount() + oldAmount < newAmount) {
            String errorMessage = rb.getString("edit.receipt.products.command.amount.error");
            session.setAttribute("errorMessage", errorMessage);
            logger.error("errorMessage --> " + errorMessage);
            return Commands.ERROR_PAGE_COMMAND;
        }

        try {
            newProductAmount = product.getAmount() + (oldAmount - newAmount);
            receiptDAO.setAmountOfProductAtTheReceipt(newAmount, newProductAmount, receiptId, productId);
        } catch (ApplicationException exception) {
            String errorMessage = rb.getString("product.dao.error.update.amount");
            session.setAttribute("errorMessage", errorMessage);
            logger.error("errorMessage -> " + errorMessage);
            return Commands.ERROR_PAGE_COMMAND;
        }

        return Commands.VIEW_RECEIPT_PRODUCTS_COMMAND;
    }

    private String doGet(HttpServletRequest request, ResourceBundle rb) {
        logger.debug("Edit receipt products command is started at the GET method");
        HttpSession session = request.getSession();

        Receipt currentReceipt = (Receipt) session.getAttribute("currentReceipt");
        try {
            currentReceipt = receiptDAO.findReceipt(currentReceipt.getId());
        } catch (ApplicationException exception) {
            String errorMessage = "An error has occurred while updating receipt, please try again later";
            session.setAttribute("errorMessage", errorMessage);
            logger.error("errorMessage --> " + exception.getMessage());
            return Commands.ERROR_PAGE_COMMAND;
        }

        if (currentReceipt == null || currentReceipt.getReceiptStatus().name().equals(ReceiptStatus.CLOSED.name())
                || currentReceipt.getReceiptStatus().name().equals(ReceiptStatus.CANCELED.name())) {
            String errorMessage = rb.getString("edit.receipt.products.command.status.error");
            session.setAttribute("errorMessage", errorMessage);
            logger.error("errorMessage --> " + errorMessage);
            return Path.ERROR_PAGE;
        }

        session.setAttribute("currentReceipt", currentReceipt);

        Map<Product, Integer> productMap;
        try {
            productMap = receiptDAO.getMapOfAmountsAndProductsFromReceipt(currentReceipt);
        } catch (ApplicationException ex) {
            String errorMessage = "An error has occurred while retrieving receipt products, please try again later";
            session.setAttribute("errorMessage", errorMessage);
            logger.error("errorMessage -> " + ex.getMessage());
            return Path.ERROR_PAGE;
        }
        request.setAttribute("receiptProductMap", productMap);

        return Path.VIEW_RECEIPT_PRODUCTS_PAGE;
    }

}
