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
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
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
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView mDrawerList;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private ActionBar mActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // setup action bar for tabs
        mActionBar = getActionBar();
        mActionBar.setDisplayShowTitleEnabled(false);
        mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

        mTitle = mDrawerTitle = getTitle();
        mPlanetTitles = getResources().getStringArray(R.array.navigation_items);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer icon to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
        ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                mActionBar.setTitle(mTitle);
                if (mTitle.equals("Places")) {
                    mActionBar.setDisplayShowTitleEnabled(false);
                    mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
                } else {
                    mActionBar.setDisplayShowTitleEnabled(true);
                    mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
                }
                invalidateOptionsMenu();
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                mActionBar.setDisplayShowTitleEnabled(true);
                mActionBar.setTitle(getResources().getString(R.string.app_name));
                mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
                invalidateOptionsMenu();
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setHomeButtonEnabled(true);
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

    /**
     * Swaps fragments in the main content view
     */
    private void selectItem(int position) {
        // Create a new fragment and specify the planet to show based on position
        Fragment fragment = null;
        PreferenceManager.getDefaultSharedPreferences(this)
                .edit().putInt("view_mode", position).apply();
        switch (position) {
            case 0:
                fragment = new CategoriesFragment();
                mActionBar.setDisplayShowTitleEnabled(false);
                mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
                break;
            case 1:
                fragment = new StoreListFragment();
                mActionBar.setDisplayShowTitleEnabled(true);
                mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
                break;
            case 2:
                fragment = new StoreListFragment();
                mActionBar.setDisplayShowTitleEnabled(true);
                mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
                break;
        }
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

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        if (menu.findItem(R.id.search) != null) {
            menu.findItem(R.id.search).setVisible(!drawerOpen);
        } else if (menu.findItem(R.id.action_map) != null) {
            menu.findItem(R.id.action_map).setVisible(!drawerOpen);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        mActionBar.setTitle(mTitle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }

    private void setupActionBarDropdown() {
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

        SpinnerAdapter adapter = new SpinnerAdapter(this,
                R.layout.actionbar_spinner,
                getResources().getStringArray(R.array.actionbar_spinner_actions));
        mActionBar.setListNavigationCallbacks(adapter, mOnNavigationListener);
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