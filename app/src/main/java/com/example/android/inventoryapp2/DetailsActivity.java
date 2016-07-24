package com.example.android.inventoryapp2;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailsActivity extends AppCompatActivity implements Observer {
    Cursor cursor;
    DBHelper helper;
    String name;
    String supplier;
    String supplierEmail;
    int qty;
    byte[] img;
    InventoryCursorAdapter itemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        helper = DBHelper.getInstance(this);
        helper.addObserver(this);
        itemAdapter = new InventoryCursorAdapter(DetailsActivity.this, cursor, 0);

        Bundle extras = getIntent().getExtras();
        name = extras.getString("name_to_display");

        cursor = helper.getOneProduct(name);

        TextView itemName = (TextView) findViewById(R.id.product_name);
        TextView itemPrice = (TextView) findViewById(R.id.product_price);
        TextView itemQty = (TextView) findViewById(R.id.product_qty);
        TextView itemSupplier = (TextView) findViewById(R.id.supplier);
        TextView itemEmail = (TextView) findViewById(R.id.email);
        ImageView itemImage = (ImageView) findViewById(R.id.product_image);

        int price = cursor.getInt(cursor.getColumnIndexOrThrow("price"));
        qty = cursor.getInt(cursor.getColumnIndexOrThrow("quantity"));
        supplier = cursor.getString(cursor.getColumnIndexOrThrow("supplier_name"));
        supplierEmail = cursor.getString(cursor.getColumnIndexOrThrow("email"));
        img = cursor.getBlob(cursor.getColumnIndexOrThrow("image"));

        Bitmap b1 = BitmapFactory.decodeByteArray(img, 0, img.length);

        itemName.setText(name);
        itemPrice.setText("Price: " + String.valueOf(price) + "$");
        itemQty.setText("Quantity: " + String.valueOf(qty));
        itemSupplier.setText("Supplier: " + supplier);
        itemEmail.setText("Email: " + supplierEmail);
        itemImage.setImageBitmap(b1);

        Button sold = (Button) findViewById(R.id.button_sold);
        sold.setOnClickListener(handleSold);

        Button received = (Button) findViewById(R.id.button_received);
        received.setOnClickListener(handleReceived);

        Button order = (Button) findViewById(R.id.button_order);
        order.setOnClickListener(handleOrder);

        Button delete = (Button) findViewById(R.id.button_delete);
        delete.setOnClickListener(handleDelete);
    }

    //handles the button that substracts a specified amount from the quantity of the product
    View.OnClickListener handleSold = new View.OnClickListener() {
        public void onClick(View v) {
            int difference = quantitySold();
            qty = helper.decreaseQty(qty, difference, name);
            int newQty = cursor.getInt(cursor.getColumnIndexOrThrow("quantity"));
            Log.v("Quantity cursor: ", Integer.toString(newQty));
            if (qty >= 0) {
                TextView itemQty = (TextView) findViewById(R.id.product_qty);
                itemQty.setText("Quantity: " + String.valueOf(qty));
            }
        }
    };

    //gets number sold from EditText
    public int quantitySold() {
        EditText number = (EditText) findViewById(R.id.enterNumberSold);
        try {
            int numberSold = Integer.parseInt(number.getText().toString());
            return numberSold;
        } catch (NumberFormatException e) {
            Log.v("Parse quantity sold: ", "Could not parse quantity sold");
        }
        return 0;
    }

    //handles the button that adds a specified amount to the quantity of the product
    View.OnClickListener handleReceived = new View.OnClickListener() {
        public void onClick(View v) {
            int difference = quantityReceived();
            qty = helper.increaseQty(qty, difference, name);
            TextView itemQty = (TextView) findViewById(R.id.product_qty);
            itemQty.setText("Quantity: " + String.valueOf(qty));
        }
    };

    //gets number received from EditText
    public int quantityReceived() {
        EditText number = (EditText) findViewById(R.id.enterNumberReceived);
        try {
            int numberReceived = Integer.parseInt(number.getText().toString());
            return numberReceived;
        } catch (NumberFormatException e) {
            Log.v("Quantity received:", "Could not parse quantity received");
        }
        return 0;
    }

    //handles the button that sends an email with the order message for this quantity of the product to this supplier
    View.OnClickListener handleOrder = new View.OnClickListener() {
        public void onClick(View v) {
            int qtyToOrder = quantityToOrder();
            String orderMessage = composeOrderMessage(supplier, qtyToOrder, name);
            Log.v("Order message: ", orderMessage);
            String subject = "Order of " + name;
            Log.v("Order subject: ", subject);
            composeEmail(supplierEmail, subject, orderMessage);
        }
    };

    //gets number to order from EditText
    public int quantityToOrder() {
        EditText qtyToOrder = (EditText) findViewById(R.id.enterNumberToOrder);
        try {
            int qty = Integer.parseInt(qtyToOrder.getText().toString());
            Log.v("Order quantity: ", String.valueOf(qty));
            return qty;
        } catch (NumberFormatException e) {
            Log.v("Parse quantity order", "Could not parse quantity to order");
        }
        return 0;
    }

    private String composeOrderMessage(String supplierName, int quantity, String productName) {
        String orderMessage = "Dear " + supplierName + ",\n";
        orderMessage = orderMessage + "Our store would like to order " + quantity + " " + productName + ".\n";
        orderMessage = orderMessage + "Sincerely,\n" + "Store";
        return orderMessage;
    }

    //implicit intent to sent an order email
    public void composeEmail(String supplierEmail, String subject, String orderMessage) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); //only email apps should handle this intent
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{supplierEmail});
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, orderMessage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    View.OnClickListener handleDelete = new View.OnClickListener() {
        public void onClick(View v) {
            new AlertDialog.Builder(DetailsActivity.this)
                    .setMessage("Are you sure you want to delete " + name)
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(DetailsActivity.this, MainActivity.class);
                            helper.deleteProduct(name);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        }
    };

    @Override
    public void update() {
        if (cursor != null) {
            Cursor newCursor = helper.getAllProducts();
            itemAdapter.swapCursor(newCursor);
        }
    }
}
