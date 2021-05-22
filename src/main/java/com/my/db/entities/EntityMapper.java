package com.my.db.entities;

import java.sql.ResultSet;

public interface EntityMapper<T> {

    T mapRow(ResultSet rs);

}
