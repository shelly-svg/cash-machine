package com.my.web.command.cashier;

import com.my.db.entities.Receipt;
import com.my.db.entities.ReceiptDAO;
import com.my.db.entities.ReceiptStatus;
import com.my.web.command.Command;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class SetReceiptStatusClosedCommand extends Command {

    private static final long serialVersionUID = -8372367112394320123L;
    private static final Logger logger = Logger.getLogger(SetReceiptStatusClosedCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        logger.debug("Set receipt status closed command is started");

        HttpSession session = request.getSession();

        Receipt currentReceipt = (Receipt) session.getAttribute("currentReceipt");
        if (currentReceipt != null) {
            new ReceiptDAO().setReceiptStatus(currentReceipt.getId(), ReceiptStatus.CLOSED);
            currentReceipt.setReceiptStatus(ReceiptStatus.CLOSED);
            session.setAttribute("currentReceipt", currentReceipt);
        }

        logger.debug("Set receipt status closed command is finished");
        return "controller?command=viewCurrentReceipt";
    }
}
