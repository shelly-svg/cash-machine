package com.my.web.command.common;

import com.my.Path;
import com.my.web.command.Command;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * View search receipt page command
 */
public class ViewSearchReceiptPageCommand extends Command {

    private static final long serialVersionUID = -4923484329592341022L;
    private static final Logger logger = Logger.getLogger(ViewSearchReceiptPageCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        logger.debug("View search receipt page command is stared");
        logger.debug("View search receipt page command is finished");
        return Path.SEARCH_RECEIPT_PAGE;
    }

}
