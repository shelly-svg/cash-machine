package web.validator;

import com.my.db.entities.Delivery;
import com.my.db.entities.Receipt;
import com.my.db.entities.ReceiptStatus;
import com.my.web.exception.ApplicationException;
import com.my.web.validator.ReceiptValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;

public class ReceiptValidatorTest {

    @Test
    public void validReceiptTest() throws ApplicationException {

        ReceiptValidator receiptValidator = new ReceiptValidator();

        int id = 1;
        Timestamp createTime = new Timestamp(System.currentTimeMillis());
        String nameRu = "receiptNameRu";
        String nameEn = "receiptNameEn";
        String addressRu = "receiptAddressRu";
        String addressEn = "receiptAddressEn";
        String descriptionRu = "receiptDescriptionRu";
        String descriptionEn = "receiptDescriptionEn";
        String phoneNumber = "0972334232";
        Delivery delivery = new Delivery();
        delivery.setId(1);
        delivery.setNameRu("deliveryRu");
        delivery.setNameEn("deliveryEn");
        ReceiptStatus receiptStatus = ReceiptStatus.NEW_RECEIPT;
        int userId = 2;

        Receipt validTestReceipt = new Receipt();
        validTestReceipt.setId(id);
        validTestReceipt.setCreateTime(createTime);
        validTestReceipt.setNameRu(nameRu);
        validTestReceipt.setNameEn(nameEn);
        validTestReceipt.setAddressRu(addressRu);
        validTestReceipt.setAddressEn(addressEn);
        validTestReceipt.setDescriptionRu(descriptionRu);
        validTestReceipt.setDescriptionEn(descriptionEn);
        validTestReceipt.setPhoneNumber(phoneNumber);
        validTestReceipt.setDelivery(delivery);
        validTestReceipt.setReceiptStatus(receiptStatus);
        validTestReceipt.setUserId(userId);

        boolean actual = receiptValidator.validate(validTestReceipt);
        Assertions.assertTrue(actual);
    }

}
