package activity;

import android.annotation.TargetApi;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import com.example.rh035578.shoppinglist.R;
import com.wdullaer.swipeactionadapter.SwipeActionAdapter;
import com.wdullaer.swipeactionadapter.SwipeDirections;


/**
 * Created by rh035578 on 7/16/15.
 */
public class MyListActivity extends ListActivity implements SwipeActionAdapter.SwipeActionListener{

    private Cursor cursor;
    protected SwipeActionAdapter swipeAdapter;
    private SQLiteDatabase dbRead;

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
        swipeAdapter.addBackground(SwipeDirections.DIRECTION_FAR_RIGHT,R.layout.row_bg)
                    .addBackground(SwipeDirections.DIRECTION_NORMAL_RIGHT, R.layout.row_bg);

        AddFoodFragment.getTotal(dbRead);
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
        dbRead = helper.getReadableDatabase();

        String[] projection = {dbConstants.myConstants.ID,
                dbConstants.myConstants.FOOD,
                dbConstants.myConstants.PRICE};

        String selection = null;
        String[] selectionArgs = null;
        String groupBy = null;
        String having = null;
        String order = null;

        Cursor cursor = dbRead.query(dbConstants.myConstants.GROCERY_LIST, projection, selection, selectionArgs, groupBy, having, order);
        return cursor;

    }

    @Override
    protected void onListItemClick(ListView listView, View view, int position, long id){
        //empty, do nothing on click
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
            int position = positionList[i];

            Cursor deleteCursor = (Cursor) swipeAdapter.getItem(position);
            long deleteID = -1;
            if(deleteCursor.moveToFirst())
                deleteID = deleteCursor.getLong(deleteCursor.getColumnIndex(dbConstants.myConstants.ID));
            Toast.makeText(this, "Food Deleted From List", Toast.LENGTH_SHORT).show();

            if(deleteID >= 0)
                deleteFood(deleteID);

            swipeAdapter.notifyDataSetChanged();
        }
    }

    public void deleteFood(long id) {

        //delete food from grocery list
        dbHelper helper = new dbHelper(this, dbConstants.myConstants.NAME, null, dbConstants.myConstants.VERSION);
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("DELETE FROM " + dbConstants.myConstants.GROCERY_LIST + " WHERE " + dbConstants.myConstants.ID + " = " + id);

        //refresh activity after deletion
        restart();
    }

    public void restart() {
        Intent intent = getIntent();
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
        AddFoodFragment.getTotal(dbRead);
    }
}
