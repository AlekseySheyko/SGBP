package aleksey.sheyko.sgbp.ui.fragments;

import android.app.ListFragment;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

import aleksey.sheyko.sgbp.R;
import aleksey.sheyko.sgbp.utils.helpers.adapters.StoresAdapter;
import aleksey.sheyko.sgbp.model.Store;
import aleksey.sheyko.sgbp.ui.activities.MapPane;
import aleksey.sheyko.sgbp.utils.tasks.UpdateStoreList;

public class StoreListFragment extends ListFragment {

    private ArrayList<Store> mStoreList = new ArrayList<>();
    private List<Store> stores;
    private String mCategory;
    private String mSearchQuery;

    @Override
    public void onStart() {
        super.onStart();
        getListView().setEmptyView(
                noItems(getResources().getString(R.string.widget_empty)));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if (getActivity().getIntent() != null) {
            if (getActivity().getIntent().hasExtra("category")) {
                mCategory = getActivity().getIntent().getStringExtra("category");
            } else if (getActivity().getIntent().hasExtra(SearchManager.QUERY)) {
                mSearchQuery = getActivity().getIntent().getStringExtra(SearchManager.QUERY);
            }
        }

        if (mCategory != null) {
            stores = Store.find(Store.class, "category = ?", mCategory);
        } else if (mSearchQuery != null) {
            stores = Store.findWithQuery(Store.class,
                    "Select * from Store where " +
                            "name like '%" + mSearchQuery + "%' or " +
                            "address like '%" + mSearchQuery + "%' or " +
                            "phone like '%" + mSearchQuery + "%' or " +
                            "category like '%" + mSearchQuery + "%'");
        } else {
            stores = Store.listAll(Store.class);
        }

        if (stores.size() == 0 && mSearchQuery == null)
            new UpdateStoreList().execute();

        for (Store store : stores) {
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
            if ((stores != null && stores.size() != 0) || stores == null)
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
