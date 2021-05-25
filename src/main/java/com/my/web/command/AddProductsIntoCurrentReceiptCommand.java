package com.my.web.command;

import com.my.Path;
import com.my.db.entities.*;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class AddProductsIntoCurrentReceiptCommand extends Command {
    private static final long serialVersionUID = -2348237473492349742L;
    private static final Logger logger = Logger.getLogger(EditProductCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        logger.debug("Add products into current receipt command is started");
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
        logger.debug("Add products into current receipt command is finished, forwarding to -> " + forward);
        return forward;
    }

    private String doPost(HttpServletRequest request) {
        /*logger.debug("Add products into current receipt command started at POST method");

        int id = Integer.parseInt(request.getParameter("id"));
        logger.debug("Received product => " + id);
        logger.trace("Set session attribute id => " + id);
        int newAmount = Integer.parseInt(request.getParameter("amount"));

        new ProductDAO().updateProductsAmount(id, newAmount);
        logger.debug("Received new amount => " + newAmount);

        logger.debug("Edit product command is finished at POST method, forwarding to view product");
        request.getSession().setAttribute("lastAction", "controller?command=editProduct&id=" + id);*/
        return "controller?command=viewSearchProductPage";
    }

    private String doGet(HttpServletRequest request) {
        logger.debug("Add products into current receipt command started at GET method");
        int id = Integer.parseInt(request.getParameter("id"));
        Receipt currentReceipt = (Receipt) request.getSession().getAttribute("currentReceipt");

        ReceiptDAO receiptDAO = new ReceiptDAO();
        receiptDAO.addProductIntoReceipt(id, currentReceipt.getId());

        logger.debug("RECEIVED PRODUCT ID => " + id);

        return Path.VIEW_SEARCH_PRODUCTS_PAGE;
    }
}
