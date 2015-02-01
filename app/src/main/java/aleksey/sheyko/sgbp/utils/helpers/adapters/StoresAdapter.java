package aleksey.sheyko.sgbp.utils.helpers.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import aleksey.sheyko.sgbp.R;
import aleksey.sheyko.sgbp.model.Store;
import aleksey.sheyko.sgbp.utils.helpers.Constants;

public class StoresAdapter extends ArrayAdapter<Store> {

    private Context mContext;
    // declaring our ArrayList of items
    private ArrayList<Store> mObjects;

    private SharedPreferences mSharedPrefs;

    /* here we must override the constructor for ArrayAdapter
    * the only variable we care about now is ArrayList<Item> objects,
    * because it is the list of objects we want to display.
    */
    public StoresAdapter(Context context, int textViewResourceId, ArrayList<Store> objects) {
        super(context, textViewResourceId, objects);
        mContext = context;
        mObjects = objects;
        mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
    }

    /*
     * we are overriding the getView method here - this is what defines how each
     * list item will look.
     */
    public View getView(int position, View convertView, ViewGroup parent) {

        // assign the view we are converting to a local variable
        View v = convertView;

        // first check to see if the view is null. if so, we have to inflate it.
        // to inflate it basically means to render, or show, the view.
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.store_list_item, null);
        }

		/*
         * Recall that the variable position is sent in as an argument to this method.
		 * The variable simply refers to the position of the current object in the list. (The ArrayAdapter
		 * iterates through the list we sent it)
		 *
		 * Therefore, i refers to the current Item object.
		 */
        Store store = mObjects.get(position);

        if (store != null) {
            // This is how you obtain a reference to the TextViews.
            // These TextViews are created in the XML files we defined.
            TextView ttd = (TextView) v.findViewById(R.id.nameLabel);
            TextView mtd = (TextView) v.findViewById(R.id.secondaryLabel);

            // check to see if each individual textview is null.
            // if not, assign some text!
            if (ttd != null) {
                ttd.setText(store.getName());
            }
            if (mtd == null) return null;

            int viewMode = mSharedPrefs.getInt("view_mode", -1);
            if (viewMode == Constants.ARG_VIEW_NEAREST) {
                String distance = String.format("%.1f%n",
                        mSharedPrefs.getFloat(store.getStoreid() + "", -1)).replace(".0", "") + "miles";
                mtd.setText(distance);
            } else if (viewMode == Constants.ARG_VIEW_NOTIFICATIONS) {
                mtd.setText("23 dec, 4:02 PM");
            } else if (viewMode == -1) {
                mtd.setText(store.getAddress());
            }
        }
        // the view must be returned to our activity
        return v;
    }
}