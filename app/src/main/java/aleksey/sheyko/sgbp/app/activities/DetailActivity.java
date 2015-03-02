package aleksey.sheyko.sgbp.app.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import aleksey.sheyko.sgbp.R;

public class DetailActivity extends Activity {

    private SharedPreferences mSharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String name = mSharedPrefs.getString("name", "");
        String address = mSharedPrefs.getString("address", "");
        String phone = mSharedPrefs.getString("phone", "");
        final double latitude = Double.parseDouble(mSharedPrefs.getString("latitude", ""));
        final double longitude = Double.parseDouble(mSharedPrefs.getString("longitude", ""));
        boolean isMobile = mSharedPrefs.getBoolean("isMobile", false);

        Button actionButton = (Button) findViewById(R.id.button);
        if (isMobile) {
            actionButton.setText("Participate");

        } else {
            actionButton.setText("Make route");
            actionButton.setOnClickListener(new OnClickListener() {
                @Override public void onClick(View view) {
                    startActivity(new Intent(android.content.Intent.ACTION_VIEW,
                            Uri.parse("http://maps.google.com/maps?daddr=" + latitude + "," + longitude)));
                }
            });
        }

        ((TextView) findViewById(R.id.address)).setText(address);
        ((TextView) findViewById(R.id.phone)).setText(phone);

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
                .position(new LatLng(latitude, longitude))
                .title(name)).showInfoWindow();
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 13));


        if (address.isEmpty()) {
            findViewById(R.id.address_container).setVisibility(View.GONE);
        }
        if (phone.isEmpty()) {
            findViewById(R.id.phone_container).setVisibility(View.GONE);
        }
    }

    private void navigateToMap() {
        String name = mSharedPrefs.getString("name", "");
        double latitude = Double.parseDouble(mSharedPrefs.getString("latitude", ""));
        double longitude = Double.parseDouble(mSharedPrefs.getString("longitude", ""));

        startActivity(new Intent(DetailActivity.this, MapPane.class)
                .putExtra("name", name)
                .putExtra("latitude", latitude)
                .putExtra("longitude", longitude));
    }

    public void showMap(View view) {
        navigateToMap();
    }

    public void dial(View view) {
        String phone = mSharedPrefs.getString("phone", "");
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phone.replaceAll("[^0-9]", "")));
        startActivity(intent);
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
        }
        return true;
    }
}
