package com.my.web.command.senior_cashier;

import com.my.Path;
import com.my.db.entities.*;
import com.my.web.command.Command;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

public class ViewSearchCashierResultCommand extends Command {

    private static final long serialVersionUID = 8932012939203934290L;
    private static final Logger logger = Logger.getLogger(ViewSearchCashierResultCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        logger.debug("View search cashier result command is started");
        HttpSession session = request.getSession();
        //set the number of users displayed per page
        int recordsPerPage = 2;
        int currentPage = Integer.parseInt(request.getParameter("currentPage"));
        logger.debug("Current page => " + currentPage);

        String firstName = request.getParameter("cashier_first_name");
        String lastName = request.getParameter("cashier_last_name");
        logger.trace("Received first name and last name => " + firstName + " " + lastName);
        session.setAttribute("lastSearchCashierFName", firstName);
        session.setAttribute("lastSearchCashierLName", lastName);

        UserDAO userDAO = new UserDAO();
        List<User> users = userDAO.searchCashiersByName(firstName, lastName, currentPage, recordsPerPage);

        logger.debug("Search cashier result is => " + users);
        request.setAttribute("searchCashiersResult", users);

        int numberOfRows = userDAO.countOfRowsAffectedBySearchCashiers(firstName, lastName);
        logger.debug("Number of rows affected by cashiers search " + numberOfRows);
        int nOfPages = numberOfRows / recordsPerPage;
        logger.debug("nOfPages ===>>> " + nOfPages);

        if (numberOfRows % recordsPerPage > 0) {
            nOfPages++;
        }

        request.setAttribute("nOfPages", nOfPages);
        request.setAttribute("currentPage", currentPage);
        session.setAttribute("currentCashierPagPage", currentPage);
        request.setAttribute("recordsPerPage", recordsPerPage);

        logger.debug("View search cashier result command is finished");
        return Path.VIEW_SEARCH_CASHIER_RESULT_PAGE;
    }
}
