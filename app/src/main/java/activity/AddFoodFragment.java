package activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;


import com.example.rh035578.shoppinglist.R;

import java.sql.SQLException;

public class AddFoodFragment extends Fragment {

    // save so I can pass into clickListener for adding
    private View rootView;

    public AddFoodFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_add_food, container, false);

        //add food and price to db
        Button addButton = (Button) rootView.findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFood(rootView);
            }
        });

        Spinner dropdown = (Spinner) rootView.findViewById(R.id.spinner1);
        String[] items = new String[]{"1", "2", "3"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, items);
        dropdown.setAdapter(adapter);

        //see what foods the user has saved in db
        Button seeButton = (Button) rootView.findViewById(R.id.seeButton);
        seeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyListActivity.class);
                startActivity(intent);
            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void addFood(View v) {

        //add food item to database
        dbHelper helper = new dbHelper(getActivity(), dbConstants.myConstants.NAME, null, dbConstants.myConstants.VERSION);
        SQLiteDatabase db = helper.getWritableDatabase();

        boolean exists;

        EditText editFood = (EditText) v.findViewById(R.id.add_food);
        //EditText editPrice = (EditText) v.findViewById(R.id.type_price);

        String food = editFood.getText().toString();
        //String price = editPrice.getText().toString();
        editFood.setText("");
        //editPrice.setText("");

        ContentValues values = new ContentValues();
        values.put(dbConstants.myConstants.FOOD, food);
        //values.put(dbConstants.myConstants.PRICE, price);

        exists = foodExists(food, db);

        if(!exists) {
            alertNoFood(values, db);
            //db.insert(dbConstants.myConstants.TABLE, null, values);
        }
    }

    private boolean foodExists(String food, SQLiteDatabase db) throws SQLiteException {
        String[] selection = new String[]{food};
        String selectStatement = "SELECT " + dbConstants.myConstants.FOOD +  " from " + dbConstants.myConstants.TABLE + " WHERE " + dbConstants.myConstants.FOOD + "=?";
        Cursor cursor = db.rawQuery(selectStatement, selection);

        // check if the food exists
        if(cursor != null) {
            if (cursor.getCount() != 0)
                return true;
        }

        return false;
    }

    private void alertNoFood(final ContentValues values, final SQLiteDatabase db) {
        AlertDialog box = new AlertDialog.Builder(getActivity()).create();
        box.setTitle("Food doesn't exist");
        box.setMessage("Enter price for food");

        final EditText getPrice = new EditText(getActivity());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        getPrice.setLayoutParams(params);
        box.setView(getPrice);

        box.setButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                values.put(dbConstants.myConstants.PRICE, getPrice.getText().toString());
                db.insert(dbConstants.myConstants.TABLE, null, values);
                Toast.makeText(getActivity(), "Food Added", Toast.LENGTH_SHORT).show();
            }
        });

        box.show();
    }
}
