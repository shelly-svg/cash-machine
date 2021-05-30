package com.my.web.command.common;

import com.my.Path;
import com.my.db.entities.*;
import com.my.web.command.Command;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class SearchReceiptCommand extends Command {

    private static final long serialVersionUID = -8129347283482812309L;
    private static final Logger logger = Logger.getLogger(SearchReceiptCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        logger.debug("Search receipt command is started");

        Receipt currentReceipt = (Receipt) request.getSession().getAttribute("currentReceipt");
        if (currentReceipt != null) {
            currentReceipt = new ReceiptDAO().findReceipt(currentReceipt.getId());
            request.getSession().setAttribute("currentReceipt", currentReceipt);
        }

        /* String errorMessage = rb.getString("edit.receipt.products.command.status.error");
            session.setAttribute("errorMessage", errorMessage);
            logger.error("errorMessage --> " + errorMessage);
            return Commands.ERROR_PAGE_COMMAND;*/

        //set the number of receipts displayed per page
        int recordsPerPage = 5;
        int currentPage = Integer.parseInt(request.getParameter("currentPage"));
        logger.debug("Current page => " + currentPage);

        String pattern = request.getParameter("receipt_pattern");
        logger.debug("Pattern is ==> " + pattern);
        request.getSession().setAttribute("lastSearchReceiptPattern", pattern);

        ReceiptDAO receiptDAO = new ReceiptDAO();
        List<Receipt> result = receiptDAO.searchReceipts(pattern, currentPage, recordsPerPage);

        logger.debug("Search result is => " + result);
        request.setAttribute("searchReceiptResult", result);

        int numberOfRows = receiptDAO.countOfRowsAffectedBySearch(pattern);
        logger.debug("Number of rows affected by search " + numberOfRows);
        int nOfPages = numberOfRows / recordsPerPage;
        logger.debug("nOfPages ===>>> " + nOfPages);

        if (numberOfRows % recordsPerPage > 0) {
            nOfPages++;
        }

        request.setAttribute("nOfPages", nOfPages);
        request.setAttribute("currentPage", currentPage);
        request.getSession().setAttribute("currentRecPagPage", currentPage);
        request.setAttribute("recordsPerPage", recordsPerPage);

        logger.debug("Search command is finished");
        return Path.VIEW_SEARCH_RECEIPT_RESULT_PAGE;
    }

}
