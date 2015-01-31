package aleksey.sheyko.sgbp.utils.tasks;

import android.os.AsyncTask;
import android.util.Log;

public class SubmitParticipationTask extends AsyncTask<Void, Void, Void> {
    public static final String TAG = SubmitParticipationTask.class.getSimpleName();

    @Override
    protected Void doInBackground(Void... voids) {
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        Log.i(TAG, "Приходите еще");
    }
}
