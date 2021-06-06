package com.my.db.entities.dao;

import com.my.db.entities.DBManager;
import com.my.db.entities.EntityMapper;
import com.my.db.entities.Fields;
import com.my.db.entities.User;
import com.my.web.exception.DBException;

import java.security.SecureRandom;
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

    private static final String SQL__FIND_USER_FOR_REPORT_BY_ID = "SELECT id, first_name, last_name, role_id FROM user WHERE id=?;";

    private static final String SQL__UPDATE_USER_LANGUAGE = "UPDATE user SET locale_name=? WHERE id=?;";

    private static final String SQL__ADD_CONFIRMATION_CODE = "INSERT INTO user_details(user_id, salt, code) VALUE (?, ?, ?);";

    private static final String SQL__FIRST_PART_OF_EVENT = "CREATE EVENT IF NOT EXISTS delete_code";
    private static final String SQL__SECOND_PART_OF_EVENT = " ON SCHEDULE AT CURRENT_TIMESTAMP + INTERVAL 30 SECOND DO DELETE FROM user_details WHERE code=?;";

    private static final String SQL__GET_SALT_FROM_USER_DETAILS = "SELECT salt FROM user_details WHERE user_id=?;";

    private static final String SQL__GET_CODE_FROM_USER_DETAILS = "SELECT code FROM user_details WHERE user_id=?;";

    private static final String SQL__UPDATE_USER_PASSWORD = "UPDATE user SET password=?, salt=? WHERE id=?;";

    public void updateUserPassword(String newSecurePassword, String newSalt, int userId) throws DBException {
        PreparedStatement p = null;
        Connection con = null;
        try {
            con = DBManager.getInstance().getConnection();
            p = con.prepareStatement(SQL__UPDATE_USER_PASSWORD);
            p.setString(1, newSecurePassword);
            p.setString(2, newSalt);
            p.setInt(3, userId);
            p.execute();
        } catch (SQLException ex) {
            DBManager.getInstance().rollbackAndClose(con, p);
            throw new DBException(ex.getMessage(), ex);
        } finally {
            DBManager.getInstance().commitAndClose(con, p);
        }
    }

    public String getCode(int userId) throws DBException {
        PreparedStatement p = null;
        Connection con = null;
        ResultSet rs = null;
        String code = null;
        try {
            con = DBManager.getInstance().getConnection();
            p = con.prepareStatement(SQL__GET_CODE_FROM_USER_DETAILS);
            p.setInt(1, userId);
            rs = p.executeQuery();
            if (rs.next()) {
                code = rs.getString(Fields.USER_DETAILS_CODE);
            }
        } catch (SQLException ex) {
            DBManager.getInstance().rollbackAndClose(con, p, rs);
            throw new DBException(ex.getMessage(), ex);
        } finally {
            DBManager.getInstance().commitAndClose(con, p, rs);
        }
        return code;
    }

    public String getSalt(int userId) throws DBException {
        PreparedStatement p = null;
        Connection con = null;
        ResultSet rs = null;
        String salt = null;
        try {
            con = DBManager.getInstance().getConnection();
            p = con.prepareStatement(SQL__GET_SALT_FROM_USER_DETAILS);
            p.setInt(1, userId);
            rs = p.executeQuery();
            if (rs.next()) {
                salt = rs.getString(Fields.USER__SALT);
            }
        } catch (SQLException ex) {
            DBManager.getInstance().rollbackAndClose(con, p, rs);
            throw new DBException(ex.getMessage(), ex);
        } finally {
            DBManager.getInstance().commitAndClose(con, p, rs);
        }
        return salt;
    }

    public void addConfirmationCode(int userId, String salt, String code) throws DBException {
        PreparedStatement p = null;
        Connection con = null;
        try {
            con = DBManager.getInstance().getConnection();
            con.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            p = con.prepareStatement(SQL__ADD_CONFIRMATION_CODE);
            p.setInt(1, userId);
            p.setString(2, salt);
            p.setString(3, code);
            p.execute();
            String event = SQL__FIRST_PART_OF_EVENT +
                    salt.substring(new SecureRandom().nextInt(salt.length() / 2)) +
                    SQL__SECOND_PART_OF_EVENT;
            p = con.prepareStatement(event);
            p.setString(1, code);
            p.execute();
            con.commit();
        } catch (SQLException ex) {
            DBManager.getInstance().rollbackAndClose(con, p);
            throw new DBException(ex.getMessage(), ex);
        } finally {
            DBManager.getInstance().closeResources(con, p);
        }
    }

    public void updateUserLanguage(int userId, String newLang) throws DBException {
        PreparedStatement p = null;
        Connection con = null;
        try {
            con = DBManager.getInstance().getConnection();
            p = con.prepareStatement(SQL__UPDATE_USER_LANGUAGE);
            p.setString(1, newLang);
            p.setInt(2, userId);
            p.execute();
        } catch (SQLException ex) {
            DBManager.getInstance().rollbackAndClose(con, p);
            throw new DBException(ex.getMessage(), ex);
        } finally {
            DBManager.getInstance().commitAndClose(con, p);
        }
    }

    public User getUserForReport(int id) throws DBException {
        User user = new User();
        PreparedStatement p = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = DBManager.getInstance().getConnection();
            p = con.prepareStatement(SQL__FIND_USER_FOR_REPORT_BY_ID);
            p.setInt(1, id);
            rs = p.executeQuery();
            if (rs.next()) {
                user.setId(rs.getInt(Fields.ENTITY__ID));
                user.setFirstName(rs.getString(Fields.USER__FIRST_NAME));
                user.setLastName(rs.getString(Fields.USER__LAST_NAME));
                user.setRoleId(rs.getInt(Fields.USER__ROLE_ID));
            }
        } catch (SQLException ex) {
            DBManager.getInstance().rollbackAndClose(con, p, rs);
            throw new DBException(ex.getMessage(), ex);
        } finally {
            DBManager.getInstance().commitAndClose(con, p, rs);
        }
        return user;
    }

    public int countOfRowsAffectedBySearchCashiers(String firstName, String lastName) throws DBException {
        int numberOfRows = 0;
        PreparedStatement p = null;
        ResultSet rs = null;
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
        } catch (SQLException ex) {
            DBManager.getInstance().rollbackAndClose(con, p, rs);
            throw new DBException(ex.getMessage(), ex);
        } finally {
            DBManager.getInstance().commitAndClose(con, p, rs);
        }
        return numberOfRows;
    }

    public List<User> searchCashiersByName(String firstName, String lastName, int currentPage, int recordsPerPage) throws DBException {
        List<User> users = new ArrayList<>();
        PreparedStatement p = null;
        ResultSet rs = null;
        Connection con = null;
        int start = currentPage * recordsPerPage - recordsPerPage;
        try {
            con = DBManager.getInstance().getConnection();
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
        } catch (SQLException ex) {
            DBManager.getInstance().rollbackAndClose(con, p, rs);
            throw new DBException(ex.getMessage(), ex);
        } finally {
            DBManager.getInstance().commitAndClose(con, p, rs);
        }
        return users;
    }

    public String findUsersFNameLName(int id) throws DBException {
        String result = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = DBManager.getInstance().getConnection();
            preparedStatement = con.prepareStatement(SQL_FIND_USERS_FNAME_LNAME_BY_ID);
            preparedStatement.setInt(1, id);
            rs = preparedStatement.executeQuery();
            if (rs.next()) {
                result = rs.getString(Fields.USER__FIRST_NAME) + " " + rs.getString(Fields.USER__LAST_NAME);
            }
        } catch (SQLException ex) {
            DBManager.getInstance().rollbackAndClose(con, preparedStatement, rs);
            throw new DBException(ex.getMessage(), ex);
        } finally {
            DBManager.getInstance().commitAndClose(con, preparedStatement, rs);
        }
        return result;
    }

    public User findUser(int id) throws DBException {
        User user = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
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
        } catch (SQLException ex) {
            DBManager.getInstance().rollbackAndClose(con, preparedStatement, rs);
            throw new DBException(ex.getMessage(), ex);
        } finally {
            DBManager.getInstance().commitAndClose(con, preparedStatement, rs);
        }
        return user;
    }

    public User findUserByLogin(String login) throws DBException {
        User user = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = DBManager.getInstance().getConnection();
            UserMapper mapper = new UserMapper();
            preparedStatement = con.prepareStatement(SQL__FIND_USER_BY_LOGIN);
            preparedStatement.setString(1, login);
            rs = preparedStatement.executeQuery();
            if (rs.next())
                user = mapper.mapRow(rs);
        } catch (SQLException ex) {
            DBManager.getInstance().rollbackAndClose(con, preparedStatement, rs);
            throw new DBException(ex.getMessage(), ex);
        } finally {
            DBManager.getInstance().commitAndClose(con, preparedStatement, rs);
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
