package com.my.web.command.common;

import com.my.Path;
import com.my.db.entities.Receipt;
import com.my.db.entities.ReceiptDAO;
import com.my.web.Commands;
import com.my.web.command.Command;
import com.my.web.exception.ApplicationException;
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
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        logger.debug("View menu command is started");

        HttpSession session = request.getSession();
        Receipt currentReceipt = (Receipt) request.getSession().getAttribute("currentReceipt");
        if (currentReceipt != null) {
            try {
                currentReceipt = new ReceiptDAO().findReceipt(currentReceipt.getId());
            } catch (ApplicationException exception) {
                String errorMessage = "An error has occurred while updating receipt, please try again later";
                session.setAttribute("errorMessage", errorMessage);
                logger.error("errorMessage --> " + exception.getMessage());
                return Commands.ERROR_PAGE_COMMAND;
            }
            request.getSession().setAttribute("currentReceipt", currentReceipt);
        }
        logger.debug("View menu command is finished");
        return Path.MENU_PAGE;
    }

}
