package com.example.android.inventoryapp2;

import android.provider.BaseColumns;

public class Contract {

    public Contract() {}

    public static abstract class Products implements BaseColumns {
        public static final String TABLE_NAME = "products";
        public static final String PNAME = "product_name";
        public static final String PRICE = "price";
        public static final String QTY = "quantity"; // product quantity
        public static final String SNAME = "supplier_name";
        public static final String EMAIL = "email"; // supplier email
        public static final String IMAGE = "image";
    }
}