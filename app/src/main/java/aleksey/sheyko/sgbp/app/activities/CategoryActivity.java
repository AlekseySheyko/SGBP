package aleksey.sheyko.sgbp.app.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import aleksey.sheyko.sgbp.R;
import aleksey.sheyko.sgbp.app.fragments.StoreListFragment;
import aleksey.sheyko.sgbp.app.helpers.Constants;
import aleksey.sheyko.sgbp.model.Store;

public class CategoryActivity extends Activity {

    private SharedPreferences mSharedPrefs;
    private int mViewMode;
    private ArrayList<Store> mStoreList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_containter);

        getActionBar().setTitle(getIntent().getStringExtra("category"));

        StoreListFragment storeListFragment = new StoreListFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();

        ft.replace(R.id.fragment_container, storeListFragment, "listFragment");
        ft.commit();

        mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        mViewMode = mSharedPrefs.getInt(
                "view_mode", Constants.VIEW_CATEGORIES);
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

    public void onClickDirections(View view) {
        LinearLayout itemContainer = (LinearLayout) view.getParent();
        LinearLayout labelsContainer = (LinearLayout) itemContainer.getChildAt(0);
        TextView storeNameView = (TextView) labelsContainer.getChildAt(0);
        String storeName = storeNameView.getText().toString();
        
        Store selectedStore = Store.find(Store.class, "name = ?", storeName).get(0);

        if (mViewMode == Constants.VIEW_NEAREST || mViewMode == Constants.VIEW_CATEGORIES) {
            int storeId = selectedStore.getStoreId();
            String name = selectedStore.getName();
            String address = selectedStore.getAddress();
            String phone = selectedStore.getPhone();
            String latitude = selectedStore.getLatitude();
            String longitude = selectedStore.getLongitude();
            mSharedPrefs.edit()
                    .putInt("storeId", storeId)
                    .putString("name", name)
                    .putString("address", address)
                    .putString("phone", phone)
                    .putString("latitude", latitude)
                    .putString("longitude", longitude)
                    .apply();
            startActivity(new Intent(this, DetailActivity.class));
        }
    }
}
