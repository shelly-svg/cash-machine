package db.entities;

import com.my.db.entities.*;
import com.my.db.entities.dao.ReceiptDAO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.sql.*;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class ReceiptDAOTest {

    private ReceiptDAO instance;

    @Mock
    Connection mockCon;
    @Mock
    PreparedStatement mockPreparedStatement;
    @Mock
    ResultSet mockRS;

    @BeforeEach
    public void setUp() throws SQLException {
        mockCon = Mockito.mock(Connection.class);
        mockPreparedStatement = Mockito.mock(PreparedStatement.class);
        mockRS = Mockito.mock(ResultSet.class);
        instance = new ReceiptDAO();

        doNothing().when(mockCon).commit();
        when(mockCon.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        doNothing().when(mockPreparedStatement).setInt(anyInt(), anyInt());
        when(mockPreparedStatement.executeQuery()).thenReturn(mockRS);
        when(mockRS.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);
    }

    @Test
    public void testUpdateProductAmount() throws SQLException {
        DBManager dbManager = Mockito.mock(DBManager.class);
        try (MockedStatic<DBManager> ignored = mockStatic(DBManager.class)) {
            when(DBManager.getInstance()).thenReturn(dbManager);
            when(DBManager.getInstance().getConnection()).thenReturn(mockCon);

            instance.deleteProductFromReceipt(1, new Product(), 5);
            verify(mockCon, times(2)).prepareStatement(anyString());
            verify(mockPreparedStatement, times(2)).execute();
        }
    }

    @Test
    public void testSetReceiptStatus() throws SQLException {
        DBManager dbManager = Mockito.mock(DBManager.class);
        try (MockedStatic<DBManager> ignored = mockStatic(DBManager.class)) {
            when(DBManager.getInstance()).thenReturn(dbManager);
            when(DBManager.getInstance().getConnection()).thenReturn(mockCon);

            instance.setReceiptStatus(1, ReceiptStatus.CLOSED);
            verify(mockCon, times(1)).prepareStatement(anyString());
            verify(mockPreparedStatement, times(1)).execute();
            verify(dbManager, times(1)).commitAndClose(any(), any());
        }
    }

    @Test
    public void testSetAmountOfTheProductsAtTheReceipt() throws SQLException {
        DBManager dbManager = Mockito.mock(DBManager.class);
        try (MockedStatic<DBManager> ignored = mockStatic(DBManager.class)) {
            when(DBManager.getInstance()).thenReturn(dbManager);
            when(DBManager.getInstance().getConnection()).thenReturn(mockCon);

            instance.setAmountOfProductAtTheReceipt(1, 1, 1, 1);
            verify(mockCon, times(2)).prepareStatement(anyString());
            verify(mockPreparedStatement, times(2)).execute();
        }
    }

    @Test
    public void testCountOfRowsAffectedBySearch() throws SQLException {
        DBManager dbManager = Mockito.mock(DBManager.class);
        try (MockedStatic<DBManager> ignored = mockStatic(DBManager.class)) {
            when(DBManager.getInstance()).thenReturn(dbManager);
            when(DBManager.getInstance().getConnection()).thenReturn(mockCon);

            when(mockRS.getInt(1)).thenReturn(1);
            doNothing().when(mockPreparedStatement).setString(anyInt(), anyString());

            int expected = 1;
            int actual = instance.countOfRowsAffectedBySearch("name");

            verify(mockCon, times(1)).prepareStatement(anyString());
            verify(mockPreparedStatement, times(1)).executeQuery();
            verify(dbManager, times(1)).commitAndClose(any(), any(), any());

            Assertions.assertEquals(expected, actual);
        }
    }

    @Test
    public void testAddProductIntoReceipt() throws SQLException {
        DBManager dbManager = Mockito.mock(DBManager.class);
        try (MockedStatic<DBManager> ignored = mockStatic(DBManager.class)) {
            when(DBManager.getInstance()).thenReturn(dbManager);
            when(DBManager.getInstance().getConnection()).thenReturn(mockCon);

            instance.addProductIntoReceipt(new Product(), 1);
            verify(mockCon, times(2)).prepareStatement(anyString());
            verify(mockPreparedStatement, times(2)).execute();
        }
    }

    @Test
    public void testCreateReceipt() throws SQLException {
        DBManager dbManager = Mockito.mock(DBManager.class);
        try (MockedStatic<DBManager> ignored = mockStatic(DBManager.class)) {
            when(DBManager.getInstance()).thenReturn(dbManager);
            when(DBManager.getInstance().getConnection()).thenReturn(mockCon);

            int id = 1;
            Timestamp createTime = new Timestamp(System.currentTimeMillis());
            String nameRu = "receiptNameRu";
            String nameEn = "receiptNameEn";
            String addressRu = "receiptAddressRu";
            String addressEn = "receiptAddressEn";
            String descriptionRu = "receiptDescriptionRu";
            String descriptionEn = "receiptDescriptionEn";
            String phoneNumber = "receiptPhoneNumber";
            Delivery delivery = new Delivery();
            delivery.setId(1);
            delivery.setNameRu("deliveryRu");
            delivery.setNameEn("deliveryEn");
            ReceiptStatus receiptStatus = ReceiptStatus.NEW_RECEIPT;
            int userId = 2;

            when(mockCon.prepareStatement(anyString(), anyInt())).thenReturn(mockPreparedStatement);
            doNothing().when(mockPreparedStatement).setInt(anyInt(), anyInt());
            doNothing().when(mockPreparedStatement).setString(anyInt(), anyString());

            when(mockPreparedStatement.getGeneratedKeys()).thenReturn(mockRS);
            when(mockRS.getInt(1)).thenReturn(1);

            Receipt receipt = new Receipt();
            receipt.setId(id);
            receipt.setCreateTime(createTime);
            receipt.setNameRu(nameRu);
            receipt.setNameEn(nameEn);
            receipt.setAddressRu(addressRu);
            receipt.setAddressEn(addressEn);
            receipt.setDescriptionRu(descriptionRu);
            receipt.setDescriptionEn(descriptionEn);
            receipt.setPhoneNumber(phoneNumber);
            receipt.setDelivery(delivery);
            receipt.setReceiptStatus(receiptStatus);
            receipt.setUserId(userId);

            int expected = 1;
            int actual = instance.createReceipt(receipt);

            Assertions.assertEquals(expected, actual);

            verify(mockCon, times(1)).prepareStatement(anyString(), anyInt());
            verify(mockPreparedStatement, times(1)).execute();
            verify(dbManager, times(1)).commitAndClose(any(), any());
        }

    }

}
