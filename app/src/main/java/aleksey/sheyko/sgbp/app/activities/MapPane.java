package aleksey.sheyko.sgbp.app.activities;

import android.app.Activity;
import android.app.SearchManager;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.InputStream;
import java.util.List;

import aleksey.sheyko.sgbp.R;
import aleksey.sheyko.sgbp.model.Store;
import aleksey.sheyko.sgbp.rest.ApiService;
import aleksey.sheyko.sgbp.rest.RestClient;
import aleksey.sheyko.sgbp.rest.StoresXmlParser;
import retrofit.ResponseCallback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MapPane extends Activity {

    private String mCategory;
    private String mSearchQuery;
    private GoogleMap mMap;
    private List<Store> mStores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mMap = mapFragment.getMap();
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);

        if (getIntent() != null) {
            if (getIntent().hasExtra("category")) {
                mCategory = getIntent().getStringExtra("category");
            } else if (getIntent().hasExtra(SearchManager.QUERY)) {
                mSearchQuery = getIntent().getStringExtra(SearchManager.QUERY);
            } else {
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
                String name = sharedPrefs.getString("name", "");
                Double latitude = Double.parseDouble(
                        sharedPrefs.getString("latitude", ""));
                Double longitude = Double.parseDouble(
                        sharedPrefs.getString("longitude", ""));
                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(latitude, longitude))
                        .title(name)).showInfoWindow();
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 13));
                return;
            }
        }

        if (mCategory != null) {
            mStores = Store.find(Store.class, "category = ?", mCategory);
        } else if (mSearchQuery != null) {
            mStores = Store.findWithQuery(Store.class,
                    "Select * from Store where name like '%" + mSearchQuery + "%'");
        } else {
            mStores = Store.listAll(Store.class);
        }

        if (mStores.size() == 0 && mSearchQuery == null) {
            setProgressBarIndeterminateVisibility(true);
            ApiService service = new RestClient().getApiService();
            service.listStores(new ResponseCallback() {
                @Override public void success(Response response) {
                    try (InputStream in = response.getBody().in()) {
                        StoresXmlParser storesXmlParser = new StoresXmlParser();
                        storesXmlParser.parse(in);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    mStores = Store.listAll(Store.class);
                    addMarkers();
                    setProgressBarIndeterminateVisibility(false);
                }

                @Override public void failure(RetrofitError e) {
                    e.printStackTrace();
                }
            });
            return;
        }
        addMarkers();
    }

    private void addMarkers() {
        final LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Store store : mStores) {
            Double latitude = Double.parseDouble(store.getLatitude());
            Double longitude = Double.parseDouble(store.getLongitude());
            if (latitude > 35 && latitude < 50) {
                Marker marker = mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(latitude, longitude))
                        .title(store.getName()));

                builder.include(marker.getPosition());
            }
        }
        final int PADDING = 100; // offset from edges of the map in pixels
        mMap.setOnCameraChangeListener(new OnCameraChangeListener() {

            @Override
            public void onCameraChange(CameraPosition arg0) {
                // Move camera
                mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), PADDING));
                // Remove listener to prevent position reset on camera move
                mMap.setOnCameraChangeListener(null);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (item.getItemId() == R.id.action_location) {
            Location myLocation = mMap.getMyLocation();
            if (myLocation != null) {
                mMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(
                        myLocation.getLatitude(), myLocation.getLongitude())));
            } else {
                Toast.makeText(this, "Please enable GPS", Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        return false;
    }

}
