package com.my.db.entities;

import java.sql.ResultSet;

/**
 * Root for all entity mappers
 */
public interface EntityMapper<T> {

    /**
     * Map current entity
     *
     * @return a mapped entity
     */
    T mapRow(ResultSet rs);

}
