package activity;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.example.rh035578.shoppinglist.R;
import com.wdullaer.swipeactionadapter.SwipeActionAdapter;
import com.wdullaer.swipeactionadapter.SwipeDirections;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rh035578 on 7/16/15.
 */
public class MyListActivity extends ListActivity implements SwipeActionAdapter.SwipeActionListener{

    private Cursor cursor;
    protected SwipeActionAdapter swipeAdapter;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        cursor = readDB();

        String[] columns = {dbConstants.myConstants.FOOD, dbConstants.myConstants.PRICE};
        int[] to = {R.id.foodName, R.id.foodPrice};
        SimpleCursorAdapter listAdapter = new SimpleCursorAdapter(this, R.layout.food_info, cursor, columns, to, 0);

        swipeAdapter = new SwipeActionAdapter(listAdapter);
        swipeAdapter.setListView(getListView())
                    .setSwipeActionListener(this)
                    .setDimBackgrounds(true);

        setListAdapter(swipeAdapter);

        //just for testing purposes
        swipeAdapter.addBackground(SwipeDirections.DIRECTION_FAR_LEFT,R.layout.row_bg)
                .addBackground(SwipeDirections.DIRECTION_NORMAL_LEFT,R.layout.row_bg)
                .addBackground(SwipeDirections.DIRECTION_FAR_RIGHT,R.layout.row_bg)
                .addBackground(SwipeDirections.DIRECTION_NORMAL_RIGHT, R.layout.row_bg);

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
        dbHelper helper = new dbHelper(this, dbConstants.myConstants.NAME, null, dbConstants.myConstants.VERSION);
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] projection = {dbConstants.myConstants.ID,
                dbConstants.myConstants.FOOD,
                dbConstants.myConstants.PRICE};

        String selection = null;
        String[] selectionArgs = null;
        String groupBy = null;
        String having = null;
        String order = null;

        Cursor cursor = db.query(dbConstants.myConstants.GROCERY_LIST, projection, selection, selectionArgs, groupBy, having, order);
        return cursor;

    }

    @Override
    protected void onListItemClick(ListView listView, View view, int position, long id){
        Toast.makeText(
                this,
                "Clicked "+ swipeAdapter.getItem(position),
                Toast.LENGTH_SHORT
        ).show();
    }

    @Override
    public boolean hasActions(int position){
        return true;
    }

    @Override
    public boolean shouldDismiss(int position, int direction){
        return direction == SwipeDirections.DIRECTION_NORMAL_LEFT;
    }

    @Override
    public void onSwipe(int[] positionList, int[] directionList){
        for(int i=0;i<positionList.length;i++) {
            int direction = directionList[i];
            int position = positionList[i];
            String dir = "";

            switch (direction) {
                case SwipeDirections.DIRECTION_FAR_LEFT:
                    dir = "Far left";
                    break;
                case SwipeDirections.DIRECTION_NORMAL_LEFT:
                    dir = "Left";
                    break;
                case SwipeDirections.DIRECTION_FAR_RIGHT:
                    dir = "Far right";
                    break;
                case SwipeDirections.DIRECTION_NORMAL_RIGHT:
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Test Dialog").setMessage("You swiped right").create().show();
                    dir = "Right";
                    break;
            }
            Toast.makeText(
                    this,
                    dir + " swipe Action triggered on " + swipeAdapter.getItem(position),
                    Toast.LENGTH_SHORT
            ).show();
            swipeAdapter.notifyDataSetChanged();
        }
    }
}
