package com.my.db.entities;

import java.io.Serializable;
import java.math.BigDecimal;

public class Product implements Serializable {

    private int id;
    private String nameRu;
    private String nameEn;
    private String code;
    private BigDecimal price;
    private int amount;
    private BigDecimal weight;
    private String descriptionRu;
    private String descriptionEn;
    private int categoryId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
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

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public String toString() {
        return "Product: -> id: " + id + "; name ru:" + nameRu + "; name en:" + nameEn + "; code:" + code + "; price:" + price +
                "; amount:" + amount + "; weight:" + weight + "; ru description:" + descriptionRu + "; en description:" + descriptionEn +
                "; category id:" + categoryId;
    }

}
