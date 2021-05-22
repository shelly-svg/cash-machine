package com.my.web.command;

import com.my.Path;
import com.my.db.entities.Product;
import com.my.db.entities.ProductDAO;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class ViewAllProductsCommand extends Command {

    private static final long serialVersionUID = 2348934726283494344L;
    private static final Logger logger = Logger.getLogger(ViewAllProductsCommand.class);


    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        List<Product> productList = new ProductDAO().findAllProducts();
        request.setAttribute("products", productList);
        logger.debug("All products container => " + productList);
        return Path.VIEW_SEARCH_PRODUCTS_PAGE;
    }

}
