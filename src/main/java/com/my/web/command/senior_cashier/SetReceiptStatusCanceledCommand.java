package com.my.web.command.senior_cashier;

import com.my.db.entities.*;
import com.my.web.command.Command;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public class SetReceiptStatusCanceledCommand extends Command {

    private static final long serialVersionUID = 2391093230348598231L;
    private static final Logger logger = Logger.getLogger(SetReceiptStatusCanceledCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        logger.debug("Set receipt status canceled command is started");

        HttpSession session = request.getSession();
        ReceiptDAO receiptDAO = new ReceiptDAO();
        ProductDAO productDAO = new ProductDAO();
        String errorMessage;
        String localeName = "en";
        Object localeObj = session.getAttribute("lang");
        if (localeObj != null) {
            localeName = localeObj.toString();
        }

        Locale locale;
        if ("ru".equals(localeName)) {
            locale = new Locale("ru", "RU");
        } else {
            locale = new Locale("en", "EN");
        }
        ResourceBundle rb = ResourceBundle.getBundle("resources", locale);


        Receipt currentReceipt = (Receipt) session.getAttribute("currentReceipt");
        if (currentReceipt != null) {
            currentReceipt = receiptDAO.findReceipt(currentReceipt.getId());
            if (currentReceipt.getReceiptStatus().name().equals(ReceiptStatus.NEW_RECEIPT.name())) {
                Map<Product, Integer> productAmountsMap = receiptDAO.getMapOfAmountsAndProductsFromReceipt(currentReceipt);
                for (Product product : productAmountsMap.keySet()) {
                    productDAO.updateProductsAmount(product.getId(), product.getAmount() + productAmountsMap.get(product));
                }
                receiptDAO.setReceiptStatus(currentReceipt.getId(), ReceiptStatus.CANCELED);
                currentReceipt.setReceiptStatus(ReceiptStatus.CANCELED);
            } else {
                errorMessage = rb.getString("set.receipt.status.closed.command.invalid.status");
                session.setAttribute("errorMessage", errorMessage);
                logger.error("errorMessage --> " + errorMessage);
                return "controller?command=noCommand";
            }
            session.setAttribute("currentReceipt", currentReceipt);
        } else {
            errorMessage = rb.getString("set.receipt.status.closed.command.receipt.null");
            request.setAttribute("errorMessage", errorMessage);
            logger.error("errorMessage --> " + errorMessage);
            return "controller?command=noCommand";
        }

        logger.debug("Set receipt status canceled command is finished");
        return "controller?command=viewCurrentReceipt";
    }

}
