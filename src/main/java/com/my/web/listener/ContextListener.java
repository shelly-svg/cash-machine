package com.my.web.listener;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ContextListener implements ServletContextListener {

    private static final Logger logger = Logger.getLogger(ContextListener.class);

    public void contextDestroyed(ServletContextEvent event) {
        logger.debug("Servlet context destruction starts");
        // do nothing
        logger.debug("Servlet context destruction finished");
    }

    public void contextInitialized(ServletContextEvent event) {
        logger.debug("Servlet context initialization starts");

        ServletContext servletContext = event.getServletContext();
        initLog4J(servletContext);
        initCommandContainer();

        logger.debug("Servlet context initialization finished");
    }

    /**
     * Initializes log4j framework.
     *
     */
    private void initLog4J(ServletContext servletContext) {
        logger.debug("Log4J initialization started");
        try {
            PropertyConfigurator.configure(servletContext.getRealPath(
                    "WEB-INF/classes/log4j.properties"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        logger.debug("Log4J initialization finished");
    }

    /**
     * Initializes CommandContainer.
     *
     */
    private void initCommandContainer() {
        logger.debug("Command container initialization started");

        // initialize commands container
        // just load class to JVM
        try {
            Class.forName("com.my.web.command.CommandContainer");
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }

        logger.debug("Command container initialization finished");
    }

}
