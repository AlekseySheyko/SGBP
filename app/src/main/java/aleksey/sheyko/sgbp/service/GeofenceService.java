package aleksey.sheyko.sgbp.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.List;

public class GeofenceService extends IntentService {
    public static final String TAG = GeofenceService.class.getSimpleName();

    public GeofenceService() {
        super("GeofenceReceiver");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(TAG, "Intent handle request caught.");

        GeofencingEvent event = GeofencingEvent.fromIntent(intent);

        int transitionType = event.getGeofenceTransition();
        Log.i(TAG, "Transition type: " + transitionType);
        List<Geofence> geofences = event.getTriggeringGeofences();
        for (Geofence geofence : geofences) {
            Log.i(TAG, "Geofence id: " + geofence.getRequestId());
        }
    }
}
