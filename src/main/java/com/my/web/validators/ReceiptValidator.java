package com.my.web.validators;

import com.my.db.entities.Receipt;

import javax.servlet.http.HttpSession;
import java.util.ResourceBundle;

public class ReceiptValidator extends Validator<Receipt> {

    private static final String cyrillicPattern = "[а-яА-ЯёЁІіЇїЪъ]";
    private static final String phonePattern = "^\\d{10}$";

    @Override
    public boolean validate(Receipt receipt, HttpSession session, ResourceBundle rb) {
        if (receipt.getNameRu() == null || receipt.getNameRu().isEmpty()) {
            String errorMessage = rb.getString("create.receipt.name.ru.null");
            session.setAttribute("errorMessage", errorMessage);
            return false;
        }
        if (receipt.getNameEn() == null || receipt.getNameEn().isEmpty()) {
            String errorMessage = rb.getString("create.receipt.name.en.null");
            session.setAttribute("errorMessage", errorMessage);
            return false;
        }
        if (receipt.getAddressRu() == null || receipt.getAddressRu().isEmpty()) {
            String errorMessage = rb.getString("create.receipt.address.ru.null");
            session.setAttribute("errorMessage", errorMessage);
            return false;
        }
        if (receipt.getPhoneNumber() == null || receipt.getPhoneNumber().isEmpty()) {
            String errorMessage = rb.getString("create.receipt.phone.number.null");
            session.setAttribute("errorMessage", errorMessage);
            return false;
        }

        if (!validateReceiptNameRu(receipt.getNameRu())) {
            String errorMessage = rb.getString("create.receipt.name.ru.invalid");
            session.setAttribute("errorMessage", errorMessage);
            return false;
        }
        if (!validateReceiptNameEn(receipt.getNameEn())) {
            String errorMessage = rb.getString("create.receipt.name.en.invalid");
            session.setAttribute("errorMessage", errorMessage);
            return false;
        }
        if (!validateReceiptAddressRu(receipt.getAddressRu())) {
            String errorMessage = rb.getString("create.receipt.address.ru.invalid");
            session.setAttribute("errorMessage", errorMessage);
            return false;
        }
        if (!validateReceiptAddressEn(receipt.getAddressEn())) {
            String errorMessage = rb.getString("create.receipt.address.en.invalid");
            session.setAttribute("errorMessage", errorMessage);
            return false;
        }
        if (!validateReceiptPhoneNumber(receipt.getPhoneNumber())) {
            String errorMessage = rb.getString("create.receipt.phone.number.invalid");
            session.setAttribute("errorMessage", errorMessage);
            return false;
        }
        if (!validateReceiptDescriptionRu(receipt.getDescriptionRu())) {
            String errorMessage = rb.getString("create.receipt.description.ru.invalid");
            session.setAttribute("errorMessage", errorMessage);
            return false;
        }
        if (!validateReceiptDescriptionEn(receipt.getDescriptionEn())) {
            String errorMessage = rb.getString("create.receipt.description.en.invalid");
            session.setAttribute("errorMessage", errorMessage);
            return false;
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
