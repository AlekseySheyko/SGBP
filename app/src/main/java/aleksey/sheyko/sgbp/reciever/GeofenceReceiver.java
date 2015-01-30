package aleksey.sheyko.sgbp.reciever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingEvent;

public class GeofenceReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (GeofencingEvent.fromIntent(intent).getGeofenceTransition()
                == GeofenceStatusCodes.D;
    }
}
