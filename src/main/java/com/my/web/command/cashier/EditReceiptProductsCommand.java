package com.my.web.command.cashier;

import com.my.Path;
import com.my.db.entities.Product;
import com.my.db.entities.Receipt;
import com.my.db.entities.ReceiptDAO;
import com.my.db.entities.ReceiptStatus;
import com.my.web.command.Command;
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

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        logger.debug("Edit receipt products command is started");
        String forward = null;
        logger.debug("REQUEST METHOD =>  " + request.getMethod());
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

    private String doPost(HttpServletRequest request) {
        logger.debug("Edit receipt products command is started at the POST method");

        HttpSession session = request.getSession();
        Receipt currentReceipt = (Receipt) session.getAttribute("currentReceipt");
        currentReceipt = new ReceiptDAO().findReceipt(currentReceipt.getId());

        if (currentReceipt == null || currentReceipt.getReceiptStatus().name().equals(ReceiptStatus.CLOSED.name())
                || currentReceipt.getReceiptStatus().name().equals(ReceiptStatus.CANCELED.name())) {
            String errorMessage = "You cannot edit products at the closed and canceled receipts";
            session.setAttribute("errorMessage", errorMessage);
            logger.error("errorMessage --> " + errorMessage);
            return "controller?command=noCommand";
        }
        session.setAttribute("currentReceipt", currentReceipt);

        int productId = Integer.parseInt(request.getParameter("product_id"));
        int receiptId = Integer.parseInt(request.getParameter("receipt_id"));
        int newAmount = Integer.parseInt(request.getParameter("newAmount"));
        logger.debug("===>>>>>" + productId + " " + receiptId + " , new Amount = " + newAmount);

        new ReceiptDAO().setAmountOfProductAtTheReceipt(newAmount, receiptId, productId);
        return "controller?command=viewReceiptProducts";
    }

    private String doGet(HttpServletRequest request) {
        logger.debug("Edit receipt products command is started at the GET method");

        HttpSession session = request.getSession();
        Receipt currentReceipt = (Receipt) session.getAttribute("currentReceipt");
        currentReceipt = new ReceiptDAO().findReceipt(currentReceipt.getId());

        if (currentReceipt == null || currentReceipt.getReceiptStatus().name().equals(ReceiptStatus.CLOSED.name())
                || currentReceipt.getReceiptStatus().name().equals(ReceiptStatus.CANCELED.name())) {
            String errorMessage = "You cannot edit products at the closed and canceled receipts";
            request.setAttribute("errorMessage", errorMessage);
            logger.error("errorMessage --> " + errorMessage);
            return Path.ERROR_PAGE;
        }
        session.setAttribute("currentReceipt", currentReceipt);

        Map<Product, Integer> productMap = new ReceiptDAO().getMapOfAmountsAndProductsFromReceipt(currentReceipt);
        request.setAttribute("receiptProductMap", productMap);

        return Path.VIEW_RECEIPT_PRODUCTS_PAGE;
    }

}
