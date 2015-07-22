package activity;

import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.rh035578.shoppinglist.R;

/**
 * Created by rh035578 on 7/22/15.
 */
public class SwipeDeletion implements View.OnTouchListener {

    public static enum Action {
        LR,
        None
    }

    //private ListView listView = (ListView) AddFoodFragment.rootView.findViewById(R.id.dbList);
    private static final int MIN_DISTANCE = 20;
    private float startX, finishX;
    private Action swipeDetector = Action.None;

    public boolean swipeDetection() {
        return swipeDetector != Action.None;
    }

    public Action getAction() {
        return swipeDetector;
    }

    public boolean onTouch(View v, MotionEvent event) {

        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                startX = event.getX();
                swipeDetector = Action.None;
                Toast.makeText(v.getContext(), "action down", Toast.LENGTH_SHORT).show();

                return false;
            }

            case MotionEvent.ACTION_MOVE: {
                finishX = event.getX();

                float deltaX = startX - finishX;

                if(Math.abs(deltaX) > MIN_DISTANCE) {
                    if(deltaX > 0) {
                        Toast.makeText(v.getContext(), "left to right swipe", Toast.LENGTH_SHORT).show();
                        swipeDetector = Action.LR;
                        return true;
                    }
                }
            }
        }

        return false;
    }

}
