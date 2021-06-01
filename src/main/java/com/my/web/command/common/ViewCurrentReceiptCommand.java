package com.my.web.command.common;

import com.my.Path;
import com.my.db.entities.*;
import com.my.web.command.Command;
import com.my.web.exception.ApplicationException;
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
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, ApplicationException {
        logger.debug("View current receipt command is started");

        HttpSession session = request.getSession();
        Receipt currentReceipt = (Receipt) session.getAttribute("currentReceipt");

        try {
            currentReceipt = new ReceiptDAO().findReceipt(currentReceipt.getId());
        } catch (DBException exception) {
            String errorMessage = "receipt.dao.find.receipt";
            logger.error("errorMessage --> " + exception);
            throw new ApplicationException(errorMessage);
        }

        Map<Product, Integer> productMap;
        try {
            productMap = new ReceiptDAO().getMapOfAmountsAndProductsFromReceipt(currentReceipt);
        } catch (DBException exception) {
            String errorMessage = "error.occurred";
            logger.error("errorMessage -> " + exception);
            throw new ApplicationException(errorMessage);
        }
        request.setAttribute("currentReceiptProductMap", productMap);

        String creator;
        try {
            creator = new UserDAO().findUsersFNameLName(currentReceipt.getUserId());
        } catch (DBException exception) {
            String errorMessage = "user.dao.creator";
            logger.error("errorMessage -> " + exception);
            throw new ApplicationException(errorMessage);
        }

        request.setAttribute("creator", creator);
        session.setAttribute("currentReceipt", currentReceipt);

        logger.debug("View current receipt command is finished");
        return Path.VIEW_CURRENT_RECEIPT_PAGE;
    }

}
