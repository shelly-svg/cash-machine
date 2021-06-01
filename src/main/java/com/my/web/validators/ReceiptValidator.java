package com.my.web.validators;

import com.my.db.entities.Receipt;
import com.my.web.exception.ApplicationException;

import javax.servlet.http.HttpSession;
import java.util.ResourceBundle;

public class ReceiptValidator extends Validator<Receipt> {

    private static final String cyrillicPattern = "[а-яА-ЯёЁІіЇїЪъ]";
    private static final String phonePattern = "^\\d{10}$";

    @Override
    public boolean validate(Receipt receipt) throws ApplicationException {
        if (receipt.getNameRu() == null || receipt.getNameRu().isEmpty()) {
            String errorMessage = "create.receipt.name.ru.null";
            throw new ApplicationException(errorMessage);
        }
        if (receipt.getNameEn() == null || receipt.getNameEn().isEmpty()) {
            String errorMessage = "create.receipt.name.en.null";
            throw new ApplicationException(errorMessage);
        }
        if (receipt.getAddressRu() == null || receipt.getAddressRu().isEmpty()) {
            String errorMessage = "create.receipt.address.ru.null";
            throw new ApplicationException(errorMessage);
        }
        if (receipt.getPhoneNumber() == null || receipt.getPhoneNumber().isEmpty()) {
            String errorMessage = "create.receipt.phone.number.null";
            throw new ApplicationException(errorMessage);
        }

        if (!validateReceiptNameRu(receipt.getNameRu())) {
            String errorMessage = "create.receipt.name.ru.invalid";
            throw new ApplicationException(errorMessage);
        }
        if (!validateReceiptNameEn(receipt.getNameEn())) {
            String errorMessage = "create.receipt.name.en.invalid";
            throw new ApplicationException(errorMessage);
        }
        if (!validateReceiptAddressRu(receipt.getAddressRu())) {
            String errorMessage = "create.receipt.address.ru.invalid";
            throw new ApplicationException(errorMessage);
        }
        if (!validateReceiptAddressEn(receipt.getAddressEn())) {
            String errorMessage = "create.receipt.address.en.invalid";
            throw new ApplicationException(errorMessage);
        }
        if (!validateReceiptPhoneNumber(receipt.getPhoneNumber())) {
            String errorMessage = "create.receipt.phone.number.invalid";
            throw new ApplicationException(errorMessage);
        }
        if (!validateReceiptDescriptionRu(receipt.getDescriptionRu())) {
            String errorMessage = "create.receipt.description.ru.invalid";
            throw new ApplicationException(errorMessage);
        }
        if (!validateReceiptDescriptionEn(receipt.getDescriptionEn())) {
            String errorMessage = "create.receipt.description.en.invalid";
            throw new ApplicationException(errorMessage);
        }
        return true;
    }

    private static boolean validateReceiptNameRu(String nameRu) {
        return nameRu.length() < 45;
    }

    private static boolean validateReceiptNameEn(String nameEn) {
        return !nameEn.matches(cyrillicPattern) && nameEn.length() < 45;
    }

    private static boolean validateReceiptAddressRu(String addressRu) {
        return addressRu.length() < 150;
    }

    private static boolean validateReceiptAddressEn(String addressEn) {
        return !addressEn.matches(cyrillicPattern) && addressEn.length() < 150;
    }

    private static boolean validateReceiptPhoneNumber(String phoneNumber) {
        return phoneNumber.matches(phonePattern) && phoneNumber.length() < 14;
    }

    private static boolean validateReceiptDescriptionRu(String descriptionRu) {
        return descriptionRu.length() < 2000;
    }

    private static boolean validateReceiptDescriptionEn(String descriptionEn) {
        return descriptionEn.length() < 2000;
    }

}
