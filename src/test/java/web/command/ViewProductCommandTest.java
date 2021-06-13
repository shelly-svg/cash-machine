package web.command;

import com.my.Path;
import com.my.db.entities.Product;
import com.my.db.entities.dao.ProductDAO;
import com.my.web.command.common.ViewProductCommand;
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

public class ViewProductCommandTest {

    private static ProductDAO productDAO;
    private static ViewProductCommand underTest;
    private static HttpServletRequest mockRequest;
    private static HttpServletResponse mockResponse;

    @BeforeAll
    static void init() {
        productDAO = Mockito.mock(ProductDAO.class);
        underTest = new ViewProductCommand(productDAO);
        mockRequest = Mockito.mock(HttpServletRequest.class);
        mockResponse = Mockito.mock(HttpServletResponse.class);

        doNothing().when(mockRequest).setAttribute(anyString(), any());
    }

    @Test
    public void viewProductCommandExecuteTest() throws ServletException, IOException, DBException, ApplicationException {
        HttpSession mockSession = Mockito.mock(HttpSession.class);

        Product testProduct = new Product();

        when(mockRequest.getSession()).thenReturn(mockSession);
        doNothing().when(mockSession).setAttribute(anyString(), any());
        when(mockRequest.getParameter("id")).thenReturn("1");
        when(productDAO.findProduct(anyInt())).thenReturn(testProduct);

        String actual = underTest.execute(mockRequest, mockResponse);
        String expected = Path.VIEW_PRODUCT_PAGE;
        Assertions.assertEquals(expected, actual);

        verify(productDAO, times(1)).findProduct(anyInt());
    }

}
