package com.my.web.command.common;

import com.my.Path;
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

public class ViewSearchProductPageCommand extends Command {

    private static final long serialVersionUID = 2348934726283494344L;
    private static final Logger logger = Logger.getLogger(ViewSearchProductPageCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        logger.debug("View search page command is stared");
        logger.debug("View search page command is finished");
        return Path.VIEW_SEARCH_PRODUCTS_PAGE;
    }

}