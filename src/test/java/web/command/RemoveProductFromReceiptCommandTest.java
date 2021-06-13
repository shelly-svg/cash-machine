package web.command;

import com.my.db.entities.Receipt;
import com.my.db.entities.ReceiptStatus;
import com.my.db.entities.dao.ProductDAO;
import com.my.db.entities.dao.ReceiptDAO;
import com.my.web.Commands;
import com.my.web.command.senior_cashier.RemoveProductFromReceiptCommand;
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

public class RemoveProductFromReceiptCommandTest {

    private static ReceiptDAO receiptDAO;
    private static ProductDAO productDAO;
    private static RemoveProductFromReceiptCommand underTest;
    private static HttpServletRequest mockRequest;
    private static HttpServletResponse mockResponse;

    @BeforeAll
    static void init() {
        receiptDAO = Mockito.mock(ReceiptDAO.class);
        productDAO = Mockito.mock(ProductDAO.class);
        underTest = new RemoveProductFromReceiptCommand(receiptDAO, productDAO);
        mockRequest = Mockito.mock(HttpServletRequest.class);
        mockResponse = Mockito.mock(HttpServletResponse.class);

        doNothing().when(mockRequest).setAttribute(anyString(), any());
    }

    @Test
    public void removeProductFromReceiptCommandExecuteTest() throws ServletException, IOException, DBException, ApplicationException {
        HttpSession mockSession = Mockito.mock(HttpSession.class);

        Receipt testReceipt = new Receipt();
        testReceipt.setId(1);
        testReceipt.setReceiptStatus(ReceiptStatus.NEW_RECEIPT);

        when(mockRequest.getSession()).thenReturn(mockSession);
        doNothing().when(mockSession).setAttribute(anyString(), any());
        when(mockSession.getAttribute("currentReceipt")).thenReturn(testReceipt);
        when(receiptDAO.findReceipt(testReceipt.getId())).thenReturn(testReceipt);
        when(mockRequest.getParameter(anyString())).thenReturn("1");

        String actual = underTest.execute(mockRequest, mockResponse);
        String expected = Commands.VIEW_RECEIPT_PRODUCTS_COMMAND;
        Assertions.assertEquals(expected, actual);

        verify(receiptDAO, times(1)).findReceipt(anyInt());
        verify(productDAO, times(1)).findProduct(anyInt());
        verify(receiptDAO, times(1)).deleteProductFromReceipt(anyInt(), any(), anyInt());
    }

}
