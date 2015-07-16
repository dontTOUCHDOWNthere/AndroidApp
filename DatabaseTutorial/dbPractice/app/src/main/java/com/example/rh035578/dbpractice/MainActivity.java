package com.example.rh035578.dbpractice;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class MainActivity extends ActionBarActivity {

    String[][] recordsArray = {{"Apples", "1.00"},
                               {"Bread", "3.00"},
                               {"Milk", "2.00"}};

    private final String DELETE = "delete from "+ databaseConstants.myConstants.TABLE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button addButton = (Button) findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //add food to database
                addFood();
            }
        });

        Button deleteButton = (Button) findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteData();
            }
        });

        Button seeButton = (Button) findViewById(R.id.seeButton);
        seeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MyListActivity.class);
                startActivity(intent);
            }
        });
    }

    private void populateDatabase() {
        dbHelper helper = new dbHelper(this, databaseConstants.myConstants.NAME, null, databaseConstants.myConstants.VERSION);
        SQLiteDatabase db = helper.getWritableDatabase();

        for(int i = 0; i < recordsArray.length; i++){
            ContentValues values = new ContentValues();
            for(int j = 0; j < recordsArray[i].length; j++){
                values.put(databaseConstants.myConstants.FOOD, recordsArray[i][0]);
                values.put(databaseConstants.myConstants.PRICE, recordsArray[i][1]);
            }
            long insertedRowID = db.insert(databaseConstants.myConstants.TABLE, null, values);
        }
    }

    private void addFood() {
        dbHelper helper = new dbHelper(this, databaseConstants.myConstants.NAME, null, databaseConstants.myConstants.VERSION);
        SQLiteDatabase db = helper.getWritableDatabase();

        EditText editFood = (EditText) findViewById(R.id.add_food);
        EditText editPrice = (EditText) findViewById(R.id.add_price);

        String food = editFood.getText().toString();
        String price = editPrice.getText().toString();
        editFood.setText("");
        editPrice.setText("");

        ContentValues values = new ContentValues();
        values.put(databaseConstants.myConstants.FOOD, food);
        values.put(databaseConstants.myConstants.PRICE, price);

        db.insert(databaseConstants.myConstants.TABLE, null, values);
    }

    private void deleteData() {
        dbHelper helper = new dbHelper(this, databaseConstants.myConstants.NAME, null, databaseConstants.myConstants.VERSION);
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL(DELETE);
    }
}
