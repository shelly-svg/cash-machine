package web.command;

import com.my.Path;
import com.my.db.entities.*;
import com.my.db.entities.dao.ReceiptDAO;
import com.my.web.command.common.SearchReceiptCommand;
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

public class SearchReceiptCommandTest {

    private static ReceiptDAO receiptDAO;
    private static SearchReceiptCommand underTest;
    private static HttpServletRequest mockRequest;
    private static HttpServletResponse mockResponse;
    private static HttpSession mockSession;

    @BeforeAll
    static void init() {
        receiptDAO = Mockito.mock(ReceiptDAO.class);
        underTest = new SearchReceiptCommand(receiptDAO);
        mockRequest = Mockito.mock(HttpServletRequest.class);
        mockResponse = Mockito.mock(HttpServletResponse.class);
        mockSession = Mockito.mock(HttpSession.class);

        Receipt testReceipt = new Receipt();
        testReceipt.setId(1);
        testReceipt.setReceiptStatus(ReceiptStatus.NEW_RECEIPT);

        when(mockRequest.getSession()).thenReturn(mockSession);
        when(mockSession.getAttribute("lang")).thenReturn("ru");
        doNothing().when(mockRequest).setAttribute(anyString(), any());
        when(mockRequest.getSession().getAttribute("currentReceipt")).thenReturn(testReceipt);
    }

    @Test
    public void searchReceiptCommandTest() throws ServletException, IOException, DBException, ApplicationException {
        when(mockRequest.getParameter("currentPage")).thenReturn("1");
        when(mockRequest.getParameter("receipt_pattern")).thenReturn("name");
        doNothing().when(mockSession).setAttribute(anyString(), any());
        when(receiptDAO.countOfRowsAffectedBySearch(anyString())).thenReturn(0);

        String actual = underTest.execute(mockRequest, mockResponse);
        String expected = Path.VIEW_SEARCH_RECEIPT_RESULT_PAGE;
        Assertions.assertEquals(expected, actual);

        verify(receiptDAO, times(1)).countOfRowsAffectedBySearch(anyString());
    }

}
