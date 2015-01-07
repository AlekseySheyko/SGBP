package aleksey.sheyko.sgbp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import aleksey.sheyko.sgbp.database.StoreContract.StoreEntry;
import aleksey.sheyko.sgbp.models.Store;

public class StoreDataSource {

    private StoreSqliteHelper mDbHelper;

    public StoreDataSource(Context context) {
        mDbHelper = new StoreSqliteHelper(context);
        SQLiteDatabase database = mDbHelper.getReadableDatabase();
        database.close();
    }

    private SQLiteDatabase open() {
        return mDbHelper.getWritableDatabase();
    }

    private void close(SQLiteDatabase database) {
        database.close();
    }

    public void create(Store store) {
        SQLiteDatabase database = open();
        database.beginTransaction();

        ContentValues storeValues = new ContentValues();
        storeValues.put(StoreEntry.COLUMN_NAME_STORE_ID, store.getId());
        storeValues.put(StoreEntry.COLUMN_NAME_NAME, store.getName());
        storeValues.put(StoreEntry.COLUMN_NAME_ADDRESS, store.getAddress());
        storeValues.put(StoreEntry.COLUMN_NAME_PHONE, store.getPhone());
        storeValues.put(StoreEntry.COLUMN_NAME_LATITUDE, store.getLatitude());
        storeValues.put(StoreEntry.COLUMN_NAME_LONGITUDE, store.getLongitude());
        storeValues.put(StoreEntry.COLUMN_NAME_CATEGORY, store.getCategory());
        long storeId = database.insert(
                StoreEntry.TABLE_NAME,
                null,
                storeValues);
        Log.i("StoreDataSource", "Row #" + storeId + " inserted");

        database.setTransactionSuccessful();
        database.endTransaction();
        close(database);
    }
}
