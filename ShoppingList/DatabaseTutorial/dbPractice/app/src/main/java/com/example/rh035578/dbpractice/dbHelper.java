package com.example.rh035578.dbpractice;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by rh035578 on 7/16/15.
 */
public class dbHelper extends SQLiteOpenHelper {

    private static final String CREATE = "create table " +
            databaseConstants.myConstants.TABLE + " (" +
            databaseConstants.myConstants.ID +
            " integer primary key autoincrement, " +
            databaseConstants.myConstants.FOOD + " text not null, " +
            databaseConstants.myConstants.PRICE + " text not null);";

    public dbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + databaseConstants.myConstants.TABLE);
        onCreate(db);
    }
}
