package com.my.web;

import com.my.db.entities.Receipt;

public class OrderClass {

    private Receipt receipt;

    public OrderClass(Receipt receipt) {
        this.receipt = receipt;
    }

    public Receipt getReceipt() {
        return receipt;
    }

}
