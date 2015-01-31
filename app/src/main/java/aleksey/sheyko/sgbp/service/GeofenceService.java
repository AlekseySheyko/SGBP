package aleksey.sheyko.sgbp.service;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import aleksey.sheyko.sgbp.R;
import aleksey.sheyko.sgbp.utils.tasks.SubmitParticipationTask;

public class GeofenceService extends IntentService {
    public static final String TAG = GeofenceService.class.getSimpleName();

    public GeofenceService() {
        super("GeofenceReceiver");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        GeofencingEvent event = GeofencingEvent.fromIntent(intent);

        int transitionType = event.getGeofenceTransition();
        switch (transitionType) {
            case Geofence.GEOFENCE_TRANSITION_ENTER:
                Log.i(TAG, "Защоль");
                break;
            case Geofence.GEOFENCE_TRANSITION_DWELL:
                Log.i(TAG, "Скупилься");
                showNotification();
                new SubmitParticipationTask().execute();
                break;
            case Geofence.GEOFENCE_TRANSITION_EXIT:
                Log.i(TAG, "Прочь покатился");
                break;
        }
        // List<Geofence> geofences = event.getTriggeringGeofences();
    }

    private void showNotification() {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("Thanks for visiting")
                        .setContentText("Elk Grove participation accepted");
        mBuilder.setAutoCancel(true);
        // Sets an ID for the notification
        int mNotificationId = 123;
        // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }
}
