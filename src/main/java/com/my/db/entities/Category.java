package com.my.db.entities;

import java.io.Serializable;

/**
 * Category entity
 */
public class Category implements Serializable {

    private static final long serialVersionUID = -1290292001290349022L;
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
        return "Category{" +
                "id=" + id +
                ", nameRu='" + nameRu + '\'' +
                ", nameEn='" + nameEn + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Category)) {
            return false;
        }
        Category anotherCategory = (Category) obj;
        if (id != anotherCategory.getId()) {
            return false;
        }
        return nameRu.equals(anotherCategory.getNameRu());
    }
}
