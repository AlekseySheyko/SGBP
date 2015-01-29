package com.widevision.sgbp;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CoupanListAdapter extends BaseAdapter {
	Context ctx;
	LayoutInflater lInflater;
	ArrayList<HashMap<String, String>> data;

	CoupanListAdapter(Context context, ArrayList<HashMap<String, String>> data) {
		ctx = context;
		this.data = data;
		lInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if (view == null) {
			view = lInflater.inflate(R.layout.listitem, parent, false);
		}
		// view.setBackgroundResource(R.drawable.cellbg);
		/*
		 * if (position % 2 == 0) { view.setBackgroundResource(R.drawable.cellbg1); } else { view.setBackgroundResource(R.drawable.cellbg2); }
		 */
		Typeface type = Typeface.createFromAsset(ctx.getAssets(), "Candara Bold Italic.ttf");
		TextView textView = (TextView) view.findViewById(R.id.listitemtxt);
		textView.setText(data.get(position).get("Store_Name"));
		textView.setTypeface(type);

		TextView codetxt = (TextView) view.findViewById(R.id.codetxt);
		codetxt.setText(data.get(position).get("Coupon_Code"));
		codetxt.setTypeface(type);

		TextView desctxt = (TextView) view.findViewById(R.id.desctxt);
		desctxt.setText(data.get(position).get("Coupon_Code_Desc"));
		desctxt.setTypeface(type);
		// ((TextView) view.findViewById(R.id.listitemtxt)).setText(data.get(position));

		return view;
	}
}
