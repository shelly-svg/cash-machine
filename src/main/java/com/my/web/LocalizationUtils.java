package com.my.web;

import com.my.Path;
import com.my.db.entities.*;
import com.my.db.entities.dao.CategoryDAO;
import com.my.db.entities.dao.DeliveryDAO;
import com.my.db.entities.dao.ReceiptDAO;
import com.my.db.entities.dao.UserDAO;
import com.my.web.exception.ApplicationException;
import com.my.web.exception.DBException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public class LocalizationUtils {

    private LocalizationUtils() {
    }

    public static String getAction(HttpSession session, HttpServletRequest request) throws ApplicationException {
        ReceiptDAO receiptDAO = new ReceiptDAO();
        String forward = (String) session.getAttribute("lastAction");
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
            } catch (DBException exception) {
                String errorMessage = "category.dao.error";
                throw new ApplicationException(errorMessage);
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
            } catch (DBException exception) {
                String errorMessage = "delivery.dao.error";
                throw new ApplicationException(errorMessage);
            }
        }
        if (Path.EDIT_PRODUCT_PAGE.equals(forward)) {
            forward = "controller?command=editProduct&id=" + request.getSession().getAttribute("lastEditedProductId");
        }
        if (Path.VIEW_CURRENT_RECEIPT_PAGE.equals(forward)) {
            Receipt currentReceipt = (Receipt) session.getAttribute("currentReceipt");
            try {
                currentReceipt = receiptDAO.findReceipt(currentReceipt.getId());
            } catch (DBException exception) {
                String errorMessage = "receipt.dao.find.receipt";
                throw new ApplicationException(errorMessage);
            }

            Map<Product, Integer> productMap;
            try {
                productMap = new ReceiptDAO().getMapOfAmountsAndProductsFromReceipt(currentReceipt);
            } catch (DBException exception) {
                String errorMessage = "receipt.dao.receipt.products.amounts";
                throw new ApplicationException(errorMessage);
            }
            request.setAttribute("currentReceiptProductMap", productMap);
            String user;
            try {
                user = new UserDAO().findUsersFNameLName(currentReceipt.getUserId());
            } catch (DBException exception) {
                String errorMessage = "user.dao.creator";
                throw new ApplicationException(errorMessage);
            }

            request.setAttribute("creator", user);
        }
        if (Path.VIEW_RECEIPT_PRODUCTS_PAGE.equals(forward)) {
            Receipt currentReceipt = (Receipt) request.getSession().getAttribute("currentReceipt");
            try {
                currentReceipt = receiptDAO.findReceipt(currentReceipt.getId());
            } catch (DBException exception) {
                String errorMessage = "receipt.dao.find.receipt";
                throw new ApplicationException(errorMessage);
            }

            Map<Product, Integer> productMap;
            try {
                productMap = new ReceiptDAO().getMapOfAmountsAndProductsFromReceipt(currentReceipt);
            } catch (DBException ex) {
                String errorMessage = "receipt.dao.receipt.products.amounts";
                throw new ApplicationException(errorMessage);
            }

            request.setAttribute("receiptProductMap", productMap);
        }
        return forward;
    }

    public static ResourceBundle getCurrentRb(HttpSession session) {

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
        return ResourceBundle.getBundle("resources", locale);
    }

    public static ResourceBundle getEnglishRb() {
        Locale locale = new Locale("en", "EN");
        return ResourceBundle.getBundle("resources", locale);
    }

}
