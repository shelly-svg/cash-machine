package web;

import com.my.Path;
import com.my.db.entities.Delivery;
import com.my.db.entities.User;
import com.my.db.entities.dao.*;
import com.my.web.Commands;
import com.my.web.command.cashier.CreateReceiptCommand;
import com.my.web.exception.ApplicationException;
import com.my.web.exception.DBException;
import com.my.web.validator.ReceiptValidator;
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

public class CreateReceiptCommandTest {

    private static DeliveryDAO deliveryDAO;
    private static ReceiptDAO receiptDAO;
    private static ReceiptValidator receiptValidator;
    private static UserDAO userDAO;
    private static CreateReceiptCommand underTest;
    private static HttpServletRequest mockRequest;
    private static HttpServletResponse mockResponse;

    @BeforeAll
    static void init() {
        deliveryDAO = Mockito.mock(DeliveryDAO.class);
        receiptDAO = Mockito.mock(ReceiptDAO.class);
        receiptValidator = Mockito.mock(ReceiptValidator.class);
        userDAO = Mockito.mock(UserDAO.class);
        underTest = new CreateReceiptCommand(deliveryDAO, receiptDAO, receiptValidator, userDAO);
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
    public void createReceiptPOSTTest() throws ServletException, IOException, DBException, ApplicationException {
        User testUser = new User();
        testUser.setId(1);
        HttpSession mockSession = Mockito.mock(HttpSession.class);

        when(mockRequest.getSession()).thenReturn(mockSession);
        when(mockRequest.getMethod()).thenReturn("POST");
        when(mockSession.getAttribute("user")).thenReturn(testUser);
        when(userDAO.findUser(anyInt())).thenReturn(testUser);
        when(deliveryDAO.findDeliveryById(anyInt())).thenReturn(new Delivery());
        when(receiptValidator.validate(any())).thenReturn(true);
        when(receiptDAO.createReceipt(any())).thenReturn(1);

        String actual = underTest.execute(mockRequest, mockResponse);
        String expected = Commands.VIEW_CURRENT_RECEIPT_COMMAND;
        Assertions.assertEquals(expected, actual);

        verify(receiptDAO, times(1)).createReceipt(any());
    }

    @Test
    public void createReceiptGETTest() throws ServletException, IOException, DBException, ApplicationException {
        when(mockRequest.getMethod()).thenReturn("GET");

        String actual = underTest.execute(mockRequest, mockResponse);
        String expected = Path.CREATE_RECEIPT_PAGE;
        Assertions.assertEquals(expected, actual);

        verify(deliveryDAO, times(1)).getAllDeliveries();
    }

}
