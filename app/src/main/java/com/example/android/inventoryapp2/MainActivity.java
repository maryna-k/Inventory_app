package com.example.android.inventoryapp2;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements Observer{

    DBHelper helper;
    SQLiteDatabase db;
    private Cursor cursor;
    InventoryCursorAdapter itemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        helper = DBHelper.getInstance(this);
        helper.addObserver(this);

        /*helper.addProduct(new Product("Jeans", 20.0, 5, "Zara", "zara@gmail.com"));
        helper.addProduct(new Product ("T-shirt", 25.20, 5, "H&M", "hm@gmail.com"));
        helper.addProduct(new Product("Dress", 15.75, 10, "Garage", "garage@gmail.com"));
        helper.addProduct(new Product("Jeans#2", 25.0, 20, "Zara", "zara@gmail.com")); */

        cursor =  helper.getAllProducts();

        ListView listView = (ListView) findViewById(R.id.list);

        itemAdapter = new InventoryCursorAdapter(MainActivity.this, cursor, 0);

        listView.setAdapter (itemAdapter);
        listView.setItemsCanFocus(false); //helps to make both view and a sell button clickable

        //Shows an empty TextView if database is empty
        TextView defaultText = (TextView) findViewById(R.id.i_empty);
        listView.setEmptyView(defaultText);

        //opens DetailsActivity if the listview is clicked
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                cursor.moveToPosition(position);
                String name = cursor.getString(cursor.getColumnIndexOrThrow("product_name")); //finds the name of the product at this position
                intent.putExtra("name_to_display", name); //sends the name to the DetailsActivity
                startActivity(intent);
            }
        });

        Button addProduct = (Button) findViewById(R.id.add_main_activity);
        addProduct.setOnClickListener(handleAddProduct);
    }

    //add button opens AddProduct activity
    View.OnClickListener handleAddProduct = new View.OnClickListener() {
        public void onClick(View v) {
            Intent bIntent = new Intent(MainActivity.this, AddProductActivity.class);
            startActivity(bIntent);
        }
    };

    //method of the observer. Whenever there is a change in the database, the cursor is reloaded
    @Override
    public void update() {
        if (cursor != null) {
            Cursor newCursor = helper.getAllProducts();
            itemAdapter.swapCursor(newCursor);
        }
    }
}
