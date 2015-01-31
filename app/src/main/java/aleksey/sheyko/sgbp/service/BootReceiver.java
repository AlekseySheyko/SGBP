package aleksey.sheyko.sgbp.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent receiverIntent) {
        Intent intent = new Intent("aleksey.sheyko.sgbp.service.LocationService");
        intent.setClass(context, LocationService.class);
        context.startService(intent);
    }
}
