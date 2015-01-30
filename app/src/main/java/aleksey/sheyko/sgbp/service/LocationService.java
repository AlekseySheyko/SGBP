package aleksey.sheyko.sgbp.service;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.Geofence.Builder;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;

import aleksey.sheyko.sgbp.model.Store;
import aleksey.sheyko.sgbp.utils.tasks.UpdateStoreList;
import aleksey.sheyko.sgbp.utils.tasks.UpdateStoreList.OnStoreListLoaded;

public class LocationService extends Service
        implements LocationListener, ConnectionCallbacks, OnStoreListLoaded {

    protected static final String TAG = LocationService.class.getSimpleName();

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        createLocationClient();
        Toast.makeText(this, "Service started", Toast.LENGTH_SHORT).show();
        mGoogleApiClient.connect();
        return START_STICKY;
    }

    private void createLocationClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .build();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        startLocationUpdates();
        addGeofences();
    }

    private void addGeofences() {
        List<Geofence> mGeofenceList = new ArrayList<>();

        List<Store> stores = Store.listAll(Store.class);
        if (stores.size() == 0) {
            new UpdateStoreList(this).execute();
            return;
        }

        for (Store store : stores) {
            if (store.getGeofenceId() == null) {
                Geofence geofence = createGeofence(
                        "aleksey.sheyko.sgbp.GEOFENCE_" + store.getId(),
                        Double.parseDouble(store.getLatitude()),
                        Double.parseDouble(store.getLongitude())
                );
                mGeofenceList.add(geofence);
                String id = geofence.getRequestId();
                Log.i(TAG, "Geofence id: " + id);
                store.setGeofenceId(id);
                store.save();
            } else {
                Geofence geofence = createGeofence(store.getGeofenceId(),
                        Double.parseDouble(store.getLatitude()),
                        Double.parseDouble(store.getLongitude())
                );
                mGeofenceList.add(geofence);
                Log.i(TAG, "Geofence id: " + store.getGeofenceId());
            }
        }
        LocationServices.GeofencingApi.addGeofences(mGoogleApiClient, mGeofenceList, getPendingIntent())
                .setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        Log.i(TAG, "Add geofences status: " + status.getStatus());
                    }
                });
    }

    private Geofence createGeofence(String id, double latitude, double longitude) {
        Geofence geofence = new Builder()
                .setRequestId(id)
                .setCircularRegion(
                        latitude,
                        longitude,
                        300 // radius in meters (150)
                )
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setLoiteringDelay(1000) // 10 min (10 * 60 * 1000)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_DWELL)
                .build();
        return geofence;
    }

    private PendingIntent getPendingIntent() {
        Intent intent = new Intent().setClass(this, GeofenceService.class);
        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    protected void startLocationUpdates() {
        createLocationRequest();
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public void onConnectionSuspended(int cause) {
        Log.e(TAG, "Connection suspended. Cause: " + cause);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStoreListUpdated() {
        addGeofences();
    }
}
