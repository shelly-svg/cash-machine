package com.my.web.command;

import com.my.Path;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AddProductCommand extends Command {

    private static final long serialVersionUID = 2394193249932294933L;
    private static final Logger logger = Logger.getLogger(AddProductCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String forward = null;
        logger.debug("Add product command is started");
        logger.debug("REQUEST METHOD IS => " + request.getMethod());
        if (request.getMethod().equals("GET")) {
            forward = doGet(request, response);
        } else {
            if (request.getMethod().equals("POST")) {
                forward = doPost(request, response);
            }
        }
        if (forward == null) {
            forward = Path.MENU_PAGE;
        }
        logger.debug("Add product command is finished, forwarding to -> " + forward);
        return forward;
    }

    private String doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        return Path.ADD_PRODUCT_PAGE;
    }

    private String doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        return Path.MENU_PAGE;
    }

}
