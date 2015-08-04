package aleksey.sheyko.sgbp.app.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;

import aleksey.sheyko.sgbp.R;
import aleksey.sheyko.sgbp.app.fragments.StoreListFragment;
import aleksey.sheyko.sgbp.app.helpers.Constants;

public class CategoryActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_containter);

        getActionBar().setTitle(getIntent().getStringExtra("category"));

        StoreListFragment storeListFragment = new StoreListFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();

        ft.replace(R.id.fragment_container, storeListFragment, null);
        ft.commit();
    }

    public void onClickCoupon(View view) {
        Fragment fragment = new StoreListFragment();
        PreferenceManager.getDefaultSharedPreferences(this).edit()
                .putInt("view_mode", Constants.VIEW_COUPONS).apply();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(getResources()
                .getString(R.string.action_coupons));
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
    }
}
