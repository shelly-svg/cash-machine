package com.my.web.command.senior_cashier;

import com.my.Path;
import com.my.db.entities.Receipt;
import com.my.db.entities.ReceiptDAO;
import com.my.db.entities.ReceiptStatus;
import com.my.web.command.Command;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class RemoveProductFromReceiptCommand extends Command {

    private static final long serialVersionUID = -7291892230120392931L;
    private static final Logger logger = Logger.getLogger(RemoveProductFromReceiptCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        logger.debug("Remove product from receipt command is started");

        HttpSession session = request.getSession();

        Receipt currentReceipt = (Receipt) session.getAttribute("currentReceipt");
        Receipt updatedReceipt = new ReceiptDAO().findReceipt(currentReceipt.getId());
        if (!updatedReceipt.getReceiptStatus().name().equals(ReceiptStatus.NEW_RECEIPT.name())) {
            String errorMessage = "Receipt is unavailable to removing products";
            session.setAttribute("errorMessage", errorMessage);
            logger.error("errorMessage --> " + errorMessage);
            return "controller?command=noCommand";
        }

        int productId = Integer.parseInt(request.getParameter("product_id"));
        int receiptId = Integer.parseInt(request.getParameter("receipt_id"));

        new ReceiptDAO().deleteProductFromReceipt(receiptId, productId);

        logger.debug("Remove product from receipt command is finished");
        return "controller?command=viewReceiptProducts";
    }
}
