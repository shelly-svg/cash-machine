package com.my.db.entities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class CategoryDAO {

    private static final String SQL__FIND_ALL_CATEGORIES = "SELECT * FROM category;";
    private static final String SQL__FIND_CATEGORY_BY_NAME_EN = "SELECT * FROM category WHERE name_en=?;";
    private static final String SQL__FIND_CATEGORY_BY_NAME_RU = "SELECT * FROM category WHERE name_ru=?;";
    private static final String SQL__FIND_CATEGORY_BY_ID = "SELECT * FROM category WHERE id=?;";

    public Category findCategoryByName(String name, String localeName) {
        Category category = null;
        PreparedStatement preparedStatement;
        ResultSet resultSet;
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
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException ex) {
            assert con != null;
            DBManager.getInstance().rollbackAndClose(con);
            ex.printStackTrace();
        } finally {
            assert con != null;
            DBManager.getInstance().commitAndClose(con);
        }
        return category;
    }

    public Category findCategoryById(Integer id) {
        Category category = null;
        PreparedStatement preparedStatement;
        ResultSet resultSet;
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
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException ex) {
            assert con != null;
            DBManager.getInstance().rollbackAndClose(con);
            ex.printStackTrace();
        } finally {
            assert con != null;
            DBManager.getInstance().commitAndClose(con);
        }
        return category;
    }

    public Map<Integer, Category> findAllCategories() {
        Map<Integer, Category> categories = new HashMap<>();
        PreparedStatement preparedStatement;
        ResultSet rs;
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
            rs.close();
            preparedStatement.close();
        } catch (SQLException ex) {
            assert con != null;
            DBManager.getInstance().rollbackAndClose(con);
            ex.printStackTrace();
        } finally {
            assert con != null;
            DBManager.getInstance().commitAndClose(con);
        }
        return categories;
    }

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
