package com.example.android.inventoryapp2;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

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

        helper.addProduct(new Product("Jeans", 20.0, 5, "Zara", "zara@gmail.com"));
        helper.addProduct(new Product ("T-shirt", 25.20, 5, "H&M", "hm@gmail.com"));
        helper.addProduct(new Product("Dress", 15.75, 10, "Garage", "garage@gmail.com"));
        helper.addProduct(new Product("Jeans#2", 25.0, 20, "Zara", "zara@gmail.com"));

        cursor =  helper.getAllProducts();

        ListView listView = (ListView) findViewById(R.id.list);

        itemAdapter = new InventoryCursorAdapter(MainActivity.this, cursor, 0);

        listView.setAdapter (itemAdapter);
        listView.setItemsCanFocus(false);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                String name = cursor.getString(cursor.getColumnIndexOrThrow("product_name"));
                intent.putExtra("name_to_display", name);
                Log.v("Name is: ", name);
                Log.v("Position is: ", String.valueOf(position));
                startActivity(intent);
            }
        });

        Button addProduct = (Button) findViewById(R.id.add_main_activity);
        addProduct.setOnClickListener(handleAddProduct);
    }

    View.OnClickListener handleAddProduct = new View.OnClickListener() {
        public void onClick(View v) {
            Intent bIntent = new Intent(MainActivity.this, AddProductActivity.class);
            startActivity(bIntent);
        }
    };

    @Override
    public void update() {
        if (cursor != null) {
            Cursor newCursor = helper.getAllProducts();
            itemAdapter.swapCursor(newCursor);
            ListView listView = (ListView) findViewById(R.id.list);
            itemAdapter.bindView(listView, this, newCursor);
        }
    }
}
