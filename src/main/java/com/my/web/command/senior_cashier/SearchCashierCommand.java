package com.my.web.command.senior_cashier;

import com.my.Path;
import com.my.web.command.Command;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SearchCashierCommand extends Command {

    private static final long serialVersionUID = -2910392904439854875L;
    private static final Logger logger = Logger.getLogger(SearchCashierCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        logger.debug("Search cashier command is started");
        logger.debug("Search cashier command is finished");
        return Path.VIEW_SEARCH_CASHIER_PAGE;
    }

}
