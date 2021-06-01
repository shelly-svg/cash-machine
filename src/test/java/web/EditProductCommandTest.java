package web;

import com.my.Path;
import com.my.db.entities.*;
import com.my.web.command.commodity_expert.EditProductCommand;
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

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class EditProductCommandTest {

    private static ProductDAO productDAO;
    private static EditProductCommand underTest;
    private static HttpServletRequest mockRequest;
    private static HttpServletResponse mockResponse;

    @BeforeAll
    static void init(){
        productDAO = Mockito.mock(ProductDAO.class);
        underTest = new EditProductCommand(productDAO);
        mockRequest = Mockito.mock(HttpServletRequest.class);
        mockResponse = Mockito.mock(HttpServletResponse.class);
        HttpSession mockSession = Mockito.mock(HttpSession.class);

        when(mockRequest.getSession()).thenReturn(mockSession);
        when(mockSession.getAttribute("lang")).thenReturn("ru");
        doNothing().when(mockSession).setAttribute(anyString(), any());
        doNothing().when(mockRequest).setAttribute(anyString(), any());
        when(mockRequest.getParameter("id")).thenReturn("1");
    }

    @Test
    public void EditProductCommandPOSTTest() throws ServletException, IOException, DBException, ApplicationException {
        when(mockRequest.getMethod()).thenReturn("POST");
        doNothing().when(mockRequest).setAttribute(anyString(), any());
        when(mockRequest.getParameter("amount")).thenReturn("13");
        doNothing().when(productDAO).updateProductsAmount(anyInt(), anyInt());

        String actualCommand = underTest.execute(mockRequest, mockResponse);
        String expectedCommand = "controller?command=viewProduct&id=1";
        Assertions.assertEquals(expectedCommand, actualCommand);

        verify(productDAO).updateProductsAmount(anyInt(), anyInt());
    }

    @Test
    public void editProductCommandGETTest() throws ServletException, IOException, DBException, ApplicationException {
        when(mockRequest.getMethod()).thenReturn("GET");
        when(productDAO.findProduct(any())).thenReturn(new Product());

        String actual = underTest.execute(mockRequest, mockResponse);
        String expected = Path.EDIT_PRODUCT_PAGE;
        Assertions.assertEquals(expected, actual);

        verify(productDAO).findProduct(anyInt());
    }

}
