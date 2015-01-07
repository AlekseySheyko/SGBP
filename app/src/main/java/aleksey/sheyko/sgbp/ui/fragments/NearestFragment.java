package aleksey.sheyko.sgbp.ui.fragments;

import android.app.ListFragment;

import aleksey.sheyko.sgbp.database.StoreDataSource;
import aleksey.sheyko.sgbp.models.Store;

public class NearestFragment extends ListFragment {

    public NearestFragment() {
        int id = 1;
        String name = "AR Performance";
        String address = "9055 Locust Street";
        String phone = "(916) 714-5277";
        String latitude = "38.410609";
        String longitude = "-121.364239";
        String category = "Audio equipment";

        StoreDataSource dataSource = new StoreDataSource(this.getActivity());
        dataSource.create(new Store(id, name, address, phone, latitude, longitude, category));
    }
}
