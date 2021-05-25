package com.my.web.command;

import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SetReceiptStatusClosedCommand extends Command {

    private static final long serialVersionUID = -8372367112394320123L;
    private static final Logger logger = Logger.getLogger(SetReceiptStatusClosedCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        logger.debug("Set receipt status closed command is started");



        logger.debug("Set receipt status closed command is finished");
        return "controller?command=viewCurrentReceipt";
    }
}
