package aleksey.sheyko.sgbp.ui.fragments;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import aleksey.sheyko.sgbp.R;
import aleksey.sheyko.sgbp.adapters.ItemAdapter;
import aleksey.sheyko.sgbp.models.Store;

public class NearestFragment extends ListFragment {

    private ArrayList<Store> mStoreList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

//        for (int i = 1; i <= 5; i++) {
//            Store store = new Store(i,
//                    "Name #" + i,
//                    "Address #" + i,
//                    "Phone #" + i,
//                    "Latitude #" + i,
//                    "Longitude #" + i,
//                    "Category #" + i);
//            store.save();
//        }

        List<Store> stores = Store.listAll(Store.class);
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

        ItemAdapter mAdapter = new ItemAdapter(getActivity(),
                R.layout.list_item_store, mStoreList);

        setListAdapter(mAdapter);
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
