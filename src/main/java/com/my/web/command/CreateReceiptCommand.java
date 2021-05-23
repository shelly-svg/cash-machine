package com.my.web.command;

import com.my.Path;
import com.my.db.entities.Category;
import com.my.db.entities.CategoryDAO;
import com.my.db.entities.Delivery;
import com.my.db.entities.DeliveryDAO;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

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
        return Path.MENU_PAGE;
    }

    private String doGet(HttpServletRequest request) {
        logger.debug("Create receipt command started at GET method");
        List<Delivery> deliveries = new DeliveryDAO().getAllDeliveries();
        request.setAttribute("deliveries", deliveries);
        return Path.CREATE_RECEIPT_PAGE;
    }

}
