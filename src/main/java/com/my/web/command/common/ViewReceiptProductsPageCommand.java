package com.my.web.command.common;

import com.my.Path;
import com.my.db.entities.Product;
import com.my.db.entities.Receipt;
import com.my.db.entities.ReceiptDAO;
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

public class ViewReceiptProductsPageCommand extends Command {

    private static final long serialVersionUID = -4923484329592341022L;
    private static final Logger logger = Logger.getLogger(ViewReceiptProductsPageCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        logger.debug("View receipt product page command is stared");

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
        session.setAttribute("currentReceipt", currentReceipt);

        Map<Product, Integer> productMap;
        try {
            productMap = new ReceiptDAO().getMapOfAmountsAndProductsFromReceipt(currentReceipt);
        } catch (DBException ex) {
            String errorMessage = "An error has occurred while retrieving receipt products, please try again later";
            session.setAttribute("errorMessage", errorMessage);
            logger.error("errorMessage -> " + ex.getMessage());
            return Path.ERROR_PAGE;
        }
        request.setAttribute("receiptProductMap", productMap);

        logger.debug("View receipt product page command is finished");
        return Path.VIEW_RECEIPT_PRODUCTS_PAGE;
    }

}
