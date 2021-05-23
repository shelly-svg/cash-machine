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
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ChangeLangToRuCommand extends Command {

    private static final long serialVersionUID = 2349283972186473273L;
    private static final Logger logger = Logger.getLogger(ChangeLangToRuCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        logger.debug("Change lang to russian command is started");
        String forward;
        HttpSession session = request.getSession();
        forward = (String) session.getAttribute("lastAction");
        logger.trace("Received session attribute => " + forward);
        session.setAttribute("lang", "ru");
        logger.trace("Set session attribute lang => ru");
        if (forward == null) {
            return Path.LOGIN_PAGE;
        }
        if (Path.VIEW_PRODUCT_PAGE.equals(forward)) {
            forward = "controller?command=viewProduct&id=" + request.getSession().getAttribute("lastViewedProductId");
        }
        if (Path.VIEW_SEARCH_RESULT_PAGE.equals(forward)) {
            forward = "controller?command=searchProduct&pattern=" + request.getSession().getAttribute("lastSearchPattern") + "&currentPage=" + request.getSession().getAttribute("currentPagPage");
        }
        if (Path.CREATE_RECEIPT_PAGE.equals(forward)) {
            List<Delivery> deliveries = new DeliveryDAO().getAllDeliveries();
            request.setAttribute("deliveries", deliveries);
        }
        if (Path.ADD_PRODUCT_PAGE.equals(forward)) {
            Map<Integer, Category> categories = new CategoryDAO().findAllCategories();
            request.setAttribute("categories", categories);
        }
        if (Path.EDIT_PRODUCT_PAGE.equals(forward)) {
            forward = "controller?command=editProduct&id=" + request.getSession().getAttribute("lastEditedProductId");
        }
        logger.debug("Change lang to russian command is finished");
        return forward;
    }

}
