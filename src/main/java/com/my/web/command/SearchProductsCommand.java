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
import java.util.List;
import java.util.Map;

public class SearchProductsCommand extends Command {

    private static final long serialVersionUID = 2394193249932294933L;
    private static final Logger logger = Logger.getLogger(AddProductCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        logger.debug("Search command is started");

        int recordsPerPage = 3;
        int currentPage = Integer.parseInt(request.getParameter("currentPage"));
        logger.debug("Current page => " + currentPage);

        String pattern = request.getParameter("pattern");
        logger.debug("Pattern is => " + pattern);
        request.getSession().setAttribute("lastSearchPattern", pattern);
        ProductDAO productDAO = new ProductDAO();

        Map<Integer, Category> categories = new CategoryDAO().findAllCategories();
        request.setAttribute("categories", categories);
        List<Product> result = productDAO.searchProducts(pattern, currentPage, recordsPerPage);
        logger.debug("Search result is => " + result);
        request.setAttribute("searchResult", result);

        int numberOfRows = productDAO.countOfRowsAffectedBySearch(pattern);
        logger.debug("Number of rows affected by search " + numberOfRows);
        int nOfPages = numberOfRows / recordsPerPage;
        logger.debug("nOfPages ===>>> " + nOfPages);
        if (numberOfRows % recordsPerPage > 0) {
            nOfPages++;
        }
        request.setAttribute("nOfPages", nOfPages);
        request.setAttribute("currentPage", currentPage);
        request.getSession().setAttribute("currentPagPage", currentPage);
        request.setAttribute("recordsPerPage", recordsPerPage);

        logger.debug("Search command is finished");
        return Path.VIEW_SEARCH_RESULT_PAGE;
    }

}
