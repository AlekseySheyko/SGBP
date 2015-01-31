package aleksey.sheyko.sgbp.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

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
                new SubmitParticipationTask().execute();
                break;
            case Geofence.GEOFENCE_TRANSITION_EXIT:
                Log.i(TAG, "Прочь покатился");
                break;
        }
        // List<Geofence> geofences = event.getTriggeringGeofences();
    }
}
