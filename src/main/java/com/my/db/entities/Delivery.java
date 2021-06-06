package com.my.db.entities;

import java.io.Serializable;
import java.util.Objects;

/**
 * Delivery entity
 */
public class Delivery implements Serializable {

    private static final long serialVersionUID = -9128932909034892233L;
    private int id;
    private String nameRu;
    private String nameEn;

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

    @Override
    public String toString() {
        return "Delivery{" +
                "id=" + id +
                ", nameRu='" + nameRu + '\'' +
                ", nameEn='" + nameEn + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Delivery delivery = (Delivery) o;
        return id == delivery.id && Objects.equals(nameRu, delivery.nameRu) && Objects.equals(nameEn, delivery.nameEn);
    }

}
