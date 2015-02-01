package aleksey.sheyko.sgbp.ui.fragments;

import android.app.ListFragment;
import android.app.SearchManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.location.LocationServices;
import com.orm.query.Select;

import java.util.ArrayList;
import java.util.List;

import aleksey.sheyko.sgbp.R;
import aleksey.sheyko.sgbp.model.Store;
import aleksey.sheyko.sgbp.ui.activities.MapPane;
import aleksey.sheyko.sgbp.utils.helpers.Constants;
import aleksey.sheyko.sgbp.utils.helpers.adapters.StoresAdapter;
import aleksey.sheyko.sgbp.utils.tasks.UpdateStoreList;
import aleksey.sheyko.sgbp.utils.tasks.UpdateStoreList.OnStoreListLoaded;

public class StoreListFragment extends ListFragment
        implements ConnectionCallbacks, OnStoreListLoaded {

    public static final String TAG = StoreListFragment.class.getSimpleName();

    private ArrayList<Store> mStoreList = new ArrayList<>();
    private List<Store> mStores;
    private String mCategory;
    private String mSearchQuery;
    private int mViewMode;
    private GoogleApiClient mGoogleApiClient;
    private SharedPreferences mSharedPrefs;

    @Override
    public void onStart() {
        super.onStart();
        getListView().setEmptyView(
                noItems(getResources().getString(R.string.widget_empty)));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        if (getArguments() != null) {
            mViewMode = mSharedPrefs.getInt("view_mode", -1);
        } else {
            setHasOptionsMenu(true);
        }

        if (getActivity().getIntent() != null) {
            if (getActivity().getIntent().hasExtra("category")) {
                mCategory = getActivity().getIntent().getStringExtra("category");
            } else if (getActivity().getIntent().hasExtra(SearchManager.QUERY)) {
                mSearchQuery = getActivity().getIntent().getStringExtra(SearchManager.QUERY);
            }
        }

        if (mCategory != null) {
            mStores = Store.find(Store.class, "category = ?", mCategory);
        } else if (mSearchQuery != null) {
            mStores = Store.findWithQuery(Store.class,
                    "Select * from Store where " +
                            "name like '%" + mSearchQuery + "%' or " +
                            "address like '%" + mSearchQuery + "%' or " +
                            "phone like '%" + mSearchQuery + "%' or " +
                            "category like '%" + mSearchQuery + "%'");
        } else if (mViewMode == Constants.ARG_VIEW_COUPONS) {
            mStores = Store.listAll(Store.class);
        } else {
            createLocationClient();
            mGoogleApiClient.connect();
            return;
        }

        if (mStores.size() == 0 && mSearchQuery == null) {
            new UpdateStoreList(this).execute();
            return;
        }

        for (Store store : mStores) {
            mStoreList.add(new Store(
                    store.getStoreid(),
                    store.getName(),
                    store.getAddress(),
                    store.getPhone(),
                    store.getLatitude(),
                    store.getLongitude(),
                    store.getCategory()));
        }
    }

    private synchronized void createLocationClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        updateDistances();
    }

    private void updateDistances() {
        mStores = Store.listAll(Store.class);
        for (Store store : mStores) {
            Location myLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
            if (myLocation == null) return;

            Location storeLocation = new Location("store");
            storeLocation.setLatitude(Double.parseDouble(store.getLatitude()));
            storeLocation.setLongitude(Double.parseDouble(store.getLongitude()));

            float distance = myLocation.distanceTo(storeLocation);
            store.setDistance(distance);
            store.save();
        }
        mStores = Select.from(Store.class).orderBy("distance").list();
        for (Store store : mStores) {
            Log.i(TAG, store.getDistance() + "");
            mStoreList.add(new Store(
                    store.getStoreid(),
                    store.getName(),
                    store.getAddress(),
                    store.getPhone(),
                    store.getLatitude(),
                    store.getLongitude(),
                    store.getCategory()));
            mSharedPrefs.edit().putFloat(store.getStoreid() + "", store.getDistance()).apply();
        }
        StoresAdapter mAdapter = new StoresAdapter(getActivity(),
                R.layout.store_list_item, mStoreList);
        setListAdapter(mAdapter);
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        StoresAdapter mAdapter = new StoresAdapter(getActivity(),
                R.layout.store_list_item, mStoreList);
        setListAdapter(mAdapter);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (GooglePlayServicesUtil.isGooglePlayServicesAvailable(this.getActivity())
                == ConnectionResult.SUCCESS) {
            if ((mStores != null && mStores.size() != 0) || mStores == null)
                inflater.inflate(R.menu.store_list_fragment, menu);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_map:
                Intent intent = new Intent(this.getActivity(), MapPane.class);
                if (mCategory != null)
                    intent.putExtra("category", mCategory);
                if (mSearchQuery != null)
                    intent.putExtra(SearchManager.QUERY, mSearchQuery);
                startActivity(intent);
                return true;
        }
        return false;
    }

    private TextView noItems(String text) {
        TextView emptyView = new TextView(getActivity());
        //Make sure you import android.widget.LinearLayout.LayoutParams;
        LayoutParams mLayoutParams = new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        emptyView.setLayoutParams(mLayoutParams);
        //Instead of passing resource id here I passed resolved color
        //That is, getResources().getColor((R.color.gray_dark))
        emptyView.setTextColor(getResources().getColor(android.R.color.darker_gray));
        emptyView.setText(text);
        emptyView.setPadding(0, 60, 0, 0);
        emptyView.setTextSize(18);
        emptyView.setVisibility(View.GONE);
        emptyView.setGravity(Gravity.CENTER_HORIZONTAL);

        //Add the view to the list view. This might be what you are missing
        ((ViewGroup) getListView().getParent()).addView(emptyView);

        return emptyView;
    }

    @Override
    public void onStoreListUpdated() {
        StoresAdapter mAdapter = new StoresAdapter(getActivity(),
                R.layout.store_list_item, mStoreList);

        setListAdapter(mAdapter);
    }
}
