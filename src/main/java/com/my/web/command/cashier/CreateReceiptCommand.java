package com.my.web.command.cashier;

import com.my.Path;
import com.my.db.entities.*;
import com.my.web.Commands;
import com.my.web.LocalizationUtils;
import com.my.web.command.Command;
import com.my.web.exception.ApplicationException;
import com.my.web.validators.ProductValidator;
import com.my.web.validators.ReceiptValidator;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class CreateReceiptCommand extends Command {

    private static final long serialVersionUID = 6234812397439832433L;
    private static final Logger logger = Logger.getLogger(CreateReceiptCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        logger.debug("Create receipt command is started");
        String forward = null;
        logger.debug("REQUEST METHOD IS => " + request.getMethod());
        if (request.getMethod().equals("GET")) {
            forward = doGet(request);
        } else {
            if (request.getMethod().equals("POST")) {
                forward = doPost(request);
            }
        }
        if (forward == null) {
            forward = Path.MENU_PAGE;
        }
        logger.debug("Create receipt command is finished, forwarding to -> " + forward);
        return forward;
    }

    private String doPost(HttpServletRequest request) {
        logger.debug("Create receipt command started at POST method");
        HttpSession session = request.getSession();
        Receipt receipt = new Receipt();
        ResourceBundle rb = LocalizationUtils.getCurrentRb(session);
        receipt.setCreateTime(new Date());
        receipt.setNameRu(request.getParameter("name_ru"));
        receipt.setNameEn(request.getParameter("name_en"));
        receipt.setAddressRu(request.getParameter("address_ru"));
        receipt.setAddressEn(request.getParameter("address_en"));
        receipt.setDescriptionRu(request.getParameter("description_ru"));
        receipt.setDescriptionEn(request.getParameter("description_en"));
        receipt.setPhoneNumber(request.getParameter("phone_number"));
        receipt.setReceiptStatus(ReceiptStatus.NEW_RECEIPT);
        User currentUser = (User) session.getAttribute("user");
        receipt.setUserId(currentUser.getId());
        try {
            Delivery delivery = new DeliveryDAO().findDeliveryByName(request.getParameter("delivery_id"));
            receipt.setDelivery(delivery);
        } catch (ApplicationException exception) {
            String errorMessage = rb.getString("delivery.dao.error");
            session.setAttribute("errorMessage", errorMessage);
            logger.error("errorMessage -> " + errorMessage);
            return Commands.ERROR_PAGE_COMMAND;
        }

        if (!new ReceiptValidator().validate(receipt, session, rb)) {
            return Commands.ERROR_PAGE_COMMAND;
        }

        int id = new ReceiptDAO().createReceipt(receipt);
        receipt.setId(id);
        session.setAttribute("currentReceipt", receipt);
        logger.trace("Set session attribute currentReceipt => " + receipt);

        return "controller?command=viewCurrentReceipt";
    }

    private String doGet(HttpServletRequest request) {
        logger.debug("Create receipt command started at GET method");
        HttpSession session = request.getSession();
        ResourceBundle rb = LocalizationUtils.getCurrentRb(session);
        try {
            List<Delivery> deliveries = new DeliveryDAO().getAllDeliveries();
            request.setAttribute("deliveries", deliveries);
        } catch (ApplicationException exception) {
            String errorMessage = rb.getString("delivery.dao.error");
            session.setAttribute("errorMessage", errorMessage);
            logger.error("errorMessage -> " + errorMessage);
            return Path.ERROR_PAGE;
        }
        return Path.CREATE_RECEIPT_PAGE;
    }

}
