package db.entities;

import com.my.db.entities.*;
import com.my.web.exception.DBException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class DeliveryDAOTest {

    private Delivery expectedDelivery;
    private DeliveryDAO instance;

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
        instance = new DeliveryDAO(true, mockCon);

        doNothing().when(mockCon).commit();
        when(mockCon.prepareStatement(anyString())).thenReturn(mockPstm);
        doNothing().when(mockPstm).setInt(anyInt(), anyInt());
        when(mockPstm.executeQuery()).thenReturn(mockRS);
        when(mockRS.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);
        int deliveryId = 1;
        String deliveryNameRu = "deliveryNameRu";
        String deliveryNameEn = "deliveryNameEn";
        when(mockRS.getInt(Fields.ENTITY__ID)).thenReturn(deliveryId);
        when(mockRS.getString(Fields.DELIVERY_NAME_RU)).thenReturn(deliveryNameRu);
        when(mockRS.getString(Fields.DELIVERY_NAME_EN)).thenReturn(deliveryNameEn);

        expectedDelivery = new Delivery();
        expectedDelivery.setId(deliveryId);
        expectedDelivery.setNameRu(deliveryNameRu);
        expectedDelivery.setNameEn(deliveryNameEn);
    }

    @Test
    public void testFindDeliveryById() throws SQLException, DBException {
        Delivery actualDelivery = instance.findDeliveryById(expectedDelivery.getId());

        verify(mockCon, times(1)).prepareStatement(anyString());
        verify(mockPstm, times(1)).executeQuery();
        verify(mockCon, times(1)).commit();
        verify(mockRS, times(1)).next();

        Assertions.assertEquals(expectedDelivery, actualDelivery);
    }

    @Test
    public void testFindCategoryByName() throws SQLException, DBException {
        Delivery actualDelivery = instance.findDeliveryByName(expectedDelivery.getNameRu());

        verify(mockCon, times(1)).prepareStatement(anyString());
        verify(mockPstm, times(1)).executeQuery();
        verify(mockCon, times(1)).commit();
        verify(mockRS, times(1)).next();

        Assertions.assertEquals(expectedDelivery, actualDelivery);
    }

    @Test
    public void testGetAllDeliveries() throws SQLException, DBException {
        List<Delivery> deliveries = instance.getAllDeliveries();

        verify(mockCon, times(1)).prepareStatement(anyString());
        verify(mockPstm, times(1)).executeQuery();
        verify(mockCon, times(1)).commit();
        verify(mockRS, times(2)).next();

        Assertions.assertEquals(1, deliveries.size());
        Assertions.assertEquals(expectedDelivery, deliveries.get(0));
    }

}
