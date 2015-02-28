package aleksey.sheyko.sgbp.app.adapters;

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

    private Context mContext;
    // declaring our ArrayList of items
    private ArrayList<Notification> mObjects;

    /* here we must override the constructor for ArrayAdapter
    * the only variable we care about now is ArrayList<Item> objects,
    * because it is the list of objects we want to display.
    */
    public NotificationsAdapter(Context context, int textViewResourceId, ArrayList<Notification> objects) {
        super(context, textViewResourceId, objects);
        mContext = context;
        mObjects = objects;
    }

    /*
     * we are overriding the getView method here - this is what defines how each
     * list item will look.
     */
    public View getView(int position, View convertView, ViewGroup parent) {

        // assign the view we are converting to a local variable
        View view = convertView;

        // first check to see if the view is null. if so, we have to inflate it.
        // to inflate it basically means to render, or show, the view.
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.store_list_item, null);
        }

		/*
         * Recall that the variable position is sent in as an argument to this method.
		 * The variable simply refers to the position of the current object in the list. (The ArrayAdapter
		 * iterates through the list we sent it)
		 *
		 * Therefore, store refers to the current Store object.
		 */
        Notification notification = mObjects.get(position);

        if (notification != null) {
            TextView ttd = (TextView) view.findViewById(R.id.nameLabel);
            TextView mtd = (TextView) view.findViewById(R.id.secondaryLabel);
            if (ttd != null) {
                ttd.setText(notification.getStoreName());
            }
            if (mtd != null) {
                mtd.setText(notification.getDate());
            }
        }
        return view;
    }
}