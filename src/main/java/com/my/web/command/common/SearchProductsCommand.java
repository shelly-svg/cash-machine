package com.my.web.command.common;

import com.my.Path;
import com.my.db.entities.Product;
import com.my.db.entities.Receipt;
import com.my.db.entities.Role;
import com.my.db.entities.dao.ProductDAO;
import com.my.db.entities.dao.ReceiptDAO;
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
    private final ProductDAO productDAO;
    private final ReceiptDAO receiptDAO;

    public SearchProductsCommand() {
        productDAO = new ProductDAO();
        receiptDAO = new ReceiptDAO();
    }

    public SearchProductsCommand(ProductDAO productDAO, ReceiptDAO receiptDAO) {
        this.productDAO = productDAO;
        this.receiptDAO = receiptDAO;
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, ApplicationException {
        logger.debug("Search command is started");

        HttpSession session = request.getSession();
        int recordsPerPage = 4;

        int currentPage;
        try {
            currentPage = Integer.parseInt(request.getParameter("currentPage"));
        } catch (NumberFormatException exception) {
            String errorMessage = "error.occurred";
            logger.error("errorMessage --> " + exception);
            throw new ApplicationException(errorMessage);
        }

        logger.debug("Current page => " + currentPage);
        String pattern = request.getParameter("pattern");
        if (pattern == null || pattern.isEmpty()) {
            String errorMessage = "error.occurred";
            logger.error("Search pattern is empty");
            throw new ApplicationException(errorMessage);
        }
        logger.debug("Pattern is => " + pattern);

        request.setAttribute("lastSearchPattern", pattern);

        List<Product> result;
        int numberOfRows;
        Role userRole = (Role) session.getAttribute("userRole");
        if (userRole.equals(Role.COMMODITY_EXPERT)) {
            try {
                result = productDAO.searchProducts(pattern, currentPage, recordsPerPage);
                numberOfRows = productDAO.countOfRowsAffectedBySearch(pattern);
            } catch (DBException ex) {
                String errorMessage = "product.dao.search.products";
                logger.error("errorMessage --> " + ex);
                throw new ApplicationException(errorMessage);
            }
        } else {
            try {
                Receipt currentReceipt = (Receipt) session.getAttribute("currentReceipt");
                if (currentReceipt == null) {
                    logger.error("User haven`t choose receipt");
                    throw new ApplicationException("error.occurred");
                }
                currentReceipt = receiptDAO.findReceipt(currentReceipt.getId());
                result = productDAO.searchProducts(currentReceipt, pattern, currentPage, recordsPerPage);
                numberOfRows = productDAO.countOfRowsAffectedBySearch(currentReceipt, pattern);
            } catch (DBException ex) {
                String errorMessage = "product.dao.search.products";
                logger.error("errorMessage --> " + ex);
                throw new ApplicationException(errorMessage);
            }
        }

        logger.debug("Search result is => " + result);
        request.setAttribute("searchResult", result);

        logger.debug("Number of rows affected by search " + numberOfRows);
        int nOfPages = numberOfRows / recordsPerPage;
        logger.debug("nOfPages ===>>> " + nOfPages);

        if (numberOfRows % recordsPerPage > 0) {
            nOfPages++;
        }

        request.setAttribute("nOfPages", nOfPages);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("recordsPerPage", recordsPerPage);

        logger.debug("Search command is finished");
        return Path.VIEW_SEARCH_RESULT_PAGE;
    }

}
