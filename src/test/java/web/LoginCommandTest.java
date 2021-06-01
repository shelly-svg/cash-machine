package web;

import com.my.web.Commands;
import com.my.web.command.common.LoginCommand;
import com.my.web.exception.ApplicationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class LoginCommandTest {

    @Test
    public void LoginCommandExecuteTestCaptcha() throws ServletException, IOException, ApplicationException {
        HttpServletRequest mockRequest = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse mockResponse = Mockito.mock(HttpServletResponse.class);
        HttpSession mockSession = Mockito.mock(HttpSession.class);

        when(mockRequest.getSession()).thenReturn(mockSession);
        when(mockSession.getAttribute("lang")).thenReturn("ru");
        when(mockRequest.getParameter("login")).thenReturn("myLogin");
        when(mockRequest.getParameter("password")).thenReturn("myPassword");
        when(mockRequest.getParameter("g-recaptcha-response")).thenReturn("");


        String actualCommand = new LoginCommand().execute(mockRequest, mockResponse);
        String expectedCommand = Commands.ERROR_PAGE_COMMAND;

        verify(mockSession, times(1)).getAttribute(anyString());
        verify(mockRequest, times(1)).getSession();
        verify(mockRequest, times(3)).getParameter(anyString());

        Assertions.assertEquals(actualCommand, expectedCommand);

    }

}
