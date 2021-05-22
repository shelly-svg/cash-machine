package com.my.web.command;

import com.my.Path;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class ChangeLangToRuCommand extends Command {
    private static final long serialVersionUID = 2349283972186473273L;
    private static final Logger logger = Logger.getLogger(ChangeLangToRuCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String forward;
        //forward = Path.MENU_PAGE;
        HttpSession session = request.getSession();
        System.out.println("SESSION ATR" + session.getAttribute("lastAction"));
        forward = (String) session.getAttribute("lastAction");
        if (forward == null) {
            session.setAttribute("lang", "ru");
            return Path.LOGIN_PAGE;
        }
        if (Path.VIEW_PRODUCT_PAGE.equals(forward)) {
            forward = "controller?command=viewProduct&id=" + request.getSession().getAttribute("lastViewedProductId");
        }
        if (Path.VIEW_SEARCH_RESULT_PAGE.equals(forward)) {
            forward = "controller?command=search&currentPage=1&pattern=" + request.getSession().getAttribute("lastSearchString");
        }

        logger.trace("CHANGE LANG TO RU COMMAND");
        session.setAttribute("lang", "ru");
        return forward;
    }
}
