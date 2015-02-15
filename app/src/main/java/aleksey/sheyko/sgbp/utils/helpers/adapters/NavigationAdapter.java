package aleksey.sheyko.sgbp.utils.helpers.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import aleksey.sheyko.sgbp.R;

public class NavigationAdapter extends BaseAdapter {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_ITEM_SMALL = 1;
    private static final int TYPE_MAX_COUNT = TYPE_ITEM_SMALL + 1;

    private ArrayList mData = new ArrayList();
    private LayoutInflater mInflater;

    public NavigationAdapter(Context context) {
        mInflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
    }

    public void addItem(final String item) {
        mData.add(item);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (position <= 3) {
            return TYPE_ITEM;
        } else {
            return TYPE_ITEM_SMALL;
        }
    }

    @Override
    public int getViewTypeCount() {
        return TYPE_MAX_COUNT;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public String getItem(int position) {
        return mData.get(position) + "";
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        int type = getItemViewType(position);
        if (convertView == null) {
            holder = new ViewHolder();
            switch (type) {
                case TYPE_ITEM:
                    convertView = mInflater.inflate(R.layout.navigation_list_item, parent, false);
                    holder.textView = (TextView)convertView.findViewById(android.R.id.text1);
                    break;
                case TYPE_ITEM_SMALL:
                    convertView = mInflater.inflate(R.layout.navigation_list_item_small, parent, false);
                    holder.textView = (TextView)convertView.findViewById(android.R.id.text1);
                    break;
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
        holder.textView.setText(mData.get(position) + "");
        return convertView;
    }

    public static class ViewHolder {
        public TextView textView;
    }
}
