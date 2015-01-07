package aleksey.sheyko.sgbp.database;

import android.provider.BaseColumns;

public final class StoreReaderContract {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public StoreReaderContract() {}

    /* Inner class that defines the table contents */
    public static abstract class StoreEntry implements BaseColumns {
        public static final String TABLE_NAME = "stores";
        public static final String COLUMN_NAME_STORE_ID = "storeid";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_ADDRESS = "address";
        public static final String COLUMN_NAME_PHONE = "phone";
        public static final String COLUMN_NAME_LATITUDE = "latitude";
        public static final String COLUMN_NAME_LONGITUDE = "longitude";
        public static final String COLUMN_NAME_CATEGORY = "category";
    }
}
