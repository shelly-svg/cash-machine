package db.entities;

import com.my.db.entities.*;
import com.my.web.exception.ApplicationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.sql.*;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class ProductDAOTest {
    private Product expectedProduct;
    private ProductDAO instance;

    @Mock
    Connection mockCon;
    @Mock
    PreparedStatement preparedStatement;
    @Mock
    ResultSet mockRS;

    @BeforeEach
    public void setUp() throws SQLException {
        mockCon = Mockito.mock(Connection.class);
        preparedStatement = Mockito.mock(PreparedStatement.class);
        mockRS = Mockito.mock(ResultSet.class);
        instance = new ProductDAO(true, mockCon);

        doNothing().when(mockCon).commit();
        when(mockCon.prepareStatement(anyString())).thenReturn(preparedStatement);
        doNothing().when(preparedStatement).setInt(anyInt(), anyInt());
        when(preparedStatement.executeQuery()).thenReturn(mockRS);
        when(mockRS.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);

        int id = 1;
        String nameRu = "productNameRu";
        String nameEn = "productNameEn";
        String code = "productCode";
        BigDecimal price = new BigDecimal(999);
        int amount = 10;
        BigDecimal weight = new BigDecimal("333.33");
        String descriptionRu = "productDescriptionRu";
        String descriptionEn = "productDescriptionEn";
        Category category = new Category();
        category.setId(id);
        category.setNameRu("categoryNameRu");
        category.setNameEn("categoryNameEn");

        when(mockRS.getInt(Fields.ENTITY__ID)).thenReturn(id);
        when(mockRS.getString(Fields.PRODUCT__NAME_RU)).thenReturn(nameRu);
        when(mockRS.getString(Fields.PRODUCT__NAME_EN)).thenReturn(nameEn);
        when(mockRS.getString(Fields.PRODUCT__CODE)).thenReturn(code);
        when(mockRS.getBigDecimal(Fields.PRODUCT__PRICE)).thenReturn(price);
        when(mockRS.getInt(Fields.PRODUCT__AMOUNT)).thenReturn(amount);
        when(mockRS.getBigDecimal(Fields.PRODUCT__WEIGHT)).thenReturn(weight);
        when(mockRS.getString(Fields.PRODUCT__DESCRIPTION_RU)).thenReturn(descriptionRu);
        when(mockRS.getString(Fields.PRODUCT__DESCRIPTION_EN)).thenReturn(descriptionEn);
        when(mockRS.getInt(Fields.PRODUCT__CATEGORY_ID)).thenReturn(1);

        expectedProduct = new Product();
        expectedProduct.setId(id);
        expectedProduct.setNameRu(nameRu);
        expectedProduct.setNameEn(nameEn);
        expectedProduct.setCode(code);
        expectedProduct.setPrice(price);
        expectedProduct.setAmount(amount);
        expectedProduct.setWeight(weight);
        expectedProduct.setDescriptionRu(descriptionRu);
        expectedProduct.setDescriptionEn(descriptionEn);
        expectedProduct.setCategory(category);
    }

    @Test
    public void testUpdateProductAmount() throws SQLException, ApplicationException {
        instance.updateProductsAmount(expectedProduct.getId(), 10);
        verify(mockCon, times(1)).prepareStatement(anyString());
        verify(preparedStatement, times(1)).execute();
        verify(mockCon, times(1)).commit();
    }

    @Test
    public void testAddProduct() throws SQLException, ApplicationException {

        Product product = new Product();
        product.setId(expectedProduct.getId());
        product.setNameRu(expectedProduct.getNameRu());
        product.setNameEn(expectedProduct.getNameEn());
        product.setCode(expectedProduct.getCode());
        product.setPrice(expectedProduct.getPrice());
        product.setAmount(expectedProduct.getAmount());
        product.setWeight(expectedProduct.getWeight());
        product.setDescriptionRu(expectedProduct.getDescriptionRu());
        product.setDescriptionEn(expectedProduct.getDescriptionEn());
        product.setCategory(expectedProduct.getCategory());

        when(mockCon.prepareStatement(anyString(), anyInt())).thenReturn(preparedStatement);
        doNothing().when(preparedStatement).setString(anyInt(), anyString());
        doNothing().when(preparedStatement).setInt(anyInt(), anyInt());
        doNothing().when(preparedStatement).setBigDecimal(anyInt(), any());

        when(preparedStatement.getGeneratedKeys()).thenReturn(mockRS);
        when(mockRS.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);
        when(mockRS.getInt(1)).thenReturn(1);

        int expected = 1;
        int actual = instance.addProduct(product);


        verify(mockCon, times(1)).prepareStatement(anyString(), anyInt());
        verify(preparedStatement, times(1)).execute();
        verify(mockCon, times(1)).commit();
        verify(mockRS, times(1)).next();

        Assertions.assertEquals(expected, actual);
    }

}
