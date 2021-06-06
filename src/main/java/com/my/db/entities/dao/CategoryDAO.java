package com.my.db.entities.dao;

import com.my.db.entities.Category;
import com.my.db.entities.DBManager;
import com.my.db.entities.EntityMapper;
import com.my.db.entities.Fields;
import com.my.web.exception.DBException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Data access object for category related entities
 */
public class CategoryDAO {

    private static final String SQL__FIND_ALL_CATEGORIES = "SELECT * FROM category;";
    private static final String SQL__FIND_CATEGORY_BY_NAME_EN = "SELECT * FROM category WHERE name_en=?;";
    private static final String SQL__FIND_CATEGORY_BY_NAME_RU = "SELECT * FROM category WHERE name_ru=?;";
    private static final String SQL__FIND_CATEGORY_BY_ID = "SELECT * FROM category WHERE id=?;";

    /**
     * Return category with requested name
     *
     * @param name       to be found
     * @param localeName language for localization
     * @return category entity
     * @throws DBException if an error has occurred while search
     */
    public Category findCategoryByName(String name, String localeName) throws DBException {
        Category category = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Connection con = null;
        try {
            con = DBManager.getInstance().getConnection();
            CategoryDAO.CategoryMapper mapper = new CategoryDAO.CategoryMapper();
            if ("ru".equals(localeName)) {
                preparedStatement = con.prepareStatement(SQL__FIND_CATEGORY_BY_NAME_RU);
            } else {
                preparedStatement = con.prepareStatement(SQL__FIND_CATEGORY_BY_NAME_EN);
            }
            preparedStatement.setString(1, name);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                category = mapper.mapRow(resultSet);
            }
        } catch (SQLException ex) {
            DBManager.getInstance().rollbackAndClose(con, preparedStatement, resultSet);
            throw new DBException(ex.getMessage(), ex);
        } finally {
            DBManager.getInstance().commitAndClose(con, preparedStatement, resultSet);
        }
        return category;
    }

    /**
     * Return category with requested id
     *
     * @param id id to be found
     * @return category entity
     * @throws DBException if could not find category
     */
    public Category findCategoryById(Integer id) throws DBException {
        Category category = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Connection con = null;
        try {
            con = DBManager.getInstance().getConnection();
            CategoryDAO.CategoryMapper mapper = new CategoryDAO.CategoryMapper();
            preparedStatement = con.prepareStatement(SQL__FIND_CATEGORY_BY_ID);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                category = mapper.mapRow(resultSet);
            }
        } catch (SQLException ex) {
            DBManager.getInstance().rollbackAndClose(con, preparedStatement, resultSet);
            throw new DBException(ex.getMessage(), ex);
        } finally {
            DBManager.getInstance().commitAndClose(con, preparedStatement, resultSet);
        }
        return category;
    }

    /**
     * Return all categories
     *
     * @return Map of category entities with their id`s as keys
     * @throws DBException if couldn't retrieve categories
     */
    public Map<Integer, Category> findAllCategories() throws DBException {
        Map<Integer, Category> categories = new ConcurrentHashMap<>();
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = DBManager.getInstance().getConnection();
            CategoryDAO.CategoryMapper mapper = new CategoryDAO.CategoryMapper();
            preparedStatement = con.prepareStatement(SQL__FIND_ALL_CATEGORIES);
            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Category category = mapper.mapRow(rs);
                categories.put(category.getId(), category);
            }
        } catch (SQLException ex) {
            DBManager.getInstance().rollbackAndClose(con, preparedStatement, rs);
            throw new DBException(ex.getMessage(), ex);
        } finally {
            DBManager.getInstance().commitAndClose(con, preparedStatement, rs);
        }
        return categories;
    }

    /**
     * Extract a category entity from the result set row
     */
    private static class CategoryMapper implements EntityMapper<Category> {

        @Override
        public Category mapRow(ResultSet rs) {
            try {
                Category category = new Category();
                category.setId(rs.getInt(Fields.ENTITY__ID));
                category.setNameRu(rs.getString(Fields.CATEGORY__NAME_RU));
                category.setNameEn(rs.getString(Fields.CATEGORY__NAME_EN));
                return category;
            } catch (SQLException e) {
                throw new IllegalStateException(e);
            }
        }

    }

}
