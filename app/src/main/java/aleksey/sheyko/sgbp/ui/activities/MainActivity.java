package aleksey.sheyko.sgbp.ui.activities;

import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.app.FragmentTransaction;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.littlefluffytoys.littlefluffylocationlibrary.LocationInfo;
import com.littlefluffytoys.littlefluffylocationlibrary.LocationLibrary;
import com.littlefluffytoys.littlefluffylocationlibrary.LocationLibraryConstants;

import aleksey.sheyko.sgbp.R;
import aleksey.sheyko.sgbp.adapters.SpinnerAdapter;
import aleksey.sheyko.sgbp.ui.fragments.CategoriesFragment;
import aleksey.sheyko.sgbp.ui.fragments.StoreListFragment;


public class MainActivity extends FragmentActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            LocationLibrary.initialiseLibrary(getBaseContext(), 60 * 1000, 2 * 60 * 1000, "com.littlefluffytoys.littlefluffylocationlibrary");
        } catch (UnsupportedOperationException ex) {
            Log.d("TestApplication",
                    "UnsupportedOperationException thrown  " +
                            "the device doesn't have any location providers");
        }

        // setup action bar for tabs
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

        OnNavigationListener mOnNavigationListener = new OnNavigationListener() {
            // Get the same strings provided for the drop-down's ArrayAdapter
            String[] strings = getResources().getStringArray(R.array.actionbar_spinner_actions);

            @Override
            public boolean onNavigationItemSelected(int position, long itemId) {
                // Create new fragment from our own Fragment class
                CategoriesFragment firstFragment = new CategoriesFragment();
                StoreListFragment secondFragment = new StoreListFragment();

                FragmentTransaction ft = getFragmentManager().beginTransaction();

                // Replace whatever is in the fragment container with this fragment
                // and give the fragment a tag name equal to the string at the position
                // selected
                switch (position) {
                    case 0:
                        ft.replace(R.id.fragment_container, firstFragment, strings[position]);
                        break;
                    case 1:
                        ft.replace(R.id.fragment_container, secondFragment, strings[position]);
                        break;
                }
                // Apply changes
                ft.commit();
                return true;
            }
        };

        actionBar.setListNavigationCallbacks(
                new SpinnerAdapter(this,
                        R.layout.actionbar_spinner,
                        getResources().getStringArray(R.array.actionbar_spinner_actions)),
                mOnNavigationListener);
    }

    @Override
    public void onResume() {
        super.onResume();

        // cancel any notification we may have received from TestBroadcastReceiver
        ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).cancel(1234);

        refreshDisplay();

        // This demonstrates how to dynamically create a receiver to listen to the location updates.
        // You could also register a receiver in your manifest.
        final IntentFilter lftIntentFilter = new IntentFilter(LocationLibraryConstants.getLocationChangedPeriodicBroadcastAction());
        registerReceiver(lftBroadcastReceiver, lftIntentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();

        unregisterReceiver(lftBroadcastReceiver);
    }

    private void refreshDisplay() {
        refreshDisplay(new LocationInfo(this));
    }

    private void refreshDisplay(final LocationInfo locationInfo) {
        Log.i(TAG, LocationInfo.formatTimeAndDay(locationInfo.lastLocationUpdateTimestamp, true));
        Log.i(TAG, Float.toString(locationInfo.lastLat));
        Log.i(TAG, Float.toString(locationInfo.lastLong));
        Log.i(TAG, Integer.toString(locationInfo.lastAccuracy) + "m");
        Log.i(TAG, locationInfo.lastProvider);
    }

    private final BroadcastReceiver lftBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // extract the location info in the broadcast
            final LocationInfo locationInfo = (LocationInfo) intent.getSerializableExtra(LocationLibraryConstants.LOCATION_BROADCAST_EXTRA_LOCATIONINFO);
            // refresh the display with it
            refreshDisplay(locationInfo);
        }
    };
}