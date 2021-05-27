package com.my.web.command.senior_cashier;

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

public class SetReceiptStatusCanceledCommand extends Command {

    private static final long serialVersionUID = 2391093230348598231L;
    private static final Logger logger = Logger.getLogger(SetReceiptStatusCanceledCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        logger.debug("Set receipt status canceled command is started");

        HttpSession session = request.getSession();
        ReceiptDAO receiptDAO = new ReceiptDAO();
        String errorMessage;

        Receipt currentReceipt = (Receipt) session.getAttribute("currentReceipt");
        if (currentReceipt != null) {
            currentReceipt = receiptDAO.findReceipt(currentReceipt.getId());
            if (currentReceipt.getReceiptStatus().name().equals(ReceiptStatus.NEW_RECEIPT.name())) {
                receiptDAO.setReceiptStatus(currentReceipt.getId(), ReceiptStatus.CANCELED);
                currentReceipt.setReceiptStatus(ReceiptStatus.CANCELED);
            }else{
                errorMessage = "This receipt is closed already, you cannot cancel";
                session.setAttribute("errorMessage", errorMessage);
                logger.error("errorMessage --> " + errorMessage);
                return "controller?command=noCommand";
            }
            session.setAttribute("currentReceipt", currentReceipt);
        } else {
            errorMessage = "You didnt chose receipt";
            request.setAttribute("errorMessage", errorMessage);
            logger.error("errorMessage --> " + errorMessage);
            return "controller?command=noCommand";
        }

        logger.debug("Set receipt status canceled command is finished");
        return "controller?command=viewCurrentReceipt";
    }

}
