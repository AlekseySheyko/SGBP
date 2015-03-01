package aleksey.sheyko.sgbp.app.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if (getIntent() != null) {
            if (getIntent().hasExtra("latitude")) {
                mName = getIntent().getStringExtra("name");
                String address = getIntent().getStringExtra("address");
                String phone = getIntent().getStringExtra("phone");

                ((TextView) findViewById(R.id.address)).setText(address);
                ((TextView) findViewById(R.id.phone)).setText(phone);

                mLatitude = Double.parseDouble(
                        getIntent().getStringExtra("latitude"));
                mLongitude = Double.parseDouble(
                        getIntent().getStringExtra("longitude"));
            }
        }

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        GoogleMap map = mapFragment.getMap();
        map.getUiSettings().setAllGesturesEnabled(false);
        map.setMyLocationEnabled(false);
        map.getUiSettings().setZoomControlsEnabled(false);
        map.getUiSettings().setMyLocationButtonEnabled(false);
        map.setOnMapClickListener(new OnMapClickListener() {
            @Override public void onMapClick(LatLng latLng) {
                startActivity(new Intent(DetailActivity.this, MapPane.class)
                        .putExtra("name", mName)
                        .putExtra("latitude", mLatitude)
                        .putExtra("longitude", mLongitude)
                );
            }
        });
        map.addMarker(new MarkerOptions()
                .position(new LatLng(mLatitude, mLongitude))
                .title(mName)).showInfoWindow();
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLatitude, mLongitude), 13));
    }

    public void showMap(View view) {
    }

    public void call(View view) {
    }
}
