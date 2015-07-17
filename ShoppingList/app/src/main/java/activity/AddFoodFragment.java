package activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


import com.example.rh035578.shoppinglist.R;

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

        EditText editFood = (EditText) v.findViewById(R.id.add_food);
        EditText editPrice = (EditText) v.findViewById(R.id.add_price);

        String food = editFood.getText().toString();
        String price = editPrice.getText().toString();
        editFood.setText("");
        editPrice.setText("");

        ContentValues values = new ContentValues();
        values.put(dbConstants.myConstants.FOOD, food);
        values.put(dbConstants.myConstants.PRICE, price);

        db.insert(dbConstants.myConstants.TABLE, null, values);
    }
}
