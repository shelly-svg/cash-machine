package com.my.web.command;

import com.my.Path;
import com.my.db.entities.*;
import com.my.web.Commands;
import com.my.web.LocalizationUtils;
import com.my.web.exception.ApplicationException;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class ChangeLangToEnCommand extends Command {

    private static final long serialVersionUID = -3402394287548203940L;
    private static final Logger logger = Logger.getLogger(ChangeLangToEnCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        logger.debug("Change lang to english command is started");
        String forward;
        HttpSession session = request.getSession();
        ResourceBundle rb = LocalizationUtils.getCurrentRb(session);
        System.out.println("SESSION ATR" + session.getAttribute("lastAction"));
        forward = (String) session.getAttribute("lastAction");
        logger.trace("Received session attribute => " + forward);
        session.setAttribute("lang", "en");
        logger.trace("Set session attribute lang => en");
        ReceiptDAO receiptDAO = new ReceiptDAO();
        if (forward == null) {
            return Path.LOGIN_PAGE;
        }
        if (Path.ERROR_PAGE.equals(forward) && session.getAttribute("user") == null) {
            return Path.LOGIN_PAGE;
        }
        if (Path.ADD_PRODUCT_PAGE.equals(forward)) {
            try {
                Map<Integer, Category> categories = new CategoryDAO().findAllCategories();
                request.setAttribute("categories", categories);
            } catch (ApplicationException ex) {
                String errorMessage = rb.getString("category.dao.error");
                session.setAttribute("errorMessage", errorMessage);
                logger.error("errorMessage -> " + ex.getMessage());
                return Path.ERROR_PAGE;
            }
        }
        if (Path.VIEW_PRODUCT_PAGE.equals(forward)) {
            forward = "controller?command=viewProduct&id=" + request.getSession().getAttribute("lastViewedProductId");
        }
        if (Path.VIEW_SEARCH_CASHIER_RESULT_PAGE.equals(forward)) {
            forward = "controller?command=viewSearchCashierResult&cashier_first_name=" + request.getSession().getAttribute("lastSearchCashierFName") +
                    "&cashier_last_name=" + request.getSession().getAttribute("lastSearchCashierLName") + "&currentPage=" + request.getSession().getAttribute("currentCashierPagPage");
        }
        if (Path.VIEW_SEARCH_RECEIPT_RESULT_PAGE.equals(forward)) {
            forward = "controller?command=searchReceipt&receipt_pattern=" + request.getSession().getAttribute("lastSearchReceiptPattern") +
                    "&currentPage=" + request.getSession().getAttribute("currentRecPagPage");
        }
        if (Path.VIEW_SEARCH_RESULT_PAGE.equals(forward)) {
            forward = "controller?command=searchProduct&pattern=" + request.getSession().getAttribute("lastSearchPattern") + "&currentPage=" + request.getSession().getAttribute("currentPagPage");
        }
        if (Path.CREATE_RECEIPT_PAGE.equals(forward)) {
            try {
                List<Delivery> deliveries = new DeliveryDAO().getAllDeliveries();
                request.setAttribute("deliveries", deliveries);
            } catch (ApplicationException exception) {
                String errorMessage = rb.getString("delivery.dao.error");
                session.setAttribute("errorMessage", errorMessage);
                logger.error("errorMessage -> " + exception.getMessage());
                return Path.ERROR_PAGE;
            }
        }
        if (Path.EDIT_PRODUCT_PAGE.equals(forward)) {
            forward = "controller?command=editProduct&id=" + request.getSession().getAttribute("lastEditedProductId");
        }
        if (Path.VIEW_CURRENT_RECEIPT_PAGE.equals(forward)) {
            Receipt currentReceipt = (Receipt) session.getAttribute("currentReceipt");
            try {
                currentReceipt = receiptDAO.findReceipt(currentReceipt.getId());
            } catch (ApplicationException exception) {
                String errorMessage = "An error has occurred while updating receipt, please try again later";
                session.setAttribute("errorMessage", errorMessage);
                logger.error("errorMessage --> " + exception.getMessage());
                return Commands.ERROR_PAGE_COMMAND;
            }

            Map<Product, Integer> productMap;
            try {
                productMap = new ReceiptDAO().getMapOfAmountsAndProductsFromReceipt(currentReceipt);
            } catch (ApplicationException ex) {
                String errorMessage = "An error has occurred while retrieving receipt products, please try again later";
                session.setAttribute("errorMessage", errorMessage);
                logger.error("errorMessage -> " + ex.getMessage());
                return Path.ERROR_PAGE;
            }
            request.setAttribute("currentReceiptProductMap", productMap);

            String user;
            try {
                user = new UserDAO().findUsersFNameLName(currentReceipt.getUserId());
            } catch (ApplicationException exception) {
                String errorMessage = "An error has occurred while retrieving user information, please try again later";
                session.setAttribute("errorMessage", errorMessage);
                logger.error("errorMessage -> " + exception.getMessage());
                return Path.ERROR_PAGE;
            }
            request.setAttribute("creator", user);
        }
        if (Path.VIEW_RECEIPT_PRODUCTS_PAGE.equals(forward)) {
            Receipt currentReceipt = (Receipt) request.getSession().getAttribute("currentReceipt");
            try {
                currentReceipt = receiptDAO.findReceipt(currentReceipt.getId());
            } catch (ApplicationException exception) {
                String errorMessage = "An error has occurred while updating receipt, please try again later";
                session.setAttribute("errorMessage", errorMessage);
                logger.error("errorMessage --> " + exception.getMessage());
                return Commands.ERROR_PAGE_COMMAND;
            }

            Map<Product, Integer> productMap;
            try {
                productMap = new ReceiptDAO().getMapOfAmountsAndProductsFromReceipt(currentReceipt);
            } catch (ApplicationException ex) {
                String errorMessage = "An error has occurred while retrieving receipt products, please try again later";
                session.setAttribute("errorMessage", errorMessage);
                logger.error("errorMessage -> " + ex.getMessage());
                return Path.ERROR_PAGE;
            }

            request.setAttribute("receiptProductMap", productMap);
        }

        logger.debug("Change lang to english command is finished");
        return forward;
    }

}
