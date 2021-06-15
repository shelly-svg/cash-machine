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
import java.io.IOException;
import java.util.List;

/**
 * View search product page command
 */
public class ViewSearchProductPageCommand extends Command {

    private static final long serialVersionUID = 2348934726283494344L;
    private static final Logger logger = Logger.getLogger(ViewSearchProductPageCommand.class);
    private final ProductDAO productDAO;

    public ViewSearchProductPageCommand() {
        productDAO = new ProductDAO();
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, ApplicationException {
        logger.debug("View search page command is stared");

        int recordsPerPage = 4;

        int currentPage;
        if (request.getParameter("currentPage") == null) {
            currentPage = 1;
        } else {
            try {
                currentPage = Integer.parseInt(request.getParameter("currentPage"));
            } catch (NumberFormatException exception) {
                String errorMessage = "error.occurred";
                logger.error("errorMessage --> " + exception);
                throw new ApplicationException(errorMessage);
            }
        }
        List<Product> allProducts;
        int numberOfRows;
        String sort = request.getParameter("sort");

        try {
            numberOfRows = productDAO.numberOfAllProducts();
        } catch (DBException exception) {
            exception.printStackTrace();
            throw new ApplicationException();
        }
        if ("byName".equals(sort)) {
            try {
                allProducts = productDAO.findAllProductsSortByName(currentPage, recordsPerPage);
            } catch (DBException exception) {
                exception.printStackTrace();
                throw new ApplicationException();
            }
        } else if ("byPrice".equals(sort)) {
            try {
                allProducts = productDAO.findAllProductsSortByPrice(currentPage, recordsPerPage);
            } catch (DBException exception) {
                exception.printStackTrace();
                throw new ApplicationException();
            }
        } else {
            try {
                allProducts = productDAO.findAllProducts(currentPage, recordsPerPage);
            } catch (DBException exception) {
                exception.printStackTrace();
                throw new ApplicationException();
            }
        }
        request.setAttribute("sort", request.getParameter("sort"));

        request.setAttribute("allProducts", allProducts);

        logger.debug("Number of rows of products" + numberOfRows);
        int nOfPages = numberOfRows / recordsPerPage;
        logger.debug("nOfPages ===>>> " + nOfPages);

        if (numberOfRows % recordsPerPage > 0) {
            nOfPages++;
        }

        request.setAttribute("nOfPages", nOfPages);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("recordsPerPage", recordsPerPage);
        logger.debug("View search page command is finished");
        return Path.VIEW_SEARCH_PRODUCTS_PAGE;
    }

}
