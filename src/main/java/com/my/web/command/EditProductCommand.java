package com.my.web.command;

import com.my.Path;
import com.my.db.entities.Category;
import com.my.db.entities.CategoryDAO;
import com.my.db.entities.Product;
import com.my.db.entities.ProductDAO;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class EditProductCommand extends Command {

    private static final long serialVersionUID = -2348237473492349742L;
    private static final Logger logger = Logger.getLogger(EditProductCommand.class);

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

        int id = Integer.parseInt(request.getParameter("id"));
        logger.debug("Received product => " + id);
        logger.trace("Set session attribute id => " + id);
        int newAmount = Integer.parseInt(request.getParameter("amount"));

        new ProductDAO().updateProductsAmount(id, newAmount);
        logger.debug("Received new amount => " + newAmount);

        logger.debug("Edit product command is finished at POST method, forwarding to view product");
        request.getSession().setAttribute("lastAction", "controller?command=editProduct&id=" + id);
        return "controller?command=viewProduct&id=" + id;
    }

    private String doGet(HttpServletRequest request) {
        logger.debug("Edit product command started at GET method");
        int id = Integer.parseInt(request.getParameter("id"));
        Product product = new ProductDAO().findProduct(id);
        request.setAttribute("product", product);
        Category category = new CategoryDAO().findCategoryById(product.getCategoryId());
        request.setAttribute("category", category);
        request.getSession().setAttribute("lastEditedProductId", id);
        return Path.EDIT_PRODUCT_PAGE;
    }

}
