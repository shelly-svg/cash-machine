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
import java.util.Map;

public class ViewReceiptProductsPageCommand extends Command {

    private static final long serialVersionUID = -4923484329592341022L;
    private static final Logger logger = Logger.getLogger(ViewReceiptProductsPageCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        logger.debug("View receipt product page command is stared");

        Receipt currentReceipt = (Receipt) request.getSession().getAttribute("currentReceipt");
        Map<Product, Integer> productMap = new ReceiptDAO().getMapOfAmountsAndProductsFromReceipt(currentReceipt);
        request.setAttribute("receiptProductMap", productMap);

        logger.debug("View receipt product page command is finished");
        return Path.VIEW_RECEIPT_PRODUCTS_PAGE;
    }

}
