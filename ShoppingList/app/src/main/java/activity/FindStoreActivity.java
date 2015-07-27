package activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import com.example.rh035578.shoppinglist.R;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class FindStoreActivity extends Activity {
    private GoogleMap storeMap;
    private LatLng myPoint;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);
        setContentView(R.layout.activity_find_store);

        mGoogleApiClient = new GoogleApiClient
                                .Builder(this)
                                .addApiIfAvailable(Places.GEO_DATA_API)
                                .addApiIfAvailable(Places.PLACE_DETECTION_API)
                                .build();

        try {
            if(storeMap == null) {
                storeMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.findStore)).getMap();
            }

            setMyLocation();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    private void setMyLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, true);

        //getting current location
        Location myLocation = locationManager.getLastKnownLocation(provider);
        double longitude = myLocation.getLongitude();
        double latitude = myLocation.getLatitude();
        myPoint = new LatLng(latitude, longitude);

        //adjusting map view
        storeMap.moveCamera(CameraUpdateFactory.newLatLng(myPoint));
        storeMap.animateCamera(CameraUpdateFactory.zoomTo(14));
        storeMap.setMyLocationEnabled(true);
        storeMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        storeMap.addMarker(new MarkerOptions().position(myPoint).title("My Location"));
    }
}
