package com.my.web.command.common;

import com.my.Path;
import com.my.db.entities.Product;
import com.my.db.entities.ProductDAO;
import com.my.web.command.Command;
import com.my.web.exception.DBException;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class ViewProductCommand extends Command {

    private static final long serialVersionUID = -923486238461238432L;
    private static final Logger logger = Logger.getLogger(ViewProductCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        logger.debug("View Product command is started");
        HttpSession session = request.getSession();
        int id = Integer.parseInt(request.getParameter("id"));
        Product product;
        try {
            product = new ProductDAO().findProduct(id);
        } catch (DBException exception) {
            String errorMessage = "An error has occurred while searching product, please try again later";
            session.setAttribute("errorMessage", errorMessage);
            logger.error("errorMessage --> " + exception.getMessage());
            return Path.ERROR_PAGE;
        }
        logger.trace("Received product => " + product);
        request.setAttribute("product", product);
        request.getSession().setAttribute("lastViewedProductId", id);
        logger.debug("View Product command is finished");
        return Path.VIEW_PRODUCT_PAGE;
    }

}
