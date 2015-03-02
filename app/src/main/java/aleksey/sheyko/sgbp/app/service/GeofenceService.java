package aleksey.sheyko.sgbp.app.service;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import aleksey.sheyko.sgbp.R;
import aleksey.sheyko.sgbp.app.helpers.Constants;
import aleksey.sheyko.sgbp.model.Notification;
import aleksey.sheyko.sgbp.model.Store;
import aleksey.sheyko.sgbp.rest.ApiService;
import aleksey.sheyko.sgbp.rest.RestClient;
import retrofit.ResponseCallback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class GeofenceService extends IntentService {
    public static final String TAG = GeofenceService.class.getSimpleName();

    public GeofenceService() {
        super("GeofenceReceiver");
        Log.i(TAG, "Geofence service started");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        GeofencingEvent event = GeofencingEvent.fromIntent(intent);
        Log.i(TAG, "Geofence triggered");

        int transitionType = event.getGeofenceTransition();
        switch (transitionType) {
            case Geofence.GEOFENCE_TRANSITION_ENTER:
                Log.i(TAG, "Enters");
                break;
            case Geofence.GEOFENCE_TRANSITION_DWELL:
                List<Geofence> geofences = event.getTriggeringGeofences();
                for (Geofence geofence : geofences) {
                    String id = geofence.getRequestId();
                    String dateTime = new SimpleDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'").format(new Date());

                    SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
                    String deviceId = sharedPrefs.getString("device_id", "");
                    int userId = sharedPrefs.getInt("user_id", -1);
                    int schoolId = sharedPrefs.getInt("school_id", -1);

                    final Store store = Store.find(Store.class, "geofence_id = ?", id).get(0);
                    int storeId = store.getStoreid();
                    boolean isMobile;
                    isMobile = store.getCategory().equals(Constants.CATEGORY_MOBILE);

                    ApiService service = new RestClient().getApiService();
                    service.participate(userId + "", userId, deviceId, schoolId, storeId, dateTime, isMobile, new ResponseCallback() {
                        @Override public void success(Response response) {
                            showNotification(store.getName());
                            new Notification(store.getName(), getCurrentTime()).save();
                        }

                        @Override public void failure(RetrofitError e) {
                            e.printStackTrace();
                        }
                    });
                }
                break;
            case Geofence.GEOFENCE_TRANSITION_EXIT:
                Log.i(TAG, "Went away");
                break;
        }
        // List<Geofence> geofences = event.getTriggeringGeofences();
    }

    private void showNotification(String name) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("Thanks for visiting")
                        .setContentText(name + " participation accepted");
        mBuilder.setAutoCancel(true);
        // Sets an ID for the notification
        int mNotificationId = 123;
        // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }

    public String getCurrentTime() {
        return new SimpleDateFormat("dd MMM, hh:mm a", Locale.US)
                .format(new Date());
    }
}
