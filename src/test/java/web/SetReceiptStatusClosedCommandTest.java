package web;

import com.my.Path;
import com.my.db.entities.Delivery;
import com.my.db.entities.Receipt;
import com.my.db.entities.User;
import com.my.db.entities.dao.DeliveryDAO;
import com.my.db.entities.dao.ReceiptDAO;
import com.my.db.entities.dao.UserDAO;
import com.my.web.Commands;
import com.my.web.command.cashier.CreateReceiptCommand;
import com.my.web.command.cashier.SetReceiptStatusClosedCommand;
import com.my.web.exception.ApplicationException;
import com.my.web.exception.DBException;
import com.my.web.validators.ReceiptValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class SetReceiptStatusClosedCommandTest {

    private static ReceiptDAO receiptDAO;
    private static SetReceiptStatusClosedCommand underTest;
    private static HttpServletRequest mockRequest;
    private static HttpServletResponse mockResponse;

    @BeforeAll
    static void init() {
        receiptDAO = Mockito.mock(ReceiptDAO.class);
        underTest = new SetReceiptStatusClosedCommand(receiptDAO);
        mockRequest = Mockito.mock(HttpServletRequest.class);
        mockResponse = Mockito.mock(HttpServletResponse.class);

        doNothing().when(mockRequest).setAttribute(anyString(), any());
    }

    @Test
    public void setReceiptStatusClosedCommandExecuteTest() throws ServletException, IOException, DBException, ApplicationException {
        HttpSession mockSession = Mockito.mock(HttpSession.class);
        Receipt testReceipt = new Receipt();
        testReceipt.setId(1);

        when(mockRequest.getSession()).thenReturn(mockSession);
        when(mockSession.getAttribute("lang")).thenReturn("ru");
        doNothing().when(mockSession).setAttribute(anyString(), any());
        when(mockSession.getAttribute("currentReceipt")).thenReturn(testReceipt);

        String actual = underTest.execute(mockRequest, mockResponse);
        String expected = Commands.VIEW_CURRENT_RECEIPT_COMMAND;
        Assertions.assertEquals(expected, actual);

        verify(receiptDAO, times(1)).createReceipt(any());
    }

}
