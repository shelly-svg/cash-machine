package com.my.web.command.cashier;

import com.my.Path;
import com.my.db.entities.*;
import com.my.db.entities.dao.DeliveryDAO;
import com.my.db.entities.dao.ReceiptDAO;
import com.my.web.Commands;
import com.my.web.command.Command;
import com.my.web.exception.ApplicationException;
import com.my.web.exception.DBException;
import com.my.web.validators.ReceiptValidator;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;
import java.util.List;

public class CreateReceiptCommand extends Command {

    private static final long serialVersionUID = 6234812397439832433L;
    private static final Logger logger = Logger.getLogger(CreateReceiptCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, ApplicationException {
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

    private String doPost(HttpServletRequest request) throws ApplicationException {
        logger.debug("Create receipt command started at POST method");

        HttpSession session = request.getSession();
        Receipt receipt = new Receipt();

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
        } catch (DBException exception) {
            String errorMessage = "delivery.dao.error";
            logger.error("errorMessage --> " + exception);
            throw new ApplicationException(errorMessage);
        }

        new ReceiptValidator().validate(receipt);

        int id;
        try {
            id = new ReceiptDAO().createReceipt(receipt);
        } catch (DBException exception) {
            String errorMessage = "receipt.dao.create.receipt";
            logger.error("errorMessage --> " + exception);
            throw new ApplicationException(errorMessage);
        }
        logger.debug("Received created receipt id => " + id);

        receipt.setId(id);
        session.setAttribute("currentReceipt", receipt);
        logger.trace("Set session attribute currentReceipt => " + receipt);

        return Commands.VIEW_CURRENT_RECEIPT_COMMAND;
    }

    private String doGet(HttpServletRequest request) throws ApplicationException {
        logger.debug("Create receipt command started at GET method");
        try {
            List<Delivery> deliveries = new DeliveryDAO().getAllDeliveries();
            request.setAttribute("deliveries", deliveries);
        } catch (DBException exception) {
            String errorMessage = "delivery.dao.error";
            logger.error("errorMessage --> " + exception);
            throw new ApplicationException(errorMessage);
        }
        return Path.CREATE_RECEIPT_PAGE;
    }

}
