package com.my.web.validators;

import com.my.db.entities.Receipt;

import javax.servlet.http.HttpSession;
import java.util.ResourceBundle;

public class ReceiptValidator extends Validator<Receipt>{

    @Override
    public boolean validate(Receipt receipt, HttpSession session, ResourceBundle rb){
        return false;
    }

}
