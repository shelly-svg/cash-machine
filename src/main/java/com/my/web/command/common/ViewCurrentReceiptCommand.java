package com.my.web.command.common;

import com.my.Path;
import com.my.db.entities.*;
import com.my.web.command.Command;
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

        currentReceipt = new ReceiptDAO().findReceipt(currentReceipt.getId());

        Map<Product, Integer> productMap = new ReceiptDAO().getMapOfAmountsAndProductsFromReceipt(currentReceipt);
        request.setAttribute("currentReceiptProductMap", productMap);

        String user = new UserDAO().findUsersFNameLName(currentReceipt.getUserId());
        request.setAttribute("creator", user);
        session.setAttribute("currentReceipt", currentReceipt);

        logger.debug("View current receipt command is finished");
        return Path.VIEW_RECEIPT_PAGE;
    }

}
