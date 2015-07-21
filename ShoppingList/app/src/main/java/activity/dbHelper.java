package activity;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by rh035578 on 7/16/15.
 */
public class dbHelper extends SQLiteOpenHelper {

    private static final String CREATE = "create table " +
            dbConstants.myConstants.TABLE + " (" +
            dbConstants.myConstants.ID +
            " integer primary key autoincrement, " +
            dbConstants.myConstants.FOOD + " text not null, " +
            dbConstants.myConstants.PRICE + " text not null);";

    private static final String CREATE_LIST = "create table " +
            dbConstants.myConstants.GROCERY_LIST + " (" +
            dbConstants.myConstants.ID +
            " integer primary key autoincrement, " +
            dbConstants.myConstants.FOOD + " text not null, " +
            dbConstants.myConstants.PRICE + " text not null);";

    public dbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE);
        db.execSQL(CREATE_LIST);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + dbConstants.myConstants.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + dbConstants.myConstants.GROCERY_LIST);
        onCreate(db);
    }
}