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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.example.rh035578.shoppinglist.R;

import java.text.DecimalFormat;
import java.util.List;


public class AddFoodFragment extends Fragment {

    //save so I can pass into clickListener for adding
    protected static View rootView;
    private final String[] items = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20"};
    private final String RESET_GL = "delete from "+ dbConstants.myConstants.GROCERY_LIST;
    private final String RESET_MAIN = "delete from "+ dbConstants.myConstants.TABLE;
    private static final String GET_TOTAL = "select sum(" + dbConstants.myConstants.PRICE + ") from " + dbConstants.myConstants.GROCERY_LIST;
    private static DecimalFormat twoDecimals = new DecimalFormat("#.00");

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

        Spinner dropdown = (Spinner) rootView.findViewById(R.id.quantitySpinner);
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
                resetList();
            }
        });

        Button resetMainButton = (Button) rootView.findViewById(R.id.resetMainButton);
        resetMainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetMain();
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

        //get quantity value from spinner
        Spinner quantitySpinner = (Spinner) rootView.findViewById(R.id.quantitySpinner);
        String quantity = quantitySpinner.getSelectedItem().toString();

        //check to see if user entered a food name
        if(food.isEmpty()) {
            alertEmptyFood();
            return;
        }

        //get value ready for insert
        ContentValues values = new ContentValues();
        values.put(dbConstants.myConstants.FOOD, food);

        exists = foodExists(food, db, quantity, values);

        if(!exists) {
            alertNoFood(values, db, quantity, food);
        }

        editFood.setText("");
    }

    private boolean foodExists(String food, SQLiteDatabase db, String quantity, ContentValues values) throws SQLiteException {
        String[] selection = new String[]{food};
        String selectStatement = "SELECT " + dbConstants.myConstants.FOOD + ", " + dbConstants.myConstants.PRICE +  " from " + dbConstants.myConstants.TABLE + " WHERE " + dbConstants.myConstants.FOOD + "=?";
        Cursor cursor = db.rawQuery(selectStatement, selection);

        //check if the food exists
        if(cursor != null) {
            if (cursor.getCount() != 0) {
                addToGroceryList(quantity, db, cursor, values);
                return true;
            }
        }

        return false;
    }

    private void alertNoFood(final ContentValues values, final SQLiteDatabase db, final String quantity, String food) {

        final ContentValues glValues = new ContentValues();

        AlertDialog box = new AlertDialog.Builder(getActivity()).create();
        box.setTitle("Food doesn't exist");
        box.setMessage("Enter price for food");

        //add EditText field to alert dialog box for user to input price
        final EditText getPrice = new EditText(getActivity());
        getPrice.setId(R.id.getPrice);
        getPrice.setGravity(Gravity.CENTER);
        getPrice.setInputType(InputType.TYPE_CLASS_NUMBER);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        getPrice.setLayoutParams(params);
        box.setView(getPrice);

        PriceWatcher watcher = new PriceWatcher();
        watcher.setEditText(getPrice);
        getPrice.addTextChangedListener(watcher);

        glValues.put(dbConstants.myConstants.FOOD, food);

        //accept price and add new food to database
        box.setButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                glValues.put(dbConstants.myConstants.PRICE, getPrice.getText().toString().replaceAll("\\$", ""));
                values.put(dbConstants.myConstants.PRICE, dividePriceByQuantity(getPrice, quantity));

                db.insert(dbConstants.myConstants.TABLE, null, values);
                db.insert(dbConstants.myConstants.GROCERY_LIST, null, glValues);

                Toast.makeText(getActivity(), "Food Added", Toast.LENGTH_SHORT).show();
                getTotal(db);
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

    private void resetList() {
        dbHelper helper = new dbHelper(getActivity(), dbConstants.myConstants.NAME, null, dbConstants.myConstants.VERSION);
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL(RESET_GL);
        getTotal(db);
    }

    private void resetMain() {
        dbHelper helper = new dbHelper(getActivity(), dbConstants.myConstants.NAME, null, dbConstants.myConstants.VERSION);
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL(RESET_MAIN);
    }

    private String dividePriceByQuantity(EditText price, String quantity) {
        Float floatPrice = Float.parseFloat(price.getText().toString().replaceAll("\\$", ""));
        Float floatQuantity = Float.parseFloat(quantity);

        return twoDecimals.format(floatPrice / floatQuantity);

    }

    private void addToGroceryList(String quantity, SQLiteDatabase db, Cursor cursor, ContentValues values) {
        String price = "";
        if(cursor.moveToFirst()) {
            price = cursor.getString(cursor.getColumnIndex(dbConstants.myConstants.PRICE));
        }

        Float floatPrice = Float.parseFloat(price) * Float.parseFloat(quantity);
        String glPrice = twoDecimals.format(floatPrice).toString();

        values.put(dbConstants.myConstants.PRICE, glPrice);
        db.insert(dbConstants.myConstants.GROCERY_LIST, null, values);
        getTotal(db);
    }

    protected static void getTotal(SQLiteDatabase db) {
        Cursor total = db.rawQuery(GET_TOTAL, null);
        total.moveToFirst();
        String totalCost = "$" + twoDecimals.format(total.getFloat(0));
        TextView textView = (TextView) rootView.findViewById(R.id.totalCost);
        textView.setText(totalCost);
    }
}