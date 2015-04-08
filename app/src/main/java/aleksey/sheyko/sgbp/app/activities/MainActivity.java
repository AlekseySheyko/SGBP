package aleksey.sheyko.sgbp.app.activities;

import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import aleksey.sheyko.sgbp.R;
import aleksey.sheyko.sgbp.app.adapters.SpinnerAdapter;
import aleksey.sheyko.sgbp.app.fragments.AboutFragment;
import aleksey.sheyko.sgbp.app.fragments.AccountFragment;
import aleksey.sheyko.sgbp.app.fragments.CategoriesFragment;
import aleksey.sheyko.sgbp.app.fragments.StoreListFragment;
import aleksey.sheyko.sgbp.app.helpers.Constants;
import aleksey.sheyko.sgbp.app.service.LocationService;


public class MainActivity extends FragmentActivity {

    // TODO: Fix search menu item configuration

    private String[] mNavItems;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView mDrawerList;
    private CharSequence mTitle;
    private ActionBar mActionBar;
    private SharedPreferences mSharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_main);

        // Setup action bar for tabs
        mActionBar = getActionBar();
        mActionBar.setDisplayShowTitleEnabled(false);
        mActionBar.setIcon(
                new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

        mTitle = getTitle();
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
                hideKeyboard();
            }

            private void hideKeyboard() {
                InputMethodManager imm = (InputMethodManager)
                        getSystemService(Service.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(
                        getWindow().getDecorView().getRootView().getWindowToken(), 0);
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setHomeButtonEnabled(true);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mNavItems = getResources().getStringArray(R.array.navigation_items);

        // Set the adapter for the list view
        mDrawerList.setAdapter(new ArrayAdapter<>(this,
                R.layout.navigation_list_item, mNavItems));

        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        setupActionbarSpinner();
        if (!isServiceRunning(LocationService.class)) {
            startService(new Intent(this, LocationService.class));
        }

        mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
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
        switch (position) {
            case 0:
                fragment = new CategoriesFragment();
                mActionBar.setDisplayShowTitleEnabled(false);
                mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
                mSharedPrefs.edit().putInt("view_mode", Constants.VIEW_CATEGORIES).apply();
                break;
            case 1:
                Intent intent = new Intent(this, CategoryActivity.class);
                intent.putExtra("category", Constants.CATEGORY_MOBILE);
                mSharedPrefs.edit().putInt("view_mode", Constants.VIEW_CATEGORIES).apply();
                startActivity(intent);
                return;
            case 2:
                fragment = new StoreListFragment();
                mActionBar.setDisplayShowTitleEnabled(true);
                mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
                mSharedPrefs.edit().putInt("view_mode", Constants.VIEW_NOTIFICATIONS).apply();
                break;
            case 3:
                fragment = new StoreListFragment();
                mActionBar.setDisplayShowTitleEnabled(true);
                mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
                mSharedPrefs.edit().putInt("view_mode", Constants.VIEW_COUPONS).apply();
                break;
            case 4:
                fragment = new AccountFragment();
                mActionBar.setDisplayShowTitleEnabled(true);
                mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
                break;
            case 5:
                fragment = new AboutFragment();
                mActionBar.setDisplayShowTitleEnabled(true);
                mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
                break;
        }
        // Insert the fragment by replacing any existing fragment
        FragmentManager manager = getFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.replace(R.id.fragment_container, fragment);
        addToBackStackIfNeeded(ft);
        ft.commit();

        // Highlight the selected item, update the title, and close the drawer
        mDrawerList.setItemChecked(position, true);
        if (position == 5) {
            setTitle("About");
        } else if (position == 4) {
            setTitle("Account");
        } else if (position == 0) {
            setTitle("Places");
        } else {
            setTitle(mNavItems[position]);
        }
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    private void addToBackStackIfNeeded(FragmentTransaction transaction) {
        FragmentManager manager = getFragmentManager();
        if (manager.getBackStackEntryCount() < 1) {
            transaction.addToBackStack(null);
        } else {
            for (int i = 0; i < manager.getBackStackEntryCount(); ++i) {
                manager.popBackStack();
            }
            transaction.addToBackStack(null);
        }
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        if (menu.findItem(R.id.search) != null) {
            menu.findItem(R.id.search).setVisible(!drawerOpen);
        }
        if (menu.findItem(R.id.action_coupons) != null) {
            menu.findItem(R.id.action_coupons).setVisible(!drawerOpen);
        }
        if (menu.findItem(R.id.action_save) != null) {
            menu.findItem(R.id.action_save).setVisible(!drawerOpen);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupActionbarSpinner() {
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
                        mSharedPrefs.edit().putInt("view_mode", Constants.VIEW_CATEGORIES).apply();
                        getFragmentManager().popBackStack();
                        break;
                    case 1:
                        ft.replace(R.id.fragment_container, secondFragment, strings[position]);
                        mSharedPrefs.edit().putInt("view_mode", Constants.VIEW_NEAREST).apply();
                        addToBackStackIfNeeded(ft);
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

    public void dial(View view) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:9165335518"));
        startActivity(intent);
    }

    public void sendEmail(View view) {
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", "robert@schoolgivebackprogram.com", null));
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 1) {
            getFragmentManager().popBackStack();
        } else if (getFragmentManager().getBackStackEntryCount() == 1) {
            mActionBar.setDisplayShowTitleEnabled(false);
            mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
            mActionBar.setTitle("Places");
            mSharedPrefs.edit().putInt("view_mode", Constants.VIEW_CATEGORIES).apply();
            mActionBar.setSelectedNavigationItem(0);
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}