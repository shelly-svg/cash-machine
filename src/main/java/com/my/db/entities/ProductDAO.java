package com.my.db.entities;

import com.my.web.exception.ApplicationException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    private final boolean isTest;
    private Connection connection;

    public ProductDAO() {
        isTest = false;
    }

    public ProductDAO(boolean isTest, Connection connection) {
        this.isTest = isTest;
        this.connection = connection;
    }

    private static final String SQL__ADD_NEW_PRODUCT = "INSERT INTO product(name_ru, name_en, code, price, amount, " +
            "weight, description_ru, description_en, category_id) value (?, ?, ?, ?, ?, ?, ?, ?, ?);";

    private static final String SQL__FIND_PRODUCT_BY_ID = "SELECT * FROM product WHERE id=?;";

    private static final String SQL__FIND_PRODUCT_BY_SEARCH = "SELECT * FROM product WHERE name_ru LIKE ? OR name_en LIKE ? OR code LIKE ? LIMIT ?,?;";

    private static final String SQL__FIND_NUMBER_OF_ROWS_AFFECTED_BY_SEARCH = "SELECT COUNT(*) FROM product WHERE name_ru " +
            "LIKE ? OR name_en LIKE ? OR code LIKE ?;";

    private static final String SQL__UPDATE_PRODUCT_AMOUNT_BY_ID = "UPDATE product SET amount=? WHERE id=?;";

    public void updateProductsAmount(int id, int newAmount) throws ApplicationException {
        PreparedStatement preparedStatement;
        Connection con = null;
        try {
            if (!isTest) {
                con = DBManager.getInstance().getConnection();
            } else {
                con = this.connection;
            }
            preparedStatement = con.prepareStatement(SQL__UPDATE_PRODUCT_AMOUNT_BY_ID);
            preparedStatement.setInt(1, newAmount);
            preparedStatement.setInt(2, id);
            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException ex) {
            assert con != null;
            DBManager.getInstance().rollbackAndClose(con);
            throw new ApplicationException(ex.getMessage());
        } finally {
            assert con != null;
            DBManager.getInstance().commitAndClose(con);
        }
    }

    public int countOfRowsAffectedBySearch(String pattern) throws ApplicationException {
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
            p = con.prepareStatement(SQL__FIND_NUMBER_OF_ROWS_AFFECTED_BY_SEARCH);
            pattern = "%" + pattern + "%";
            p.setString(1, pattern);
            p.setString(2, pattern);
            p.setString(3, pattern);
            rs = p.executeQuery();
            if (rs.next()) {
                numberOfRows = rs.getInt(1);
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
        return numberOfRows;
    }

    public List<Product> searchProducts(String pattern, int currentPage, int recordsPerPage) throws ApplicationException {
        List<Product> products = new ArrayList<>();
        PreparedStatement p;
        ResultSet rs;
        Connection con = null;
        int start = currentPage * recordsPerPage - recordsPerPage;
        try {
            con = DBManager.getInstance().getConnection();

            ProductDAO.ProductMapper mapper = new ProductDAO.ProductMapper();
            p = con.prepareStatement(SQL__FIND_PRODUCT_BY_SEARCH);
            pattern = "%" + pattern + "%";
            p.setString(1, pattern);
            p.setString(2, pattern);
            p.setString(3, pattern);
            p.setInt(4, start);
            p.setInt(5, recordsPerPage);
            rs = p.executeQuery();
            while (rs.next()) {
                products.add(mapper.mapRow(rs));
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
        return products;
    }

    public int addProduct(Product product) throws ApplicationException {
        int generatedKey;
        PreparedStatement preparedStatement;
        Connection con = null;
        try {
            if (!isTest) {
                con = DBManager.getInstance().getConnection();
            } else {
                con = this.connection;
            }
            preparedStatement = con.prepareStatement(SQL__ADD_NEW_PRODUCT, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, product.getNameRu());
            preparedStatement.setString(2, product.getNameEn());
            preparedStatement.setString(3, product.getCode());
            preparedStatement.setBigDecimal(4, product.getPrice());
            preparedStatement.setInt(5, product.getAmount());
            preparedStatement.setBigDecimal(6, product.getWeight());
            preparedStatement.setString(7, product.getDescriptionRu());
            preparedStatement.setString(8, product.getDescriptionEn());
            preparedStatement.setInt(9, product.getCategory().getId());
            preparedStatement.execute();
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    generatedKey = generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating of product is failed, no ID obtained");
                }
            }
            preparedStatement.close();
        } catch (SQLException ex) {
            assert con != null;
            DBManager.getInstance().rollbackAndClose(con);
            throw new ApplicationException(ex.getMessage());
        } finally {
            assert con != null;
            DBManager.getInstance().commitAndClose(con);
        }
        return generatedKey;
    }

    public Product findProduct(Integer id) throws ApplicationException {
        Product product = null;
        PreparedStatement preparedStatement;
        ResultSet rs;
        Connection con = null;
        try {
            con = DBManager.getInstance().getConnection();
            ProductDAO.ProductMapper mapper = new ProductDAO.ProductMapper();
            preparedStatement = con.prepareStatement(SQL__FIND_PRODUCT_BY_ID);
            preparedStatement.setInt(1, id);
            rs = preparedStatement.executeQuery();
            if (rs.next()) {
                product = mapper.mapRow(rs);
            }
            rs.close();
            preparedStatement.close();
        } catch (SQLException ex) {
            assert con != null;
            DBManager.getInstance().rollbackAndClose(con);
            throw new ApplicationException(ex.getMessage());
        } finally {
            assert con != null;
            DBManager.getInstance().commitAndClose(con);
        }
        return product;
    }


    private static class ProductMapper implements EntityMapper<Product> {

        @Override
        public Product mapRow(ResultSet rs) {
            try {
                Product product = new Product();
                product.setId(rs.getInt(Fields.ENTITY__ID));
                product.setNameRu(rs.getString(Fields.PRODUCT__NAME_RU));
                product.setNameEn(rs.getString(Fields.PRODUCT__NAME_EN));
                product.setCode(rs.getString(Fields.PRODUCT__CODE));
                product.setPrice(rs.getBigDecimal(Fields.PRODUCT__PRICE));
                product.setAmount(rs.getInt(Fields.PRODUCT__AMOUNT));
                product.setWeight(rs.getBigDecimal(Fields.PRODUCT__WEIGHT));
                product.setDescriptionRu(rs.getString(Fields.PRODUCT__DESCRIPTION_RU));
                product.setDescriptionEn(rs.getString(Fields.PRODUCT__DESCRIPTION_EN));
                product.setCategory(new CategoryDAO().findCategoryById(rs.getInt(Fields.PRODUCT__CATEGORY_ID)));
                return product;
            } catch (SQLException | ApplicationException e) {
                throw new IllegalStateException(e);
            }
        }

    }

}
