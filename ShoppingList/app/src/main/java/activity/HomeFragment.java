package activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.content.pm.PackageManager;
import com.example.rh035578.shoppinglist.R;

public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        Button viewDB = (Button) rootView.findViewById(R.id.viewDB);
        viewDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewDB();
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

    //logged issue to open file directly in the app
    //instead of launching Dropbox app
    public void viewDB() {

        //launches Dropbox app
        Intent intent = getActivity().getPackageManager().getLaunchIntentForPackage("com.dropbox.android");
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        startActivity(intent);

    }
}
