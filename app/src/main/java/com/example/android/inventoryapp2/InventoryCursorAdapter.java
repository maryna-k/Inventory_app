package com.example.android.inventoryapp2;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class InventoryCursorAdapter extends CursorAdapter{
    private LayoutInflater cursorInflater;
    Context context;
    DBHelper helper = DBHelper.getInstance(context);

    // Default constructor
    public InventoryCursorAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // inflates a new view and returns it.
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        RelativeLayout container = (RelativeLayout) view.findViewById(R.id.item_container);
        TextView itemName = (TextView) view.findViewById(R.id.name);
        TextView itemPrice = (TextView) view.findViewById(R.id.price);
        final TextView itemQty = (TextView) view.findViewById(R.id.quantity);

        final String name = cursor.getString(cursor.getColumnIndexOrThrow("product_name"));
        int price = cursor.getInt(cursor.getColumnIndexOrThrow("price"));
        final int qty = cursor.getInt(cursor.getColumnIndexOrThrow("quantity"));

        itemName.setText(name);
        itemPrice.setText(String.valueOf(price) + "$");
        itemQty.setText("Quantity: " + String.valueOf(qty));

        Button sellOneItem = (Button) view.findViewById(R.id.sellOneItem);

        sellOneItem.setOnClickListener(new AdapterView.OnClickListener() {
            @Override
            public void onClick(View arg1) {
                int quantity = qty;
                helper.decreaseQty(quantity, 1, name);
                Log.v("Name is: ", name);
                Cursor c1 = helper.getOneProduct(name);
                Log.v("Name is: ", name);
                int newQty = c1.getInt(c1.getColumnIndexOrThrow("quantity"));
                //int newQty = quantity - 1;
                itemQty.setText("Quantity: " + String.valueOf(newQty));
                //cursor = helper.getAllProducts();
                Log.v("Click on BUTTON works!", "Click on BUTTON works! Quantity is now " + qty);
            }
        });
    }
}
