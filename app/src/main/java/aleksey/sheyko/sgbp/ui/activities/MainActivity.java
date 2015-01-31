package aleksey.sheyko.sgbp.ui.activities;

import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import aleksey.sheyko.sgbp.R;
import aleksey.sheyko.sgbp.service.LocationService;
import aleksey.sheyko.sgbp.ui.fragments.CategoriesFragment;
import aleksey.sheyko.sgbp.ui.fragments.StoreListFragment;
import aleksey.sheyko.sgbp.utils.helpers.adapters.SpinnerAdapter;


public class MainActivity extends FragmentActivity {

    private String[] mPlanetTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPlanetTitles = getResources().getStringArray(R.array.navigation_items);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // Set the adapter for the list view
        mDrawerList.setAdapter(new ArrayAdapter<>(this,
                R.layout.drawer_list_item, mPlanetTitles));
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        setupActionBarDropdown();
        if (!isServiceRunning(LocationService.class)) {
            startService(new Intent(this, LocationService.class));
        }
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    /** Swaps fragments in the main content view */
    private void selectItem(int position) {
        // Create a new fragment and specify the planet to show based on position
        Fragment fragment = new PlanetFragment();
        Bundle args = new Bundle();
        args.putInt(PlanetFragment.ARG_PLANET_NUMBER, position);
        fragment.setArguments(args);

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();

        // Highlight the selected item, update the title, and close the drawer
        mDrawerList.setItemChecked(position, true);
        setTitle(mPlanetTitles[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    private void setupActionBarDropdown() {
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

                // Replace whatever is in the fragment container with this fragment and give
                // the fragment a tag name equal to the string at the position selected
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

    private boolean isServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}