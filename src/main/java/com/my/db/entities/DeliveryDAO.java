package com.my.db.entities;

import com.my.web.exception.ApplicationException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DeliveryDAO {

    private final boolean isTest;
    private Connection connection;

    public DeliveryDAO() {
        isTest = false;
    }

    public DeliveryDAO(boolean isTest, Connection connection) {
        this.isTest = isTest;
        this.connection = connection;
    }

    private static final String SQL__GET_ALL_DELIVERIES = "SELECT * FROM delivery ORDER BY id;";

    private static final String SQL__GET_DELIVERY_BY_NAME = "SELECT * FROM delivery WHERE name_ru LIKE ? OR name_en LIKE ?;";

    private static final String SQL__GET_DELIVERY_BY_ID = "SELECT * FROM delivery WHERE id=?";

    public Delivery findDeliveryById(int id) throws ApplicationException {
        Delivery delivery = new Delivery();
        PreparedStatement p;
        ResultSet rs;
        Connection con = null;
        try {
            if (!isTest) {
                con = DBManager.getInstance().getConnection();
            } else {
                con = this.connection;
            }
            DeliveryDAO.DeliveryMapper mapper = new DeliveryDAO.DeliveryMapper();
            p = con.prepareStatement(SQL__GET_DELIVERY_BY_ID);
            p.setInt(1, id);
            rs = p.executeQuery();
            if (rs.next()) {
                delivery = mapper.mapRow(rs);
            }
            rs.close();
            p.close();
        } catch (SQLException ex) {
            assert con != null;
            DBManager.getInstance().rollbackAndClose(con);
            throw new ApplicationException(ex.getMessage());
        } finally {
            assert con != null;
            DBManager.getInstance().commitAndClose(con);
        }
        return delivery;
    }

    public Delivery findDeliveryByName(String name) throws ApplicationException {
        Delivery delivery = new Delivery();
        name = "%" + name + "%";
        PreparedStatement p;
        ResultSet rs;
        Connection con = null;
        try {
            if (!isTest) {
                con = DBManager.getInstance().getConnection();
            } else {
                con = this.connection;
            }
            DeliveryDAO.DeliveryMapper mapper = new DeliveryDAO.DeliveryMapper();
            p = con.prepareStatement(SQL__GET_DELIVERY_BY_NAME);
            p.setString(1, name);
            p.setString(2, name);
            rs = p.executeQuery();
            if (rs.next()) {
                delivery = mapper.mapRow(rs);
            }
            rs.close();
            p.close();
        } catch (SQLException ex) {
            assert con != null;
            DBManager.getInstance().rollbackAndClose(con);
            throw new ApplicationException(ex.getMessage());
        } finally {
            assert con != null;
            DBManager.getInstance().commitAndClose(con);
        }
        return delivery;
    }

    public List<Delivery> getAllDeliveries() throws ApplicationException {
        List<Delivery> deliveries = new ArrayList<>();
        PreparedStatement p;
        ResultSet rs;
        Connection con = null;
        try {
            if (!isTest) {
                con = DBManager.getInstance().getConnection();
            } else {
                con = this.connection;
            }
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
            throw new ApplicationException(ex.getMessage());
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
