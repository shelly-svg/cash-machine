package db.entities;

import com.my.db.entities.DBManager;
import com.my.db.entities.Fields;
import com.my.db.entities.User;
import com.my.db.entities.UserDAO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.mockito.Mockito.*;


public class UserDAOTest {

    private final int userId = 1;
    private User expectedUser;
    private UserDAO instance;

    @Mock
    Connection mockCon;
    @Mock
    PreparedStatement preparedStatement;
    @Mock
    ResultSet mockRS;

    @BeforeEach
    public void setUp() throws SQLException {
        mockCon = Mockito.mock(Connection.class);
        preparedStatement = Mockito.mock(PreparedStatement.class);
        mockRS = Mockito.mock(ResultSet.class);
        instance = new UserDAO();

        doNothing().when(mockCon).commit();
        when(mockCon.prepareStatement(anyString())).thenReturn(preparedStatement);
        doNothing().when(preparedStatement).setInt(anyInt(), anyInt());
        when(preparedStatement.executeQuery()).thenReturn(mockRS);
        when(mockRS.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);
        when(mockRS.getInt(Fields.ENTITY__ID)).thenReturn(1);
        when(mockRS.getString(Fields.USER__LOGIN)).thenReturn("myLogin");
        when(mockRS.getString(Fields.USER__PASSWORD)).thenReturn("myPassword");
        when(mockRS.getString(Fields.USER__FIRST_NAME)).thenReturn("first name");
        when(mockRS.getString(Fields.USER__LAST_NAME)).thenReturn("last name");
        when(mockRS.getString(Fields.USER__LOCALE_NAME)).thenReturn("en");
        when(mockRS.getInt(Fields.USER__ROLE_ID)).thenReturn(2);

        expectedUser = new User();
        expectedUser.setId(1);
        expectedUser.setLogin("myLogin");
        expectedUser.setPassword("myPassword");
        expectedUser.setFirstName("first name");
        expectedUser.setLastName("last name");
        expectedUser.setLocaleName("en");
        expectedUser.setRoleId(2);
    }

    @Test
    public void testGetUserById() throws SQLException {
        DBManager dbManager = Mockito.mock(DBManager.class);
        try (MockedStatic<DBManager> ignored = mockStatic(DBManager.class)) {
            when(DBManager.getInstance()).thenReturn(dbManager);
            when(DBManager.getInstance().getConnection()).thenReturn(mockCon);

            User testUser;
            testUser = instance.findUser(userId);

            Assertions.assertEquals(expectedUser, testUser);
            verify(mockCon, times(1)).prepareStatement(anyString());
            verify(preparedStatement, times(0)).setString(anyInt(), anyString());
            verify(preparedStatement, times(1)).executeQuery();
            verify(mockRS, times(1)).next();
            verify(dbManager, times(1)).commitAndClose(any(), any(), any());
        }
    }

    @Test
    public void testFindUsersFNameLName() throws SQLException {
        DBManager dbManager = Mockito.mock(DBManager.class);
        try (MockedStatic<DBManager> ignored = mockStatic(DBManager.class)) {
            when(DBManager.getInstance()).thenReturn(dbManager);
            when(DBManager.getInstance().getConnection()).thenReturn(mockCon);

            String result = "first name last name";
            String usersFNameLName = instance.findUsersFNameLName(userId);

            Assertions.assertEquals(result, usersFNameLName);
            verify(mockCon, times(1)).prepareStatement(anyString());
            verify(preparedStatement, times(1)).executeQuery();
            verify(mockRS, times(1)).next();
            verify(dbManager, times(1)).commitAndClose(any(), any(), any());
        }
    }

    @Test
    public void testFindUserByLogin() throws SQLException {
        DBManager dbManager = Mockito.mock(DBManager.class);
        try (MockedStatic<DBManager> ignored = mockStatic(DBManager.class)) {
            when(DBManager.getInstance()).thenReturn(dbManager);
            when(DBManager.getInstance().getConnection()).thenReturn(mockCon);

            String login = expectedUser.getLogin();
            User testUser = instance.findUserByLogin(login);

            Assertions.assertEquals(testUser, expectedUser);
            verify(mockCon, times(1)).prepareStatement(anyString());
            verify(preparedStatement, times(1)).executeQuery();
            verify(mockRS, times(1)).next();
            verify(dbManager, times(1)).commitAndClose(any(), any(), any());
        }
    }

    @Test
    public void testFindUserForReport() throws SQLException {
        DBManager dbManager = Mockito.mock(DBManager.class);
        try (MockedStatic<DBManager> ignored = mockStatic(DBManager.class)) {
            when(DBManager.getInstance()).thenReturn(dbManager);
            when(DBManager.getInstance().getConnection()).thenReturn(mockCon);

            expectedUser.setLogin(null);
            expectedUser.setPassword(null);
            expectedUser.setLocaleName(null);
            User testUser = instance.getUserForReport(expectedUser.getId());

            Assertions.assertEquals(expectedUser, testUser);
        }
    }

    @Test
    public void testCountOfRowsAffectedBySearch() throws SQLException {
        DBManager dbManager = Mockito.mock(DBManager.class);
        try (MockedStatic<DBManager> ignored = mockStatic(DBManager.class)) {
            when(DBManager.getInstance()).thenReturn(dbManager);
            when(DBManager.getInstance().getConnection()).thenReturn(mockCon);

            doNothing().when(preparedStatement).setString(anyInt(), anyString());
            when(mockRS.getInt(anyInt())).thenReturn(1);

            int expected = 1;
            int actual = instance.countOfRowsAffectedBySearchCashiers("first_name", "last_name");

            Assertions.assertEquals(expected, actual);
        }
    }

    @Test
    public void testSearchCashierByName() throws SQLException {
        DBManager dbManager = Mockito.mock(DBManager.class);
        try (MockedStatic<DBManager> ignored = mockStatic(DBManager.class)) {
            when(DBManager.getInstance()).thenReturn(dbManager);
            when(DBManager.getInstance().getConnection()).thenReturn(mockCon);

            doNothing().when(preparedStatement).setString(anyInt(), anyString());
            doNothing().when(preparedStatement).setInt(anyInt(), anyInt());

            expectedUser.setLogin(null);
            expectedUser.setPassword(null);
            expectedUser.setLocaleName(null);
            List<User> users = instance.searchCashiersByName("first_name", "last_name", 1, 5);

            Assertions.assertEquals(users.size(), 1);

            User actualUser = users.get(0);

            Assertions.assertEquals(expectedUser, actualUser);
        }
    }

}
