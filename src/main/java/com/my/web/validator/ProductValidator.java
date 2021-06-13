package com.my.web.validator;

import com.my.db.entities.Product;
import com.my.web.exception.ApplicationException;

import java.math.BigDecimal;

/**
 * Product validator class
 *
 * @author Denys Tsebro
 */
public class ProductValidator extends Validator<Product> {

    private static final String cyrillicPattern = "[а-яА-ЯёЁІіЇїЪъ]";

    /**
     * return true only if product is valid
     *
     * @param product product to be validated
     * @return true if product is valid
     * @throws ApplicationException if product invalid
     */
    @Override
    public boolean validate(Product product) throws ApplicationException {
        if (!validateProductNameRu(product.getNameRu())) {
            String errorMessage = "add.product.name.ru.invalid";
            throw new ApplicationException(errorMessage);
        }
        if (!validateProductNameEn(product.getNameEn())) {
            String errorMessage = "add.product.name.en.invalid";
            throw new ApplicationException(errorMessage);
        }
        if (!validateProductCode(product.getCode())) {
            String errorMessage = "add.product.code.invalid";
            throw new ApplicationException(errorMessage);
        }
        if (!validateProductPrice(product.getPrice())) {
            String errorMessage = "add.product.price.invalid";
            throw new ApplicationException(errorMessage);
        }
        if (!validateProductAmount(product.getAmount())) {
            String errorMessage = "add.product.amount.invalid";
            throw new ApplicationException(errorMessage);
        }
        if (!validateProductWeight(product.getWeight())) {
            String errorMessage = "add.product.weight.invalid";
            throw new ApplicationException(errorMessage);
        }
        if (product.getDescriptionRu() != null && !product.getDescriptionRu().isEmpty()) {
            if (!validateProductDescriptionRu(product.getDescriptionRu())) {
                String errorMessage = "add.product.description.ru.invalid";
                throw new ApplicationException(errorMessage);
            }
        }
        if (product.getDescriptionEn() != null && !product.getDescriptionEn().isEmpty()) {
            if (!validateProductDescriptionEn(product.getDescriptionEn())) {
                String errorMessage = "add.product.description.en.invalid";
                throw new ApplicationException(errorMessage);
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
        if (doublePrice <= 0) {
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
        if (doubleWeight <= 0) {
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
