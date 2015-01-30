package aleksey.sheyko.sgbp.ui.activities;

import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import java.util.List;

import aleksey.sheyko.sgbp.R;
import aleksey.sheyko.sgbp.model.Store;
import aleksey.sheyko.sgbp.service.GeofenceService;
import aleksey.sheyko.sgbp.ui.fragments.CategoriesFragment;
import aleksey.sheyko.sgbp.ui.fragments.StoreListFragment;
import aleksey.sheyko.sgbp.utils.helpers.adapters.SpinnerAdapter;
import aleksey.sheyko.sgbp.utils.tasks.UpdateStoreList;
import aleksey.sheyko.sgbp.utils.tasks.UpdateStoreList.OnStoreListLoaded;


public class MainActivity extends FragmentActivity implements OnStoreListLoaded {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupActionBarDropdown();

        List<Store> stores = Store.listAll(Store.class);
        if (stores.size() != 0) {
            startService(new Intent(this, GeofenceService.class));
        } else {
            new UpdateStoreList(this).execute();
        }
    }

    @Override
    public void onStoreListUpdated() {
        startService(new Intent(this, GeofenceService.class));
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
}