package aleksey.sheyko.sgbp.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.List;

public class GeofenceReceiver extends BroadcastReceiver {
    String TAG = GeofenceReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        List<Geofence> geofences =
                GeofencingEvent.fromIntent(intent).getTriggeringGeofences();
        for (Geofence geofence : geofences) {
            String id = geofence.getRequestId();
            Log.i(TAG, "Triggered geofence id: " + id);
        }
    }
}
