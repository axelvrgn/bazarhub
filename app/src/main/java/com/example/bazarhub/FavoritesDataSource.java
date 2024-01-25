package com.example.bazarhub;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class FavoritesDataSource {
    private SQLiteDatabase database;
    private DbHelper dbHelper;

    public FavoritesDataSource(Context context) {
        dbHelper = new DbHelper(context);
    }

    public void open() throws SQLException {
        dbHelper.open();
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long addFavorite(int productId) {
        ContentValues values = new ContentValues();
        values.put(DbHelper.COLUMN_PRODUCT_ID, productId);
        return database.insert(DbHelper.TABLE_FAVORITES, null, values);
    }

    public void removeFavorite(int productId) {
        database.delete(DbHelper.TABLE_FAVORITES, DbHelper.COLUMN_PRODUCT_ID + " = " + productId, null);
    }

    public boolean isProductFavorited(int productId) {
        Cursor cursor = database.query(DbHelper.TABLE_FAVORITES, null,
                DbHelper.COLUMN_PRODUCT_ID + " = " + productId, null, null, null, null);

        boolean isFavorited = cursor.getCount() > 0;
        cursor.close();

        return isFavorited;
    }

}
