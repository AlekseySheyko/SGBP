package aleksey.sheyko.sgbp.ui.fragments;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import aleksey.sheyko.sgbp.R;

public class NearestFragment extends ListFragment {

    public NearestFragment() {
        //        int storeid = 1;
        //        String name = "AR Performance";
        //        String address = "9055 Locust Street";
        //        String phone = "(916) 714-5277";
        //        String latitude = "38.410609";
        //        String longitude = "-121.364239";
        //        String category = "Audio equipment";
        //        Store store = new Store(id, name, address, phone, latitude, longitude, category);
        //        store.save();

//        List<Store> stores = Store.listAll(Store.class);
//        for (Store store : stores) {
//            Log.i("NearestFragment", store.getName());
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        String[] values = new String[] { "Android", "iPhone", "WindowsMobile",
                "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
                "Linux", "OS/2" };
        // use your custom layout
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                R.layout.rowlayout, R.id.label, values);
        setListAdapter(adapter);
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
