package web.command;

import com.my.Path;
import com.my.db.entities.dao.UserDAO;
import com.my.web.command.senior_cashier.ViewSearchCashierResultCommand;
import com.my.web.exception.ApplicationException;
import com.my.web.exception.DBException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class ViewSearchCashierResultCommandTest {

    private static UserDAO userDAO;
    private static ViewSearchCashierResultCommand underTest;
    private static HttpServletRequest mockRequest;
    private static HttpServletResponse mockResponse;

    @BeforeAll
    static void init() {
        userDAO = Mockito.mock(UserDAO.class);
        underTest = new ViewSearchCashierResultCommand(userDAO);
        mockRequest = Mockito.mock(HttpServletRequest.class);
        mockResponse = Mockito.mock(HttpServletResponse.class);

        doNothing().when(mockRequest).setAttribute(anyString(), any());
    }

    @Test
    public void viewReceiptProductsCommandExecuteTest() throws ServletException, IOException, DBException, ApplicationException {
        HttpSession mockSession = Mockito.mock(HttpSession.class);

        when(mockRequest.getSession()).thenReturn(mockSession);
        doNothing().when(mockSession).setAttribute(anyString(), any());
        when(mockRequest.getParameter("currentPage")).thenReturn("1");


        String actual = underTest.execute(mockRequest, mockResponse);
        String expected = Path.VIEW_SEARCH_CASHIER_RESULT_PAGE;
        Assertions.assertEquals(expected, actual);

        verify(userDAO, times(1)).searchCashiersByName(any(), any(), anyInt(), anyInt());
    }

}
