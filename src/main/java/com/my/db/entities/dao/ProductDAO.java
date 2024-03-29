package com.my.db.entities.dao;

import com.my.db.entities.*;
import com.my.web.exception.DBException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data access object for all product related entities
 */
public class ProductDAO {

    private static final String SQL__ADD_NEW_PRODUCT = "INSERT INTO product(name_ru, name_en, code, price, amount, " +
            "weight, description_ru, description_en, category_id) value (?, ?, ?, ?, ?, ?, ?, ?, ?);";

    private static final String SQL__FIND_PRODUCT_BY_ID = "SELECT * FROM product WHERE id=?;";

    private static final String SQL__FIND_ALL_PRODUCTS = "SELECT * FROM product ORDER BY id LIMIT ?,?;";

    private static final String SQL__FIND_ALL_PRODUCTS_SORT_BY_NAME = "SELECT * FROM product ORDER BY name_ru LIMIT ?,?;";

    private static final String SQL__FIND_ALL_PRODUCTS_SORT_BY_PRICE = "SELECT * FROM product ORDER BY price LIMIT ?,?;";

    private static final String SQL__ALL_PRODUCTS_ROWS = "SELECT COUNT(*) FROM product;";

    private static final String SQL__FIND_PRODUCT_BY_SEARCH = "SELECT * FROM product WHERE name_ru LIKE ? OR name_en LIKE ? OR code LIKE ? LIMIT ?,?;";

    private static final String SQL__FIND_PRODUCTS_BY_SEARCH_FOR_RECEIPT = "SELECT * FROM product LEFT JOIN receipt_has_product ON receipt_has_product.product_id = product.id " +
            "AND receipt_has_product.receipt_id=? WHERE receipt_has_product.product_id IS null AND name_ru LIKE ? OR name_en LIKE ? OR code LIKE ? LIMIT ?,?;";

    private static final String SQL__FIND_NUMBER_OF_ROWS_AFFECTED_BY_SEARCH = "SELECT COUNT(*) FROM product WHERE name_ru " +
            "LIKE ? OR name_en LIKE ? OR code LIKE ?;";


    private static final String SQL__FIND_NUMBER_OF_ROWS_AFFECTED_BY_SEARCH_FOR_RECEIPT = "SELECT COUNT(*) FROM product LEFT JOIN receipt_has_product ON receipt_has_product.product_id = product.id " +
            "AND receipt_has_product.receipt_id=? WHERE receipt_has_product.product_id IS null AND name_ru like ? or name_en like ? OR code like ?;";

    private static final String SQL__UPDATE_PRODUCT_AMOUNT_BY_ID = "UPDATE product SET amount=? WHERE id=?;";

    public List<Product> findAllProductsSortByPrice(int currentPage, int recordsPerPage) throws DBException {
        List<Product> products = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        Connection con = null;
        int start = currentPage * recordsPerPage - recordsPerPage;
        try {
            con = DBManager.getInstance().getConnection();
            ProductDAO.ProductMapper mapper = new ProductDAO.ProductMapper();
            preparedStatement = con.prepareStatement(SQL__FIND_ALL_PRODUCTS_SORT_BY_PRICE);
            preparedStatement.setInt(1, start);
            preparedStatement.setInt(2, recordsPerPage);
            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                products.add(mapper.mapRow(rs));
            }
        } catch (SQLException ex) {
            DBManager.getInstance().rollbackAndClose(con, preparedStatement, rs);
            throw new DBException(ex.getMessage(), ex);
        } finally {
            DBManager.getInstance().commitAndClose(con, preparedStatement, rs);
        }
        return products;
    }

    public List<Product> findAllProductsSortByName(int currentPage, int recordsPerPage) throws DBException {
        List<Product> products = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        Connection con = null;
        int start = currentPage * recordsPerPage - recordsPerPage;
        try {
            con = DBManager.getInstance().getConnection();
            ProductDAO.ProductMapper mapper = new ProductDAO.ProductMapper();
            preparedStatement = con.prepareStatement(SQL__FIND_ALL_PRODUCTS_SORT_BY_NAME);
            preparedStatement.setInt(1, start);
            preparedStatement.setInt(2, recordsPerPage);
            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                products.add(mapper.mapRow(rs));
            }
        } catch (SQLException ex) {
            DBManager.getInstance().rollbackAndClose(con, preparedStatement, rs);
            throw new DBException(ex.getMessage(), ex);
        } finally {
            DBManager.getInstance().commitAndClose(con, preparedStatement, rs);
        }
        return products;
    }

    public int numberOfAllProducts() throws DBException {
        int numberOfRows = 0;
        PreparedStatement p = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = DBManager.getInstance().getConnection();
            p = con.prepareStatement(SQL__ALL_PRODUCTS_ROWS);
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

    public List<Product> findAllProducts(int currentPage, int recordsPerPage) throws DBException {
        List<Product> products = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        Connection con = null;
        int start = currentPage * recordsPerPage - recordsPerPage;
        try {
            con = DBManager.getInstance().getConnection();
            ProductDAO.ProductMapper mapper = new ProductDAO.ProductMapper();
            preparedStatement = con.prepareStatement(SQL__FIND_ALL_PRODUCTS);
            preparedStatement.setInt(1, start);
            preparedStatement.setInt(2, recordsPerPage);
            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                products.add(mapper.mapRow(rs));
            }
        } catch (SQLException ex) {
            DBManager.getInstance().rollbackAndClose(con, preparedStatement, rs);
            throw new DBException(ex.getMessage(), ex);
        } finally {
            DBManager.getInstance().commitAndClose(con, preparedStatement, rs);
        }
        return products;
    }

    /**
     * Updates product amount at the store
     *
     * @param id        id of product to be modified
     * @param newAmount new amount of product
     * @throws DBException if couldn't update data
     */
    public void updateProductsAmount(int id, int newAmount) throws DBException {
        PreparedStatement preparedStatement = null;
        Connection con = null;
        try {
            con = DBManager.getInstance().getConnection();
            preparedStatement = con.prepareStatement(SQL__UPDATE_PRODUCT_AMOUNT_BY_ID);
            preparedStatement.setInt(1, newAmount);
            preparedStatement.setInt(2, id);
            preparedStatement.execute();
        } catch (SQLException ex) {
            DBManager.getInstance().rollbackAndClose(con, preparedStatement);
            throw new DBException(ex.getMessage(), ex);
        } finally {
            DBManager.getInstance().commitAndClose(con, preparedStatement);
        }
    }

    /**
     * Return number of rows affected by search product for special receipt, search excludes products that already added into receipt
     *
     * @param pattern search pattern
     * @return number of affected rows
     * @throws DBException if couldn't retrieve data
     */
    public int countOfRowsAffectedBySearch(Receipt receipt, String pattern) throws DBException {
        int numberOfRows = 0;
        PreparedStatement p = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = DBManager.getInstance().getConnection();
            p = con.prepareStatement(SQL__FIND_NUMBER_OF_ROWS_AFFECTED_BY_SEARCH_FOR_RECEIPT);
            pattern = "%" + pattern + "%";
            p.setInt(1, receipt.getId());
            p.setString(2, pattern);
            p.setString(3, pattern);
            p.setString(4, pattern);
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

    /**
     * Return number of rows affected by search product
     *
     * @param pattern search pattern
     * @return number of affected rows
     * @throws DBException if couldn't retrieve data
     */
    public int countOfRowsAffectedBySearch(String pattern) throws DBException {
        int numberOfRows = 0;
        PreparedStatement p = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = DBManager.getInstance().getConnection();
            p = con.prepareStatement(SQL__FIND_NUMBER_OF_ROWS_AFFECTED_BY_SEARCH);
            pattern = "%" + pattern + "%";
            p.setString(1, pattern);
            p.setString(2, pattern);
            p.setString(3, pattern);
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

    /**
     * Return products that hit search pattern and excludes products that already added into receipt
     *
     * @param pattern        search pattern
     * @param currentPage    current pagination page
     * @param recordsPerPage number of products displayed per page
     * @return List of product entities
     * @throws DBException if couldn't retrieve data
     */
    public List<Product> searchProducts(Receipt receipt, String pattern, int currentPage, int recordsPerPage) throws DBException {
        List<Product> products = new ArrayList<>();
        PreparedStatement p = null;
        ResultSet rs = null;
        Connection con = null;
        int start = currentPage * recordsPerPage - recordsPerPage;
        try {
            con = DBManager.getInstance().getConnection();
            ProductDAO.ProductMapper mapper = new ProductDAO.ProductMapper();
            p = con.prepareStatement(SQL__FIND_PRODUCTS_BY_SEARCH_FOR_RECEIPT);
            pattern = "%" + pattern + "%";
            p.setInt(1, receipt.getId());
            p.setString(2, pattern);
            p.setString(3, pattern);
            p.setString(4, pattern);
            p.setInt(5, start);
            p.setInt(6, recordsPerPage);
            rs = p.executeQuery();
            while (rs.next()) {
                products.add(mapper.mapRow(rs));
            }
        } catch (SQLException ex) {
            DBManager.getInstance().rollbackAndClose(con, p, rs);
            throw new DBException(ex.getMessage(), ex);
        } finally {
            DBManager.getInstance().commitAndClose(con, p, rs);
        }
        return products;
    }

    /**
     * Return products that hit search pattern
     *
     * @param pattern        search pattern
     * @param currentPage    current pagination page
     * @param recordsPerPage number of products displayed per page
     * @return List of product entities
     * @throws DBException if couldn't retrieve data
     */
    public List<Product> searchProducts(String pattern, int currentPage, int recordsPerPage) throws DBException {
        List<Product> products = new ArrayList<>();
        PreparedStatement p = null;
        ResultSet rs = null;
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
        } catch (SQLException ex) {
            DBManager.getInstance().rollbackAndClose(con, p, rs);
            throw new DBException(ex.getMessage(), ex);
        } finally {
            DBManager.getInstance().commitAndClose(con, p, rs);
        }
        return products;
    }

    /**
     * Adds product into DB, and returns generated id
     *
     * @param product product to be added
     * @return int
     * @throws DBException if couldn't update data
     */
    public int addProduct(Product product) throws DBException {
        int generatedKey;
        PreparedStatement preparedStatement = null;
        Connection con = null;
        try {
            con = DBManager.getInstance().getConnection();
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
        } catch (SQLException ex) {
            DBManager.getInstance().rollbackAndClose(con, preparedStatement);
            throw new DBException(ex.getMessage(), ex);
        } finally {
            DBManager.getInstance().commitAndClose(con, preparedStatement);
        }
        return generatedKey;
    }

    /**
     * find product with requested id
     *
     * @param id product id
     * @return product entity
     * @throws DBException if couldn't retrieve data
     */
    public Product findProduct(int id) throws DBException {
        Product product = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
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
        } catch (SQLException ex) {
            DBManager.getInstance().rollbackAndClose(con, preparedStatement, rs);
            throw new DBException(ex.getMessage(), ex);
        } finally {
            DBManager.getInstance().commitAndClose(con, preparedStatement, rs);
        }
        return product;
    }

    /**
     * Extract product entity from the result set row
     */
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
            } catch (SQLException e) {
                throw new IllegalStateException(e);
            }
        }

    }

}
