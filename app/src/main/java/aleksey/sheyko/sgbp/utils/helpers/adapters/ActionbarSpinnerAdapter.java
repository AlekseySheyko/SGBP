package aleksey.sheyko.sgbp.utils.helpers.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import aleksey.sheyko.sgbp.R;

public class ActionbarSpinnerAdapter extends ArrayAdapter<String> {

    // CUSTOM SPINNER ADAPTER
    private Context mContext;
    private String[] mActionTitles;

    public ActionbarSpinnerAdapter(Context context, int textViewResourceId,
                                   String[] actionTitles) {
        super(context, textViewResourceId, actionTitles);

        mContext = context;
        mActionTitles = actionTitles;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater =
                (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(android.R.layout.simple_spinner_dropdown_item, null);
            holder = new ViewHolder();
            holder.txt01 = (TextView) convertView.findViewById(android.R.id.text1);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txt01.setText(mActionTitles[position]);

        return convertView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater =
                (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.actionbar_spinner, null);
            holder = new ViewHolder();
            holder.txt01 = (TextView) convertView.findViewById(R.id.action_bar_title);
            holder.txt02 = (TextView) convertView.findViewById(android.R.id.text1);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txt01.setText("Places");
        holder.txt02.setText(mActionTitles[position]);

        return convertView;
    }


    class ViewHolder {
        TextView txt01;
        TextView txt02;
    }


}
