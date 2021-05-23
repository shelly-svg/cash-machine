package com.my.db.entities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReceiptDAO {

    private static final String SQL__CREATE_NEW_RECEIPT = "INSERT INTO receipt(creation_time, name_ru, name_en, address_ru, address_en, " +
            "description_ru, description_en, phone_number, delivery_id, receipt_status_id, user_id) value \n" +
            "(default, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";



    public int createReceipt(Receipt receipt) {
        int generatedKey = 0;
        PreparedStatement preparedStatement;
        Connection con = null;
        try {
            con = DBManager.getInstance().getConnection();
            preparedStatement = con.prepareStatement(SQL__CREATE_NEW_RECEIPT, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, receipt.getNameRu());
            preparedStatement.setString(2, receipt.getNameEn());
            preparedStatement.setString(3, receipt.getAddressRu());
            preparedStatement.setString(4, receipt.getAddressEn());
            preparedStatement.setString(5, receipt.getDescriptionRu());
            preparedStatement.setString(6, receipt.getDescriptionEn());
            preparedStatement.setString(7, receipt.getPhoneNumber());
            preparedStatement.setInt(8, receipt.getDeliveryId());
            preparedStatement.setInt(9, receipt.getReceiptStatusId());
            preparedStatement.setInt(10, receipt.getUserId());
            preparedStatement.execute();

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    generatedKey = generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating of receipt is failed, no ID obtained");
                }
            }

            preparedStatement.close();
        } catch (SQLException ex) {
            assert con != null;
            DBManager.getInstance().rollbackAndClose(con);
            ex.printStackTrace();
        } finally {
            assert con != null;
            DBManager.getInstance().commitAndClose(con);
        }
        return generatedKey;
    }

    private static class ReceiptMapper implements EntityMapper<Receipt> {

        @Override
        public Receipt mapRow(ResultSet rs) {
            try {
                Receipt receipt = new Receipt();
                receipt.setId(rs.getInt(Fields.ENTITY__ID));
                receipt.setCreateTime(rs.getTime(Fields.RECEIPT_CREATION_TIME));
                receipt.setNameRu(rs.getString(Fields.RECEIPT_NAME_RU));
                receipt.setNameEn(rs.getString(Fields.RECEIPT_NAME_EN));
                receipt.setAddressRu(rs.getString(Fields.RECEIPT_ADDRESS_RU));
                receipt.setAddressEn(rs.getString(Fields.RECEIPT_ADDRESS_EN));
                receipt.setDescriptionRu(rs.getString(Fields.RECEIPT_DESCRIPTION_RU));
                receipt.setDescriptionEn(rs.getString(Fields.RECEIPT_DESCRIPTION_EN));
                receipt.setPhoneNumber(rs.getString(Fields.RECEIPT_PHONE_NUMBER));
                receipt.setDeliveryId(rs.getInt(Fields.RECEIPT_DELIVERY_ID));
                receipt.setReceiptStatusId(rs.getInt(Fields.RECEIPT_RECEIPT_STATUS_ID));
                receipt.setUserId(rs.getInt(Fields.RECEIPT_USER_ID));
                return receipt;
            } catch (SQLException e) {
                throw new IllegalStateException(e);
            }
        }

    }

}
