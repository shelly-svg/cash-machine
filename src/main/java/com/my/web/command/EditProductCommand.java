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
import java.util.Map;

public class EditProductCommand extends Command {

    private static final long serialVersionUID = -2348237473492349742L;
    private static final Logger logger = Logger.getLogger(EditProductCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String forward = null;
        logger.debug("Edit product command is started");
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
        return "controller?command=viewMenu";
    }

    private String doGet(HttpServletRequest request) {
        logger.debug("Edit product command started at GET method");
        int id = Integer.parseInt(request.getParameter("id"));
        Product product = new ProductDAO().findProduct(id);
        request.setAttribute("product", product);
        Category category = new CategoryDAO().findCategoryById(product.getCategoryId());
        request.setAttribute("category", category);
        return Path.EDIT_PRODUCT_PAGE;
    }
}
