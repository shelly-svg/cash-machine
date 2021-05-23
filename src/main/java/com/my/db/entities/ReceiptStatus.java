package com.my.db.entities;

public enum ReceiptStatus {

    NEW_RECEIPT(1), CLOSED(2), CANCELED(3);

    private int id;

    ReceiptStatus(int i) {
        this.id = i;
    }

    public static ReceiptStatus getReceiptStatus(Receipt receipt) {
        int receiptStatusId = receipt.getReceiptStatusId();
        return ReceiptStatus.values()[receiptStatusId - 1];
    }

    public String getName() {
        return name().toLowerCase();
    }

    public int getId() {
        return id;
    }

}