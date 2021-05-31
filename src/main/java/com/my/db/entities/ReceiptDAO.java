package com.my.db.entities;

import com.my.web.exception.ApplicationException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class ReceiptDAO {

    private final boolean isTest;
    private Connection connection;

    public ReceiptDAO() {
        isTest = false;
    }

    public ReceiptDAO(boolean isTest, Connection connection) {
        this.isTest = isTest;
        this.connection = connection;
    }

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

    public List<Receipt> getCurrentDayClosedReceiptsForUser(User user) {
        List<Receipt> receipts = new ArrayList<>();
        PreparedStatement p;
        ResultSet rs;
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
            p.execute();
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

    public List<Receipt> getLastWeekClosedReceipts() {
        List<Receipt> receiptList = new ArrayList<>();
        PreparedStatement p;
        ResultSet rs;
        Connection con = null;
        try {
            con = DBManager.getInstance().getConnection();
            ReceiptDAO.ReceiptMapper mapper = new ReceiptDAO.ReceiptMapper();
            p = con.prepareStatement(SQL__GET_LAST_WEEK_CLOSED_RECEIPTS);
            rs = p.executeQuery();
            while (rs.next()) {
                receiptList.add(mapper.mapRow(rs));
            }
            p.execute();
        } catch (SQLException ex) {
            assert con != null;
            DBManager.getInstance().rollbackAndClose(con);
            ex.printStackTrace();
        } finally {
            assert con != null;
            DBManager.getInstance().commitAndClose(con);
        }
        return receiptList;
    }

    public void deleteProductFromReceipt(int receiptId, int productId) {
        PreparedStatement p;
        Connection con = null;
        try {
            if (!isTest) {
                con = DBManager.getInstance().getConnection();
            } else {
                con = this.connection;
            }
            p = con.prepareStatement(SQL__DELETE_PRODUCT_FROM_RECEIPT);
            p.setInt(1, receiptId);
            p.setInt(2, productId);
            p.execute();
        } catch (SQLException ex) {
            assert con != null;
            DBManager.getInstance().rollbackAndClose(con);
            ex.printStackTrace();
        } finally {
            assert con != null;
            DBManager.getInstance().commitAndClose(con);
        }
    }

    public void setReceiptStatus(int receiptId, ReceiptStatus receiptStatus) {
        PreparedStatement p;
        Connection con = null;
        try {
            if (!isTest) {
                con = DBManager.getInstance().getConnection();
            } else {
                con = this.connection;
            }
            p = con.prepareStatement(SQL__SET_RECEIPT_STATUS);
            p.setInt(1, receiptStatus.getId());
            p.setInt(2, receiptId);
            p.execute();
        } catch (SQLException ex) {
            assert con != null;
            DBManager.getInstance().rollbackAndClose(con);
            ex.printStackTrace();
        } finally {
            assert con != null;
            DBManager.getInstance().commitAndClose(con);
        }
    }

    public void setAmountOfProductAtTheReceipt(int amount, int receiptId, int productId) {
        PreparedStatement p;
        Connection con = null;
        try {
            if (!isTest) {
                con = DBManager.getInstance().getConnection();
            } else {
                con = this.connection;
            }
            p = con.prepareStatement(SQL__SET_AMOUNT_OF_PRODUCT_AT_THE_RECEIPT);
            p.setInt(1, amount);
            p.setInt(2, receiptId);
            p.setInt(3, productId);
            p.execute();
        } catch (SQLException ex) {
            assert con != null;
            DBManager.getInstance().rollbackAndClose(con);
            ex.printStackTrace();
        } finally {
            assert con != null;
            DBManager.getInstance().commitAndClose(con);
        }
    }

    public Map<Product, Integer> getMapOfAmountsAndProductsFromReceipt(Receipt receipt) {
        Map<Product, Integer> productMap = new TreeMap<>();
        List<Product> products = new ReceiptDAO().getAllProductsFromReceipt(receipt.getId());
        PreparedStatement p;
        ResultSet rs;
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
            assert con != null;
            DBManager.getInstance().rollbackAndClose(con);
            ex.printStackTrace();
        } finally {
            assert con != null;
            DBManager.getInstance().commitAndClose(con);
        }
        return productMap;
    }

    public Receipt findReceipt(int id) {
        Receipt receipt = new Receipt();
        PreparedStatement p;
        ResultSet rs;
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
            assert con != null;
            DBManager.getInstance().rollbackAndClose(con);
            ex.printStackTrace();
        } finally {
            assert con != null;
            DBManager.getInstance().commitAndClose(con);
        }
        return receipt;
    }

    public int countOfRowsAffectedBySearch(String pattern) {
        int numberOfRows = 0;
        PreparedStatement p;
        ResultSet rs;
        Connection con = null;
        try {
            if (!isTest) {
                con = DBManager.getInstance().getConnection();
            } else {
                con = this.connection;
            }
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

    public void addProductIntoReceipt(int productId, int receiptId) throws ApplicationException {
        PreparedStatement preparedStatement;
        Connection con = null;
        try {
            if (!isTest) {
                con = DBManager.getInstance().getConnection();
            } else {
                con = this.connection;
            }
            preparedStatement = con.prepareStatement(SQL__ADD_PRODUCT_INTO_RECEIPT);
            preparedStatement.setInt(1, receiptId);
            preparedStatement.setInt(2, productId);
            preparedStatement.setInt(3, productId);
            preparedStatement.execute();
            preparedStatement.close();
            DBManager.getInstance().commitAndClose(con);
        } catch (SQLException ex) {
            assert con != null;
            DBManager.getInstance().rollbackAndClose(con);
            throw new ApplicationException(ex.getMessage());
        }
    }

    public int createReceipt(Receipt receipt) {
        int generatedKey = 0;
        PreparedStatement preparedStatement;
        Connection con = null;
        try {
            if (!isTest) {
                con = DBManager.getInstance().getConnection();
            } else {
                con = this.connection;
            }
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
            } catch (SQLException | ApplicationException e) {
                throw new IllegalStateException(e);
            }
        }

    }

}
