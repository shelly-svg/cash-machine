package com.my.db.entities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    private static final String SQL__FIND_USER_BY_LOGIN =
            "SELECT * FROM user WHERE login=?";

    private static final String SQL__FIND_USER_BY_ID =
            "SELECT * FROM user WHERE id=?";

    private static final String SQL_FIND_USERS_FNAME_LNAME_BY_ID = "SELECT first_name,last_name FROM user WHERE id=?";

    private static final String SQL__SEARCH_USERS_BY_NAME = "SELECT id, first_name, last_name, role_id FROM " +
            "user WHERE first_name LIKE ? AND last_name LIKE ? AND role_id=? LIMIT ?,?;";

    private static final String SQL__NUMBER_OF_ROWS_AFFECTED_BY_SEARCH_USERS = "SELECT count(*) FROM user WHERE first_name " +
            "LIKE ? AND last_name LIKE ? AND role_id=?;";

    public int countOfRowsAffectedBySearchCashiers(String firstName, String lastName) {
        int numberOfRows = 0;
        PreparedStatement p;
        ResultSet rs;
        Connection con = null;
        try {
            con = DBManager.getInstance().getConnection();
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
            ex.printStackTrace();
        } finally {
            assert con != null;
            DBManager.getInstance().commitAndClose(con);
        }
        return numberOfRows;
    }

    public List<User> searchCashiersByName(String firstName, String lastName, int currentPage, int recordsPerPage) {
        List<User> users = new ArrayList<>();
        PreparedStatement p;
        ResultSet rs;
        Connection con = null;
        int start = currentPage * recordsPerPage - recordsPerPage;
        try {
            con = DBManager.getInstance().getConnection();
            UserDAO.UserMapper mapper = new UserDAO.UserMapper();
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
            ex.printStackTrace();
        } finally {
            assert con != null;
            DBManager.getInstance().commitAndClose(con);
        }
        return users;
    }

    public String findUsersFNameLName(int id) {
        String result = null;
        PreparedStatement preparedStatement;
        ResultSet rs;
        Connection con = null;
        try {
            con = DBManager.getInstance().getConnection();
            UserMapper mapper = new UserMapper();
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
            ex.printStackTrace();
        } finally {
            assert con != null;
            DBManager.getInstance().commitAndClose(con);
        }
        return result;
    }

    public User findUser(int id) {
        User user = null;
        PreparedStatement preparedStatement;
        ResultSet rs;
        Connection con = null;
        try {
            con = DBManager.getInstance().getConnection();
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
            ex.printStackTrace();
        } finally {
            assert con != null;
            DBManager.getInstance().commitAndClose(con);
        }
        return user;
    }

    public static void main(String[] args) {
        System.out.println(new UserDAO().findUser(2));
    }

    public User findUserByLogin(String login) {
        User user = null;
        PreparedStatement preparedStatement;
        ResultSet rs;
        Connection con = null;
        try {
            con = DBManager.getInstance().getConnection();
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
            ex.printStackTrace();
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
                user.setFirstName(rs.getString(Fields.USER__FIRST_NAME));
                user.setLastName(rs.getString(Fields.USER__LAST_NAME));
                user.setLocaleName(rs.getString(Fields.USER__LOCALE_NAME));
                user.setRoleId(rs.getInt(Fields.USER__ROLE_ID));
                return user;
            } catch (SQLException e) {
                throw new IllegalStateException(e);
            }
        }
    }

}
