package com.my.db.entities.dao;

import com.my.db.entities.*;
import com.my.web.exception.DBException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class ReceiptDAO {

    private static final String SQL__CREATE_NEW_RECEIPT = "INSERT INTO receipt(creation_time, name_ru, name_en, address_ru, address_en, " +
            "description_ru, description_en, phone_number, delivery_id, receipt_status_id, user_id) value \n" +
            "(default, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

    private static final String SQL__ADD_PRODUCT_INTO_RECEIPT = "INSERT INTO receipt_has_product(receipt_id, product_id, amount, price) value" +
            " (?,?,1,(SELECT price FROM product WHERE id=?));";

    private static final String SQL__FIND_PRODUCTS_FROM_RECEIPT = "SELECT product_id FROM receipt_has_product WHERE receipt_id=?;";

    private static final String SQL__FIND_RECEIPT_BY_SEARCH = "SELECT * FROM receipt WHERE id LIKE ? OR creation_time LIKE ? LIMIT ?,?;";

    private static final String SQL__FIND_NUMBER_OF_ROWS_AFFECTED_BY_SEARCH_RECEIPT = "SELECT COUNT(*) FROM receipt WHERE id LIKE ? OR creation_time LIKE ?;";

    private static final String SQL__FIND_RECEIPT_BY_ID = "SELECT * FROM receipt WHERE id=?;";

    private static final String SQL__FIND_PRODUCT_AMOUNT_AT_THE_RECEIPT = "SELECT amount FROM receipt_has_product WHERE receipt_id=? AND product_id=?;";

    private static final String SQL__SET_AMOUNT_OF_PRODUCT_AT_THE_RECEIPT = "UPDATE receipt_has_product SET amount=? WHERE receipt_id=? and product_id=?;";

    private static final String SQL__SET_RECEIPT_STATUS = "UPDATE receipt SET receipt_status_id=? WHERE id=?;";

    private static final String SQL__DELETE_PRODUCT_FROM_RECEIPT = "DELETE FROM receipt_has_product WHERE receipt_id=? AND product_id=?;";

    private static final String SQL__GET_LAST_WEEK_CLOSED_RECEIPTS = "SELECT * FROM receipt WHERE week(creation_time, 1) = week(now(),1) AND receipt_status_id=2;";

    private static final String SQL__GET_CURRENT_DAY_CLOSED_RECEIPTS_FOR_USER = "SELECT * FROM receipt WHERE creation_time >= curdate() AND user_id = ? AND receipt_status_id=2;";

    private static final String SQL__UPDATE_PRODUCT_AMOUNT_BY_ID = "UPDATE product SET amount=? WHERE id=?;";

    public void addProductIntoReceipt(Product product, int receiptId) throws DBException {
        PreparedStatement preparedStatement = null;
        Connection con = null;
        try {
            con = DBManager.getInstance().getConnection();
            con.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            preparedStatement = con.prepareStatement(SQL__ADD_PRODUCT_INTO_RECEIPT);
            preparedStatement.setInt(1, receiptId);
            preparedStatement.setInt(2, product.getId());
            preparedStatement.setInt(3, product.getId());
            preparedStatement.execute();
            preparedStatement = con.prepareStatement(SQL__UPDATE_PRODUCT_AMOUNT_BY_ID);
            preparedStatement.setInt(1, product.getAmount() - 1);
            preparedStatement.setInt(2, product.getId());
            preparedStatement.execute();
            con.commit();
        } catch (SQLException ex) {
            DBManager.getInstance().rollbackAndClose(con, preparedStatement);
            throw new DBException(ex.getMessage(), ex);
        } finally {
            DBManager.getInstance().closeResources(con, preparedStatement);
        }
    }

    public void deleteProductFromReceipt(int receiptId, Product product, int amountAtReceipt) throws DBException {
        PreparedStatement p = null;
        Connection con = null;
        try {
            con = DBManager.getInstance().getConnection();
            con.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            p = con.prepareStatement(SQL__DELETE_PRODUCT_FROM_RECEIPT);
            p.setInt(1, receiptId);
            p.setInt(2, product.getId());
            p.execute();
            p = con.prepareStatement(SQL__UPDATE_PRODUCT_AMOUNT_BY_ID);
            p.setInt(1, product.getAmount() + amountAtReceipt);
            p.setInt(2, product.getId());
            p.execute();
            con.commit();
        } catch (SQLException ex) {
            DBManager.getInstance().rollbackAndClose(con, p);
            throw new DBException(ex.getMessage(), ex);
        } finally {
            DBManager.getInstance().closeResources(con, p);
        }
    }

    public void cancelReceipt(Receipt receipt) throws DBException {
        PreparedStatement p = null;
        Connection con = null;
        try {
            con = DBManager.getInstance().getConnection();
            con.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            p = con.prepareStatement(SQL__SET_RECEIPT_STATUS);
            p.setInt(1, ReceiptStatus.CANCELED.getId());
            p.setInt(2, receipt.getId());
            p.execute();
            Map<Product, Integer> productAmountsMap = new ReceiptDAO().getMapOfAmountsAndProductsFromReceipt(receipt);
            for (Product product : productAmountsMap.keySet()) {
                p = con.prepareStatement(SQL__UPDATE_PRODUCT_AMOUNT_BY_ID);
                p.setInt(1, product.getAmount() + productAmountsMap.get(product));
                p.setInt(2, product.getId());
                p.execute();
            }
            con.commit();
        } catch (SQLException ex) {
            DBManager.getInstance().rollbackAndClose(con, p);
            throw new DBException(ex.getMessage(), ex);
        } finally {
            DBManager.getInstance().closeResources(con, p);
        }
    }

    public void setAmountOfProductAtTheReceipt(int amount, int newProductAmount, int receiptId, int productId) throws DBException {
        PreparedStatement p = null;
        Connection con = null;
        try {
            con = DBManager.getInstance().getConnection();
            con.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            p = con.prepareStatement(SQL__SET_AMOUNT_OF_PRODUCT_AT_THE_RECEIPT);
            p.setInt(1, amount);
            p.setInt(2, receiptId);
            p.setInt(3, productId);
            p.execute();
            p = con.prepareStatement(SQL__UPDATE_PRODUCT_AMOUNT_BY_ID);
            p.setInt(1, newProductAmount);
            p.setInt(2, productId);
            p.execute();
            con.commit();
        } catch (SQLException ex) {
            DBManager.getInstance().rollbackAndClose(con, p);
            throw new DBException(ex.getMessage(), ex);
        } finally {
            DBManager.getInstance().closeResources(con, p);
        }
    }

    public List<Receipt> getCurrentDayClosedReceiptsForUser(User user) throws DBException {
        List<Receipt> receipts = new ArrayList<>();
        PreparedStatement p = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = DBManager.getInstance().getConnection();
            ReceiptDAO.ReceiptMapper mapper = new ReceiptDAO.ReceiptMapper();
            p = con.prepareStatement(SQL__GET_CURRENT_DAY_CLOSED_RECEIPTS_FOR_USER);
            p.setInt(1, user.getId());
            rs = p.executeQuery();
            while (rs.next()) {
                receipts.add(mapper.mapRow(rs));
            }
        } catch (SQLException ex) {
            DBManager.getInstance().rollbackAndClose(con, p, rs);
            throw new DBException(ex.getMessage(), ex);
        } finally {
            DBManager.getInstance().commitAndClose(con, p, rs);
        }
        return receipts;
    }

    public List<Receipt> getLastWeekClosedReceipts() throws DBException {
        List<Receipt> receiptList = new ArrayList<>();
        PreparedStatement p = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = DBManager.getInstance().getConnection();
            ReceiptDAO.ReceiptMapper mapper = new ReceiptDAO.ReceiptMapper();
            p = con.prepareStatement(SQL__GET_LAST_WEEK_CLOSED_RECEIPTS);
            rs = p.executeQuery();
            while (rs.next()) {
                receiptList.add(mapper.mapRow(rs));
            }
        } catch (SQLException ex) {
            DBManager.getInstance().rollbackAndClose(con, p, rs);
            throw new DBException(ex.getMessage(), ex);
        } finally {
            DBManager.getInstance().commitAndClose(con, p, rs);
        }
        return receiptList;
    }

    public void setReceiptStatus(int receiptId, ReceiptStatus receiptStatus) throws DBException {
        PreparedStatement p = null;
        Connection con = null;
        try {
            con = DBManager.getInstance().getConnection();
            p = con.prepareStatement(SQL__SET_RECEIPT_STATUS);
            p.setInt(1, receiptStatus.getId());
            p.setInt(2, receiptId);
            p.execute();
        } catch (SQLException ex) {
            DBManager.getInstance().rollbackAndClose(con, p);
            throw new DBException(ex.getMessage(), ex);
        } finally {
            DBManager.getInstance().commitAndClose(con, p);
        }
    }

    public Map<Product, Integer> getMapOfAmountsAndProductsFromReceipt(Receipt receipt) throws DBException {
        Map<Product, Integer> productMap = new TreeMap<>();
        List<Product> products = new ReceiptDAO().getAllProductsFromReceipt(receipt.getId());
        PreparedStatement p = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = DBManager.getInstance().getConnection();
            for (Product product : products) {
                p = con.prepareStatement(SQL__FIND_PRODUCT_AMOUNT_AT_THE_RECEIPT);
                p.setInt(1, receipt.getId());
                p.setInt(2, product.getId());
                rs = p.executeQuery();
                if (rs.next()) {
                    productMap.put(product, rs.getInt(Fields.RECEIPT_HAS_PRODUCT_AMOUNT));
                }
            }
        } catch (SQLException ex) {
            DBManager.getInstance().rollbackAndClose(con, p, rs);
            throw new DBException(ex.getMessage(), ex);
        } finally {
            DBManager.getInstance().commitAndClose(con, p, rs);
        }
        return productMap;
    }

    public Receipt findReceipt(int id) throws DBException {
        Receipt receipt = new Receipt();
        PreparedStatement p = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = DBManager.getInstance().getConnection();
            ReceiptDAO.ReceiptMapper mapper = new ReceiptDAO.ReceiptMapper();
            p = con.prepareStatement(SQL__FIND_RECEIPT_BY_ID);
            p.setInt(1, id);
            rs = p.executeQuery();
            if (rs.next()) {
                receipt = mapper.mapRow(rs);
            }
        } catch (SQLException ex) {
            DBManager.getInstance().rollbackAndClose(con, p, rs);
            throw new DBException(ex.getMessage(), ex);
        } finally {
            DBManager.getInstance().commitAndClose(con, p, rs);
        }
        return receipt;
    }

    public int countOfRowsAffectedBySearch(String pattern) throws DBException {
        int numberOfRows = 0;
        PreparedStatement p = null;
        ResultSet rs = null;
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
        } catch (SQLException ex) {
            DBManager.getInstance().rollbackAndClose(con, p, rs);
            throw new DBException(ex.getMessage(), ex);
        } finally {
            DBManager.getInstance().commitAndClose(con, p, rs);
        }
        return numberOfRows;
    }

    public List<Receipt> searchReceipts(String pattern, int currentPage, int recordsPerPage) throws DBException {
        List<Receipt> receipts = new ArrayList<>();
        PreparedStatement p = null;
        ResultSet rs = null;
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
        } catch (SQLException ex) {
            DBManager.getInstance().rollbackAndClose(con, p, rs);
            throw new DBException(ex.getMessage(), ex);
        } finally {
            DBManager.getInstance().commitAndClose(con, p, rs);
        }
        return receipts;
    }

    public List<Product> getAllProductsFromReceipt(int receiptId) throws DBException {
        List<Product> products = new ArrayList<>();
        List<Integer> productsIds = new ArrayList<>();
        ProductDAO productDAO = new ProductDAO();
        PreparedStatement p = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = DBManager.getInstance().getConnection();
            p = con.prepareStatement(SQL__FIND_PRODUCTS_FROM_RECEIPT);
            p.setInt(1, receiptId);
            rs = p.executeQuery();
            while (rs.next()) {
                productsIds.add(rs.getInt(Fields.RECEIPT_HAS_PRODUCT_PRODUCT_ID));
            }
            for (Integer id : productsIds) {
                products.add(productDAO.findProduct(id));
            }
        } catch (SQLException ex) {
            DBManager.getInstance().rollbackAndClose(con, p, rs);
            throw new DBException(ex.getMessage(), ex);
        } finally {
            DBManager.getInstance().commitAndClose(con, p, rs);
        }
        return products;
    }

    public int createReceipt(Receipt receipt) throws DBException {
        int generatedKey;
        PreparedStatement p = null;
        Connection con = null;
        try {
            con = DBManager.getInstance().getConnection();
            p = con.prepareStatement(SQL__CREATE_NEW_RECEIPT, PreparedStatement.RETURN_GENERATED_KEYS);
            p.setString(1, receipt.getNameRu());
            p.setString(2, receipt.getNameEn());
            p.setString(3, receipt.getAddressRu());
            p.setString(4, receipt.getAddressEn());
            p.setString(5, receipt.getDescriptionRu());
            p.setString(6, receipt.getDescriptionEn());
            p.setString(7, receipt.getPhoneNumber());
            p.setInt(8, receipt.getDelivery().getId());
            p.setInt(9, receipt.getReceiptStatus().getId());
            p.setInt(10, receipt.getUserId());
            p.execute();

            try (ResultSet generatedKeys = p.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    generatedKey = generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating of receipt is failed, no ID obtained");
                }
            }
        } catch (SQLException ex) {
            DBManager.getInstance().rollbackAndClose(con, p);
            throw new DBException(ex.getMessage(), ex);
        } finally {
            DBManager.getInstance().commitAndClose(con, p);
        }
        return generatedKey;
    }

    private static class ReceiptMapper implements EntityMapper<Receipt> {

        @Override
        public Receipt mapRow(ResultSet rs) {
            try {
                Receipt receipt = new Receipt();
                receipt.setId(rs.getInt(Fields.ENTITY__ID));
                receipt.setCreateTime(rs.getTimestamp(Fields.RECEIPT_CREATION_TIME));
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
