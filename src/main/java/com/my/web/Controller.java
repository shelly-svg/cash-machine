package com.my.web;

import com.my.web.command.Command;
import com.my.web.command.CommandContainer;
import com.my.web.exception.ApplicationException;
import com.my.web.localization.LocalizationUtils;
import org.apache.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ResourceBundle;

/**
 * Main servlet controller
 */
public class Controller extends HttpServlet {

    private static final long serialVersionUID = 2342839518293047283L;
    private static final Logger logger = Logger.getLogger(Controller.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        process(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        process(req, resp);
    }

    /**
     * Main method
     *
     * @param request request
     * @param response response
     */
    private void process(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        logger.debug("Controller is started");

        HttpSession session = request.getSession();
        ResourceBundle userRb = LocalizationUtils.getCurrentRb(session);
        ResourceBundle enRb = LocalizationUtils.getEnglishRb();

        String commandName = request.getParameter("command");
        logger.trace("Request parameter: command -> " + commandName);

        Command command = CommandContainer.get(commandName);
        logger.trace("Obtained command: " + commandName);

        String forward = Commands.ERROR_PAGE_COMMAND;
        try {
            forward = command.execute(request, response);
        } catch (ApplicationException exception) {
            logger.error(enRb.getString(exception.getMessage()));
            session.setAttribute("errorMessage", userRb.getString(exception.getMessage()));
        }
        logger.trace("Forward address -> " + forward);

        logger.debug("Controller is finished, forward to address -> " + forward);

        if (request.getMethod().equals("GET")) {
            if (forward != null) {
                RequestDispatcher dispatcher = request.getRequestDispatcher(forward);
                dispatcher.forward(request, response);
            }
        } else {
            response.sendRedirect(forward);
        }
    }

}
