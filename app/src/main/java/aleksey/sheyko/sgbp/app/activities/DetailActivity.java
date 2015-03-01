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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        GoogleMap map = mapFragment.getMap();
        map.getUiSettings().setAllGesturesEnabled(false);
        map.setMyLocationEnabled(false);
        map.getUiSettings().setZoomControlsEnabled(false);
        map.getUiSettings().setMyLocationButtonEnabled(false);
        map.setOnMapClickListener(new OnMapClickListener() {
            @Override public void onMapClick(LatLng latLng) {
                startActivity(new Intent(DetailActivity.this, MapPane.class));
            }
        });

        if (getIntent() != null) {
            if (getIntent().hasExtra("latitude")) {
                String name = getIntent().getStringExtra("name");
                (TextView) findViewById(R.id.)

                Double latitude = Double.parseDouble(
                        getIntent().getStringExtra("latitude"));
                Double longitude = Double.parseDouble(
                        getIntent().getStringExtra("longitude"));
                map.addMarker(new MarkerOptions()
                        .position(new LatLng(latitude, longitude))
                        .title(name)).showInfoWindow();
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 13));
            }
        }
    }

    public void showMap(View view) {
    }

    public void call(View view) {
    }
}
