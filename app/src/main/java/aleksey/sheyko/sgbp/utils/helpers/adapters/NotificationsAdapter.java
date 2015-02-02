package aleksey.sheyko.sgbp.utils.helpers.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import aleksey.sheyko.sgbp.R;
import aleksey.sheyko.sgbp.model.Notification;

public class NotificationsAdapter extends ArrayAdapter<Notification> {

    // declaring our ArrayList of items
    private ArrayList<Notification> mObjects;

    public NotificationsAdapter(Context context, int textViewResourceId, ArrayList<Notification> objects) {
        super(context, textViewResourceId, objects);
        mObjects = objects;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.store_list_item, null);
        }
        Notification notification = mObjects.get(position);
        if (notification != null) {
            TextView primaryTextView = (TextView) view.findViewById(R.id.nameLabel);
            TextView secondaryTextView = (TextView) view.findViewById(R.id.secondaryLabel);

            primaryTextView.setText(notification.getStoreName());
            secondaryTextView.setText(notification.getDate());
        }
        return view;
    }
}