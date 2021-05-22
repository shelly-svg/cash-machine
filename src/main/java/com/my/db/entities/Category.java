package com.my.db.entities;

public class Category {

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
        return "Category [ id :" + id + "; nameRu: " + nameRu + "; nameEn: " + nameEn + ";";
    }

}
