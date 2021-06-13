package web.command;

import com.my.db.entities.Receipt;
import com.my.db.entities.ReceiptStatus;
import com.my.db.entities.dao.ReceiptDAO;
import com.my.web.Commands;
import com.my.web.command.senior_cashier.SetReceiptStatusCanceledCommand;
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

public class SetReceiptStatusCanceledCommandTest {

    private static ReceiptDAO receiptDAO;
    private static SetReceiptStatusCanceledCommand underTest;
    private static HttpServletRequest mockRequest;
    private static HttpServletResponse mockResponse;

    @BeforeAll
    static void init() {
        receiptDAO = Mockito.mock(ReceiptDAO.class);
        underTest = new SetReceiptStatusCanceledCommand(receiptDAO);
        mockRequest = Mockito.mock(HttpServletRequest.class);
        mockResponse = Mockito.mock(HttpServletResponse.class);

        doNothing().when(mockRequest).setAttribute(anyString(), any());
    }

    @Test
    public void setReceiptStatusCanceledCommandExecuteTest() throws ServletException, IOException, DBException, ApplicationException {
        HttpSession mockSession = Mockito.mock(HttpSession.class);

        Receipt testReceipt = new Receipt();
        testReceipt.setId(1);
        testReceipt.setReceiptStatus(ReceiptStatus.NEW_RECEIPT);

        when(mockRequest.getSession()).thenReturn(mockSession);
        doNothing().when(mockSession).setAttribute(anyString(), any());
        when(mockSession.getAttribute("currentReceipt")).thenReturn(testReceipt);
        when(receiptDAO.findReceipt(testReceipt.getId())).thenReturn(testReceipt);

        String actual = underTest.execute(mockRequest, mockResponse);
        String expected = Commands.VIEW_CURRENT_RECEIPT_COMMAND;
        Assertions.assertEquals(expected, actual);

        verify(receiptDAO, times(1)).findReceipt(anyInt());
        verify(receiptDAO, times(1)).cancelReceipt(any());
    }

}
