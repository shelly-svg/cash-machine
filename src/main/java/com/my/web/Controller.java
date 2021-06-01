package com.my.web;

import com.my.web.command.Command;
import com.my.web.command.CommandContainer;
import org.apache.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

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

    private void process(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        logger.debug("Controller starts");

        String commandName = request.getParameter("command");
        logger.trace("Request parameter: command -> " + commandName);

        Command command = CommandContainer.get(commandName);
        logger.trace("Obtained command: " + commandName);

        String forward = command.execute(request, response);
        logger.trace("Forward address -> " + forward);

        logger.debug("Controller is finished, forward to address -> " + forward);
        HttpSession session = request.getSession();
        session.setAttribute("lastAction", forward);

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
