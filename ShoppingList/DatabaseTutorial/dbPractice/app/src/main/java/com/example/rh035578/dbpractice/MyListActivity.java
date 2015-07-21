package com.example.rh035578.dbpractice;

import android.annotation.TargetApi;
import android.app.ListActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.widget.SimpleCursorAdapter;

/**
 * Created by rh035578 on 7/16/15.
 */
public class MyListActivity extends ListActivity {

    private Cursor cursor;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cursor = readDB();

        String[] columns = {databaseConstants.myConstants.FOOD, databaseConstants.myConstants.PRICE};
        int[] to = {android.R.id.text1, android.R.id.text2};
        SimpleCursorAdapter listAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_expandable_list_item_2, cursor, columns, to, 0);
        setListAdapter(listAdapter);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private Cursor readDB() {
        dbHelper helper = new dbHelper(this, databaseConstants.myConstants.NAME, null, databaseConstants.myConstants.VERSION);
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] projection = {databaseConstants.myConstants.ID,
                               databaseConstants.myConstants.FOOD,
                               databaseConstants.myConstants.PRICE};

        String selection = null;
        String[] selectionArgs = null;
        String groupBy = null;
        String having = null;
        String order = null;

        Cursor cursor = db.query(databaseConstants.myConstants.TABLE, projection, selection, selectionArgs, groupBy, having, order);
        return cursor;

    }
}
