package aleksey.sheyko.sgbp.utils.tasks;

import android.app.NotificationManager;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;

import aleksey.sheyko.sgbp.R;
import aleksey.sheyko.sgbp.model.Store;

public class SubmitParticipationTask extends AsyncTask<Void, Void, Void> {

    private Context mContext;
    private String mId;

    public SubmitParticipationTask(Context context, String id) {
        mContext = context;
        mId = id;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        Store store = Store.find(Store.class, "geofence_id = ?", mId).get(0);
        showNotification(store.getName());
    }

    private void showNotification(String name) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(mContext)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("Thanks for visiting")
                        .setContentText(name + " participation accepted");
        mBuilder.setAutoCancel(true);
        // Sets an ID for the notification
        int mNotificationId = 123;
        // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        // Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }
}
