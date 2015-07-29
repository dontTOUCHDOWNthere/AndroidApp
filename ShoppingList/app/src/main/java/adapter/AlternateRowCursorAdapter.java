package adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;

import com.example.rh035578.shoppinglist.R;

/**
 * Created by rh035578 on 7/29/15.
 */
public class AlternateRowCursorAdapter extends SimpleCursorAdapter {

    private int[] colors =new int[] {Color.parseColor("#61d45d"), Color.parseColor("#FFFFFF")};
    public AlternateRowCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to) {
        super(context, layout, c, from, to);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);

        if(position % 2 == 0) {
            view.setBackgroundColor(colors[0]);
        }

        else {
            view.setBackgroundColor(colors[1]);
        }

        return view;
    }
}
