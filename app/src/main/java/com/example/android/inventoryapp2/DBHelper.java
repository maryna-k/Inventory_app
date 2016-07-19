package com.example.android.inventoryapp2;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper implements Observable {

    private ArrayList<Observer> Observers = new ArrayList<>();

    @Override
    public void addObserver (Observer observer) {
        if (observer != null) {
            Observers.add(observer);
        }
    }

    @Override
    public void removeObserver(Observer observer) {
        if (observer != null) {
            Observers.remove(observer);
        }
    }

    @Override
    public void notifyObservers() {
        for (int i = 0; i < Observers.size(); i++) {
            Observers.get(i).update();
        }
    }

    private static DBHelper helper;
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Inventory.db";

    public static synchronized DBHelper getInstance(Context context) {
        if (helper == null) {
            helper = new DBHelper(context.getApplicationContext());
        }
        return helper;
    }

    private DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + Contract.Products.TABLE_NAME + " ( " +
                        Contract.Products.PNAME + " TEXT PRIMARY KEY" + ", " +
                        Contract.Products.PRICE + " DECIMAL" + ", " +
                        Contract.Products.QTY + " INTEGER" + ", " +
                        Contract.Products.SNAME + " TEXT" + ", " +
                        Contract.Products.EMAIL + " TEXT" + " )";
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + Contract.Products.TABLE_NAME;
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    //deletes database
    public void deleteDB(Context context) {
        context.getApplicationContext().deleteDatabase(DATABASE_NAME);
    }

    //Insert method that adds a row to the database
    public void addProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Contract.Products.PNAME, product.getName());
        values.put(Contract.Products.PRICE, product.getPrice());
        values.put(Contract.Products.QTY, product.getQty());
        values.put(Contract.Products.SNAME, product.getSupplier());
        values.put(Contract.Products.EMAIL, product.getEmail());

        String name = product.getName();
        String sqlQuery = "SELECT rowid _id, * FROM " + Contract.Products.TABLE_NAME + " WHERE product_name =? ";
        Cursor c = db.rawQuery(sqlQuery, new String[] {name});
        //c.moveToFirst();
        if (c != null && c.getCount() != 0) {
        } else {
            db.insert(Contract.Products.TABLE_NAME, null, values);
            notifyObservers();
        }
    }

    public Cursor getAllProducts() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT rowid _id, * FROM " + Contract.Products.TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        return cursor;
    }

    public Cursor getOneProduct(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT rowid _id, * FROM " + Contract.Products.TABLE_NAME + " WHERE product_name =? ";
        Cursor cursor = db.rawQuery(query, new String[] {name});
        cursor.moveToFirst();
        return cursor;
    }

    public int getCurrentQty (String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT rowid _id, * FROM " + Contract.Products.TABLE_NAME + " WHERE product_name =? ";
        Cursor cursor = db.rawQuery(query, new String[] {name});
        cursor.moveToFirst();
        int currQty = cursor.getInt(cursor.getColumnIndexOrThrow("quantity"));
        return currQty;
    }

    public int decreaseQty (int quantity, int decrease, String name) {

        if (quantity - decrease >= 0) {
            quantity = quantity - decrease;
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(Contract.Products.QTY, quantity);
            String where = "product_name =?";
            String[] whereArgs = {name};
            db.update(Contract.Products.TABLE_NAME, values, where, whereArgs);
            notifyObservers();
        }
        return quantity;
    }

    public int increaseQty (int quantity, int increase, String name) {
        quantity = quantity + increase;
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(Contract.Products.QTY, quantity);
            String where = "product_name =?";
            String[] whereArgs = {name};
            db.update(Contract.Products.TABLE_NAME, values, where, whereArgs);
            notifyObservers();
        return quantity;
    }

    //deletes all the entries from the table
    public void deleteAllEntries() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Contract.Products.TABLE_NAME, null, null);
        notifyObservers();
    }

    //deletes a product with this name
    public void deleteProduct(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        String where = "product_name =?";
        String[] whereArgs = {name};
        try {
            db.delete(Contract.Products.TABLE_NAME, where, whereArgs);
            notifyObservers();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
