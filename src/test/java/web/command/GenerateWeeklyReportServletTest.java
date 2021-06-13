package web.command;

import com.my.web.command.senior_cashier.GenerateWeeklyReportServlet;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class GenerateWeeklyReportServletTest {

    @Test
    public void createProductPOSTTest() throws IOException {
        HttpServletRequest mockRequest = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse mockResponse = Mockito.mock(HttpServletResponse.class);
        HttpSession mockSession = Mockito.mock(HttpSession.class);

        when(mockRequest.getSession()).thenReturn(mockSession);

        new GenerateWeeklyReportServlet().doPost(mockRequest, mockResponse);

        verify(mockResponse, times(1)).sendRedirect(anyString());
    }

}
