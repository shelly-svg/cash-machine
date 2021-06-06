package com.my.db.entities;

import java.io.Serializable;

/**
 * Role entity
 */
public enum Role implements Serializable {

    ADMIN, CASHIER, SENIOR_CASHIER, COMMODITY_EXPERT;

    public static Role getRole(User user) {
        int roleId = user.getRoleId();
        return Role.values()[roleId - 1];
    }

    public String getName() {
        return name().toLowerCase();
    }

}
