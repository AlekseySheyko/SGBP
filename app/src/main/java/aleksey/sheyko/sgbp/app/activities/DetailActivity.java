package aleksey.sheyko.sgbp.app.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import aleksey.sheyko.sgbp.R;

public class DetailActivity extends Activity {

    private double mLatitude;
    private double mLongitude;
    private String mName;
    private String mPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        mName = sharedPrefs.getString("name", "");
        String address = sharedPrefs.getString("address", "");
        mPhone = sharedPrefs.getString("phone", "");

        ((TextView) findViewById(R.id.address)).setText(address);
        ((TextView) findViewById(R.id.phone)).setText(mPhone);

        mLatitude = Double.parseDouble(
                sharedPrefs.getString("latitude", ""));
        mLongitude = Double.parseDouble(
                sharedPrefs.getString("longitude", ""));

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        GoogleMap map = mapFragment.getMap();
        map.getUiSettings().setAllGesturesEnabled(false);
        map.setMyLocationEnabled(false);
        map.getUiSettings().setZoomControlsEnabled(false);
        map.getUiSettings().setMyLocationButtonEnabled(false);
        map.setOnMapClickListener(new OnMapClickListener() {
            @Override public void onMapClick(LatLng latLng) {
                navigateToMap();
            }
        });
        map.addMarker(new MarkerOptions()
                .position(new LatLng(mLatitude, mLongitude))
                .title(mName)).showInfoWindow();
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLatitude, mLongitude), 13));
    }

    private void navigateToMap() {
        startActivity(new Intent(DetailActivity.this, MapPane.class)
                        .putExtra("name", mName)
                        .putExtra("latitude", mLatitude)
                        .putExtra("longitude", mLongitude)
        );
    }

    public void showMap(View view) {
        navigateToMap();
    }

    public void dial(View view) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + mPhone.replaceAll("[^0-9]", "")));
        startActivity(intent);
    }
}
