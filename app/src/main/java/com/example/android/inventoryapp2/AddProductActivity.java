package com.example.android.inventoryapp2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class AddProductActivity extends AppCompatActivity {

    DBHelper helper;
    String newName;
    double newPrice;
    int newQuantity;
    String newSupplierName;
    String newSupplierEmail;
    boolean canAdd = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        helper = DBHelper.getInstance(this);

        Button addProduct = (Button) findViewById(R.id.add_product_button);
        addProduct.setOnClickListener(handleAddNewProduct);
    }

    View.OnClickListener handleAddNewProduct = new View.OnClickListener() {
        public void onClick(View v) {
            newName = getNewProductName();
            newPrice = getNewProductPrice();
            newQuantity = getNewProductQty();
            newSupplierName = getNewSupplierName();
            newSupplierEmail = getNewSupplierEmail();

            if (newName.equals("") || newPrice == 0 || newSupplierName.equals("") || newSupplierEmail.equals("")) {
                Toast.makeText(AddProductActivity.this, "Please, enter full information about the product.",
                        Toast.LENGTH_LONG).show();
            }
            else canAdd = true;

            if (canAdd) {
                helper.addProduct(new Product(newName, newPrice, newQuantity, newSupplierName, newSupplierEmail));
                Intent intent = new Intent(AddProductActivity.this, MainActivity.class);
                startActivity(intent);
            }
        }
    };

    //gets name of new product from EditText
    public String getNewProductName() {
        EditText name = (EditText) findViewById(R.id.add_product_name);
        try {
            return name.getText().toString();
        } catch (NumberFormatException e) {
            Log.v("Name: ", "Could not parse name");
            return "";
        }
    }

    //gets price of new product from EditText
    public double getNewProductPrice() {
        EditText price = (EditText) findViewById(R.id.add_product_price);
        try {
            return Double.parseDouble(price.getText().toString());
        } catch (NumberFormatException e) {
            Log.v("Price: ", "Could not parse price");
            return 0;
        }
    }

    //gets quantity of new product from EditText
    public int getNewProductQty() {
        EditText qty = (EditText) findViewById(R.id.add_product_qty);
        try {
            return Integer.parseInt(qty.getText().toString());
        } catch (NumberFormatException e) {
            Log.v("Quantity: ", "Could not parse quantity");
            return 0;
        }
    }

    //gets  name of new product supplier from EditText
    public String getNewSupplierName() {
        EditText supplierName = (EditText) findViewById(R.id.add_product_supplier);
        try {
            return supplierName.getText().toString();
        } catch (NumberFormatException e) {
            Log.v("Supplier: ", "Could not parse supplier");
            return "";
        }
    }

    //gets email of new product supplier from EditText
    public String getNewSupplierEmail() {
        EditText email = (EditText) findViewById(R.id.add_product_email);
        try {
            return email.getText().toString();
        } catch (NumberFormatException e) {
            Log.v("Supplier email: ", "Could not parse supplier email");
            return "";
        }
    }
}