package com.widevision.sgbp.util;

import android.app.ActionBar.LayoutParams;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.widevision.sgbp.R;


public class AlertDialogManager {
	/**
	 * Function to display simple Alert Dialog
	 * @param context - application context
	 * @param title - alert dialog title
	 * @param message - alert message
	 * @param status - success/failure (used to set icon)
	 * 				 - pass null if you don't want icon
	 * */
	public void showAlertDialog(Context context, String title, String message,	Boolean status) {
		final Dialog alertDialog = new Dialog(context);
		alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		alertDialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		alertDialog.setContentView(R.layout.popupwindow);
		alertDialog.setCancelable(true);
		Typeface type = Typeface.createFromAsset(context.getAssets(),
				"Candara Bold Italic.ttf");
		Button btnDismiss = (Button) alertDialog.findViewById(R.id.dismiss);
		TextView aboutprogram = (TextView) alertDialog
				.findViewById(R.id.aboutprogram);
		TextView alerttxtwindow = (TextView) alertDialog
				.findViewById(R.id.alerttxtwindow);
		aboutprogram.setTypeface(type);
		alerttxtwindow.setTypeface(type);
		aboutprogram.setText(message);
		alerttxtwindow.setText(title);
		btnDismiss.setTypeface(type);
		btnDismiss.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				alertDialog.dismiss();
			}
		});
		alertDialog.getWindow().setLayout(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		alertDialog.getWindow().setGravity(Gravity.CENTER);
		alertDialog.show();
	}

	
}
