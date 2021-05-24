package com.my.db.entities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReceiptDAO {

    private static final String SQL__CREATE_NEW_RECEIPT = "INSERT INTO receipt(creation_time, name_ru, name_en, address_ru, address_en, " +
            "description_ru, description_en, phone_number, delivery_id, receipt_status_id, user_id) value \n" +
            "(default, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

    private static final String SQL__ADD_PRODUCT_INTO_RECEIPT = "INSERT INTO receipt_has_product(receipt_id, product_id, amount, price) value" +
            " (?,?,1,(SELECT price FROM product WHERE id=?));";

    private static final String SQL__FIND_PRODUCTS_FROM_RECEIPT = "SELECT product_id FROM receipt_has_product WHERE receipt_id=?;";

    private static final String SQL__FIND_RECEIPT_BY_SEARCH = "SELECT * FROM receipt WHERE id LIKE ? OR creation_time LIKE ? LIMIT ?,?;";

    private static final String SQL__FIND_NUMBER_OF_ROWS_AFFECTED_BY_SEARCH_RECEIPT = "SELECT COUNT(*) FROM receipt WHERE id LIKE ? OR creation_time LIKE ?;";

    public int countOfRowsAffectedBySearch(String pattern) {
        int numberOfRows = 0;
        PreparedStatement p;
        ResultSet rs;
        Connection con = null;
        try {
            con = DBManager.getInstance().getConnection();
            p = con.prepareStatement(SQL__FIND_NUMBER_OF_ROWS_AFFECTED_BY_SEARCH_RECEIPT);
            pattern = "%" + pattern + "%";
            p.setString(1, pattern);
            p.setString(2, pattern);
            rs = p.executeQuery();
            if (rs.next()) {
                numberOfRows = rs.getInt(1);
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
        return numberOfRows;
    }

    public List<Receipt> searchReceipts(String pattern, int currentPage, int recordsPerPage) {
        List<Receipt> receipts = new ArrayList<>();
        PreparedStatement p;
        ResultSet rs;
        Connection con = null;
        int start = currentPage * recordsPerPage - recordsPerPage;
        try {
            con = DBManager.getInstance().getConnection();
            ReceiptDAO.ReceiptMapper mapper = new ReceiptDAO.ReceiptMapper();
            p = con.prepareStatement(SQL__FIND_RECEIPT_BY_SEARCH);
            pattern = "%" + pattern + "%";
            p.setString(1, pattern);
            p.setString(2, pattern);
            p.setInt(3, start);
            p.setInt(4, recordsPerPage);
            rs = p.executeQuery();
            while (rs.next()) {
                receipts.add(mapper.mapRow(rs));
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
        return receipts;
    }

    public List<Product> getAllProductsFromReceipt(int receiptId) {
        List<Product> products = new ArrayList<>();
        List<Integer> productsIds = new ArrayList<>();
        ProductDAO productDAO = new ProductDAO();
        PreparedStatement p;
        ResultSet rs;
        Connection con = null;

        try {
            con = DBManager.getInstance().getConnection();
            p = con.prepareStatement(SQL__FIND_PRODUCTS_FROM_RECEIPT);
            p.setInt(1, receiptId);
            rs = p.executeQuery();
            while (rs.next()) {
                productsIds.add(rs.getInt(Fields.RECEIPT_HAS_PRODUCT_PRODUCT_ID));
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

        for (Integer id : productsIds) {
            products.add(productDAO.findProduct(id));
        }
        return products;
    }

    public void addProductIntoReceipt(int productId, int receiptId) {
        PreparedStatement preparedStatement;
        Connection con = null;
        try {
            con = DBManager.getInstance().getConnection();
            preparedStatement = con.prepareStatement(SQL__ADD_PRODUCT_INTO_RECEIPT);
            preparedStatement.setInt(1, receiptId);
            preparedStatement.setInt(2, productId);
            preparedStatement.setInt(3, productId);
            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException ex) {
            assert con != null;
            DBManager.getInstance().rollbackAndClose(con);
            ex.printStackTrace();
        } finally {
            assert con != null;
            DBManager.getInstance().commitAndClose(con);
        }
    }

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
            preparedStatement.setInt(8, receipt.getDelivery().getId());
            preparedStatement.setInt(9, receipt.getReceiptStatus().getId());
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
                receipt.setDelivery(new DeliveryDAO().findDeliveryById(rs.getInt(Fields.RECEIPT_DELIVERY_ID)));
                receipt.setReceiptStatus(ReceiptStatus.getReceiptStatus(rs.getInt(Fields.RECEIPT_RECEIPT_STATUS_ID)));
                receipt.setUserId(rs.getInt(Fields.RECEIPT_USER_ID));
                return receipt;
            } catch (SQLException e) {
                throw new IllegalStateException(e);
            }
        }

    }

}
