package com.my.web.command.common;

import com.my.Path;
import com.my.db.entities.*;
import com.my.web.Commands;
import com.my.web.command.Command;
import com.my.web.exception.DBException;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

public class SearchReceiptCommand extends Command {

    private static final long serialVersionUID = -8129347283482812309L;
    private static final Logger logger = Logger.getLogger(SearchReceiptCommand.class);
    private final ReceiptDAO receiptDAO;

    public SearchReceiptCommand() {
        receiptDAO = new ReceiptDAO();
    }

    public SearchReceiptCommand(ReceiptDAO receiptDAO) {
        this.receiptDAO = receiptDAO;
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        logger.debug("Search receipt command is started");

        HttpSession session = request.getSession();
        Receipt currentReceipt = (Receipt) request.getSession().getAttribute("currentReceipt");
        if (currentReceipt != null) {
            try {
                currentReceipt = receiptDAO.findReceipt(currentReceipt.getId());
            } catch (DBException exception) {
                String errorMessage = "An error has occurred while updating receipt, please try again later";
                session.setAttribute("errorMessage", errorMessage);
                logger.error("errorMessage --> " + exception.getMessage());
                return Commands.ERROR_PAGE_COMMAND;
            }
            session.setAttribute("currentReceipt", currentReceipt);
        }

        //set the number of receipts displayed per page
        int recordsPerPage = 5;
        int currentPage = Integer.parseInt(request.getParameter("currentPage"));
        logger.debug("Current page => " + currentPage);

        String pattern = request.getParameter("receipt_pattern");
        logger.debug("search pattern is ==> " + pattern);
        session.setAttribute("lastSearchReceiptPattern", pattern);

        List<Receipt> result;
        try {
            result = receiptDAO.searchReceipts(pattern, currentPage, recordsPerPage);
        } catch (DBException exception) {
            String errorMessage = "An error has occurred while searching products, please try again later";
            session.setAttribute("errorMessage", errorMessage);
            logger.error("errorMessage --> " + exception.getMessage());
            return Commands.ERROR_PAGE_COMMAND;
        }

        logger.debug("Search result is => " + result);
        request.setAttribute("searchReceiptResult", result);

        int numberOfRows;
        try {
            numberOfRows = receiptDAO.countOfRowsAffectedBySearch(pattern);
        } catch (DBException exception) {
            String errorMessage = "An error has occurred, please try again later";
            session.setAttribute("errorMessage", errorMessage);
            logger.error("errorMessage --> " + exception.getMessage());
            return Commands.ERROR_PAGE_COMMAND;
        }
        logger.debug("Number of rows affected by search " + numberOfRows);
        int nOfPages = numberOfRows / recordsPerPage;
        logger.debug("nOfPages ===>>> " + nOfPages);

        if (numberOfRows % recordsPerPage > 0) {
            nOfPages++;
        }

        request.setAttribute("nOfPages", nOfPages);
        request.setAttribute("currentPage", currentPage);
        session.setAttribute("currentRecPagPage", currentPage);
        request.setAttribute("recordsPerPage", recordsPerPage);

        logger.debug("Search receipt command is finished");
        return Path.VIEW_SEARCH_RECEIPT_RESULT_PAGE;
    }

}
