package web;

import com.my.Path;
import com.my.db.entities.*;
import com.my.web.Commands;
import com.my.web.command.cashier.EditReceiptProductsCommand;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

import static org.mockito.Mockito.*;

public class EditReceiptProductsCommandTest {

    private static ReceiptDAO receiptDAO;
    private static ProductDAO productDAO;
    private static EditReceiptProductsCommand underTest;
    private static HttpServletRequest mockRequest;
    private static HttpServletResponse mockResponse;
    private static HttpSession mockSession;

    @BeforeAll
    static void init(){
        receiptDAO = Mockito.mock(ReceiptDAO.class);
        productDAO = Mockito.mock(ProductDAO.class);
        underTest = new EditReceiptProductsCommand(receiptDAO, productDAO);
        mockRequest = Mockito.mock(HttpServletRequest.class);
        mockResponse = Mockito.mock(HttpServletResponse.class);
        mockSession = Mockito.mock(HttpSession.class);

        Receipt testReceipt = new Receipt();
        testReceipt.setId(1);
        testReceipt.setReceiptStatus(ReceiptStatus.NEW_RECEIPT);

        when(mockRequest.getSession()).thenReturn(mockSession);
        when(mockSession.getAttribute("lang")).thenReturn("ru");
        when(mockRequest.getSession().getAttribute("currentReceipt")).thenReturn(testReceipt);
        when(receiptDAO.findReceipt(anyInt())).thenReturn(testReceipt);
    }

    @Test
    public void EditReceiptProductsCommandPOSTTest() throws ServletException, IOException {

        Product testProduct = new Product();
        testProduct.setId(1);
        testProduct.setAmount(99);

        when(mockRequest.getMethod()).thenReturn("POST");
        doNothing().when(mockRequest).setAttribute(anyString(), any());
        when(mockRequest.getParameter("product_id")).thenReturn("1");
        when(mockRequest.getParameter("receipt_id")).thenReturn("1");
        when(mockRequest.getParameter("oldAmount")).thenReturn("1");
        when(mockRequest.getParameter("newAmount")).thenReturn("15");
        when(productDAO.findProduct(anyInt())).thenReturn(testProduct);
        doNothing().when(receiptDAO).setAmountOfProductAtTheReceipt(anyInt(), anyInt(), anyInt());
        doNothing().when(productDAO).updateProductsAmount(anyInt(), anyInt());

        String actualCommand = underTest.execute(mockRequest, mockResponse);
        String expectedCommand = Commands.VIEW_RECEIPT_PRODUCTS_COMMAND;
        Assertions.assertEquals(expectedCommand, actualCommand);

    }

    @Test
    public void editReceiptProductsCommandGETTest() throws ServletException, IOException {

        when(mockRequest.getMethod()).thenReturn("GET");
        doNothing().when(mockRequest).setAttribute(anyString(), any());
        doNothing().when(mockSession).setAttribute(anyString(), any());
        doNothing().when(mockRequest).setAttribute(anyString(), any());

        String actualCommand = underTest.execute(mockRequest, mockResponse);
        String expectedCommand = Path.VIEW_RECEIPT_PRODUCTS_PAGE;
        Assertions.assertEquals(expectedCommand, actualCommand);

        verify(receiptDAO, times(2)).findReceipt(anyInt());
        verify(receiptDAO, times(1)).getMapOfAmountsAndProductsFromReceipt(any());
    }

}
