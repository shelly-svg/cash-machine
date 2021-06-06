package web;

import com.my.db.entities.*;
import com.my.db.entities.dao.ProductDAO;
import com.my.db.entities.dao.ReceiptDAO;
import com.my.web.Commands;
import com.my.web.command.cashier.AddProductsIntoCurrentReceiptCommand;
import com.my.web.exception.ApplicationException;
import com.my.web.exception.DBException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

import static org.mockito.Mockito.*;

public class AddProductsIntoCurrentReceiptCommandTest {

    @Test
    public void AddProductsCommandTest() throws ServletException, IOException, DBException, ApplicationException {
        ReceiptDAO receiptDAO = Mockito.mock(ReceiptDAO.class);
        ProductDAO productDAO = Mockito.mock(ProductDAO.class);
        AddProductsIntoCurrentReceiptCommand underTest = new AddProductsIntoCurrentReceiptCommand(receiptDAO, productDAO);
        HttpServletRequest mockRequest = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse mockResponse = Mockito.mock(HttpServletResponse.class);
        HttpSession mockSession = Mockito.mock(HttpSession.class);

        Receipt testReceipt = new Receipt();
        testReceipt.setId(1);
        testReceipt.setReceiptStatus(ReceiptStatus.NEW_RECEIPT);
        Product testProduct = new Product();
        testProduct.setId(1);
        testProduct.setAmount(5);

        when(mockRequest.getSession()).thenReturn(mockSession);
        when(mockSession.getAttribute("lang")).thenReturn("ru");
        when(mockRequest.getParameter("id")).thenReturn("1");
        when(mockRequest.getSession().getAttribute("currentReceipt")).thenReturn(testReceipt);
        when(receiptDAO.findReceipt(anyInt())).thenReturn(testReceipt);
        when(productDAO.findProduct(anyInt())).thenReturn(testProduct);
        doNothing().when(receiptDAO).addProductIntoReceipt(any(), anyInt());
        doNothing().when(productDAO).updateProductsAmount(anyInt(), anyInt());


        String actualCommand = underTest.execute(mockRequest, mockResponse);
        String expectedCommand = Commands.VIEW_SEARCH_PRODUCT_COMMAND;
        Assertions.assertEquals(actualCommand, expectedCommand);

        verify(receiptDAO, times(1)).findReceipt(anyInt());
        verify(productDAO, times(1)).findProduct(anyInt());
    }

}
