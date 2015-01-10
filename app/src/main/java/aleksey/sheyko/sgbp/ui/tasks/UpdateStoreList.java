package aleksey.sheyko.sgbp.ui.tasks;

import android.os.AsyncTask;

import aleksey.sheyko.sgbp.models.Store;

public class UpdateStoreList extends AsyncTask <Void, Void, Void> {
    public static final String TAG = UpdateStoreList.class.getSimpleName();

    @Override
    protected Void doInBackground(Void... params) {

        Store.deleteAll(Store.class);

        Store store1 = new Store(1,
                    "AR Performance",
                    "9055 Locust Street",
                    "(916) 714-5277",
                    "38.410609",
                    "-121.364239",
                    "Sound systems");

        Store store2 = new Store(2,
                "Import Garage",
                "9901 Kent Street",
                "(916) 686-2610",
                "38.398914",
                "-121.354938",
                "Auto");

        store1.save();
        store2.save();

//        HttpURLConnection urlConnection = null;
//        BufferedReader reader = null;
//
//        // Will contain JSON responses as a string
//        String updateTripJsonResponse;
//
//        try {
//            // Construct the URL for the query
//            Uri.Builder builder = new Uri.Builder();
//            builder.scheme("http")
//                    .authority("wsapp.mapthetrip.com")
//                    .appendPath("TrucFuelLog.svc")
//                    .appendPath("TFLUpdateTripStatus")
//                    .appendQueryParameter("UserId", "");
//            String mUrlString = builder.build().toString();
//
//            Log.i(TAG, "Service: TFLUpdateTripStatus,\n" +
//                    "Query: " + java.net.URLDecoder.decode(mUrlString, "UTF-8"));
//
//            URL mUrl = new URL(mUrlString);
//
//            // Create the request and open the connection
//            urlConnection = (HttpURLConnection) mUrl.openConnection();
//            urlConnection.setRequestMethod("GET");
//            urlConnection.connect();
//
//            // Read the input stream into a String
//            InputStream inputStream = urlConnection.getInputStream();
//            StringBuffer buffer = new StringBuffer();
//            if (inputStream == null) {
//                // Nothing to do.
//                return null;
//            }
//            reader = new BufferedReader(new InputStreamReader(inputStream));
//
////            String line;
////            while ((line = reader.readLine()) != null) {
////                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
////                // But it does make debugging a *lot* easier if you print out the completed
////                // buffer for debugging.
////                buffer.append(line + "\n");
////            }
//
//            JSONObject jsonBody = new JSONObject(buffer.toString());
//
//            jsonBody.getJSONObject("Store_info");
//
//
//            updateTripJsonResponse = buffer.toString();
//            Log.i(TAG, "Service: TFLUpdateTripStatus " + "(" + params[1] + " trip)" + ",\n" +
//                    "Result: " + java.net.URLDecoder.decode(updateTripJsonResponse, "UTF-8"));
//        } catch (IOException e) {
//            Log.e(TAG, "Error ", e);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        } finally{
//            if (urlConnection != null) {
//                urlConnection.disconnect();
//            }
//            if (reader != null) {
//                try {
//                    reader.close();
//                } catch (final IOException e) {
//                    Log.e(TAG, "Error closing stream", e);
//                }
//            }
//        }
        return null;
    }
}
