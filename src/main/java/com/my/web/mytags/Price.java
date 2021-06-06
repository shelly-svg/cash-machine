package com.my.web.mytags;

import com.my.Path;
import com.my.db.entities.Product;
import com.my.db.entities.dao.ReceiptDAO;
import com.my.web.exception.DBException;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

public class Price extends SimpleTagSupport {

    private static final Logger logger = Logger.getLogger(Price.class);

    private String receiptId;

    public void setReceiptId(String receiptId) {
        this.receiptId = receiptId;
    }

    public void doTag() throws IOException {
        logger.debug("doTag at the price tag is started");
        JspWriter out = getJspContext().getOut();
        PageContext context = (PageContext) getJspContext();
        HttpSession session = context.getSession();
        HttpServletResponse response = (HttpServletResponse) context.getResponse();
        ReceiptDAO receiptDAO = new ReceiptDAO();
        if (receiptId != null) {
            int id = Integer.parseInt(receiptId);

            Map<Product, Integer> products = new TreeMap<>();
            try {
                products = new ReceiptDAO().getMapOfAmountsAndProductsFromReceipt(receiptDAO.findReceipt(id));
            } catch (DBException ex) {
                String errorMessage = "An error has occurred while retrieving receipt products, please try again later";
                session.setAttribute("errorMessage", errorMessage);
                logger.error("errorMessage -> " + ex.getMessage());
                response.sendRedirect(Path.ERROR_PAGE);
            }

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
