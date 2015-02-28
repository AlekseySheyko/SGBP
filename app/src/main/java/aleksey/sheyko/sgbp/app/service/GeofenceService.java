package aleksey.sheyko.sgbp.app.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.List;

import aleksey.sheyko.sgbp.app.tasks.SubmitParticipationTask;

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
                Log.i(TAG, "Enters");
                break;
            case Geofence.GEOFENCE_TRANSITION_DWELL:
                List<Geofence> geofences = event.getTriggeringGeofences();
                for (Geofence geofence : geofences) {
                    String id = geofence.getRequestId();
                    new SubmitParticipationTask(this, id).execute();
                }
                break;
            case Geofence.GEOFENCE_TRANSITION_EXIT:
                Log.i(TAG, "Went away");
                break;
        }
        // List<Geofence> geofences = event.getTriggeringGeofences();
    }
}
