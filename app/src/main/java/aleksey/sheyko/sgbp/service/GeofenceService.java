package aleksey.sheyko.sgbp.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class GeofenceService extends IntentService {
    public static final String TAG = GeofenceService.class.getSimpleName();

    public GeofenceService() {
        super("GeofenceReceiver");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Toast.makeText(this, "Йобана! Зашел, засранец!", Toast.LENGTH_SHORT).show();
        Log.i(TAG, "И в довершение хочется отметить");
    }
}
