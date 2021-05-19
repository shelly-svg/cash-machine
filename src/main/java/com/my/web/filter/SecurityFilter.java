package com.my.web.filter;

import com.my.Path;
import com.my.db.entities.Role;
import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

public class SecurityFilter implements Filter {

    private static final Logger logger = Logger.getLogger(SecurityFilter.class);
    private static Map<Role, List<String>> accessMap = new HashMap<>();
    private static List<String> commons = new ArrayList<>();
    private static List<String> outOfControl = new ArrayList<>();

    @Override
    public void init(FilterConfig config) throws ServletException {
        logger.debug("Security filter initialization is started");

        accessMap.put(Role.ADMIN, asList(config.getInitParameter("admin")));
        accessMap.put(Role.CASHIER, asList(config.getInitParameter("cashier")));
        accessMap.put(Role.SENIOR_CASHIER, asList(config.getInitParameter("senior-cashier")));
        accessMap.put(Role.COMMODITY_EXPERT, asList(config.getInitParameter("commodity-expert")));
        logger.trace("Access map --> " + accessMap);

        commons = asList(config.getInitParameter("common"));
        logger.trace("Common commands --> " + commons);

        outOfControl = asList(config.getInitParameter("out-of-control"));
        logger.trace("Out of control commands --> " + outOfControl);

        logger.debug("Security filter initialization is finished");
    }

    private List<String> asList(String str) {
        List<String> result = new ArrayList<>();
        StringTokenizer st = new StringTokenizer(str);
        while (st.hasMoreTokens()) {
            result.add(st.nextToken());
        }
        return result;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        logger.debug("Security filter is started");
        if (accessAllowed(req)) {
            logger.debug("Filter is finished");
            chain.doFilter(req, res);
        } else {
            String errorMessage = "You do not have access to the chosen page";
            req.setAttribute("errorMessage", errorMessage);
            logger.trace("Set the request attribute: error message -> " + errorMessage);
            req.getRequestDispatcher(Path.ERROR_PAGE).forward(req, res);
        }
    }

    private boolean accessAllowed(ServletRequest request) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        String commandName = request.getParameter("command");
        logger.trace("COMMAND NAME = " + commandName);
        if (commandName == null || commandName.isEmpty()) {
            return false;
        }

        if (outOfControl.contains(commandName)) {
            return true;
        }

        HttpSession session = httpServletRequest.getSession(false);
        logger.trace("SESSION = " + session);
        if (session == null) {
            return false;
        }

        Role userRole = (Role) session.getAttribute("userRole");
        logger.trace("USER ROLE =" + userRole);
        if (userRole == null) {
            return false;
        }
        return accessMap.get(userRole).contains(commandName) || commons.contains(commandName);
    }

    @Override
    public void destroy() {
        logger.debug("Security filter destruction");
    }

}
