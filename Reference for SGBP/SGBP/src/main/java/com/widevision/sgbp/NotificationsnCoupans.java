package com.widevision.sgbp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.widevision.sgbp.util.AlertDialogManager;
import com.widevision.sgbp.util.ConnectionDetector;
import com.widevision.sgbp.util.Constant;
import com.widevision.sgbp.util.Get_Deviceid;
import com.widevision.sgbp.util.JSONParser;

@SuppressLint("NewApi")
public class NotificationsnCoupans extends Activity {

	LinearLayout storenamelayout, storelayout, notidayslayout, dayslayout;
	Button home, notifications, coupans;
	TextView titleView;
	TextView storebtn, daysbtn;
	ListView notificationslistView, coupanslistView;
	String TAG = "Clickheretoparticipate", Result, device_Info_Id = "12", device_idString, days = "5", storename, Store_Id;
	JSONParser jsonParser = new JSONParser();
	boolean IsCouponAllowed, IsNotificationAllowed;
	NotificationListAdapter notificationListAdapter;
	CoupanListAdapter coupanListAdapter;
	// private List<String> notidata;
	// private List<String> coupandata;
	private List<String> storedata;
	int UserIdint;
	ArrayList<HashMap<String, String>> store_arrlst = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> copoun_arrlst = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> noti_arrlst = new ArrayList<HashMap<String, String>>();
	// Asyntask
	AsyncTask<Void, Void, Void> mRegisterTask;
	Get_Deviceid get_dv_id;
	// Alert dialog manager
	AlertDialogManager alert = new AlertDialogManager();
	//
	// Connection detector
	ConnectionDetector cd;
	ProgressBar prgLoading;
	SharedPreferences pref, userdpref, storepref, dayspref;
	Typeface type;
	String UserId, Has_Multiple_Child, Is_Coupon_Allowed, Is_Location_Service_Allowed, Is_Notification_Allowed, Is_User_Over_18_Year, Is_User_Registered;
	ArrayList<HashMap<String, String>> getUserReginfo = new ArrayList<HashMap<String, String>>();

	ArrayList<Sort_noti_Bean> sor_not_List = new ArrayList<Sort_noti_Bean>();

	@Override
	public void onBackPressed() {

		startActivity(new Intent(NotificationsnCoupans.this, MainActivity.class));
		finish();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notificationscoupans);
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		// notidata = new ArrayList<String>();
		storedata = new ArrayList<String>();

		storenamelayout = (LinearLayout) findViewById(R.id.storenamelayout);
		storelayout = (LinearLayout) findViewById(R.id.storelayout);
		notidayslayout = (LinearLayout) findViewById(R.id.notidayslayout);
		dayslayout = (LinearLayout) findViewById(R.id.dayslayout);

		notifications = (Button) findViewById(R.id.notifications);
		coupans = (Button) findViewById(R.id.coupans);
		notificationslistView = (ListView) findViewById(R.id.notificationslistView);
		coupanslistView = (ListView) findViewById(R.id.coupanslistView);
		prgLoading = (ProgressBar) findViewById(R.id.prgLoading);

		titleView = (TextView) findViewById(R.id.title);

		type = Typeface.createFromAsset(getAssets(), "Candara Bold Italic.ttf");
		titleView.setTypeface(type);
		notifications.setTypeface(type);
		coupans.setTypeface(type);
		titleView.setText("COUPONS");

		storebtn = (TextView) findViewById(R.id.storebtn);
		daysbtn = (TextView) findViewById(R.id.daysbtn);

		storebtn.setInputType(InputType.TYPE_NULL);
		daysbtn.setInputType(InputType.TYPE_NULL);

		storebtn.setTypeface(type);
		daysbtn.setTypeface(type);

		storebtn.setEnabled(false);
		daysbtn.setEnabled(false);

		home = (Button) findViewById(R.id.home);

		pref = getSharedPreferences("device", Context.MODE_PRIVATE);

		dayspref = getSharedPreferences("daysprefvalue", Context.MODE_PRIVATE);
		storepref = getSharedPreferences("storeprefvalue", Context.MODE_PRIVATE);

		get_dv_id = new Get_Deviceid(getApplicationContext());

		device_idString = get_dv_id.get_unique_deviceid();
		userdpref = getSharedPreferences("userid", Context.MODE_PRIVATE);
		UserId = userdpref.getString("UserId", null);
		Log.d(TAG, "UserId----------" + UserId);
		try {
			UserIdint = Integer.parseInt(UserId);
		} catch (Exception e1) {

			e1.printStackTrace();
		}

		// storename = storepref.getString("storename", "");
		 Store_Id = storepref.getString("Store_Id", "");
		// storebtn.setText(storename);
		//
		// days = dayspref.getString("days", "5");
		daysbtn.setText("5 days");

		cd = new ConnectionDetector(getApplicationContext());

		// Check if Internet present
		if (!cd.isConnectingToInternet()) {
			// Internet Connection is not present
			alert.showAlertDialog(NotificationsnCoupans.this, "Oops!!", "Internet Connection seems to offline.Please check your network.", false);
			// stop executing code by return
			return;
		} else {
			try {
				Is_Coupon_Allowed = PreferenceConnector.readString(getApplicationContext(), PreferenceConnector.Is_Coupon_Allowed, "false");
				if (Is_Coupon_Allowed.equalsIgnoreCase("true")) {
					PreferenceConnector.writeString(getApplicationContext(), PreferenceConnector.Is_Location_Service_Allowed, Is_Coupon_Allowed);
					new GetAllStore().execute();
					// new GetCoupon().execute();
				} else {
					showAlertDialogC();
				}
			} catch (Exception e) {

				e.printStackTrace();
			}
		}

		storelayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				initPopupStoreName();
			}
		});
		dayslayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				initPopupDays();
			}
		});

		notifications.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				notifications.setBackgroundResource(R.drawable.notificationselected);
				coupans.setBackgroundResource(R.drawable.coupons);

				notidayslayout.setVisibility(View.VISIBLE);
				storenamelayout.setVisibility(View.GONE);

				titleView.setText("NOTIFICATIONS");
				Log.d(TAG, "Is_Notification_Allowed----------" + Is_Notification_Allowed);
				// sor_not_List.clear();
				// store_arrlst.clear();
				try {
					Is_Notification_Allowed = PreferenceConnector.readString(getApplicationContext(), PreferenceConnector.Is_Notification_Allowed, "false");
					if (Is_Notification_Allowed.equalsIgnoreCase("true")) {
						new GetNotifications().execute();
						notificationslistView.setVisibility(View.VISIBLE);
						coupanslistView.setVisibility(View.GONE);
					} else {
						notificationslistView.setVisibility(View.VISIBLE);
						coupanslistView.setVisibility(View.GONE);
						showAlertDialogN();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		coupans.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				coupans.setBackgroundResource(R.drawable.couponsselected);
				notifications.setBackgroundResource(R.drawable.notifications);
				notificationslistView.setVisibility(View.GONE);

				notidayslayout.setVisibility(View.GONE);
				storenamelayout.setVisibility(View.VISIBLE);
				titleView.setText("COUPONS");
				// copoun_arrlst.clear();
				// store_arrlst.clear();
				try {
					Is_Coupon_Allowed = PreferenceConnector.readString(getApplicationContext(), PreferenceConnector.Is_Coupon_Allowed, "false");

					if (Is_Coupon_Allowed.equalsIgnoreCase("true")) {
						notificationslistView.setVisibility(View.GONE);
						coupanslistView.setVisibility(View.VISIBLE);
						new GetCoupon().execute();
					} else {
						notificationslistView.setVisibility(View.GONE);
						coupanslistView.setVisibility(View.VISIBLE);
						showAlertDialogC();
					}
				} catch (Exception e) {

					e.printStackTrace();
				}
			}
		});

		home.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(NotificationsnCoupans.this, MainActivity.class));

				finish();
			}
		});

	}

	protected void initPopupStoreName() {
		try {

			// store_arrlst.clear();
			final Dialog alertDialog = new Dialog(NotificationsnCoupans.this);

			alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
			alertDialog.setContentView(R.layout.singlegrade_popup);

			ListView Storenamelist = (ListView) alertDialog.findViewById(R.id.singlegradelist);

			alertDialog.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(NotificationsnCoupans.this, android.R.layout.simple_list_item_1, storedata);
			Storenamelist.setAdapter(adapter);
			Storenamelist.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
					Store_Id = store_arrlst.get(pos).get("Store_Id").toString();
					System.out.println("Store_Id>>>>>>>>>>>>>>>>>>>>" + Store_Id);
					storename = arg0.getItemAtPosition(pos).toString();
					storebtn.setText(storename);
					Editor editor = storepref.edit();
					editor.putString("storename", storename);
					editor.putString("Store_Id", Store_Id);
					editor.commit();
					new GetCoupon().execute();
					alertDialog.cancel();

				}
			});
			alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

			alertDialog.show();
		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	protected void initPopupDays() {
		try {

			final Dialog alertDialog = new Dialog(NotificationsnCoupans.this);

			alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
			alertDialog.setContentView(R.layout.singlegrade_popup);

			ListView Daylist = (ListView) alertDialog.findViewById(R.id.singlegradelist);

			alertDialog.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

			ArrayList<String> daysList = new ArrayList<String>();
			daysList.add("5 days");
			daysList.add("10 days");
			daysList.add("15 days");
			daysList.add("20 days");
			daysList.add("25 days");
			daysList.add("All days");

			ArrayAdapter<String> adapter = new ArrayAdapter<String>(NotificationsnCoupans.this, android.R.layout.simple_list_item_1, daysList);
			Daylist.setAdapter(adapter);
			Daylist.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {

					days = arg0.getItemAtPosition(pos).toString();
					if (days.equalsIgnoreCase("All days")) {
						days = "90";
						daysbtn.setText("All days");
					} else {

						daysbtn.setText(days);
						days = days.substring(0, days.indexOf(" days"));
					}
					Editor editor = dayspref.edit();
					editor.putString("days", days);
					editor.commit();
					new GetNotifications().execute();
					alertDialog.cancel();

				}
			});

			alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

			alertDialog.show();
		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	private String getCouponList() {
		String resultString = "";
		copoun_arrlst.clear();
		try {
			JSONObject json = jsonParser.getJSONFromUrl(Constant.SERVER_URL + "GetCouponCodeByStoreID?Key=123456&StoreID=" + Store_Id);
			if (json.length() == 0) {
				showMesgDlg("Server not found.Please try again later.");
			}
			// Store_Id
			Log.w("getCouponList---->", "-------" + json);
			JSONObject ja = json.getJSONObject("GetCouponCodeByStoreIDResult");
			resultString = ja.getString("Message");
			JSONArray jarr = ja.getJSONArray("CouponList");
			for (int i = 0; i < jarr.length(); i++) {

				JSONObject c = jarr.getJSONObject(i);
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("Store_Id", c.getString("Store_Id"));
				if (!c.getString("Coupon_Code").equals("null")) {
					map.put("Coupon_Code", c.getString("Coupon_Code"));
				}
				if (!c.getString("Coupon_Code_Desc").equals("null")) {
					map.put("Coupon_Code_Desc", c.getString("Coupon_Code_Desc"));
				}
				if (!c.getString("Store_Name").equals("null")) {
					map.put("Store_Name", c.getString("Store_Name"));
				}
				map.put("End_Date", c.getString("End_Date"));
				copoun_arrlst.add(map);
			}
		} catch (Exception e) {

			e.printStackTrace();
		}

		return resultString;
	}

	public class GetCoupon extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			prgLoading.setVisibility(View.VISIBLE);
			coupans.setEnabled(false);
			notifications.setEnabled(false);
			dayslayout.setEnabled(false);
			storelayout.setEnabled(false);

		}

		@Override
		protected Void doInBackground(Void... params) {

			try {

				Result = getCouponList();
			} catch (Exception e) {

				e.printStackTrace();
			}
			Log.d("getCouponList-------->", "-------" + Result);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			coupans.setEnabled(true);
			notifications.setEnabled(true);
			dayslayout.setEnabled(true);
			storelayout.setEnabled(true);
			try {
				coupans.setBackgroundResource(R.drawable.couponsselected);
				notifications.setBackgroundResource(R.drawable.notifications);
				coupanListAdapter = new CoupanListAdapter(NotificationsnCoupans.this, copoun_arrlst);

				if (!copoun_arrlst.isEmpty()) {

					coupanslistView.setAdapter(coupanListAdapter);

					prgLoading.setVisibility(View.INVISIBLE);
				} else {
					String msg = "No active coupon codes.";
					prgLoading.setVisibility(View.GONE);
					showMesgDlgshow(msg);
				}
			} catch (Exception e) {

				e.printStackTrace();
			}

		}
	}

	public class GetAllStore extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			prgLoading.setVisibility(View.VISIBLE);
			coupans.setEnabled(false);
			notifications.setEnabled(false);
			dayslayout.setEnabled(false);
			storelayout.setEnabled(false);

		}

		@Override
		protected Void doInBackground(Void... params) {

			try {
				Result = GetAllStore();
			} catch (Exception e) {

				e.printStackTrace();
			}
			Log.d("getStoreList-------->", "-------" + Result);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			prgLoading.setVisibility(View.INVISIBLE);
			coupans.setEnabled(true);
			notifications.setEnabled(true);
			dayslayout.setEnabled(true);
			storelayout.setEnabled(true);

			storebtn.setText("Select Business name");
//			 new GetCoupon().execute();
			/*
			 * try { coupans.setBackgroundResource(R.drawable.couponsselected); coupanListAdapter = new CoupanListAdapter( NotificationsnCoupans.this,
			 * copoun_arrlst); if (!copoun_arrlst.isEmpty()) { coupanslistView.setAdapter(coupanListAdapter); prgLoading.setVisibility(View.INVISIBLE); } else {
			 * String msg = "No active coupon codes."; prgLoading.setVisibility(View.GONE); showMesgDlgshow(msg); } } catch (Exception e) { e.printStackTrace();
			 * }
			 */

		}
	}

	public class GetNotifications extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			prgLoading.setVisibility(View.VISIBLE);
			coupans.setEnabled(false);
			notifications.setEnabled(false);
			dayslayout.setEnabled(false);
			storelayout.setEnabled(false);
		}

		@Override
		protected Void doInBackground(Void... params) {

			try {
				Result = getNotificationList();
			} catch (Exception e) {

				e.printStackTrace();
			}
			Log.d("getCouponList-------->", "-------" + Result);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			coupans.setEnabled(true);
			notifications.setEnabled(true);
			dayslayout.setEnabled(true);
			storelayout.setEnabled(true);
			try {
				notificationListAdapter = new NotificationListAdapter(NotificationsnCoupans.this, sor_not_List);

				if (!sor_not_List.isEmpty()) {

					notifications.setBackgroundResource(R.drawable.notificationselected);
					coupans.setBackgroundResource(R.drawable.coupons);

					notificationslistView.setAdapter(notificationListAdapter);
					prgLoading.setVisibility(View.INVISIBLE);
				} else {
					String msg = "No active notifications.";
					prgLoading.setVisibility(View.GONE);
					showMesgDlgshow(msg);
				}
			} catch (Exception e) {

				e.printStackTrace();
			}

		}
	}

	private void showAlertDialogN() {

		try {

			final Dialog alertDialog = new Dialog(NotificationsnCoupans.this);

			alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
			alertDialog.setContentView(R.layout.clickhere);
			alertDialog.setCancelable(true);
			Typeface type = Typeface.createFromAsset(getAssets(), "Candara Bold Italic.ttf");
			Button dontallow = (Button) alertDialog.findViewById(R.id.dontallow);
			Button allow = (Button) alertDialog.findViewById(R.id.allow);

			TextView program = (TextView) alertDialog.findViewById(R.id.program);
			TextView alerttxtclick = (TextView) alertDialog.findViewById(R.id.alerttxtclick);

			alerttxtclick.setText("About Push Notification");
			program.setText("To receive notifications you need 'Push Notification Service' feature enabled. Do you want to allow application to use the Push Notification Service?");
			program.setTypeface(type);
			alerttxtclick.setTypeface(type);
			dontallow.setTypeface(type);
			allow.setTypeface(type);
			dontallow.setOnClickListener(new Button.OnClickListener() {

				@Override
				public void onClick(View v) {
					alertDialog.dismiss();
					// finish();
					PreferenceConnector.writeString(getApplicationContext(), PreferenceConnector.Is_Notification_Allowed, "false");
				}
			});

			allow.setOnClickListener(new Button.OnClickListener() {

				@Override
				public void onClick(View v) {

					try {

						new UpdateIsNotificationAllowed().execute();

					} catch (Exception e) {

						e.printStackTrace();
					}
					alertDialog.dismiss();
				}
			});
			alertDialog.getWindow().setGravity(Gravity.CENTER);
			alertDialog.show();
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	public class UpdateIsNotificationAllowed extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {

			prgLoading.setVisibility(View.VISIBLE);
		}

		@Override
		protected Void doInBackground(Void... params) {

			String status = UpdateIsNotificationAllowed();
			Log.e(TAG, "status-------" + status);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			PreferenceConnector.writeString(getApplicationContext(), PreferenceConnector.Is_Notification_Allowed, "true");
			new GetNotifications().execute();
		}
	}

	private String UpdateIsNotificationAllowed() {

		String status = null;
		try {
			JSONObject json = jsonParser.getJSONFromUrl(Constant.SERVER_URL + "UpdateIsNotificationAllowed" + "?User_Id=" + UserIdint
					+ "&Is_Notification_Allowed=true");
			Log.d(TAG, "UpdateIsNotificationAllowed---------" + json);
			JSONObject UpdateLocationConsentResult = json.getJSONObject("UpdateIsNotificationAllowedResult");
			status = UpdateLocationConsentResult.getString("Status");

		} catch (Exception e) {

			e.printStackTrace();
		}
		return status;
	}

	private void showAlertDialogC() {

		try {

			final Dialog alertDialog = new Dialog(NotificationsnCoupans.this);

			alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
			alertDialog.setContentView(R.layout.clickhere);
			alertDialog.setCancelable(true);
			Typeface type = Typeface.createFromAsset(getAssets(), "Candara Bold Italic.ttf");
			Button dontallow = (Button) alertDialog.findViewById(R.id.dontallow);
			Button allow = (Button) alertDialog.findViewById(R.id.allow);

			TextView program = (TextView) alertDialog.findViewById(R.id.program);
			TextView alerttxtclick = (TextView) alertDialog.findViewById(R.id.alerttxtclick);

			alerttxtclick.setText("About Receive Coupons Codes");
			program.setText("To receive coupons codes you need 'Receive Coupons Codes Service' feature enabled. Do you want to allow application to use the Receive Coupons Codes Service?");
			program.setTypeface(type);
			alerttxtclick.setTypeface(type);
			dontallow.setTypeface(type);
			allow.setTypeface(type);
			dontallow.setOnClickListener(new Button.OnClickListener() {

				@Override
				public void onClick(View v) {
					alertDialog.dismiss();
					// finish();
					PreferenceConnector.writeString(getApplicationContext(), PreferenceConnector.Is_Coupon_Allowed, "false");
				}
			});

			allow.setOnClickListener(new Button.OnClickListener() {

				@Override
				public void onClick(View v) {

					try {

						new UpdateIsCouponAllowed().execute();

					} catch (Exception e) {

						e.printStackTrace();
					}
					alertDialog.dismiss();
				}
			});
			alertDialog.getWindow().setGravity(Gravity.CENTER);
			alertDialog.show();
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	public class UpdateIsCouponAllowed extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {

			prgLoading.setVisibility(View.VISIBLE);
		}

		@Override
		protected Void doInBackground(Void... params) {

			String status = UpdateIsCouponAllowed();

			Log.e(TAG, "status-------" + status);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			PreferenceConnector.writeString(getApplicationContext(), PreferenceConnector.Is_Coupon_Allowed, "true");
			new GetAllStore().execute();
		}
	}

	private String GetAllStore() {

		String status = "";
		store_arrlst.clear();
		try {
			JSONObject json = jsonParser.getJSONFromUrl(Constant.SERVER_URL + "GetAllStore");
			Log.d(TAG, "GetAllStoreResult---------" + json);
			JSONObject GetAllStoreResult = json.getJSONObject("GetAllStoreResult");
			status = GetAllStoreResult.getString("Status");
			Log.d(TAG, "status---------" + status);

			JSONArray jarr = GetAllStoreResult.getJSONArray("StoreInfoNewList");
			for (int i = 0; i < jarr.length(); i++) {

				JSONObject c = jarr.getJSONObject(i);
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("Is_Store_Participating", c.getString("Is_Store_Participating"));
				map.put("Is_Store_Participating", c.getString("Is_Store_Participating"));
				map.put("Store_Id", c.getString("Store_Id"));
				map.put("Store_Name", c.getString("Store_Name"));
				storedata.add(c.getString("Store_Name"));
				store_arrlst.add(map);
			}
		} catch (Exception e) {

			e.printStackTrace();
		}
		return status;
	}

	private String UpdateIsCouponAllowed() {

		String status = null;
		try {
			JSONObject json = jsonParser.getJSONFromUrl(Constant.SERVER_URL + "UpdateIsCouponAllowed" + "?User_Id=" + UserIdint + "&Is_Coupon_Allowed=true");
			Log.d(TAG, "UpdateIsCouponAllowed---------" + json);
			JSONObject UpdateLocationConsentResult = json.getJSONObject("UpdateIsCouponAllowedResult");
			status = UpdateLocationConsentResult.getString("Status");

		} catch (Exception e) {

			e.printStackTrace();
		}
		return status;
	}

	public void showMesgDlgshow(String msg) {

		try {
			final Dialog alertDialog = new Dialog(NotificationsnCoupans.this);

			alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
			alertDialog.setContentView(R.layout.popupwindow);
			alertDialog.setCancelable(true);
			Button btnDismiss = (Button) alertDialog.findViewById(R.id.dismiss);
			TextView aboutprogram = (TextView) alertDialog.findViewById(R.id.aboutprogram);
			TextView alerttxt = (TextView) alertDialog.findViewById(R.id.alerttxtwindow);
			alerttxt.setTypeface(type);
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

	@SuppressLint("InlinedApi")
	public void showMesgDlg(String msg) {

		try {
			final Dialog alertDialog = new Dialog(NotificationsnCoupans.this);

			alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
			alertDialog.setContentView(R.layout.popupwindow);
			alertDialog.setCancelable(true);
			Button btnDismiss = (Button) alertDialog.findViewById(R.id.dismiss);
			TextView aboutprogram = (TextView) alertDialog.findViewById(R.id.aboutprogram);
			TextView alerttxt = (TextView) alertDialog.findViewById(R.id.alerttxtwindow);
			alerttxt.setTypeface(type);
			aboutprogram.setText(msg);
			aboutprogram.setTypeface(type);
			btnDismiss.setTypeface(type);
			btnDismiss.setOnClickListener(new Button.OnClickListener() {

				@Override
				public void onClick(View v) {
					alertDialog.dismiss();
					coupans.setEnabled(false);
					notifications.setEnabled(false);
				}
			});

			alertDialog.getWindow().setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			alertDialog.getWindow().setGravity(Gravity.CENTER);
			alertDialog.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String getNotificationList() {
		String resultString = "";
		sor_not_List.clear();
		try {

			// change by pankaj when online

			// http://68.190.187.106/SGBP_Push/SGBP_PushNotification.svc/GetPushNotificationbyDeviceNew?Device_UId="
			// + device_idString + "&Day=" + days

			JSONArray json1 = jsonParser.getJSONArrayFromUrl(Constant.SERVER_URL_1 + "GetPushNotificationbyDevice?Device_UId=" + device_idString + "&Day="
					+ days);
			try {
				if (json1.length() == 0) {
					showMesgDlg("Server not found.Please try again later.");
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Log.w("GetPushNotificationbyDevice---->", "-------" + json1);
			for (int i = 0; i < json1.length(); i++) {

				JSONObject c = json1.getJSONObject(i);
				HashMap<String, String> noti_map = new HashMap<String, String>();

				if (!c.getString("Message").equals("null")) {
					noti_map.put("Message", c.getString("Message"));
				}
				if (!c.getString("Notification_Date").equals("null")) {
					noti_map.put("Notification_Date", c.getString("Notification_Date"));
				}
				if (!c.getString("Device_UID").equals("null")) {
					noti_map.put("Device_UID", c.getString("Device_UID"));
				}
				Sort_noti_Bean sortBean = new Sort_noti_Bean(c.getString("Message"), c.getString("Notification_Date"), c.getString("Device_UID"));

				sor_not_List.add(sortBean);

				noti_arrlst.add(noti_map);
			}

			/*
			 * Collections.sort(sor_not_List, new Comparator<Sort_noti_Bean>() { public int compare(Sort_noti_Bean sortBean1, Sort_noti_Bean sortBean2) { return
			 * sortBean1.Notification_Date .compareTo(sortBean2.Notification_Date); } });
			 */
		} catch (Exception e) {

			e.printStackTrace();
		}

		return resultString;
	}

}
