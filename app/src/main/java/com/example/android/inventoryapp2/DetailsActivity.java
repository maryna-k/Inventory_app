package com.example.android.inventoryapp2;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class DetailsActivity extends AppCompatActivity implements Observer{
    Cursor cursor;
    DBHelper helper;
    String name;
    int qty;
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
        Log.v("Name of item: ", name);

        cursor = helper.getOneProduct(name);

        TextView itemName = (TextView) findViewById(R.id.product_name);
        TextView itemPrice = (TextView) findViewById(R.id.product_price);
        TextView itemQty = (TextView) findViewById(R.id.product_qty);
        TextView itemSupplier = (TextView) findViewById(R.id.supplier);
        TextView itemEmail = (TextView) findViewById(R.id.email);

        int price = cursor.getInt(cursor.getColumnIndexOrThrow("price"));
        qty = cursor.getInt(cursor.getColumnIndexOrThrow("quantity"));
        String supplier = cursor.getString(cursor.getColumnIndexOrThrow("supplier_name"));
        String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));

        itemName.setText("Product: " + name);
        itemPrice.setText("Price: " + String.valueOf(price) + "$");
        itemQty.setText("Quantity: " + String.valueOf(qty));
        itemSupplier.setText("Supplier: " + supplier);
        itemEmail.setText("Email: " + email);

        Button sold = (Button) findViewById(R.id.button_sold);
        sold.setOnClickListener(handleSold);

        Button received = (Button) findViewById(R.id.button_received);
        received.setOnClickListener(handleReceived);

        Button order = (Button) findViewById(R.id.button_order);
        order.setOnClickListener(handleOrder);

        Button delete = (Button) findViewById(R.id.button_delete);
        delete.setOnClickListener(handleDelete);
    }

    /*View.OnClickListener handleSold = new View.OnClickListener() {
        public void onClick(View v) {
            int difference = quantitySold();
            int currQty = helper.getCurrentQty(name);
            helper.decreaseQty(currQty, difference, name);
            if (currQty - difference >= 0) {
                TextView itemQty = (TextView) findViewById(R.id.product_qty);
                itemQty.setText("Quantity: " + String.valueOf(currQty - difference));
            }
        }
    };

    View.OnClickListener handleReceived = new View.OnClickListener() {
        public void onClick(View v) {
            int difference = quantityReceived();
            int currQty = helper.getCurrentQty(name);
            helper.increaseQty(currQty, difference, name);
            TextView itemQty = (TextView) findViewById(R.id.product_qty);
            itemQty.setText("Quantity: " + String.valueOf(currQty + difference));
        }
    };*/


    View.OnClickListener handleSold = new View.OnClickListener() {
        public void onClick(View v) {
            int difference = quantitySold();
            helper.decreaseQty(qty, difference, name);
            int newQty = cursor.getInt(cursor.getColumnIndexOrThrow("quantity"));
            if (qty - difference >= 0) {
                TextView itemQty = (TextView) findViewById(R.id.product_qty);
                itemQty.setText("Quantity: " + String.valueOf(qty - difference));
            }
        }
    };

    View.OnClickListener handleReceived = new View.OnClickListener() {
        public void onClick(View v) {
            int difference = quantityReceived();
            helper.increaseQty(qty, difference, name);
            TextView itemQty = (TextView) findViewById(R.id.product_qty);
            itemQty.setText("Quantity: " + String.valueOf(qty + difference));
        }
    };

    View.OnClickListener handleOrder = new View.OnClickListener() {
        public void onClick(View v) {

        }
    };

    View.OnClickListener handleDelete = new View.OnClickListener() {
        public void onClick(View v) {

        }
    };

    public int quantitySold () {
        EditText number = (EditText) findViewById(R.id.enterNumberSold);
        try {
            int numberSold = Integer.parseInt(number.getText().toString());
            return numberSold;
        }catch (NumberFormatException e) {
            Log.v("Parse quantity sold: ", "Could not parse quantity sold");
        }
        return 0;
    }

    public int quantityReceived () {
        EditText number = (EditText) findViewById(R.id.enterNumberReceived);
        try {
            int numberReceived = Integer.parseInt(number.getText().toString());
            return numberReceived;
        }catch (NumberFormatException e) {
            Log.v("Quantity received:", "Could not parse quantity received");
        }
        return 0;
    }

    @Override
    public void update() {
        if (cursor != null) {
            Cursor newCursor = helper.getAllProducts();
            itemAdapter.swapCursor(newCursor);
        }
    }
}
