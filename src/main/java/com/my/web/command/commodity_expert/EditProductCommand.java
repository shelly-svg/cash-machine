package com.my.web.command.commodity_expert;

import com.my.Path;
import com.my.db.entities.Product;
import com.my.db.entities.ProductDAO;
import com.my.web.Commands;
import com.my.web.command.Command;
import com.my.web.exception.DBException;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class EditProductCommand extends Command {

    private static final long serialVersionUID = -2348237473492349742L;
    private static final Logger logger = Logger.getLogger(EditProductCommand.class);
    private final ProductDAO productDAO;

    public EditProductCommand() {
        productDAO = new ProductDAO();
    }

    public EditProductCommand(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        logger.debug("Edit product command is started");
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
        logger.debug("Edit product command is finished, forwarding to -> " + forward);
        return forward;
    }

    private String doPost(HttpServletRequest request) {
        logger.debug("Edit product command started at POST method");
        HttpSession session = request.getSession();

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
        logger.debug("Received product => " + id);
        logger.trace("Set session attribute id => " + id);
        int newAmount;
        try {
            newAmount = Integer.parseInt(request.getParameter("amount"));
        } catch (NumberFormatException exception) {
            String errorMessage = rb.getString("add.product.invalid.numeric");
            session.setAttribute("errorMessage", errorMessage);
            return Commands.ERROR_PAGE_COMMAND;
        }

        if (newAmount < 0 || newAmount > 999999999) {
            String errorMessage = rb.getString("add.product.amount.invalid");
            session.setAttribute("errorMessage", errorMessage);
            return Commands.ERROR_PAGE_COMMAND;
        }

        try {
            productDAO.updateProductsAmount(id, newAmount);
        } catch (DBException exception) {
            String errorMessage = rb.getString("product.dao.error.update.amount");
            session.setAttribute("errorMessage", errorMessage);
            logger.error("errorMessage -> " + errorMessage);
            return Commands.ERROR_PAGE_COMMAND;
        }

        logger.debug("Received new amount => " + newAmount);

        logger.debug("Edit product command is finished at POST method, forwarding to view product");
        session.setAttribute("lastAction", "controller?command=editProduct&id=" + id);
        return "controller?command=viewProduct&id=" + id;
    }

    private String doGet(HttpServletRequest request) {
        logger.debug("Edit product command started at GET method");

        HttpSession session = request.getSession();
        int id;
        try {
            id = Integer.parseInt(request.getParameter("id"));
        } catch (NumberFormatException exception) {
            String errorMessage = "Product with requested index does not exist";
            session.setAttribute("errorMessage", errorMessage);
            logger.error("errorMessage -> " + exception.getMessage());
            return Commands.ERROR_PAGE_COMMAND;
        }

        Product product;
        try {
            product = productDAO.findProduct(id);
        } catch (DBException exception) {
            String errorMessage = "An error has occurred while retrieving product, try again later";
            session.setAttribute("errorMessage", errorMessage);
            logger.error("errorMessage -> " + exception.getMessage());
            return Commands.ERROR_PAGE_COMMAND;
        }

        if (product == null) {
            String errorMessage = "Product with requested index does not exist, try again later";
            session.setAttribute("errorMessage", errorMessage);
            logger.error("errorMessage -> " + errorMessage);
            return Commands.ERROR_PAGE_COMMAND;
        }

        request.setAttribute("product", product);
        request.getSession().setAttribute("lastEditedProductId", id);
        return Path.EDIT_PRODUCT_PAGE;
    }

}
