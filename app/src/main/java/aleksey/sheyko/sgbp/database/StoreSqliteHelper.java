package aleksey.sheyko.sgbp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import aleksey.sheyko.sgbp.database.StoreContract.StoreEntry;

public class StoreSqliteHelper extends SQLiteOpenHelper {
    // Top-level database info
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "StoreList.db";
    // Main table functionality
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + StoreEntry.TABLE_NAME + " (" +
                    StoreEntry._ID + " INTEGER PRIMARY KEY," +
                    StoreEntry.COLUMN_NAME_STORE_ID + TEXT_TYPE + COMMA_SEP +
                    StoreEntry.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                    StoreEntry.COLUMN_NAME_ADDRESS + TEXT_TYPE + COMMA_SEP +
                    StoreEntry.COLUMN_NAME_PHONE + TEXT_TYPE + COMMA_SEP +
                    StoreEntry.COLUMN_NAME_LATITUDE + TEXT_TYPE + COMMA_SEP +
                    StoreEntry.COLUMN_NAME_LONGITUDE + TEXT_TYPE + COMMA_SEP +
                    StoreEntry.COLUMN_NAME_CATEGORY + TEXT_TYPE +
                    " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + StoreEntry.TABLE_NAME;


    public StoreSqliteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
