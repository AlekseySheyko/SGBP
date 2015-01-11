package aleksey.sheyko.sgbp.ui.fragments;

import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import java.util.ArrayList;
import java.util.List;

import aleksey.sheyko.sgbp.R;
import aleksey.sheyko.sgbp.adapters.StoresAdapter;
import aleksey.sheyko.sgbp.models.Store;
import aleksey.sheyko.sgbp.ui.activities.MapPane;
import aleksey.sheyko.sgbp.ui.tasks.UpdateStoreList;

public class NearestFragment extends ListFragment {

    private ArrayList<Store> mStoreList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        List<Store> stores = Store.listAll(Store.class);

        if (stores.size() == 0)
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
                R.layout.list_item_store, mStoreList);

        setListAdapter(mAdapter);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (GooglePlayServicesUtil.isGooglePlayServicesAvailable(this.getActivity())
            == ConnectionResult.SUCCESS)
        inflater.inflate(R.menu.nearest, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_map:
                startActivity(new Intent(this.getActivity(), MapPane.class));
                return true;
        }
        return false;
    }
}
