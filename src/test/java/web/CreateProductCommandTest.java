package web;

import com.my.Path;
import com.my.db.entities.Category;
import com.my.db.entities.dao.CategoryDAO;
import com.my.db.entities.dao.ProductDAO;
import com.my.web.command.commodity_expert.CreateProductCommand;
import com.my.web.exception.ApplicationException;
import com.my.web.exception.DBException;
import com.my.web.validator.ProductValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class CreateProductCommandTest {

    private static ProductDAO productDAO;
    private static CategoryDAO categoryDAO;
    private static ProductValidator productValidator;
    private static CreateProductCommand underTest;
    private static HttpServletRequest mockRequest;
    private static HttpServletResponse mockResponse;

    @BeforeAll
    static void init() {
        productDAO = Mockito.mock(ProductDAO.class);
        categoryDAO = Mockito.mock(CategoryDAO.class);
        productValidator = Mockito.mock(ProductValidator.class);
        underTest = new CreateProductCommand(categoryDAO, productValidator, productDAO);
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
    public void createProductPOSTTest() throws ServletException, IOException, DBException, ApplicationException {
        int productId = 1;
        String nameRu = "testCreateProductNameRu";
        String nameEn = "testCreateProductNameEn";
        String code = "testCode";
        BigDecimal price = new BigDecimal("1299.99");
        int amount = 12;
        BigDecimal weight = new BigDecimal("3.200");
        String descriptionRu = "testDescriptionRu";
        String descriptionEn = "testDescriptionEn";
        Category category = new Category();
        category.setId(1);
        category.setNameRu("categoryNameRu");
        category.setNameEn("categoryNameEn");

        when(mockRequest.getMethod()).thenReturn("POST");
        doNothing().when(mockRequest).setAttribute(anyString(), any());
        when(mockRequest.getParameter("name_ru")).thenReturn(nameRu);
        when(mockRequest.getParameter("name_en")).thenReturn(nameEn);
        when(mockRequest.getParameter("code")).thenReturn(code);
        when(mockRequest.getParameter("price")).thenReturn(price.toString());
        when(mockRequest.getParameter("amount")).thenReturn(amount + "");
        when(mockRequest.getParameter("weight")).thenReturn(weight.toString());
        when(mockRequest.getParameter("description_ru")).thenReturn(descriptionRu);
        when(mockRequest.getParameter("description_en")).thenReturn(descriptionEn);
        when(mockRequest.getParameter("category_id")).thenReturn("customName");
        when(categoryDAO.findCategoryByName(anyString(), anyString())).thenReturn(category);
        when(productValidator.validate(any())).thenReturn(Boolean.TRUE);
        when(productDAO.addProduct(any())).thenReturn(productId);

        String actual = underTest.execute(mockRequest, mockResponse);
        String expected = "controller?command=viewProduct&id=1";
        Assertions.assertEquals(expected, actual);
        
        verify(categoryDAO, times(1)).findCategoryByName(anyString(), anyString());
        verify(productValidator, times(1)).validate(any());
        verify(productDAO, times(1)).addProduct(any());
    }

    @Test
    public void createProductGETTest() throws ServletException, IOException, DBException, ApplicationException {
        when(mockRequest.getMethod()).thenReturn("GET");

        String actual = underTest.execute(mockRequest, mockResponse);
        String expected = Path.ADD_PRODUCT_PAGE;
        Assertions.assertEquals(expected, actual);

        verify(categoryDAO, times(1)).findAllCategories();
    }

}
