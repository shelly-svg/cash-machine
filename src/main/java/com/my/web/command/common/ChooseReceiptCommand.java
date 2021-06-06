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
import java.io.IOException;
import java.util.List;

/**
 * Choose receipt command
 */
public class ChooseReceiptCommand extends Command {

    private static final long serialVersionUID = 7231839234283109321L;
    private static final Logger logger = Logger.getLogger(ChooseReceiptCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, ApplicationException {
        logger.debug("Choose receipt command is started");

        ReceiptDAO receiptDAO = new ReceiptDAO();

        int id;
        try {
            id = Integer.parseInt(request.getParameter("id"));
        } catch (NumberFormatException exception) {
            String errorMessage = "number.format.exception.product";
            logger.error(exception);
            throw new ApplicationException(errorMessage);
        }

        logger.trace("Received id from search product result => " + id);

        Receipt receipt;
        try {
            receipt = receiptDAO.findReceipt(id);
        } catch (DBException exception) {
            String errorMessage = "receipt.dao.find.receipt";
            logger.error("errorMessage --> " + exception);
            throw new ApplicationException(errorMessage);
        }
        request.getSession().setAttribute("currentReceipt", receipt);

        List<Product> products;
        try {
            products = receiptDAO.getAllProductsFromReceipt(receipt.getId());
        } catch (DBException exception) {
            String errorMessage = "error.occurred";
            logger.error("errorMessage --> " + exception);
            throw new ApplicationException(errorMessage);
        }
        request.getSession().setAttribute("currentReceiptProducts", products);

        logger.trace("Received receipt from database and set session attribute=> " + receipt);
        logger.debug("Choose receipt command is finished");
        return Path.MENU_PAGE;
    }
}
