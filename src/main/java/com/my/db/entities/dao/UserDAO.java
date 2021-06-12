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

/**
 * Data access object for user related entities
 */
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
    private static final String SQL__SECOND_PART_OF_EVENT = " ON SCHEDULE AT CURRENT_TIMESTAMP + INTERVAL 1 HOUR DO DELETE FROM user_details WHERE code=?;";

    private static final String SQL__GET_SALT_FROM_USER_DETAILS = "SELECT salt FROM user_details WHERE user_id=?;";

    private static final String SQL__GET_CODE_FROM_USER_DETAILS = "SELECT code FROM user_details WHERE user_id=?;";

    private static final String SQL__UPDATE_USER_PASSWORD = "UPDATE user SET password=?, salt=? WHERE id=?;";

    private static final String SQL__DROP_CONFIRMATION_CODE = "DELETE FROM user_details WHERE user_id=?;";

    /**
     * Updates user password
     *
     * @param newSecurePassword new user encrypted password
     * @param newSalt           new key
     * @param userId            user id
     * @throws DBException if couldn't update user password
     */
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

    /**
     * Retrieves code from user details table
     *
     * @param userId user id
     * @return String code
     * @throws DBException if couldn't retrieve data
     */
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

    /**
     * Retrieves key from user details table
     *
     * @param userId user id
     * @return String key
     * @throws DBException if couldn't retrieve data
     */
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

    /**
     * Add code and key to the user details table, also creates event, that delete this data after 1 hour
     *
     * @param userId user id
     * @param salt   key
     * @param code   code
     * @throws DBException if couldn't add data or create event
     */
    public void addConfirmationCode(int userId, String salt, String code) throws DBException {
        PreparedStatement p = null;
        Connection con = null;
        try {
            con = DBManager.getInstance().getConnection();
            con.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            p = con.prepareStatement(SQL__DROP_CONFIRMATION_CODE);
            p.setInt(1, userId);
            p.execute();
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

    /**
     * Updates user locale
     *
     * @param userId  user id
     * @param newLang new locale name
     * @throws DBException if couldn't update data
     */
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

    /**
     * Get user
     *
     * @param id user id
     * @return user entity
     * @throws DBException if couldn't retrieve data
     */
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

    /**
     * Return count of rows affected by search cashiers
     *
     * @param firstName users first name
     * @param lastName  users last name
     * @return int
     * @throws DBException if couldn't retrieve data
     */
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

    /**
     * Search cashiers by their first name and last name
     *
     * @param firstName      users first name
     * @param lastName       users last name
     * @param currentPage    current pagination page
     * @param recordsPerPage number of users, displayed per page
     * @return List of user entities
     * @throws DBException if couldn't retrieve data
     */
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

    /**
     * find user first and last name for report
     *
     * @param id user id
     * @return String
     * @throws DBException if couldn't retrieve data
     */
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

    /**
     * Return user with given id
     *
     * @param id user id
     * @return user entity
     * @throws DBException if couldn't retrieve data
     */
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

    /**
     * Return user with given login
     *
     * @param login user login
     * @return user entity
     * @throws DBException if couldn't retrieve data
     */
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

    /**
     * Extract user entity from the result set row
     */
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
