package com.my.web.command;

import com.my.Path;
import com.my.db.entities.Product;
import com.my.db.entities.Receipt;
import com.my.db.entities.ReceiptDAO;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
        Receipt receipt = receiptDAO.findReceipt(id);
        request.getSession().setAttribute("currentReceipt", receipt);

        List<Product> products = receiptDAO.getAllProductsFromReceipt(receipt.getId());
        request.getSession().setAttribute("currentReceiptProducts", products);

        logger.trace("Received receipt from database and set session attribute=> " + receipt);
        logger.debug("Choose receipt command is finished");
        return Path.MENU_PAGE;
    }
}
