package aleksey.sheyko.sgbp.fragments;

import android.app.ListFragment;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import aleksey.sheyko.sgbp.database.StoreReaderContract.StoreEntry;
import aleksey.sheyko.sgbp.database.StoreReaderDbHelper;

public class NearestFragment extends ListFragment {

    public NearestFragment() {
        String id = "1";
        String title = "AR Performance";
        String address = "9055 Locust Street";
        String phone = "(916) 714-5277";
        String latitude = "38.410609";
        String longitude = "-121.364239";
        String category = "Audio equipment";

        StoreReaderDbHelper mDbHelper = new StoreReaderDbHelper(getActivity());

        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(StoreEntry.COLUMN_NAME_STORE_ID, id);
        values.put(StoreEntry.COLUMN_NAME_NAME, title);
        values.put(StoreEntry.COLUMN_NAME_ADDRESS, address);
        values.put(StoreEntry.COLUMN_NAME_PHONE, phone);
        values.put(StoreEntry.COLUMN_NAME_LATITUDE, latitude);
        values.put(StoreEntry.COLUMN_NAME_LONGITUDE, longitude);
        values.put(StoreEntry.COLUMN_NAME_CATEGORY, category);

        // Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(
                StoreEntry.TABLE_NAME,
                null,
                values);
    }
}
