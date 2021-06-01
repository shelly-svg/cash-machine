package com.my.db.entities;

import com.my.web.exception.DBException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    private final boolean isTest;
    private Connection connection;

    public UserDAO() {
        isTest = false;
    }

    public UserDAO(boolean isTest, Connection connection) {
        this.isTest = isTest;
        this.connection = connection;
    }

    private static final String SQL__FIND_USER_BY_LOGIN =
            "SELECT * FROM user WHERE login=?";

    private static final String SQL__FIND_USER_BY_ID =
            "SELECT * FROM user WHERE id=?";

    private static final String SQL_FIND_USERS_FNAME_LNAME_BY_ID = "SELECT first_name,last_name FROM user WHERE id=?";

    private static final String SQL__SEARCH_USERS_BY_NAME = "SELECT id, first_name, last_name, role_id FROM " +
            "user WHERE first_name LIKE ? AND last_name LIKE ? AND role_id=? LIMIT ?,?;";

    private static final String SQL__NUMBER_OF_ROWS_AFFECTED_BY_SEARCH_USERS = "SELECT count(*) FROM user WHERE first_name " +
            "LIKE ? AND last_name LIKE ? AND role_id=?;";

    private static final String SQL__FIND_USER_FOR_REPORT_BY_ID = "SELECT id, first_name, last_name, role_id FROM user WHERE id=?;";

    public User getUserForReport(int id) throws DBException {
        User user = new User();
        PreparedStatement p;
        ResultSet rs;
        Connection con = null;
        try {
            if (!isTest) {
                con = DBManager.getInstance().getConnection();
            } else {
                con = this.connection;
            }
            p = con.prepareStatement(SQL__FIND_USER_FOR_REPORT_BY_ID);
            p.setInt(1, id);
            rs = p.executeQuery();
            if (rs.next()) {
                user.setId(rs.getInt(Fields.ENTITY__ID));
                user.setFirstName(rs.getString(Fields.USER__FIRST_NAME));
                user.setLastName(rs.getString(Fields.USER__LAST_NAME));
                user.setRoleId(rs.getInt(Fields.USER__ROLE_ID));
            }
            rs.close();
            p.close();
        } catch (SQLException ex) {
            assert con != null;
            DBManager.getInstance().rollbackAndClose(con);
            throw new DBException(ex.getMessage());
        } finally {
            assert con != null;
            DBManager.getInstance().commitAndClose(con);
        }
        return user;
    }

    public int countOfRowsAffectedBySearchCashiers(String firstName, String lastName) throws DBException {
        int numberOfRows = 0;
        PreparedStatement p;
        ResultSet rs;
        Connection con = null;
        try {
            if (!isTest) {
                con = DBManager.getInstance().getConnection();
            } else {
                con = this.connection;
            }
            p = con.prepareStatement(SQL__NUMBER_OF_ROWS_AFFECTED_BY_SEARCH_USERS);
            firstName = "%" + firstName + "%";
            lastName = "%" + lastName + "%";
            p.setString(1, firstName);
            p.setString(2, lastName);
            p.setInt(3, 2);
            rs = p.executeQuery();
            if (rs.next()) {
                numberOfRows = rs.getInt(1);
            }
            rs.close();
            p.close();
        } catch (SQLException ex) {
            assert con != null;
            DBManager.getInstance().rollbackAndClose(con);
            throw new DBException(ex.getMessage());
        } finally {
            assert con != null;
            DBManager.getInstance().commitAndClose(con);
        }
        return numberOfRows;
    }

    public List<User> searchCashiersByName(String firstName, String lastName, int currentPage, int recordsPerPage) throws DBException {
        List<User> users = new ArrayList<>();
        PreparedStatement p;
        ResultSet rs;
        Connection con = null;
        int start = currentPage * recordsPerPage - recordsPerPage;
        try {
            if (!isTest) {
                con = DBManager.getInstance().getConnection();
            } else {
                con = this.connection;
            }
            p = con.prepareStatement(SQL__SEARCH_USERS_BY_NAME);
            firstName = "%" + firstName + "%";
            lastName = "%" + lastName + "%";
            p.setString(1, firstName);
            p.setString(2, lastName);
            p.setInt(3, 2);
            p.setInt(4, start);
            p.setInt(5, recordsPerPage);
            rs = p.executeQuery();
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt(Fields.ENTITY__ID));
                user.setFirstName(rs.getString(Fields.USER__FIRST_NAME));
                user.setLastName(rs.getString(Fields.USER__LAST_NAME));
                user.setRoleId(rs.getInt(Fields.USER__ROLE_ID));
                users.add(user);
            }
            rs.close();
            p.close();
        } catch (SQLException ex) {
            assert con != null;
            DBManager.getInstance().rollbackAndClose(con);
            throw new DBException(ex.getMessage());
        } finally {
            assert con != null;
            DBManager.getInstance().commitAndClose(con);
        }
        return users;
    }

    public String findUsersFNameLName(int id) throws DBException {
        String result = null;
        PreparedStatement preparedStatement;
        ResultSet rs;
        Connection con = null;
        try {
            if (!isTest) {
                con = DBManager.getInstance().getConnection();
            } else {
                con = this.connection;
            }
            preparedStatement = con.prepareStatement(SQL_FIND_USERS_FNAME_LNAME_BY_ID);
            preparedStatement.setInt(1, id);
            rs = preparedStatement.executeQuery();
            if (rs.next()) {
                result = rs.getString(Fields.USER__FIRST_NAME) + " " + rs.getString(Fields.USER__LAST_NAME);
            }
            rs.close();
            preparedStatement.close();
        } catch (SQLException ex) {
            assert con != null;
            DBManager.getInstance().rollbackAndClose(con);
            throw new DBException(ex.getMessage());
        } finally {
            assert con != null;
            DBManager.getInstance().commitAndClose(con);
        }
        return result;
    }

    public User findUser(int id) throws DBException {
        User user = null;
        PreparedStatement preparedStatement;
        ResultSet rs;
        Connection con = null;
        try {
            if (!isTest) {
                con = DBManager.getInstance().getConnection();
            } else {
                con = this.connection;
            }
            UserMapper mapper = new UserMapper();
            preparedStatement = con.prepareStatement(SQL__FIND_USER_BY_ID);
            preparedStatement.setInt(1, id);
            rs = preparedStatement.executeQuery();
            if (rs.next()) {
                user = mapper.mapRow(rs);
            }
            rs.close();
            preparedStatement.close();
        } catch (SQLException ex) {
            assert con != null;
            DBManager.getInstance().rollbackAndClose(con);
            throw new DBException(ex.getMessage());
        } finally {
            assert con != null;
            DBManager.getInstance().commitAndClose(con);
        }
        return user;
    }

    public User findUserByLogin(String login) throws DBException {
        User user = null;
        PreparedStatement preparedStatement;
        ResultSet rs;
        Connection con = null;
        try {
            if (!isTest) {
                con = DBManager.getInstance().getConnection();
            } else {
                con = this.connection;
            }
            UserMapper mapper = new UserMapper();
            preparedStatement = con.prepareStatement(SQL__FIND_USER_BY_LOGIN);
            preparedStatement.setString(1, login);
            rs = preparedStatement.executeQuery();
            if (rs.next())
                user = mapper.mapRow(rs);
            rs.close();
            preparedStatement.close();
        } catch (SQLException ex) {
            assert con != null;
            DBManager.getInstance().rollbackAndClose(con);
            throw new DBException(ex.getMessage());
        } finally {
            assert con != null;
            DBManager.getInstance().commitAndClose(con);
        }
        return user;
    }

    private static class UserMapper implements EntityMapper<User> {

        @Override
        public User mapRow(ResultSet rs) {
            try {
                User user = new User();
                user.setId(rs.getInt(Fields.ENTITY__ID));
                user.setLogin(rs.getString(Fields.USER__LOGIN));
                user.setPassword(rs.getString(Fields.USER__PASSWORD));
                user.setSalt(rs.getString(Fields.USER__SALT));
                user.setFirstName(rs.getString(Fields.USER__FIRST_NAME));
                user.setLastName(rs.getString(Fields.USER__LAST_NAME));
                user.setEmail(rs.getString(Fields.USER__EMAIL));
                user.setLocaleName(rs.getString(Fields.USER__LOCALE_NAME));
                user.setRoleId(rs.getInt(Fields.USER__ROLE_ID));
                return user;
            } catch (SQLException e) {
                throw new IllegalStateException(e);
            }
        }
    }

}
