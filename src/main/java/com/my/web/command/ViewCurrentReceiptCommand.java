package com.my.web.command;

import com.my.Path;
import com.my.db.entities.*;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class ViewCurrentReceiptCommand extends Command {

    private static final long serialVersionUID = -8293127324782348212L;
    private static final Logger logger = Logger.getLogger(ViewCurrentReceiptCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        logger.debug("View current receipt command is started");

        Receipt currentReceipt = (Receipt) request.getSession().getAttribute("currentReceipt");
        List<Product> products = new ReceiptDAO().getAllProductsFromReceipt(currentReceipt.getId());
        request.getSession().setAttribute("currentReceiptProducts", products);

        Delivery delivery = new DeliveryDAO().findDeliveryById(currentReceipt.getDeliveryId());
        request.setAttribute("delivery", delivery);

        ReceiptStatus receiptStatus = ReceiptStatus.getReceiptStatus(currentReceipt);
        request.setAttribute("receiptStatus", receiptStatus);

        User user = new UserDAO().findUser(currentReceipt.getUserId());
        request.setAttribute("creator", user);

        logger.debug("View current receipt command is finished");
        return Path.VIEW_RECEIPT_PAGE;
    }

}
