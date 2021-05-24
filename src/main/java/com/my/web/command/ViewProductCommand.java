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

public class ViewProductCommand extends Command {

    private static final long serialVersionUID = -923486238461238432L;
    private static final Logger logger = Logger.getLogger(ViewProductCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        logger.debug("View Product command is started");
        int id = Integer.parseInt(request.getParameter("id"));
        Product product = new ProductDAO().findProduct(id);
        logger.trace("Received product => " + product);
        request.setAttribute("product", product);
        request.getSession().setAttribute("lastViewedProductId", id);
        logger.debug("View Product command is finished");
        return Path.VIEW_PRODUCT_PAGE;
    }

}
