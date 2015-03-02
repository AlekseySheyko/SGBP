package aleksey.sheyko.sgbp.app.fragments;

import android.app.ListFragment;
import android.app.SearchManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.location.LocationServices;
import com.orm.query.Select;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import aleksey.sheyko.sgbp.R;
import aleksey.sheyko.sgbp.app.activities.DetailActivity;
import aleksey.sheyko.sgbp.app.activities.MapPane;
import aleksey.sheyko.sgbp.app.adapters.CouponAdapter;
import aleksey.sheyko.sgbp.app.adapters.NotificationsAdapter;
import aleksey.sheyko.sgbp.app.adapters.StoresAdapter;
import aleksey.sheyko.sgbp.app.helpers.Constants;
import aleksey.sheyko.sgbp.model.Coupon;
import aleksey.sheyko.sgbp.model.Notification;
import aleksey.sheyko.sgbp.model.Store;
import aleksey.sheyko.sgbp.rest.ApiService;
import aleksey.sheyko.sgbp.rest.CouponsXmlParser;
import aleksey.sheyko.sgbp.rest.RestClient;
import aleksey.sheyko.sgbp.rest.StoresXmlParser;
import retrofit.ResponseCallback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class StoreListFragment extends ListFragment
        implements ConnectionCallbacks {

    private ArrayList<Store> mStoreList = new ArrayList<>();
    private ArrayList<Notification> mNotificationList = new ArrayList<>();
    private ArrayList<Coupon> mCouponList = new ArrayList<>();
    private List<Store> mStores;
    private List<Notification> mNotifications;
    private List<Coupon> mCoupons;
    private String mCategory;
    private String mSearchQuery;
    private int mViewMode;
    private GoogleApiClient mGoogleApiClient;
    private SharedPreferences mSharedPrefs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mViewMode = mSharedPrefs.getInt(
                "view_mode", Constants.VIEW_CATEGORIES);
        if (mViewMode == Constants.VIEW_CATEGORIES ||
                mViewMode == Constants.VIEW_NEAREST) {
            setHasOptionsMenu(true);
        }

        if (getActivity().getIntent() != null) {
            if (getActivity().getIntent().hasExtra("category")) {
                mCategory = getActivity().getIntent().getStringExtra("category");
                if (mCategory.equals(Constants.CATEGORY_MOBILE)) {
                    setHasOptionsMenu(false);
                }
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
                            "category like '%" + mSearchQuery + "%'");
        } else if (mViewMode == Constants.VIEW_NOTIFICATIONS) {
            mNotifications = Select.from(Notification.class).orderBy("id DESC").list();
            for (Notification notification : mNotifications) {
                mNotificationList.add(new Notification(
                        notification.getStoreName(), notification.getDate()));
            }
            return;
        } else if (mViewMode == Constants.VIEW_NEAREST) {
            createLocationClient();
            mGoogleApiClient.connect();
            return;
        } else if (mViewMode == Constants.VIEW_COUPONS) {

            mCoupons = Coupon.listAll(Coupon.class);
            if (mCoupons.size() > 0) {
                for (Coupon coupon : mCoupons) {
                    mCouponList.add(new Coupon(
                            coupon.getStoreid(),
                            coupon.getStoreName(),
                            coupon.getCode(),
                            coupon.getDesc(),
                            coupon.getExpireDate()));
                }
            } else {
                loadCouponsFromNetwork();
            }
            return;
        }

        if (mStores != null && mStores.size() == 0 && mSearchQuery == null) {
            loadStoresFromNetwork();
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

    private void loadStoresFromNetwork() {
        getActivity().setProgressBarIndeterminateVisibility(true);
        ApiService service = new RestClient().getApiService();
        service.listAllStores(new ResponseCallback() {
            @Override public void success(Response response) {
                try (InputStream in = response.getBody().in()) {
                    StoresXmlParser storesXmlParser = new StoresXmlParser();
                    storesXmlParser.parse(in);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mStores = Store.listAll(Store.class);
                populateStoresListAdapter();
                getActivity().setProgressBarIndeterminateVisibility(false);
            }

            @Override public void failure(RetrofitError e) {
                e.printStackTrace();
            }
        });
    }

    private void loadCouponsFromNetwork() {
        getActivity().setProgressBarIndeterminateVisibility(true);
        ApiService service = new RestClient().getApiService();
        service.listCoupons(new ResponseCallback() {
            @Override public void success(Response response) {
                try (InputStream in = response.getBody().in()) {
                    CouponsXmlParser couponsXmlParser = new CouponsXmlParser();
                    couponsXmlParser.parse(in);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mCoupons = Coupon.listAll(Coupon.class);
                populateCouponsListAdapter();
                getActivity().setProgressBarIndeterminateVisibility(false);
            }

            @Override public void failure(RetrofitError e) {
                e.printStackTrace();
            }
        });
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
        Location myLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (myLocation == null) {
            populateStoresListAdapter();
            return;
        }
        for (Store store : mStores) {
            Location storeLocation = new Location("store");
            storeLocation.setLatitude(Double.parseDouble(store.getLatitude()));
            storeLocation.setLongitude(Double.parseDouble(store.getLongitude()));

            float distance = myLocation.distanceTo(storeLocation) * 0.000621371f;
            store.setDistance(distance);
            store.save();
        }
        mStores = Select.from(Store.class).orderBy("distance").list();
        populateStoresListAdapter();
    }

    private void populateStoresListAdapter() {
        for (Store store : mStores) {
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
        StoresAdapter adapter = new StoresAdapter(getActivity(),
                R.layout.store_list_item, mStoreList);
        setListAdapter(adapter);
    }

    private void populateCouponsListAdapter() {
        for (Coupon coupon : mCoupons) {
            mCouponList.add(new Coupon(
                    coupon.getStoreid(),
                    coupon.getStoreName(),
                    coupon.getCode(),
                    coupon.getDesc(),
                    coupon.getExpireDate()));
        }
        CouponAdapter adapter = new CouponAdapter(getActivity(),
                R.layout.store_list_item, mCouponList);
        setListAdapter(adapter);
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mViewMode == Constants.VIEW_NOTIFICATIONS) {
            NotificationsAdapter mAdapter = new NotificationsAdapter(getActivity(),
                    R.layout.store_list_item, mNotificationList);
            setListAdapter(mAdapter);
        } else if (mViewMode == Constants.VIEW_COUPONS) {
            CouponAdapter adapter = new CouponAdapter(getActivity(),
                    R.layout.store_list_item, mCouponList);
            setListAdapter(adapter);
        } else {
            StoresAdapter mAdapter = new StoresAdapter(getActivity(),
                    R.layout.store_list_item, mStoreList);
            setListAdapter(mAdapter);
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mViewMode == Constants.VIEW_NOTIFICATIONS) {
            getListView().setEmptyView(
                    noItems(getResources().getString(R.string.notifications_empty)));
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        if (mViewMode == Constants.VIEW_NEAREST || mViewMode == Constants.VIEW_CATEGORIES) {
            int storeId = mStoreList.get(position).getStoreid();
            String name = mStoreList.get(position).getName();
            String address = mStoreList.get(position).getAddress();
            String phone = mStoreList.get(position).getPhone();
            String latitude = mStoreList.get(position).getLatitude();
            String longitude = mStoreList.get(position).getLongitude();
            mSharedPrefs.edit()
                    .putInt("storeId", storeId)
                    .putString("name", name)
                    .putString("address", address)
                    .putString("phone", phone)
                    .putString("latitude", latitude)
                    .putString("longitude", longitude)
                    .apply();
            if (mViewMode != Constants.VIEW_NEAREST) {
                mSharedPrefs.edit().putBoolean("isMobile", mCategory.equals(Constants.CATEGORY_MOBILE)).apply();
            } else {
                mSharedPrefs.edit().putBoolean("isMobile", false).apply();
            }
            startActivity(new Intent(this.getActivity(), DetailActivity.class));
        } else if (mViewMode == Constants.VIEW_COUPONS) {
            int storeId = mCouponList.get(position).getStoreid();
            Store selectedStore = Store.find(Store.class, "storeid = ?", String.valueOf(storeId)).get(0);
            String name = selectedStore.getName();
            String latitude = selectedStore.getLatitude();
            String longitude = selectedStore.getLongitude();
            mSharedPrefs.edit()
                    .putInt("storeId", storeId)
                    .putString("name", name)
                    .putString("latitude", latitude)
                    .putString("longitude", longitude)
                    .apply();
            startActivity(new Intent(this.getActivity(), MapPane.class));
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (GooglePlayServicesUtil.isGooglePlayServicesAvailable(this.getActivity()) == ConnectionResult.SUCCESS) {
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
}
