package aleksey.sheyko.sgbp.app.service;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.Geofence.Builder;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import aleksey.sheyko.sgbp.model.Store;
import aleksey.sheyko.sgbp.rest.ApiService;
import aleksey.sheyko.sgbp.rest.RestClient;
import aleksey.sheyko.sgbp.model.StoresXmlParser;
import retrofit.ResponseCallback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class LocationService extends Service
        implements LocationListener, ConnectionCallbacks {

    protected static final String TAG = LocationService.class.getSimpleName();

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private List<Store> mStores;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        createLocationClient();
        mGoogleApiClient.connect();
        return START_STICKY;
    }

    private synchronized void createLocationClient() {
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

        mStores = Store.listAll(Store.class);
        if (mStores.size() == 0) {
            ApiService service = new RestClient().getApiService();
            service.listAllStores(new ResponseCallback() {
                @Override public void success(Response response) {
                    try {
                        InputStream in = response.getBody().in();
                        StoresXmlParser storesXmlParser = new StoresXmlParser();
                        storesXmlParser.parse(in);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    mStores = Store.listAll(Store.class);
                    addGeofences();
                }

                @Override public void failure(RetrofitError e) {
                    e.printStackTrace();
                }
            });
            return;
        }

        for (Store store : mStores) {
            if (store.getGeofenceId() == null) {
                try {
                    Geofence geofence = createGeofence(
                            "aleksey.sheyko.sgbp.GEOFENCE_" + store.getId(),
                            Double.parseDouble(store.getLatitude()),
                            Double.parseDouble(store.getLongitude()),
                            store.getParticipateDistance());
                    mGeofenceList.add(geofence);
                    String id = geofence.getRequestId();
                    store.setGeofenceId(id);
                    store.save();
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            } else {
                Geofence geofence = createGeofence(store.getGeofenceId(),
                        Double.parseDouble(store.getLatitude()),
                        Double.parseDouble(store.getLongitude()),
                        store.getParticipateDistance()
                );
                mGeofenceList.add(geofence);
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

    private Geofence createGeofence(String id, double latitude, double longitude, int participateDistance) {
        Geofence geofence = new Builder()
                .setRequestId(id)
                .setCircularRegion(
                        latitude,
                        longitude,
                        participateDistance
                )
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setLoiteringDelay(5 * 60 * 1000) // 5 min
                .setTransitionTypes(
                        Geofence.GEOFENCE_TRANSITION_ENTER |
                        Geofence.GEOFENCE_TRANSITION_DWELL |
                        Geofence.GEOFENCE_TRANSITION_EXIT)
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
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
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
