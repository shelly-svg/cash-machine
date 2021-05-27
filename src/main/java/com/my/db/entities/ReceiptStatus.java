package com.my.db.entities;

import java.io.Serializable;

public enum ReceiptStatus implements Serializable {

    NEW_RECEIPT(1, "new receipt", "новый заказ"),
    CLOSED(2, "closed", "закрыт"),
    CANCELED(3, "canceled", "отменён");

    private final int id;
    private final String nameEn;
    private final String nameRu;

    ReceiptStatus(int id, String nameEn, String nameRu) {
        this.id = id;
        this.nameEn = nameEn;
        this.nameRu = nameRu;
    }

    public static ReceiptStatus getReceiptStatus(int receiptStatusId) {
        return ReceiptStatus.values()[receiptStatusId - 1];
    }

    public String getName() {
        return name().toLowerCase();
    }

    public int getId() {
        return id;
    }

    public String getNameEn() {
        return nameEn;
    }

    public String getNameRu() {
        return nameRu;
    }
}
