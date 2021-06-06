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
import java.util.List;

/**
 * Search products command
 */
public class SearchProductsCommand extends Command {

    private static final long serialVersionUID = 2394193249932294933L;
    private static final Logger logger = Logger.getLogger(SearchProductsCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, ApplicationException {
        logger.debug("Search command is started");

        HttpSession session = request.getSession();
        ProductDAO productDAO = new ProductDAO();
        int recordsPerPage = 3;

        int currentPage = Integer.parseInt(request.getParameter("currentPage"));
        logger.debug("Current page => " + currentPage);
        String pattern = request.getParameter("pattern");
        logger.debug("Pattern is => " + pattern);
        session.setAttribute("lastSearchPattern", pattern);

        List<Product> result;
        try {
            result = productDAO.searchProducts(pattern, currentPage, recordsPerPage);
        } catch (DBException ex) {
            String errorMessage = "product.dao.search.products";
            logger.error("errorMessage --> " + ex);
            throw new ApplicationException(errorMessage);
        }

        logger.debug("Search result is => " + result);
        request.setAttribute("searchResult", result);

        int numberOfRows;
        try {
            numberOfRows = productDAO.countOfRowsAffectedBySearch(pattern);
        } catch (DBException ex) {
            String errorMessage = "error.occurred";
            logger.error("errorMessage --> " + ex);
            throw new ApplicationException(errorMessage);
        }
        logger.debug("Number of rows affected by search " + numberOfRows);

        int nOfPages = numberOfRows / recordsPerPage;
        logger.debug("nOfPages ===>>> " + nOfPages);

        if (numberOfRows % recordsPerPage > 0) {
            nOfPages++;
        }

        request.setAttribute("nOfPages", nOfPages);
        request.setAttribute("currentPage", currentPage);
        session.setAttribute("currentPagPage", currentPage);
        request.setAttribute("recordsPerPage", recordsPerPage);

        logger.debug("Search command is finished");
        return Path.VIEW_SEARCH_RESULT_PAGE;
    }

}
