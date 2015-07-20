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
import android.text.InputType;
import android.view.Gravity;
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


public class AddFoodFragment extends Fragment {

    //save so I can pass into clickListener for adding
    private View rootView;
    private final String[] items = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20"};
    private final String DELETE = "delete from "+ dbConstants.myConstants.TABLE;

    public AddFoodFragment() {
        //Required empty public constructor
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

        //reset database
        Button resetButton = (Button) rootView.findViewById(R.id.resetButton);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetDB();
            }
        });

        //Inflate the layout for this fragment
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

        //does the inputted food exist already
        boolean exists;

        //get the text of the inputted food
        EditText editFood = (EditText) v.findViewById(R.id.add_food);
        String food = editFood.getText().toString();

        //check to see if user entered a food name
        if(food.isEmpty()) {
            alertEmptyFood();
            return;
        }

        //get value ready for insert
        ContentValues values = new ContentValues();
        values.put(dbConstants.myConstants.FOOD, food);

        exists = foodExists(food, db);

        if(!exists) {
            alertNoFood(values, db);
        }

        else {
            //TODO: add food to grocery list
        }

        editFood.setText("");
    }

    private boolean foodExists(String food, SQLiteDatabase db) throws SQLiteException {
        String[] selection = new String[]{food};
        String selectStatement = "SELECT " + dbConstants.myConstants.FOOD +  " from " + dbConstants.myConstants.TABLE + " WHERE " + dbConstants.myConstants.FOOD + "=?";
        Cursor cursor = db.rawQuery(selectStatement, selection);

        //check if the food exists
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

        //add EditText field to alert dialog box for user to input price
        final EditText getPrice = new EditText(getActivity());
        getPrice.setId(R.id.getPrice);
        getPrice.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        getPrice.setLayoutParams(params);
        box.setView(getPrice);

        PriceWatcher watcher = new PriceWatcher();
        watcher.setEditText(getPrice);
        getPrice.addTextChangedListener(watcher);

        //accept price and add new food to database
        box.setButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                values.put(dbConstants.myConstants.PRICE, getPrice.getText().toString().replaceAll("\\$", ""));
                db.insert(dbConstants.myConstants.TABLE, null, values);

                //TODO: add to grocery list
                Toast.makeText(getActivity(), "Food Added", Toast.LENGTH_SHORT).show();
            }
        });

        box.show();
    }

    //empty food field check
    private void alertEmptyFood() {
        AlertDialog box = new AlertDialog.Builder(getActivity()).create();
        box.setTitle("No Food Input Detected");
        box.setMessage("You must enter the name of the food");

        box.show();
    }

    private void resetDB() {
        dbHelper helper = new dbHelper(getActivity(), dbConstants.myConstants.NAME, null, dbConstants.myConstants.VERSION);
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL(DELETE);
    }
    private boolean validatePriceFormat(String price) {
        //TODO: check to make sure price is in the form x.xx
        return true;
    }

}
