package com.my.db.entities;

public final class Fields {

    // entities
    public static final String ENTITY__ID = "id";

    //user
    public static final String USER__LOGIN = "login";
    public static final String USER__PASSWORD = "password";
    public static final String USER__SALT = "salt";
    public static final String USER__FIRST_NAME = "first_name";
    public static final String USER__LAST_NAME = "last_name";
    public static final String USER__EMAIL = "email";
    public static final String USER__LOCALE_NAME = "locale_name";
    public static final String USER__ROLE_ID = "role_id";

    //product
    public static final String PRODUCT__NAME_RU = "name_ru";
    public static final String PRODUCT__NAME_EN = "name_en";
    public static final String PRODUCT__CODE = "code";
    public static final String PRODUCT__PRICE = "price";
    public static final String PRODUCT__AMOUNT = "amount";
    public static final String PRODUCT__WEIGHT = "weight";
    public static final String PRODUCT__DESCRIPTION_RU = "description_ru";
    public static final String PRODUCT__DESCRIPTION_EN = "description_en";
    public static final String PRODUCT__CATEGORY_ID = "category_id";

    //category
    public static final String CATEGORY__NAME_RU = "name_ru";
    public static final String CATEGORY__NAME_EN = "name_en";

    //receipt
    public static final String RECEIPT_CREATION_TIME = "creation_time";
    public static final String RECEIPT_NAME_RU = "name_ru";
    public static final String RECEIPT_NAME_EN = "name_en";
    public static final String RECEIPT_ADDRESS_RU = "address_ru";
    public static final String RECEIPT_ADDRESS_EN = "address_en";
    public static final String RECEIPT_DESCRIPTION_RU = "description_ru";
    public static final String RECEIPT_DESCRIPTION_EN = "description_en";
    public static final String RECEIPT_PHONE_NUMBER = "phone_number";
    public static final String RECEIPT_DELIVERY_ID = "delivery_id";
    public static final String RECEIPT_RECEIPT_STATUS_ID = "receipt_status_id";
    public static final String RECEIPT_USER_ID = "user_id";

    //receipt_has_products
    public static final String RECEIPT_HAS_PRODUCT_PRODUCT_ID = "product_id";
    public static final String RECEIPT_HAS_PRODUCT_AMOUNT = "amount";

    //delivery
    public static final String DELIVERY_NAME_RU = "name_ru";
    public static final String DELIVERY_NAME_EN = "name_en";

    //user_details
    public static final String USER_DETAILS_SALT = "salt";
    public static final String USER_DETAILS_CODE = "code";

}
