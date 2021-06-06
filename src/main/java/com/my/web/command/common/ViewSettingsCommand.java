package com.my.web.command.common;

import com.my.Path;
import com.my.web.command.Command;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * View settings command
 */
public class ViewSettingsCommand extends Command {

    private static final long serialVersionUID = 923462384362343410L;
    private static final Logger logger = Logger.getLogger(ViewSettingsCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        logger.debug("View Settings command is started");
        logger.debug("View Settings command is finished");
        return Path.SETTINGS_PAGE;
    }

}
