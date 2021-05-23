package com.my.db.entities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DeliveryDAO {

    private static final String SQL__GET_ALL_DELIVERIES = "SELECT * FROM delivery ORDER BY id;";

    public List<Delivery> getAllDeliveries() {
        List<Delivery> deliveries = new ArrayList<>();
        PreparedStatement p;
        ResultSet rs;
        Connection con = null;
        try {
            con = DBManager.getInstance().getConnection();
            DeliveryDAO.DeliveryMapper mapper = new DeliveryDAO.DeliveryMapper();
            p = con.prepareStatement(SQL__GET_ALL_DELIVERIES);
            rs = p.executeQuery();
            while (rs.next()) {
                deliveries.add(mapper.mapRow(rs));
            }
            rs.close();
            p.close();
        } catch (SQLException ex) {
            assert con != null;
            DBManager.getInstance().rollbackAndClose(con);
            ex.printStackTrace();
        } finally {
            assert con != null;
            DBManager.getInstance().commitAndClose(con);
        }
        return deliveries;
    }

    private static class DeliveryMapper implements EntityMapper<Delivery> {

        @Override
        public Delivery mapRow(ResultSet rs) {
            try {
                Delivery delivery = new Delivery();
                delivery.setId(rs.getInt(Fields.ENTITY__ID));
                delivery.setNameRu(rs.getString(Fields.DELIVERY_NAME_RU));
                delivery.setNameEn(rs.getString(Fields.DELIVERY_NAME_EN));
                return delivery;
            } catch (SQLException e) {
                throw new IllegalStateException(e);
            }
        }
    }

}
