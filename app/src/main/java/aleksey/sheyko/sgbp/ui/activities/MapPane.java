package aleksey.sheyko.sgbp.ui.activities;

import android.app.Activity;
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
import aleksey.sheyko.sgbp.models.Store;
import aleksey.sheyko.sgbp.ui.tasks.UpdateStoreList;

public class MapPane extends Activity implements OnMapReadyCallback {

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

        List<Store> stores = Store.listAll(Store.class);
        if (stores.size() == 0)
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
