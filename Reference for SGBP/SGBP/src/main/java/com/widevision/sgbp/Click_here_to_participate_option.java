package com.widevision.sgbp;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class Click_here_to_participate_option extends Activity {

	Button physical_store, mobile_store;
	private Button home;
	private TextView titleView;
	private String status;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.participate_options);

		physical_store = (Button) findViewById(R.id.physical_store);
		mobile_store = (Button) findViewById(R.id.mobile_store);

		// status = PreferenceConnector.readString(getApplicationContext(),
		// PreferenceConnector.AUTOMATIC_CHECK_FLAG, "true");

		// Toast.makeText(getApplicationContext(), ""+status, 3000).show();

		home = (Button) findViewById(R.id.home);
		titleView = (TextView) findViewById(R.id.title);

		Typeface type = Typeface.createFromAsset(getAssets(),
				"Candara Bold Italic.ttf");
		titleView.setTypeface(type);
		titleView.setText("PARTICIPATE");

		home.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				startActivity(new Intent(Click_here_to_participate_option.this,
						MainActivity.class));
				finish();
			}
		});

		// if (status.equalsIgnoreCase("true")) {
		//
		// physical_store.setEnabled(false);
		//
		// } else {
		//
		// physical_store.setEnabled(true);
		//
		// }

		physical_store.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// if (status.equalsIgnoreCase("true")) {
				//
				// // physical_store.setEnabled(false);
				//
				// showMesgDlg("Disabled Right Now... Automatic Participation is Runnning in Background");
				//
				// } else {

				// physical_store.setEnabled(true);
				startActivity(new Intent(Click_here_to_participate_option.this,
						Clickheretoparticipate.class));

				finish();

				// }

			}
		});

		mobile_store.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				startActivity(new Intent(Click_here_to_participate_option.this,
						Mobile_store_to_participate.class));

				finish();

			}
		});

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();

		startActivity(new Intent(Click_here_to_participate_option.this,
				MainActivity.class));
		finish();

	}

	public void showMesgDlg(String msg) {
		try {

			final Dialog alertDialog = new Dialog(
					Click_here_to_participate_option.this);

			alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			alertDialog.getWindow().setBackgroundDrawable(
					new ColorDrawable(android.graphics.Color.TRANSPARENT));
			alertDialog.setContentView(R.layout.popupwindow);
			alertDialog.setCancelable(true);
			Typeface type = Typeface.createFromAsset(getAssets(),
					"Candara Bold Italic.ttf");
			Button btnDismiss = (Button) alertDialog.findViewById(R.id.dismiss);
			TextView aboutprogram = (TextView) alertDialog
					.findViewById(R.id.aboutprogram);

			TextView alerttxtwindow = (TextView) alertDialog
					.findViewById(R.id.alerttxtwindow);
			alerttxtwindow.setText("Participate");

			aboutprogram.setText(msg);
			alerttxtwindow.setTypeface(type);
			aboutprogram.setTypeface(type);
			btnDismiss.setTypeface(type);
			btnDismiss.setOnClickListener(new Button.OnClickListener() {

				@Override
				public void onClick(View v) {
					alertDialog.dismiss();
					// prgLoading.setVisibility(View.GONE);
				}
			});
			alertDialog.getWindow().setGravity(Gravity.CENTER);
			alertDialog.show();
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

}
