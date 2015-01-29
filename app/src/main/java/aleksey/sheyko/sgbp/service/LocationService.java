package aleksey.sheyko.sgbp.service;

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

public class LocationService extends Service
        implements LocationListener, ConnectionCallbacks {

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

        List<Store> stores = Store.listAll(Store.class);
        List<Geofence> mGeofenceList = new ArrayList<>();
        for (Store store : stores) {
            if (store.getGeofenceId() == null) {
                Geofence geofence = new Builder()
                        .setExpirationDuration(Geofence.NEVER_EXPIRE)
                        .setCircularRegion(
                                Double.parseDouble(store.getLatitude()),
                                Double.parseDouble(store.getLongitude()),
                                300
                        )
                        .build();
                mGeofenceList.add(geofence);
                String id = geofence.getRequestId();
                store.setGeofenceId(id);
                store.save();
            } else {
                // TODO: Set geofence with existing id from database
            }
        }
        // TODO: Set resolver for pending intent (as the trip parameter)
        LocationServices.GeofencingApi.addGeofences(mGoogleApiClient, mGeofenceList, null)
                .setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        Log.i(TAG, "Geofencing status: " + status.getStatus());
                    }
                });
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
}
