package com.my.web.command.senior_cashier;

import com.my.db.entities.ReceiptDAO;
import com.my.web.command.Command;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RemoveProductFromReceiptCommand extends Command {

    private static final long serialVersionUID = -7291892230120392931L;
    private static final Logger logger = Logger.getLogger(RemoveProductFromReceiptCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        logger.debug("Remove product from receipt command is started");

        int productId = Integer.parseInt(request.getParameter("product_id"));
        int receiptId = Integer.parseInt(request.getParameter("receipt_id"));

        new ReceiptDAO().deleteProductFromReceipt(receiptId, productId);

        logger.debug("Remove product from receipt command is finished");
        return "controller?command=viewReceiptProducts";
    }
}
