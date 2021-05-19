package com.my.db.entities;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Logger;

public class DBManager {

    private static final Logger log = Logger.getLogger(DBManager.class.getName());

    private static DBManager instance;

    //singleton

    private DBManager() {
    }

    public static synchronized DBManager getInstance() {
        if (instance == null)
            instance = new DBManager();
        return instance;
    }

    /**
     * Returns a DB connection from the Pool Connections. Before using this
     * method you must configure the Date Source and the Connections Pool in your
     * app.properties file.
     *
     * @return A DB connection.
     */
    public Connection getConnection() throws SQLException {
        /*Connection connection;
        Properties config = new Properties();
        try (FileInputStream fileInputStream = new FileInputStream("app.properties")) {
            config.load(fileInputStream);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        connection = DriverManager.getConnection(config.getProperty("connection.url"));
        return connection;*/
        Connection con = null;
        try {
            Context initContext = new InitialContext();
            Context envContext  = (Context)initContext.lookup("java:/comp/env");

            // ST4DB - the name of data source
            DataSource ds = (DataSource)envContext.lookup("jdbc/ST4DB");
            con = ds.getConnection();
        } catch (NamingException ex) {
            log.warning("Cannot obtain a connection from the pool");
        }
        return con;
    }

    /**
     * Commits and close the given connection.
     *
     * @param con Connection to be committed and closed.
     */
    public void commitAndClose(Connection con) {
        try {
            con.commit();
            con.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Rollbacks and close the given connection.
     *
     * @param con Connection to be rollbacked and closed.
     */
    public void rollbackAndClose(Connection con) {
        try {
            con.rollback();
            con.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

}
