package com.widevision.sgbp;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.SpannableStringBuilder;
import android.text.style.CharacterStyle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.widevision.sgbp.util.AlertDialogManager;
import com.widevision.sgbp.util.ConnectionDetector;
import com.widevision.sgbp.util.Constant;
import com.widevision.sgbp.util.JSONParser;

public class AboutProgram extends Activity {
	ProgressBar prgLoading;
	Button home;
	String aboutprogramstr, TAG = "AboutProgram";
	TextView aboutprogram;
	TextView titleView;
	JSONParser jsonParser = new JSONParser();
	TextView aboutprogram_win1, aboutprogram_win2, aboutprogram_win3, aboutprogram_bottom;
	TextView win1, win2, win3,aboutprogram_bottom1,aboutprogram_bottom2;
	ImageView fb, tweet;
	AsyncTask<Void, Void, Void> mRegisterTask;
	AlertDialogManager alert = new AlertDialogManager();
	ConnectionDetector cd;

	@Override
	public void onBackPressed() {

		startActivity(new Intent(AboutProgram.this, MainActivity.class));

		finish();
	}

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aboutprogram);
		prgLoading = (ProgressBar) findViewById(R.id.prgLoading);
		aboutprogram = (TextView) findViewById(R.id.aboutprogram);
		titleView = (TextView) findViewById(R.id.title);
		fb = (ImageView) findViewById(R.id.fb);
		tweet = (ImageView) findViewById(R.id.tweet);

		aboutprogram_win1 = (TextView) findViewById(R.id.aboutprogram_win1);
		aboutprogram_win2 = (TextView) findViewById(R.id.aboutprogram_win2);
		aboutprogram_win3 = (TextView) findViewById(R.id.aboutprogram_win3);
		aboutprogram_bottom1 = (TextView) findViewById(R.id.aboutprogram_bottom1);
		aboutprogram_bottom2 = (TextView) findViewById(R.id.aboutprogram_bottom2);
		
		
	
		win1 = (TextView) findViewById(R.id.win1);
		win2 = (TextView) findViewById(R.id.win2);
		win3 = (TextView) findViewById(R.id.win3);

		titleView.setText("ABOUT PROGRAM");
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		// Typeface type = Typeface.createFromAsset(getAssets(), "Candara Bold Italic.ttf");
		// titleView.setTypeface(type);
		// aboutprogram.setTypeface(type);
		home = (Button) findViewById(R.id.home);
		home.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(AboutProgram.this, MainActivity.class));

				finish();
			}
		});
		try {
			cd = new ConnectionDetector(getApplicationContext());

			// Check if Internet present
			if (!cd.isConnectingToInternet()) {
				// Internet Connection is not present
				alert.showAlertDialog(AboutProgram.this, "Oops!!", "Internet Connection seems to offline.Please check your network.", false);
				// stop executing code by return
				return;
			} else {
				new sendaboutprogramData().execute();
			}
		} catch (Exception e) {

			e.printStackTrace();
		}

		fb.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/schoolgivebackprogram"));

				startActivity(intent);

			}
		});

		tweet.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/SchoolGiveBack"));

				startActivity(intent);

			}
		});

	}

	public boolean isInternetOn() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();

		// if no network is available networkInfo will be null
		// otherwise check if we are connected

		if (networkInfo != null && networkInfo.isConnected()) {

			return true;
		}

		return false;
	}

	public boolean wifiConnectivity() {
		ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

		// For 3G check
		boolean is3g = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();

		// For WiFi Check
		boolean isWifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();

		if (!is3g && !isWifi) {
			return false;
		} else {
			return true;
		}
	}

	public class sendaboutprogramData extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {

			prgLoading.setVisibility(View.VISIBLE);
		}

		@Override
		protected Void doInBackground(Void... params) {
			aboutprogramstr = getAboutProgramList();
//			Log.e(TAG, "aboutprogramstr-------" + aboutprogramstr);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			prgLoading.setVisibility(View.INVISIBLE);
			aboutprogram.setVisibility(View.VISIBLE);
			aboutprogram_win1.setVisibility(View.VISIBLE);
			aboutprogram_win2.setVisibility(View.VISIBLE);
			aboutprogram_win3.setVisibility(View.VISIBLE);
			aboutprogram_bottom1.setVisibility(View.VISIBLE);
			aboutprogram_bottom2.setVisibility(View.VISIBLE);
			
			win1.setVisibility(View.VISIBLE);
			win2.setVisibility(View.VISIBLE);
			win3.setVisibility(View.VISIBLE);
			fb.setVisibility(View.VISIBLE);
			tweet.setVisibility(View.VISIBLE);

			if (aboutprogramstr.equals("null")) {

			} else {

				CharSequence aboutprogramstr1 = "School Give Back Program is a non-profit organization, which "
						+ "distributes 100% of local business donations to participating schools."
						+ "Encourage your favorite businesses to join us today! \n\nSchool Give Back Program is a…";

				// aboutprogramstr1 = setSpanBetweenTokens(aboutprogramstr1, "Win!-",new StyleSpan(android.graphics.Typeface.BOLD));

				// String styledText1 = "<small> <font color='#EFFBF5'>" + "Cash 1 Customers" + "</font> </small>" + "<br />" + "<big>" + "Go To Log in" +
				// "</big>"
				// + "<small> <font color='#EFFBF5'>" + "" + "</font> </small>" + "\n";
				// aboutprogram.setText(Html.fromHtml(styledText1));

				aboutprogram.setText("" + aboutprogramstr1);

				aboutprogram_win1.setText("Our Schools- Our Children!");
				aboutprogram_win2.setText("Our Community- Local Business!");
				aboutprogram_win3.setText("You – Exclusive Money Saving Offers!");
				aboutprogram_bottom1.setText("Follow School Give Back Program Online");
				aboutprogram_bottom2.setText("www.schoolgivebackprogram.com");

				aboutprogram_bottom2.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.schoolgivebackprogram.com/"));

						startActivity(intent);

					}
				});

			}

		}

		public String getAboutProgramList() {

			String resultString = null;
			JSONObject json = jsonParser.getJSONFromUrl(Constant.SERVER_URL + "GetAboutProgram");
//			Log.d(TAG, "------------" + json);
			try {
				JSONObject c = json.getJSONObject("GetAboutProgramResult");
				resultString = c.getString("About_Program_Desc");
			}

			catch (Exception e) {

				showMesgDlg("Server not found.Please try again later.");

				e.printStackTrace();
			}

			return resultString;

		}
	}

	@SuppressLint("InlinedApi")
	public void showMesgDlg(String msg) {
		try {
			final Dialog alertDialog = new Dialog(AboutProgram.this);

			alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
			alertDialog.setContentView(R.layout.popupwindow);
			alertDialog.setCancelable(true);
			Typeface type = Typeface.createFromAsset(getAssets(), "Candara Bold Italic.ttf");
			Button btnDismiss = (Button) alertDialog.findViewById(R.id.dismiss);
			TextView aboutprogram = (TextView) alertDialog.findViewById(R.id.aboutprogram);
			aboutprogram.setText(msg);
			aboutprogram.setTypeface(type);
			btnDismiss.setTypeface(type);
			btnDismiss.setOnClickListener(new Button.OnClickListener() {

				@Override
				public void onClick(View v) {
					alertDialog.dismiss();
				}
			});
			alertDialog.getWindow().setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			alertDialog.getWindow().setGravity(Gravity.CENTER);
			alertDialog.show();
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	public static CharSequence setSpanBetweenTokens(CharSequence text, String token, CharacterStyle... cs) {
		// Start and end refer to the points where the span will apply
		int tokenLen = token.length();
		int start = text.toString().indexOf(token) + tokenLen;
		int end = text.toString().indexOf(token, start);

		if (start > -1 && end > -1) {
			// Copy the spannable string to a mutable spannable string
			SpannableStringBuilder ssb = new SpannableStringBuilder(text);
			for (CharacterStyle c : cs)
				ssb.setSpan(c, start, end, 0);

			// Delete the tokens before and after the span
			ssb.delete(end, end + tokenLen);
			ssb.delete(start - tokenLen, start);

			text = ssb;
		}

		return text;
	}

}
