package db.entities;

import com.my.db.entities.Fields;
import com.my.db.entities.User;
import com.my.db.entities.UserDAO;
import com.my.web.exception.ApplicationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
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
    PreparedStatement mockPstm;
    @Mock
    ResultSet mockRS;

    @BeforeEach
    public void setUp() throws SQLException {
        mockCon = Mockito.mock(Connection.class);
        mockPstm = Mockito.mock(PreparedStatement.class);
        mockRS = Mockito.mock(ResultSet.class);
        instance = new UserDAO(true, mockCon);

        doNothing().when(mockCon).commit();
        when(mockCon.prepareStatement(anyString())).thenReturn(mockPstm);
        doNothing().when(mockPstm).setInt(anyInt(), anyInt());
        when(mockPstm.executeQuery()).thenReturn(mockRS);
        when(mockRS.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);
        when(mockRS.getInt(Fields.ENTITY__ID)).thenReturn(1);
        when(mockRS.getString(Fields.USER__LOGIN)).thenReturn("mylogin");
        when(mockRS.getString(Fields.USER__PASSWORD)).thenReturn("mypassword");
        when(mockRS.getString(Fields.USER__FIRST_NAME)).thenReturn("first name");
        when(mockRS.getString(Fields.USER__LAST_NAME)).thenReturn("last name");
        when(mockRS.getString(Fields.USER__LOCALE_NAME)).thenReturn("en");
        when(mockRS.getInt(Fields.USER__ROLE_ID)).thenReturn(2);

        expectedUser = new User();
        expectedUser.setId(1);
        expectedUser.setLogin("mylogin");
        expectedUser.setPassword("mypassword");
        expectedUser.setFirstName("first name");
        expectedUser.setLastName("last name");
        expectedUser.setLocaleName("en");
        expectedUser.setRoleId(2);
    }

    @Test
    public void testGetUserById() throws SQLException, ApplicationException {
        User testUser;
        testUser = instance.findUser(userId);

        Assertions.assertEquals(expectedUser, testUser);
        verify(mockCon, times(1)).prepareStatement(anyString());
        verify(mockPstm, times(0)).setString(anyInt(), anyString());
        verify(mockPstm, times(1)).executeQuery();
        verify(mockCon, times(1)).commit();
        verify(mockRS, times(1)).next();
    }

    @Test
    public void testFindUsersFNameLName() throws SQLException, ApplicationException {
        String result = "first name last name";
        String usersFNameLName = instance.findUsersFNameLName(userId);

        Assertions.assertEquals(result, usersFNameLName);
        verify(mockCon, times(1)).prepareStatement(anyString());
        verify(mockPstm, times(1)).executeQuery();
        verify(mockCon, times(1)).commit();
        verify(mockRS, times(1)).next();
    }

    @Test
    public void testFindUserByLogin() throws SQLException, ApplicationException {
        String login = expectedUser.getLogin();
        User testUser = instance.findUserByLogin(login);

        Assertions.assertEquals(testUser, expectedUser);
        verify(mockCon, times(1)).prepareStatement(anyString());
        verify(mockPstm, times(1)).executeQuery();
        verify(mockCon, times(1)).commit();
        verify(mockRS, times(1)).next();
    }

    @Test
    public void testFindUserForReport() throws ApplicationException {
        expectedUser.setLogin(null);
        expectedUser.setPassword(null);
        expectedUser.setLocaleName(null);
        User testUser = instance.getUserForReport(expectedUser.getId());

        Assertions.assertEquals(expectedUser, testUser);
    }

    @Test
    public void testCountOfRowsAffectedBySearch() throws SQLException, ApplicationException {
        doNothing().when(mockPstm).setString(anyInt(), anyString());
        when(mockRS.getInt(anyInt())).thenReturn(1);

        int expected = 1;
        int actual = instance.countOfRowsAffectedBySearchCashiers("first_name", "last_name");

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testSearchCashierByName() throws SQLException, ApplicationException {
        doNothing().when(mockPstm).setString(anyInt(), anyString());
        doNothing().when(mockPstm).setInt(anyInt(), anyInt());

        expectedUser.setLogin(null);
        expectedUser.setPassword(null);
        expectedUser.setLocaleName(null);
        List<User> users = instance.searchCashiersByName("first_name", "last_name", 1, 5);

        Assertions.assertEquals(users.size(), 1);

        User actualUser = users.get(0);

        Assertions.assertEquals(expectedUser, actualUser);
    }

}
