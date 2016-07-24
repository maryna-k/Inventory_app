package com.example.android.inventoryapp2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;


public class AddProductActivity extends AppCompatActivity {

    private static int ACTIVITY_SELECT_IMAGE = 1;

    DBHelper helper;
    String newName;
    double newPrice;
    int newQuantity;
    String newSupplierName;
    String newSupplierEmail;
    boolean canAdd = false; //canAdd is set to true if no info is missing
    byte[] imageInByte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        helper = DBHelper.getInstance(this);

        //opens the image store program on the device
        Button buttonLoadImage = (Button) findViewById(R.id.add_product_image);
        buttonLoadImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, ACTIVITY_SELECT_IMAGE);
            }
        });

        //adds product to the database
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

            //validates user input and if something is missing displays a toast message. Else sets canAdd to true
            if (newName.equals("") || newPrice == 0 || newSupplierName.equals("") || newSupplierEmail.equals("") || imageInByte == null) {
                Toast.makeText(AddProductActivity.this, "Please, enter full information about the product.",
                        Toast.LENGTH_LONG).show();
            } else canAdd = true;

            //if all the information is there, program can add the product to the database
            if (canAdd) {
                helper.addProduct(new Product(newName, newPrice, newQuantity, newSupplierName, newSupplierEmail, imageInByte));
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ACTIVITY_SELECT_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();

            try {
                InputStream imageStream = getContentResolver().openInputStream(selectedImage);
                Bitmap yourSelectedImage = BitmapFactory.decodeStream(imageStream);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                yourSelectedImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
                imageInByte = stream.toByteArray();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}