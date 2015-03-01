package aleksey.sheyko.sgbp.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import aleksey.sheyko.sgbp.R;
import aleksey.sheyko.sgbp.app.helpers.Constants;

public class CategoriesAdapter extends BaseAdapter {
    private List<Item> items = new ArrayList<Item>();
    private LayoutInflater inflater;

    public CategoriesAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        items.add(new Item(Constants.CATEGORY_FOOD, R.drawable.category_food));
        items.add(new Item(Constants.CATEGORY_AUTO, R.drawable.category_auto));
        items.add(new Item(Constants.CATEGORY_SOUND, R.drawable.category_sound));
        items.add(new Item(Constants.CATEGORY_HOTELS, R.drawable.category_hotels));
        items.add(new Item(Constants.CATEGORY_OTHER, R.drawable.category_other));
        items.add(new Item(Constants.CATEGORY_MOBILE, R.drawable.category_mobile));
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return items.get(i).drawableId;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = view;
        ImageView picture;
        TextView name;

        if (v == null) {
            v = inflater.inflate(R.layout.category_grid_item, viewGroup, false);
            v.setTag(R.id.picture, v.findViewById(R.id.picture));
            v.setTag(R.id.text, v.findViewById(R.id.text));
        }

        picture = (ImageView) v.getTag(R.id.picture);
        name = (TextView) v.getTag(R.id.text);

        Item item = (Item) getItem(i);

        picture.setImageResource(item.drawableId);
        name.setText(item.name);

        return v;
    }

    private class Item {
        final String name;
        final int drawableId;

        Item(String name, int drawableId) {
            this.name = name;
            this.drawableId = drawableId;
        }
    }
}
