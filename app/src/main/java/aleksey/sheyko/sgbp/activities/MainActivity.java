package aleksey.sheyko.sgbp.activities;

import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;

import aleksey.sheyko.sgbp.R;
import aleksey.sheyko.sgbp.fragments.CategoriesFragment;
import aleksey.sheyko.sgbp.fragments.NearestFragment;


public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // setup action bar for tabs
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

        SpinnerAdapter mSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.action_list, android.R.layout.simple_spinner_dropdown_item);

        OnNavigationListener mOnNavigationListener = new OnNavigationListener() {
            // Get the same strings provided for the drop-down's ArrayAdapter
            String[] strings = getResources().getStringArray(R.array.action_list);

            @Override
            public boolean onNavigationItemSelected(int position, long itemId) {
                // Create new fragment from our own Fragment class
                CategoriesFragment firstFragment = new CategoriesFragment();
                NearestFragment secondFragment = new NearestFragment();

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

        actionBar.setListNavigationCallbacks(mSpinnerAdapter, mOnNavigationListener);
    }
}