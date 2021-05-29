package com.my.web.validators;

import com.my.db.entities.Product;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.ResourceBundle;

public class ProductValidator extends Validator<Product> {

    private static final String cyrillicPattern = "[а-яА-ЯёЁІіЇїЪъ]";

    @Override
    public boolean validate(Product product, HttpSession session, ResourceBundle rb) {
        if (!validateProductNameRu(product.getNameRu())) {
            String errorMessage = rb.getString("add.product.name.ru.invalid");
            session.setAttribute("errorMessage", errorMessage);
            return false;
        }
        if (!validateProductNameEn(product.getNameEn())) {
            String errorMessage = rb.getString("add.product.name.en.invalid");
            session.setAttribute("errorMessage", errorMessage);
            return false;
        }
        if (!validateProductCode(product.getCode())) {
            String errorMessage = rb.getString("add.product.code.invalid");
            session.setAttribute("errorMessage", errorMessage);
            return false;
        }
        if (!validateProductPrice(product.getPrice())) {
            String errorMessage = rb.getString("add.product.price.invalid");
            session.setAttribute("errorMessage", errorMessage);
            return false;
        }
        if (!validateProductAmount(product.getAmount())) {
            String errorMessage = rb.getString("add.product.amount.invalid");
            session.setAttribute("errorMessage", errorMessage);
            return false;
        }
        if (!validateProductWeight(product.getWeight())) {
            String errorMessage = rb.getString("add.product.weight.invalid");
            session.setAttribute("errorMessage", errorMessage);
            return false;
        }
        if (product.getDescriptionRu() != null && !product.getDescriptionRu().isEmpty()) {
            if (validateProductDescriptionRu(product.getDescriptionRu())) {
                String errorMessage = rb.getString("add.product.description.ru.invalid");
                session.setAttribute("errorMessage", errorMessage);
                return false;
            }
        }
        if (product.getDescriptionEn() != null && !product.getDescriptionEn().isEmpty()) {
            if (validateProductDescriptionEn(product.getDescriptionEn())) {
                String errorMessage = rb.getString("add.product.description.en.invalid");
                session.setAttribute("errorMessage", errorMessage);
                return false;
            }
        }
        return true;
    }

    private static boolean validateProductNameRu(String nameRu) {
        return nameRu.length() < 100;
    }

    private static boolean validateProductNameEn(String nameEn) {
        return !nameEn.matches(cyrillicPattern) && nameEn.length() < 100;
    }

    private static boolean validateProductCode(String code) {
        return !code.matches(cyrillicPattern) && code.length() < 128;
    }

    private static boolean validateProductPrice(BigDecimal price) {
        double doublePrice = price.doubleValue();
        if (doublePrice < 0) {
            return false;
        }
        return !(doublePrice >= 1000000000);
    }

    private static boolean validateProductAmount(int amount) {
        if (amount < 0) {
            return false;
        }
        return amount < 1000000000;
    }

    private static boolean validateProductWeight(BigDecimal weight) {
        double doubleWeight = weight.doubleValue();
        if (doubleWeight < 0) {
            return false;
        }
        return !(doubleWeight >= 1000000000);
    }

    private static boolean validateProductDescriptionRu(String descriptionRu) {
        return descriptionRu.length() < 2000;
    }

    private static boolean validateProductDescriptionEn(String descriptionEn) {
        return descriptionEn.length() < 2000;
    }
}
