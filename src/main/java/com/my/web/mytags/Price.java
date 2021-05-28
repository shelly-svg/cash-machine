package com.my.web.mytags;

import com.my.db.entities.Product;
import com.my.db.entities.ReceiptDAO;
import org.apache.log4j.Logger;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.util.Map;

public class Price extends SimpleTagSupport {

    private static final Logger logger = Logger.getLogger(Price.class);

    private String receiptId;

    public void setReceiptId(String receiptId) {
        this.receiptId = receiptId;
    }

    public void doTag() throws IOException {
        logger.debug("doTag at the price tag is started");
        JspWriter out = getJspContext().getOut();
        ReceiptDAO receiptDAO = new ReceiptDAO();
        if (receiptId != null) {
            int id = Integer.parseInt(receiptId);
            Map<Product, Integer> products = receiptDAO.getMapOfAmountsAndProductsFromReceipt(receiptDAO.findReceipt(id));
            double price = 0d;
            if (!products.isEmpty()) {
                for (Product product : products.keySet()) {
                    price += product.getPrice().doubleValue() * products.get(product);
                }
            }
            out.print(price);
        } else {
            out.println("ERROR");
        }
    }

}
