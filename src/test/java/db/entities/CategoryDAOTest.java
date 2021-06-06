package db.entities;

import com.my.db.entities.*;
import com.my.db.entities.dao.CategoryDAO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
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
    PreparedStatement preparedStatement;
    @Mock
    ResultSet mockRS;

    @BeforeEach
    public void setUp() throws SQLException {
        mockCon = Mockito.mock(Connection.class);
        preparedStatement = Mockito.mock(PreparedStatement.class);
        mockRS = Mockito.mock(ResultSet.class);
        instance = new CategoryDAO();

        doNothing().when(mockCon).commit();
        when(mockCon.prepareStatement(anyString())).thenReturn(preparedStatement);
        doNothing().when(preparedStatement).setInt(anyInt(), anyInt());
        when(preparedStatement.executeQuery()).thenReturn(mockRS);
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
        DBManager dbManager = Mockito.mock(DBManager.class);
        try (MockedStatic<DBManager> ignored = mockStatic(DBManager.class)) {
            when(DBManager.getInstance()).thenReturn(dbManager);
            when(DBManager.getInstance().getConnection()).thenReturn(mockCon);

            Category actualCategory = instance.findCategoryByName("categoryRu", "ru");

            verify(mockCon, times(1)).prepareStatement(anyString());
            verify(preparedStatement, times(1)).executeQuery();
            verify(mockRS, times(1)).next();
            Assertions.assertEquals(expectedCategory, actualCategory);
            verify(dbManager, times(1)).commitAndClose(any(), any(), any());
        }
    }

    @Test
    public void testFindCategoryById() throws SQLException {
        DBManager dbManager = Mockito.mock(DBManager.class);
        try (MockedStatic<DBManager> ignored = mockStatic(DBManager.class)) {
            when(DBManager.getInstance()).thenReturn(dbManager);
            when(DBManager.getInstance().getConnection()).thenReturn(mockCon);

            Category actualCategory = instance.findCategoryById(1);

            verify(mockCon, times(1)).prepareStatement(anyString());
            verify(preparedStatement, times(1)).executeQuery();
            Assertions.assertEquals(expectedCategory, actualCategory);
            verify(dbManager, times(1)).commitAndClose(any(), any(), any());
        }
    }

    @Test
    public void testFindAllCategories() throws SQLException{
        DBManager dbManager = Mockito.mock(DBManager.class);
        try (MockedStatic<DBManager> ignored = mockStatic(DBManager.class)) {
            when(DBManager.getInstance()).thenReturn(dbManager);
            when(DBManager.getInstance().getConnection()).thenReturn(mockCon);

            Map<Integer, Category> actualCategories = instance.findAllCategories();
            Map<Integer, Category> expectedCategories = new HashMap<>();
            expectedCategories.put(expectedCategory.getId(), expectedCategory);

            verify(mockCon, times(1)).prepareStatement(anyString());
            verify(preparedStatement, times(1)).executeQuery();
            verify(mockRS, times(2)).next();
            verify(dbManager, times(1)).commitAndClose(any(), any(), any());

            Assertions.assertEquals(1, actualCategories.size());
            Assertions.assertEquals(expectedCategories, actualCategories);
        }
    }

}
