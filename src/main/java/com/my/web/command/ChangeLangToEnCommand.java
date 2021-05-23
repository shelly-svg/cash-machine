package com.my.web.command;

import com.my.Path;
import com.my.db.entities.Category;
import com.my.db.entities.CategoryDAO;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

public class ChangeLangToEnCommand extends Command {

    private static final long serialVersionUID = -3402394287548203940L;
    private static final Logger logger = Logger.getLogger(ChangeLangToEnCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        logger.debug("Change lang to english command is started");
        String forward;
        HttpSession session = request.getSession();
        System.out.println("SESSION ATR" + session.getAttribute("lastAction"));
        forward = (String) session.getAttribute("lastAction");
        logger.trace("Received session attribute => " + forward);
        session.setAttribute("lang", "en");
        logger.trace("Set session attribute lang => en");
        if (forward == null) {
            return Path.LOGIN_PAGE;
        }
        if (Path.VIEW_PRODUCT_PAGE.equals(forward)) {
            forward = "controller?command=viewProduct&id=" + request.getSession().getAttribute("lastViewedProductId");
        }
        if (Path.VIEW_SEARCH_RESULT_PAGE.equals(forward)) {
            forward = "controller?command=searchProduct&pattern=" + request.getSession().getAttribute("lastSearchPattern") + "&currentPage=" + request.getSession().getAttribute("currentPagPage");
        }
        if (Path.ADD_PRODUCT_PAGE.equals(forward)) {
            Map<Integer, Category> categories = new CategoryDAO().findAllCategories();
            request.setAttribute("categories", categories);
        }
        if (Path.EDIT_PRODUCT_PAGE.equals(forward)) {
            forward = "controller?command=editProduct&id=" + request.getSession().getAttribute("lastEditedProductId");
            logger.debug("KEKW => " + "controller?command=editProduct&id=" + request.getSession().getAttribute("lastEditedProductId"));
        }
        logger.debug("Change lang to english command is finished");
        return forward;
    }

}
