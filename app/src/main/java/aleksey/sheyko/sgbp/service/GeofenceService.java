package aleksey.sheyko.sgbp.service;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.Geofence.Builder;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;

import aleksey.sheyko.sgbp.model.Store;

public class GeofenceService extends IntentService
        implements ConnectionCallbacks {

    private GoogleApiClient mGoogleApiClient;

    public GeofenceService() {
        super("GeofenceService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        createLocationClient();
        mGoogleApiClient.connect();
    }

    private void createLocationClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .build();
    }

    @Override
    public void onConnected(Bundle bundle) {
        addGeofences();
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    private void addGeofences() {
        List<Geofence> mGeofenceList = new ArrayList<>();
        List<Store> stores = Store.listAll(Store.class);
        for (Store store : stores) {
            if (store.getGeofenceId() == null) {
                Geofence geofence = createGeofence(
                        "aleksey.sheyko.sgbp.GEOFENCE_" + store.getId(),
                        Double.parseDouble(store.getLatitude()),
                        Double.parseDouble(store.getLongitude())
                );
                mGeofenceList.add(geofence);
                String id = geofence.getRequestId();
                store.setGeofenceId(id);
                store.save();
            } else {
                Geofence geofence = createGeofence(store.getGeofenceId(),
                        Double.parseDouble(store.getLatitude()),
                        Double.parseDouble(store.getLongitude())
                );
                mGeofenceList.add(geofence);
            }
        }
        LocationServices.GeofencingApi
                .addGeofences(mGoogleApiClient, mGeofenceList, getPendingIntent());
    }

    private Geofence createGeofence(String id, double latitude, double longitude) {
        Geofence geofence = new Builder()
                .setRequestId(id)
                .setCircularRegion(
                        latitude,
                        longitude,
                        100 // radius in meters
                )
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setLoiteringDelay(1000) // 10 min (10 * 60 * 1000)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_DWELL)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_EXIT)
                .build();
        return geofence;
    }

    private PendingIntent getPendingIntent() {
        Intent intent = new Intent().setClass(this, ResponseService.class);
        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
