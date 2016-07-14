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

public class InventoryCursorAdapter extends CursorAdapter implements Observer{
    private LayoutInflater cursorInflater;
    Context context;
    DBHelper helper = DBHelper.getInstance(context);
    Cursor cursor;
    InventoryCursorAdapter itemAdapter;

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

        //handles "Sell Item" button from the MainActivity
        sellOneItem.setOnClickListener(new AdapterView.OnClickListener() {
            @Override
            public void onClick(View arg1) {
                int quantity = qty;
                Log.v("Name is: ", name);
                helper.decreaseQty(quantity, 1, name);
                int newQty = quantity - 1;
                if (newQty >= 0) {
                    itemQty.setText("Quantity: " + String.valueOf(newQty));
                }
            }
        });
    }

    @Override
    public void update() {
        if (cursor != null) {
            Cursor newCursor = helper.getAllProducts();
            itemAdapter.swapCursor(newCursor);
        }
    }
}
