package aleksey.sheyko.sgbp.ui.activities;

import android.app.Activity;
import android.app.SearchManager;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import aleksey.sheyko.sgbp.R;
import aleksey.sheyko.sgbp.model.Store;
import aleksey.sheyko.sgbp.utils.tasks.UpdateStoreList;

public class MapPane extends Activity implements OnMapReadyCallback {

    private String mCategory;
    private String mSearchQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(final GoogleMap map) {
        map.setMyLocationEnabled(true);

        if (getIntent() != null) {
            if (getIntent().hasExtra("category")) {
                mCategory = getIntent().getStringExtra("category");
            } else if (getIntent().hasExtra(SearchManager.QUERY)) {
                mSearchQuery = getIntent().getStringExtra(SearchManager.QUERY);
            }
        }

        List<Store> stores;
        if (mCategory != null) {
            stores = Store.find(Store.class, "category = ?", mCategory);
        } else if (mSearchQuery != null) {
            stores = Store.findWithQuery(Store.class,
                    "Select * from Store where name like '%" + mSearchQuery + "%'");
        } else {
            stores = Store.listAll(Store.class);
        }

        if (stores.size() == 0 && mSearchQuery == null)
            new UpdateStoreList().execute();

        final LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Store store : stores) {
            Marker marker = map.addMarker(new MarkerOptions()
                    .position(new LatLng(
                            Double.parseDouble(store.getLatitude()),
                            Double.parseDouble(store.getLongitude())))
                    .title(store.getName()));

            builder.include(marker.getPosition());
        }
        final int PADDING = 100; // offset from edges of the map in pixels
        map.setOnCameraChangeListener(new OnCameraChangeListener() {

            @Override
            public void onCameraChange(CameraPosition arg0) {
                // Move camera
                map.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), PADDING));
                // Remove listener to prevent position reset on camera move
                map.setOnCameraChangeListener(null);
            }
        });
    }


}
