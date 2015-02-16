package aleksey.sheyko.sgbp.utils.tasks;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.Settings.Secure;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RegUserTask extends AsyncTask<String, Void, Void> {

    public static final String TAG = RegUserTask.class.getSimpleName();

    private Context mContext;

    public RegUserTask(Context context) {
        mContext = context;
    }

    @Override
    protected Void doInBackground(String... strings) {

        final String KEY = "123456";
        final String USER_TYPE = "1";
        final String IS_REGISTERED = "true";

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain JSON response
        String resultJsonStr;

        String id = getDeviceId();

        try {
            // Construct the URL for the query
            Uri.Builder builder = new Uri.Builder();

            builder.scheme("http")
                    .authority("test.sgbp.info")
                    .appendPath("sgbpwsjson.svc")
                    .appendPath("SaveTeamMemberInfo")
                    .appendQueryParameter("Key", KEY)
                    .appendQueryParameter("Device_Info_Id", id)
                    .appendQueryParameter("First_Name", strings[0])
                    .appendQueryParameter("Last_Name", strings[1])
                    .appendQueryParameter("Device_UID", id)
                    .appendQueryParameter("School_Id", strings[2])
                    .appendQueryParameter("User_Email", strings[3])
                    .appendQueryParameter("User_Type", USER_TYPE)
                    .appendQueryParameter("Has_Multiple_Child", strings[4])
                    .appendQueryParameter("Is_User_Registered", IS_REGISTERED)
                    .appendQueryParameter("Is_Coupon_Allowed", strings[5])
                    .appendQueryParameter("Is_Notification_Allowed", strings[6])
                    .appendQueryParameter("Is_Location_Service_Allowed", strings[7])
                    .appendQueryParameter("Is_User_Over_18_Year", strings[8])
                    .appendQueryParameter("Is_Device_Registered", IS_REGISTERED)
                    .appendQueryParameter("Device_Token_Registration_Id", id);

            String mUrlString = builder.build().toString();

            Log.i(TAG, "Service: SaveTeamMemberInfo,\n" +
                    "Query: " + java.net.URLDecoder.decode(mUrlString, "UTF-8"));

            URL mUrl = new URL(mUrlString);
            // Create the request and open the connection
            urlConnection = (HttpURLConnection) mUrl.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder buffer = new StringBuilder();
            if (inputStream == null) return null;

            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line).append("\n");
            }
            resultJsonStr = buffer.toString();

            Log.i(TAG, "Service: SaveTeamMemberInfo,\n" +
                    "Result: " + java.net.URLDecoder.decode(resultJsonStr, "UTF-8"));

//            try {
//                JSONObject mParseObject = new JSONObject(resultJsonStr);
//                JSONObject mServerResponseObject = mParseObject.getJSONObject("SaveTeamMemberInfoResult");
//
//                String mParseStatus = mServerResponseObject.getString("Status");
//                if (mParseStatus.equals("Success")) {
//                    mTripId = mServerResponseObject.getString("TripId");
//                }
//            } catch (JSONException e) {
//                Log.e(TAG, e.getMessage());
//            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(TAG, "Error closing stream", e);
                }
            }
        }
        return null;
    }

    public String getDeviceId() {
        return Secure.getString(mContext.getContentResolver(), Secure.ANDROID_ID);
    }
}
