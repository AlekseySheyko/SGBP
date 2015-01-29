package aleksey.sheyko.sgbp.utils.tasks;

import android.os.AsyncTask;

import aleksey.sheyko.sgbp.utils.helpers.Constants;
import aleksey.sheyko.sgbp.model.Store;

public class UpdateStoreList extends AsyncTask<Void, Void, Void> {
    public static final String TAG = UpdateStoreList.class.getSimpleName();

    @Override
    protected Void doInBackground(Void... params) {

        Store.deleteAll(Store.class);

        Store store1 = new Store(
                1,
                "AR Performance",
                "9055 Locust Street",
                "(916) 714-5277",
                "38.410609",
                "-121.364239",
                Constants.CATEGORY_SOUND);
        store1.save();

        Store store2 = new Store(
                2,
                "Import Garage",
                "9901 Kent Street",
                "(916) 686-2610",
                "38.398914",
                "-121.354938",
                Constants.CATEGORY_AUTO);
        store2.save();

        Store store3 = new Store(
                3,
                "Starbucks",
                "8868 Bond Road",
                "(916) 682-2140",
                "38.423125",
                "-121.373509",
                Constants.CATEGORY_FOOD);
        store3.save();

        Store store4 = new Store(
                4,
                "Thai Chilli",
                "8696 Elk Grove Blvd",
                "(916) 714-3519",
                "38.407893",
                "-121.381385",
                Constants.CATEGORY_FOOD);
        store4.save();

        Store store5 = new Store(
                5,
                "Maharani India Restaurant",
                "9583 Elk Grove Florin Rd",
                "",
                "38.409744",
                "-121.371114",
                Constants.CATEGORY_FOOD);
        store5.save();

        Store store6 = new Store(
                6,
                "Mike's Sound Solutions",
                "8948 Elk Grove Blvd",
                "",
                "38.408924",
                "-121.369121",
                Constants.CATEGORY_SOUND);
        store6.save();

        Store store7 = new Store(
                7,
                "Chuck E Cheese's",
                "9161 East Stockton Blvd",
                "(916) 293-9894",
                "38.425857",
                "-121.392306",
                Constants.CATEGORY_FOOD);
        store7.save();

        Store store8 = new Store(
                8,
                "La Fuente",
                "9631 East Stockton Blvd",
                "(916) 685-0503",
                "38.407338",
                "-121.384356",
                Constants.CATEGORY_HOTELS);
        store8.save();

        Store store17 = new Store(
                17,
                "O’Reilly Auto Parts",
                "8751 Elk grove Blvd",
                "(916) 685-1575",
                "38.409751",
                "-121.378927",
                Constants.CATEGORY_AUTO);
        store17.save();

        Store store18 = new Store(
                18,
                "Bubbles Car Wash",
                "8458 Bond Road",
                "(916) 714-8001",
                "38.423478",
                "-121.392038",
                Constants.CATEGORY_AUTO);
        store18.save();

        Store store19 = new Store(
                19,
                "Wion’s Body Shop",
                "3529 La Grande Blvd",
                "(916) 422-1564",
                "38.50766",
                "-121.467333",
                Constants.CATEGORY_BODY_CARE);
        store19.save();

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
