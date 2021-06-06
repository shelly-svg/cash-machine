package com.my.web.command.common;

import com.my.Path;
import com.my.db.entities.Product;
import com.my.db.entities.Receipt;
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
import java.util.Map;

/**
 * View receipt products page command
 */
public class ViewReceiptProductsPageCommand extends Command {

    private static final long serialVersionUID = -4923484329592341022L;
    private static final Logger logger = Logger.getLogger(ViewReceiptProductsPageCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, ApplicationException {
        logger.debug("View receipt product page command is stared");

        HttpSession session = request.getSession();

        Receipt currentReceipt = (Receipt) session.getAttribute("currentReceipt");
        try {
            currentReceipt = new ReceiptDAO().findReceipt(currentReceipt.getId());
        } catch (DBException exception) {
            String errorMessage = "receipt.dao.find.receipt ";
            logger.error("errorMessage --> " + exception);
            throw new ApplicationException(errorMessage);
        }
        logger.debug("Set session attribute: updated current receipt info => " + currentReceipt);
        session.setAttribute("currentReceipt", currentReceipt);

        Map<Product, Integer> productMap;
        try {
            productMap = new ReceiptDAO().getMapOfAmountsAndProductsFromReceipt(currentReceipt);
        } catch (DBException ex) {
            String errorMessage = "receipt.dao.receipt.products.amounts";
            logger.error("errorMessage -> " + ex);
            throw new ApplicationException(errorMessage);
        }
        logger.debug("Set session attribute: current receipt products map => " + productMap);
        request.setAttribute("receiptProductMap", productMap);

        logger.debug("View receipt product page command is finished");
        return Path.VIEW_RECEIPT_PRODUCTS_PAGE;
    }

}
