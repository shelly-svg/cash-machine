package com.my.web.command.common;

import com.my.Path;
import com.my.db.entities.Receipt;
import com.my.db.entities.ReceiptDAO;
import com.my.web.command.Command;
import com.my.web.exception.ApplicationException;
import com.my.web.exception.DBException;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class ViewMenuCommand extends Command {

    private static final long serialVersionUID = 2348934726283494344L;
    private static final Logger logger = Logger.getLogger(ViewMenuCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, ApplicationException {
        logger.debug("View menu command is started");

        HttpSession session = request.getSession();

        Receipt currentReceipt = (Receipt) request.getSession().getAttribute("currentReceipt");
        if (currentReceipt != null) {
            try {
                currentReceipt = new ReceiptDAO().findReceipt(currentReceipt.getId());
            } catch (DBException exception) {
                String errorMessage = "receipt.dao.find.receipt";
                logger.error("errorMessage --> " + exception);
                throw new ApplicationException(errorMessage);
            }
            logger.debug("Set session attribute : updated current receipt info => " + currentReceipt);
            session.setAttribute("currentReceipt", currentReceipt);
        }

        logger.debug("View menu command is finished");
        return Path.MENU_PAGE;
    }

}
