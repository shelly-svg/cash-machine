package com.my.web.command.commodity_expert;

import com.my.Path;
import com.my.db.entities.Product;
import com.my.db.entities.dao.ProductDAO;
import com.my.web.command.Command;
import com.my.web.exception.ApplicationException;
import com.my.web.exception.DBException;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Edit product command
 */
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
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, ApplicationException {
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

    private String doPost(HttpServletRequest request) throws ApplicationException {
        logger.debug("Edit product command started at POST method");
        HttpSession session = request.getSession();

        int id;
        try {
            id = Integer.parseInt(request.getParameter("id"));
        } catch (NumberFormatException exception) {
            String errorMessage = "error.occurred";
            logger.error("errorMessage =>" + exception);
            throw new ApplicationException(errorMessage);
        }
        logger.debug("Received product => " + id);

        int newAmount;
        try {
            newAmount = Integer.parseInt(request.getParameter("amount"));
        } catch (NumberFormatException exception) {
            String errorMessage = "add.product.invalid.numeric";
            logger.error("errorMessage --> " + exception);
            throw new ApplicationException(errorMessage);
        }

        if (newAmount < 0 || newAmount > 999999999) {
            String errorMessage = "add.product.amount.invalid";
            logger.error("errorMessage --> amount invalid");
            throw new ApplicationException(errorMessage);
        }

        try {
            productDAO.updateProductsAmount(id, newAmount);
        } catch (DBException exception) {
            String errorMessage = "product.dao.error.update.amount";
            logger.error("errorMessage --> " + exception);
            throw new ApplicationException(errorMessage);
        }

        logger.debug("Received new amount => " + newAmount);

        logger.debug("Edit product command is finished at POST method, forwarding to view product");
        return "controller?command=viewProduct&id=" + id;
    }

    private String doGet(HttpServletRequest request) throws ApplicationException {
        logger.debug("Edit product command started at GET method");

        int id;
        try {
            id = Integer.parseInt(request.getParameter("id"));
        } catch (NumberFormatException exception) {
            String errorMessage = "error.occurred";
            logger.error("errorMessage --> " + exception);
            throw new ApplicationException(errorMessage);
        }

        Product product;
        try {
            product = productDAO.findProduct(id);
        } catch (DBException exception) {
            String errorMessage = "product.dao.find.product";
            logger.error("errorMessage --> " + exception);
            throw new ApplicationException(errorMessage);
        }

        if (product == null) {
            String errorMessage = "number.format.exception.product";
            logger.error("errorMessage --> invalid product");
            throw new ApplicationException(errorMessage);
        }

        request.setAttribute("product", product);
        return Path.EDIT_PRODUCT_PAGE;
    }

}
