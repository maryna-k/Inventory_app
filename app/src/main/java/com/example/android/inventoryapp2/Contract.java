package com.example.android.inventoryapp2;

import android.provider.BaseColumns;


public class Contract {

    public Contract(){}

    public static abstract class Products implements BaseColumns {
        public static final String TABLE_NAME = "products";
        //public static final String PID = "_id"; //product id
        public static final String PNAME = "product_name";
        public static final String PRICE = "price";
        public static final String QTY = "quantity"; // product quantiry
        public static final String SNAME = "supplier_name";
        public static final String EMAIL = "email"; // supplier email
        //image

    }
}