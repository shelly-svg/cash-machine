package com.my.db.entities;

import java.util.Date;

public class Receipt {

    private int id;
    private Date createTime;
    private String nameRu;
    private String nameEn;
    private String addressRu;
    private String addressEn;
    private String descriptionRu;
    private String descriptionEn;
    private String phoneNumber;
    private int deliveryId;
    private int receiptStatusId;
    private int userId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getNameRu() {
        return nameRu;
    }

    public void setNameRu(String nameRu) {
        this.nameRu = nameRu;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getAddressRu() {
        return addressRu;
    }

    public void setAddressRu(String addressRu) {
        this.addressRu = addressRu;
    }

    public String getAddressEn() {
        return addressEn;
    }

    public void setAddressEn(String addressEn) {
        this.addressEn = addressEn;
    }

    public String getDescriptionRu() {
        return descriptionRu;
    }

    public void setDescriptionRu(String descriptionRu) {
        this.descriptionRu = descriptionRu;
    }

    public String getDescriptionEn() {
        return descriptionEn;
    }

    public void setDescriptionEn(String descriptionEn) {
        this.descriptionEn = descriptionEn;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(int deliveryId) {
        this.deliveryId = deliveryId;
    }

    public int getReceiptStatusId() {
        return receiptStatusId;
    }

    public void setReceiptStatusId(int receiptStatusId) {
        this.receiptStatusId = receiptStatusId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Receipt{" +
                "id=" + id +
                ", createTime=" + createTime +
                ", nameRu='" + nameRu + '\'' +
                ", nameEn='" + nameEn + '\'' +
                ", addressRu='" + addressRu + '\'' +
                ", addressEn='" + addressEn + '\'' +
                ", descriptionRu='" + descriptionRu + '\'' +
                ", descriptionEn='" + descriptionEn + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", deliveryId=" + deliveryId +
                ", receiptStatusId=" + receiptStatusId +
                ", userId=" + userId +
                '}';
    }

}
