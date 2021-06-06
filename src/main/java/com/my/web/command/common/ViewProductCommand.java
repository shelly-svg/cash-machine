package com.my.web.command.common;

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

public class ViewProductCommand extends Command {

    private static final long serialVersionUID = -923486238461238432L;
    private static final Logger logger = Logger.getLogger(ViewProductCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, ApplicationException {
        logger.debug("View Product command is started");

        HttpSession session = request.getSession();

        int id;
        try {
            id = Integer.parseInt(request.getParameter("id"));
        } catch (NumberFormatException exception) {
            String errorMessage = "error.occurred";
            logger.error("errorMessage => " + exception);
            throw new ApplicationException(errorMessage);
        }

        Product product;
        try {
            product = new ProductDAO().findProduct(id);
        } catch (DBException exception) {
            String errorMessage = "product.dao.find.product";
            logger.error("errorMessage --> " + exception);
            throw new ApplicationException(errorMessage);
        }
        logger.debug("Received product => " + product);
        if (product == null) {
            String errorMessage = "product.doesnt.exist";
            logger.error("errorMessage --> " + "Chosen product does not exist");
            throw new ApplicationException(errorMessage);
        }

        request.setAttribute("product", product);
        session.setAttribute("lastViewedProductId", id);

        logger.debug("View Product command is finished");
        return Path.VIEW_PRODUCT_PAGE;
    }

}
