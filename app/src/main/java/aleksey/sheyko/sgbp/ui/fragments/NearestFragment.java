package aleksey.sheyko.sgbp.ui.fragments;

import android.app.ListFragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import aleksey.sheyko.sgbp.R;
import aleksey.sheyko.sgbp.adapters.ItemAdapter;
import aleksey.sheyko.sgbp.models.Item;

public class NearestFragment extends ListFragment {

    // declare class variables
    private ArrayList<Item> m_parts = new ArrayList<Item>();
    private Runnable viewParts;
    private ItemAdapter m_adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // instantiate our ItemAdapter class
        // TODO: Maybe I'll be needed to change getActivity() to NearestFragment
        m_adapter = new ItemAdapter(getActivity(), R.layout.list_item_store, m_parts);
        setListAdapter(m_adapter);

        // here we are defining our runnable thread.
        viewParts = new Runnable(){
            public void run(){
                handler.sendEmptyMessage(0);
            }
        };

        // here we call the thread we just defined - it is sent to the handler below.
        Thread thread =  new Thread(null, viewParts, "MagentoBackground");
        thread.start();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private Handler handler = new Handler()
    {
        public void handleMessage(Message msg)
        {
            // create some objects
            // here is where you could also request data from a server
            // and then create objects from that data.
            m_parts.add(new Item(1, "Name #1", "Address #1", "Phone #1", "Latitude #1", "Longitude #1", "Category #1"));
            m_parts.add(new Item(2, "Name #2", "Address #2", "Phone #2", "Latitude #2", "Longitude #2", "Category #1"));

            m_adapter = new ItemAdapter(getActivity(), R.layout.list_item_store, m_parts);

            // display the list.
            setListAdapter(m_adapter);
        }
    };
}
