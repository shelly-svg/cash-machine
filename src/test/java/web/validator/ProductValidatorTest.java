package web.validator;

import com.my.db.entities.Category;
import com.my.db.entities.Product;
import com.my.web.exception.ApplicationException;
import com.my.web.validator.ProductValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

public class ProductValidatorTest {

    @Test
    public void validProductTest() throws ApplicationException {
        ProductValidator productValidator = new ProductValidator();
        Product validTestProduct = new Product();

        int id = 1;
        String nameRu = "productNameRu";
        String nameEn = "productNameEn";
        String code = "productCode";
        BigDecimal price = new BigDecimal(999);
        int amount = 10;
        BigDecimal weight = new BigDecimal("333.33");
        String descriptionRu = "productDescriptionRu";
        String descriptionEn = "productDescriptionEn";
        Category category = new Category();
        category.setId(id);
        category.setNameRu("categoryNameRu");
        category.setNameEn("categoryNameEn");

        validTestProduct.setId(id);
        validTestProduct.setNameRu(nameRu);
        validTestProduct.setNameEn(nameEn);
        validTestProduct.setCode(code);
        validTestProduct.setPrice(price);
        validTestProduct.setAmount(amount);
        validTestProduct.setWeight(weight);
        validTestProduct.setDescriptionRu(descriptionRu);
        validTestProduct.setDescriptionEn(descriptionEn);
        validTestProduct.setCategory(category);

        boolean actual = productValidator.validate(validTestProduct);
        Assertions.assertTrue(actual);
    }

}
