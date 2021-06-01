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
import java.util.Map;

public class ViewCurrentReceiptCommand extends Command {

    private static final long serialVersionUID = -8293127324782348212L;
    private static final Logger logger = Logger.getLogger(ViewCurrentReceiptCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        logger.debug("View current receipt command is started");

        HttpSession session = request.getSession();
        Receipt currentReceipt = (Receipt) session.getAttribute("currentReceipt");

        try {
            currentReceipt = new ReceiptDAO().findReceipt(currentReceipt.getId());
        } catch (DBException exception) {
            String errorMessage = "An error has occurred while updating receipt, please try again later";
            session.setAttribute("errorMessage", errorMessage);
            logger.error("errorMessage --> " + exception.getMessage());
            return Commands.ERROR_PAGE_COMMAND;
        }

        Map<Product, Integer> productMap;
        try {
            productMap = new ReceiptDAO().getMapOfAmountsAndProductsFromReceipt(currentReceipt);
        } catch (DBException exception) {
            String errorMessage = "An error has occurred while retrieving receipt products, please try again later";
            session.setAttribute("errorMessage", errorMessage);
            logger.error("errorMessage -> " + exception.getMessage());
            return Path.ERROR_PAGE;
        }

        request.setAttribute("currentReceiptProductMap", productMap);
        String user;
        try {
            user = new UserDAO().findUsersFNameLName(currentReceipt.getUserId());
        } catch (DBException exception) {
            String errorMessage = "An error has occurred while retrieving user information, please try again later";
            session.setAttribute("errorMessage", errorMessage);
            logger.error("errorMessage -> " + exception.getMessage());
            return Path.ERROR_PAGE;
        }
        request.setAttribute("creator", user);
        session.setAttribute("currentReceipt", currentReceipt);

        logger.debug("View current receipt command is finished");
        return Path.VIEW_CURRENT_RECEIPT_PAGE;
    }

}
