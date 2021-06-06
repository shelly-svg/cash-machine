package com.my.web.command.senior_cashier;

import com.my.Path;
import com.my.web.command.Command;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * View reports menu command
 */
public class ViewReportsMenuCommand extends Command {

    private static final long serialVersionUID = -928437123982934321L;
    private static final Logger logger = Logger.getLogger(ViewReportsMenuCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        logger.debug("View reports menu command is started");
        logger.debug("View reports menu command is finished");
        return Path.VIEW_REPORTS_MENU;
    }
}
