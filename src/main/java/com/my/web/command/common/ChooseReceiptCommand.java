package com.my.web.command.common;

import com.my.Path;
import com.my.db.entities.Product;
import com.my.db.entities.Receipt;
import com.my.db.entities.ReceiptDAO;
import com.my.web.command.Command;
import com.my.web.exception.DBException;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

public class ChooseReceiptCommand extends Command {

    private static final long serialVersionUID = 7231839234283109321L;
    private static final Logger logger = Logger.getLogger(ChooseReceiptCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        logger.debug("Choose receipt command is started");
        int id = Integer.parseInt(request.getParameter("id"));
        logger.trace("Received id from search product result => " + id);
        ReceiptDAO receiptDAO = new ReceiptDAO();
        HttpSession session = request.getSession();
        Receipt receipt;
        try {
            receipt = receiptDAO.findReceipt(id);
        } catch (DBException exception) {
            String errorMessage = "An error has occurred while receiving receipt, please try again later";
            session.setAttribute("errorMessage", errorMessage);
            logger.error("errorMessage --> " + exception.getMessage());
            return Path.ERROR_PAGE;
        }
        request.getSession().setAttribute("currentReceipt", receipt);

        List<Product> products;
        try {
            products = receiptDAO.getAllProductsFromReceipt(receipt.getId());
        } catch (DBException exception) {
            String errorMessage = "An error has occurred, please try again later";
            session.setAttribute("errorMessage", errorMessage);
            logger.error("errorMessage --> " + exception.getMessage());
            return Path.ERROR_PAGE;
        }
        request.getSession().setAttribute("currentReceiptProducts", products);

        logger.trace("Received receipt from database and set session attribute=> " + receipt);
        logger.debug("Choose receipt command is finished");
        return Path.MENU_PAGE;
    }
}
