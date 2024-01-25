package com.example.bazarhub;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "favorites.db";
    private static final int DATABASE_VERSION = 1;

    // Table and column names
    public static final String TABLE_FAVORITES = "favorites";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_PRODUCT_ID = "product_id";

    // Database creation SQL statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_FAVORITES + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_PRODUCT_ID
            + " integer not null);";

    private SQLiteDatabase database;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Add upgrade logic if needed
    }

    public void open() throws SQLException {
        // Open the database for writing
        database = getWritableDatabase();
    }

    public void close() {
        // Close the database
        if (database != null && database.isOpen()) {
            database.close();
        }
    }
}
