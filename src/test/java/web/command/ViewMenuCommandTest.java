package web.command;

import com.my.Path;
import com.my.db.entities.Receipt;
import com.my.db.entities.dao.ReceiptDAO;
import com.my.web.command.common.ViewMenuCommand;
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
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class ViewMenuCommandTest {

    private static ReceiptDAO receiptDAO;
    private static ViewMenuCommand underTest;
    private static HttpServletRequest mockRequest;
    private static HttpServletResponse mockResponse;

    @BeforeAll
    static void init() {
        receiptDAO = Mockito.mock(ReceiptDAO.class);
        underTest = new ViewMenuCommand(receiptDAO);
        mockRequest = Mockito.mock(HttpServletRequest.class);
        mockResponse = Mockito.mock(HttpServletResponse.class);

        doNothing().when(mockRequest).setAttribute(anyString(), any());
    }

    @Test
    public void viewMenuCommandExecuteTest() throws ServletException, IOException, DBException, ApplicationException {
        HttpSession mockSession = Mockito.mock(HttpSession.class);

        Receipt testReceipt = new Receipt();
        testReceipt.setId(1);

        when(mockRequest.getSession()).thenReturn(mockSession);
        doNothing().when(mockSession).setAttribute(anyString(), any());
        when(mockSession.getAttribute("currentReceipt")).thenReturn(testReceipt);

        String actual = underTest.execute(mockRequest, mockResponse);
        String expected = Path.MENU_PAGE;
        Assertions.assertEquals(expected, actual);

        verify(receiptDAO, times(1)).findReceipt(anyInt());
    }

}
