package db.entities;

import com.my.db.entities.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;


public class CategoryDAOTest {

    private Category expectedCategory;
    private CategoryDAO instance;

    @Mock
    Connection mockCon;
    @Mock
    PreparedStatement mockPstm;
    @Mock
    ResultSet mockRS;

    @BeforeEach
    public void setUp() throws SQLException {
        mockCon = Mockito.mock(Connection.class);
        mockPstm = Mockito.mock(PreparedStatement.class);
        mockRS = Mockito.mock(ResultSet.class);
        instance = new CategoryDAO(true, mockCon);

        doNothing().when(mockCon).commit();
        when(mockCon.prepareStatement(anyString())).thenReturn(mockPstm);
        doNothing().when(mockPstm).setInt(anyInt(), anyInt());
        when(mockPstm.executeQuery()).thenReturn(mockRS);
        when(mockRS.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);
        int categoryId = 1;
        String catNameRu = "categoryRu";
        String catNameEn = "categoryEn";
        when(mockRS.getInt(Fields.ENTITY__ID)).thenReturn(categoryId);
        when(mockRS.getString(Fields.CATEGORY__NAME_RU)).thenReturn(catNameRu);
        when(mockRS.getString(Fields.CATEGORY__NAME_EN)).thenReturn(catNameEn);

        expectedCategory = new Category();
        expectedCategory.setId(categoryId);
        expectedCategory.setNameRu(catNameRu);
        expectedCategory.setNameEn(catNameEn);
    }

    @Test
    public void testFindCategoryByName() throws SQLException {
        Category actualCategory = instance.findCategoryByName("categoryRu", "ru");

        verify(mockCon, times(1)).prepareStatement(anyString());
        verify(mockPstm, times(1)).executeQuery();
        verify(mockCon, times(1)).commit();
        verify(mockRS, times(1)).next();
        Assertions.assertEquals(expectedCategory, actualCategory);
    }

    @Test
    public void testFindCategoryById() throws SQLException {
        Category actualCategory = instance.findCategoryById(1);

        verify(mockCon, times(1)).prepareStatement(anyString());
        verify(mockPstm, times(1)).executeQuery();
        verify(mockCon, times(1)).commit();
        verify(mockRS, times(1)).next();
        Assertions.assertEquals(expectedCategory, actualCategory);
    }

    @Test
    public void testFindAllCategories() throws SQLException {
        Map<Integer, Category> actualCategories = instance.findAllCategories();
        Map<Integer, Category> expectedCategories = new HashMap<>();
        expectedCategories.put(expectedCategory.getId(), expectedCategory);

        verify(mockCon, times(1)).prepareStatement(anyString());
        verify(mockPstm, times(1)).executeQuery();
        verify(mockCon, times(1)).commit();
        verify(mockRS, times(2)).next();

        Assertions.assertEquals(1, actualCategories.size());
        Assertions.assertEquals(expectedCategories, actualCategories);
    }

}
