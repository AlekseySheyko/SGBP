package com.widevision.sgbp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.widevision.sgbp.util.AlertDialogManager;
import com.widevision.sgbp.util.ConnectionDetector;
import com.widevision.sgbp.util.Constant;
import com.widevision.sgbp.util.Get_Deviceid;
import com.widevision.sgbp.util.JSONParser;

@SuppressLint("NewApi")
public class Clickheretoparticipate extends Activity {
	Button home;
	ProgressBar prgLoading;
	TextView titleView;

	String TAG = "Clickheretoparticipate", Result, Device_Info_Id, device_idString, Grade_Id, School_Id, Store_Id,
			Store_Name, Store_Add, Store_Phone, UserId;
	int UserIdint;
	JSONParser jsonParser = new JSONParser();

	boolean IsLocationServiceAllowed, getstoreid;

	double latitude, storelatitude, storeCorner1latitude, storeCorner2latitude, storeCorner3latitude,
			storeCorner4latitude;

	double longitude, storelongitude, storeCorner1longitude, storeCorner2longitude, storeCorner3longitude,
			storeCorner4longitude;

	double distance, mindistance, distancemeter1, distancemeter2, distancemeter3, distancemeter4;

	ArrayList<SortBean> distanceList = new ArrayList<SortBean>();

	ArrayList<HashMap<String, String>> getUserGradeinfo = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> getUserReginfo = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> saveStoredata = new ArrayList<HashMap<String, String>>();
	SharedPreferences pref, userdpref;
	private String Is_Location_Service_Allowed;

	// Asyntask
	AsyncTask<Void, Void, Void> mRegisterTask;

	// Alert dialog manager
	AlertDialogManager alert = new AlertDialogManager();
	//
	// Connection detector
	ConnectionDetector cd;

	Get_Deviceid get_dv_id;
	double dis_in_mile;
	private String Current_date;

	@Override
	public void onBackPressed() {
		startActivity(new Intent(Clickheretoparticipate.this, MainActivity.class));
		finish();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.clickheretoparticipate);
		pref = getSharedPreferences("device", Context.MODE_PRIVATE);

		get_dv_id = new Get_Deviceid(getApplicationContext());

		device_idString = get_dv_id.get_unique_deviceid();

		// TelephonyManager telephonyManager = (TelephonyManager)
		// getSystemService(Context.TELEPHONY_SERVICE);
		// device_idString = telephonyManager.getDeviceId();

		// device_idString = pref.getString("device_idString", null);
		userdpref = getSharedPreferences("userid", Context.MODE_PRIVATE);
		UserId = userdpref.getString("UserId", null);
		// Log.d(TAG, "UserId----------" + UserId);
		try {
			UserIdint = Integer.parseInt(UserId);
		} catch (Exception e1) {

			e1.printStackTrace();
		}
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		final Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);

		int month1 = month + 1;

		Current_date = month1 + "/" + day + "/" + year;

		prgLoading = (ProgressBar) findViewById(R.id.prgLoading);

		home = (Button) findViewById(R.id.home);
		titleView = (TextView) findViewById(R.id.title);

		Typeface type = Typeface.createFromAsset(getAssets(), "Candara Bold Italic.ttf");
		titleView.setTypeface(type);
		titleView.setText("PARTICIPATE");

		home.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				startActivity(new Intent(Clickheretoparticipate.this, MainActivity.class));
				finish();
			}
		});

		cd = new ConnectionDetector(getApplicationContext());

		// Check if Internet present
		if (!cd.isConnectingToInternet()) {
			// Internet Connection is not present
			alert.showAlertDialog(Clickheretoparticipate.this, "Oops!!",
					"Internet Connection seems to offline.Please check your network.", false);
			// stop executing code by return
			return;
		} else {

			try {

				My_Current_Location gpsTracker = new My_Current_Location(Clickheretoparticipate.this);

				if (gpsTracker.canGetLocation()) {
					Location location1 = gpsTracker.getLocation();
					// Log.d("location----------", "" + location1);
					 latitude = gpsTracker.getLocation().getLatitude();
					 longitude = gpsTracker.getLocation().getLongitude();

					// latitude = 22.75071;
					// longitude = 75.89542;
					
//					 latitude = 39.162461;
//					 longitude = -119.761963;

					// Reno City
					// latitude = 39.529633;
					// longitude = -119.813803;

					Log.e(TAG, "latitude-----" + latitude);
					Log.e(TAG, "longitude-----" + longitude);
					// Toast.makeText(getApplicationContext(),
					// "latitude->"+latitude +"\n" + "longitude->"+longitude,
					// 7000).show();
				} else {
					gpsTracker.showSettingsAlert();
				}

				Is_Location_Service_Allowed = PreferenceConnector.readString(getApplicationContext(),
						PreferenceConnector.Is_Location_Service_Allowed, "false");

				if (Is_Location_Service_Allowed.equalsIgnoreCase("true")) {
					new GetDatatoFindStoreId().execute();
				} else {
					showAlertDialog();
				}

				// new GetLocationConsent().execute();

			} catch (Exception e) {

				e.printStackTrace();
			}
		}
	}

	// public boolean isInternetOn() {
	// ConnectivityManager cm = (ConnectivityManager)
	// getSystemService(CONNECTIVITY_SERVICE);
	// NetworkInfo networkInfo = cm.getActiveNetworkInfo();
	// // if no network is available networkInfo will be null
	// // otherwise check if we are connected
	// if (networkInfo != null && networkInfo.isConnected()) {
	//
	// return true;
	// }
	//
	// return false;
	// }
	//
	// public boolean wifiConnectivity() {
	// ConnectivityManager manager = (ConnectivityManager)
	// getSystemService(CONNECTIVITY_SERVICE);
	// // For 3G check
	// boolean is3g = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
	// .isConnectedOrConnecting();
	// // For WiFi Check
	// boolean isWifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
	// .isConnectedOrConnecting();
	//
	// if (!is3g && !isWifi) {
	// return false;
	// } else {
	// return true;
	// }
	// }

	private void showAlertDialog() {

		try {

			final Dialog alertDialog = new Dialog(Clickheretoparticipate.this);

			alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
			alertDialog.setContentView(R.layout.clickhere);
			alertDialog.setCancelable(true);
			Typeface type = Typeface.createFromAsset(getAssets(), "Candara Bold Italic.ttf");
			Button dontallow = (Button) alertDialog.findViewById(R.id.dontallow);
			Button allow = (Button) alertDialog.findViewById(R.id.allow);

			TextView program = (TextView) alertDialog.findViewById(R.id.program);
			TextView alerttxtclick = (TextView) alertDialog.findViewById(R.id.alerttxtclick);

			alerttxtclick.setText("About Location");
			program.setText("To participate in the program you need 'Location Service' feature enabled. Do you want to allow application to use the Location Service?");
			program.setTypeface(type);
			alerttxtclick.setTypeface(type);
			dontallow.setTypeface(type);
			allow.setTypeface(type);
			dontallow.setOnClickListener(new Button.OnClickListener() {

				@Override
				public void onClick(View v) {
					alertDialog.dismiss();

					PreferenceConnector.writeString(getApplicationContext(),
							PreferenceConnector.Is_Location_Service_Allowed, "false");

					startActivity(new Intent(Clickheretoparticipate.this, MainActivity.class));

					finish();
				}
			});

			allow.setOnClickListener(new Button.OnClickListener() {

				@Override
				public void onClick(View v) {

					try {

						new UpdateLocationConsent().execute();

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

	// public class GetLocationConsent extends AsyncTask<Void, Void, Void> {
	//
	// @Override
	// protected void onPreExecute() {
	//
	// }
	//
	// @Override
	// protected Void doInBackground(Void... params) {
	//
	// IsLocationServiceAllowed = getLocationConsent();
	// Log.e(TAG, "Result-------" + Result);
	// Log.e(TAG, "IsLocationServiceAllowed-------"
	// + IsLocationServiceAllowed);
	//
	// return null;
	// }
	//
	// @Override
	// protected void onPostExecute(Void result) {
	// prgLoading.setVisibility(View.GONE);
	// try {
	// if (IsLocationServiceAllowed) {
	// new GetDatatoFindStoreId().execute();
	// } else {
	// showAlertDialog();
	// }
	// } catch (Exception e) {
	//
	// e.printStackTrace();
	// }
	// }
	// }

	public boolean getLocationConsent() {

		boolean IsLocationServiceAllowedreturn = false;
		JSONObject json = jsonParser.getJSONFromUrl(Constant.SERVER_URL + "GetLocationConsent?Device_UID="
				+ device_idString);

		// Log.d(TAG, "-GetLocationConsent-----------" + json);

		if (json.length() == 0) {
			showMesgDlg("Server not found.Please try again later.");
		}

		JSONObject GetLocationConsentResult = null;
		try {
			GetLocationConsentResult = json.getJSONObject("GetLocationConsentResult");
		} catch (Exception e1) {

			e1.printStackTrace();
		}

		try {
			boolean IsLocationServiceAllowed = GetLocationConsentResult.getBoolean("IsLocationServiceAllowed");
			IsLocationServiceAllowedreturn = IsLocationServiceAllowed;
		} catch (Exception e) {

			e.printStackTrace();

		}
		return IsLocationServiceAllowedreturn;
	}

	public class GetDatatoFindStoreId extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {

			prgLoading.setVisibility(View.VISIBLE);
		}

		@Override
		protected Void doInBackground(Void... params) {

			getstoreid = getDatatoFindStoreId();
			// Log.e(TAG, "getstoreid-------" + getstoreid);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			prgLoading.setVisibility(View.GONE);
			if (getstoreid) {
				showAlertDialog("Participate",
						"Please confirm by clicking on the button below to participate in the the School Give Back Program for'"
								+ Store_Name + "'.");
			} else {
				showMesgDlg1(Constant.storeparticipatemsg);
			}
		}
	}

	public class UpdateLocationConsent extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {

			prgLoading.setVisibility(View.VISIBLE);
		}

		@Override
		protected Void doInBackground(Void... params) {

			String status = UpdateLocationConsent();
			// Log.e(TAG, "status-------" + status);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			prgLoading.setVisibility(View.GONE);
			new GetDatatoFindStoreId().execute();

			PreferenceConnector.writeString(getApplicationContext(), PreferenceConnector.Is_Location_Service_Allowed,
					"true");
			// showAlertDialog("Message",
			// "Do you want participating in the program of Store?");

		}
	}

	private String UpdateLocationConsent() {

		String status = null;
		try {
			JSONObject json = jsonParser.getJSONFromUrl(Constant.SERVER_URL + "UpdateLocationConsent" + "?User_Id="
					+ UserIdint + "&Is_Location_Service_Allowed=true");
			// t
			// http://68.190.187.106/SGBP/SGBPWSJSON.svc/UpdateLocationConsent?User_Id={USER_ID}&Is_Location_Service_Allowed={IS_LOCATION_SERVICE_ALLOWED}
			// Log.d(TAG, "UpdateLocationConsent---------" + json);
			JSONObject UpdateLocationConsentResult = json.getJSONObject("UpdateLocationConsentResult");
			status = UpdateLocationConsentResult.getString("Status");
			// result = GetUserGradeinfoByDevice_UIDResult
			// .getJSONArray("UserRegGradeInfo");
		} catch (Exception e) {

			e.printStackTrace();
		}
		/*
		 * for (int i = 0; i < result.length(); i++) { try { JSONObject c =
		 * result.getJSONObject(i); Grade_Id = c.getString("Grade_Id");
		 * HashMap<String, String> map = new HashMap<String, String>();
		 * map.put("Grade_Id", Grade_Id); getUserGradeinfo.add(map); } catch
		 * (Exception e) { e.printStackTrace(); } }
		 */
		return status;
	}

	public boolean getDatatoFindStoreId() {

		boolean resultString = false;

		JSONObject json = jsonParser.getJSONFromUrl(Constant.SERVER_URL + "GetDatatoFindStoreId");
		// Log.d("GetDatatoFindStoreId--------", "" + json);
		if (json.length() == 0) {
			showMesgDlg("Server not found.Please try again later.");
		}
		JSONArray result = null;
		JSONObject GetDatatoFindStoreIdResult = null;
		try {
			GetDatatoFindStoreIdResult = json.getJSONObject("GetDatatoFindStoreIdResult");

			dis_in_mile = Double.parseDouble(GetDatatoFindStoreIdResult.getString("Distance"));

			Log.e("distanse from admin", "---------->" + dis_in_mile);
		} catch (Exception e1) {

			e1.printStackTrace();
		}

		try {
			result = GetDatatoFindStoreIdResult.getJSONArray("StoreInfo");
		} catch (Exception e1) {

			e1.printStackTrace();
		}
		JSONObject c = null;
		for (int i = 0; i < result.length(); i++) {

			try {
				c = result.getJSONObject(i);
			} catch (Exception e1) {

				e1.printStackTrace();
			}

			String Store_Corner1_Latitude = null;
			String Store_Corner1_Longitude = null;
			try {
				if (!c.getString("Store_Corner1_Latitude").toString().equals("null")) {
					Store_Corner1_Latitude = c.getString("Store_Corner1_Latitude");
					storeCorner1latitude = Double.parseDouble(Store_Corner1_Latitude);
				}
				if (!c.getString("Store_Corner1_Longitude").toString().equals("null")) {
					Store_Corner1_Longitude = c.getString("Store_Corner1_Longitude");

					storeCorner1longitude = Double.parseDouble(Store_Corner1_Longitude);
				}

			} catch (Exception e) {

				e.printStackTrace();
			}

			String Store_Corner2_Latitude = null;
			String Store_Corner2_Longitude = null;
			try {
				if (!c.getString("Store_Corner2_Latitude").toString().equals("null")) {
					Store_Corner2_Latitude = c.getString("Store_Corner2_Latitude");
					storeCorner2latitude = Double.parseDouble(Store_Corner2_Latitude);
				}
				if (!c.getString("Store_Corner2_Longitude").toString().equals("null")) {
					Store_Corner2_Longitude = c.getString("Store_Corner2_Longitude");

					storeCorner2longitude = Double.parseDouble(Store_Corner2_Longitude);
				}
			} catch (Exception e) {

				e.printStackTrace();
			}

			String Store_Corner3_Latitude = null;
			String Store_Corner3_Longitude = null;
			try {
				if (!c.getString("Store_Corner3_Latitude").toString().equals("null")) {
					Store_Corner3_Latitude = c.getString("Store_Corner3_Latitude");
					storeCorner3latitude = Double.parseDouble(Store_Corner3_Latitude);
				}
				if (!c.getString("Store_Corner3_Longitude").toString().equals("null")) {
					Store_Corner3_Longitude = c.getString("Store_Corner3_Longitude");

					storeCorner3longitude = Double.parseDouble(Store_Corner3_Longitude);
				}
			} catch (Exception e) {

				e.printStackTrace();
			}

			String Store_Corner4_Latitude = null;
			String Store_Corner4_Longitude = null;
			try {
				if (!c.getString("Store_Corner4_Latitude").toString().equals("null")) {
					Store_Corner4_Latitude = c.getString("Store_Corner4_Latitude");
					storeCorner4latitude = Float.parseFloat(Store_Corner4_Latitude);
				}
				if (!c.getString("Store_Corner4_Longitude").toString().equals("null")) {
					Store_Corner4_Longitude = c.getString("Store_Corner4_Longitude");

					storeCorner4longitude = Double.parseDouble(Store_Corner4_Longitude);
				}
			} catch (Exception e) {

				e.printStackTrace();
			}

			String Store_Address_Latitude = null;
			String Store_Address_Longitude = null;
			try {
				if (!c.getString("Store_Address_Latitude").toString().equals("null")) {
					Store_Address_Latitude = c.getString("Store_Address_Latitude");
				}
				storelatitude = Double.parseDouble(Store_Address_Latitude);

				if (!c.getString("Store_Address_Longitude").toString().equals("null")) {
					Store_Address_Longitude = c.getString("Store_Address_Longitude");
					storelongitude = Double.parseDouble(Store_Address_Longitude);

				}

			} catch (Exception e) {

				e.printStackTrace();
			}

			distancemeter1 = distance(latitude, longitude, storeCorner1latitude, storeCorner1longitude);
			distancemeter2 = distance(latitude, longitude, storeCorner2latitude, storeCorner2longitude);
			distancemeter3 = distance(latitude, longitude, storeCorner3latitude, storeCorner3longitude);
			distancemeter4 = distance(latitude, longitude, storeCorner4latitude, storeCorner4longitude);
			distance = distance(latitude, longitude, storelatitude, storelongitude);
			Log.e(TAG, "storelatitude=------------" + storelatitude + " & storelongitude=------------" + storelongitude);
			// Log.e(TAG, "storelongitude=------------" + storelongitude);

			// Log.e(TAG, "latitude=------------" + latitude);
			// Log.e(TAG, "longitude=------------" + longitude);

			Log.e(TAG, "distancemeter1=------------" + distancemeter1);
			Log.e(TAG, "distancemeter2=------------" + distancemeter2);
			Log.e(TAG, "distancemeter3=------------" + distancemeter3);
			Log.e(TAG, "distancemeter4=------------" + distancemeter4);
			Log.e(TAG, "distance---------" + distance);

			String Store_Id1 = null;
			try {
				Store_Id1 = c.getString("Store_Id");
			} catch (Exception e) {

				e.printStackTrace();
			}
			String Store_Name1 = null;
			try {
				Store_Name1 = c.getString("Store_Name");
			} catch (Exception e1) {

				e1.printStackTrace();
			}
			String Store_Add1 = null;
			try {
				Store_Add1 = c.getString("Store_Address_Line1");
			} catch (Exception e) {

				e.printStackTrace();
			}
			String Store_Phone1 = null;
			try {
				Store_Phone1 = c.getString("Store_Phone");
			} catch (Exception e) {

				e.printStackTrace();
			}

			if (distancemeter1 < dis_in_mile) {
				SortBean sortBean = new SortBean(distance, distancemeter1, distancemeter2, distancemeter3,
						distancemeter4, Store_Id1, Store_Name1, Store_Add1, Store_Phone1);

				distanceList.add(sortBean);
			} else if (distancemeter2 < dis_in_mile) {
				SortBean sortBean = new SortBean(distance, distancemeter1, distancemeter2, distancemeter3,
						distancemeter4, Store_Id1, Store_Name1, Store_Add1, Store_Phone1);

				distanceList.add(sortBean);
			} else if (distancemeter3 < dis_in_mile) {
				SortBean sortBean = new SortBean(distance, distancemeter1, distancemeter2, distancemeter3,
						distancemeter4, Store_Id1, Store_Name1, Store_Add1, Store_Phone1);

				distanceList.add(sortBean);
			} else if (distancemeter4 < dis_in_mile) {
				SortBean sortBean = new SortBean(distance, distancemeter1, distancemeter2, distancemeter3,
						distancemeter4, Store_Id1, Store_Name1, Store_Add1, Store_Phone1);

				distanceList.add(sortBean);
			} else if (distance < dis_in_mile) {
				SortBean sortBean = new SortBean(distance, distancemeter1, distancemeter2, distancemeter3,
						distancemeter4, Store_Id1, Store_Name1, Store_Add1, Store_Phone1);
				// Log.d(TAG, "distance---------" +
				// distance+"--------Store_Name1-------"+Store_Name1);
				// Log.d(TAG, "storelatitude=------------" + storelatitude);
				// Log.d(TAG, "storelongitude=------------" + storelongitude);
				// Log.d(TAG, "latitude=------------" + latitude);
				// Log.d(TAG, "longitude=------------" + longitude);
				distanceList.add(sortBean);
			}
			// Log.d(TAG, "distance---------" + distance+
			// "--------Store_Name1-------" + Store_Name1);
			// Log.d(TAG, "storelatitude=------------" + storelatitude);
			// Log.d(TAG, "storelongitude=------------" + storelongitude);

			Collections.sort(distanceList, new Comparator<SortBean>() {
				public int compare(SortBean sortBean1, SortBean sortBean2) {
					return sortBean1.distance.compareTo(sortBean2.distance);
				}
			});
			/*
			 * SortBean sortBean = new
			 * SortBean(distance,distancemeter1,distancemeter2
			 * ,distancemeter3,distancemeter4, Store_Id1, Store_Name1,
			 * Store_Add1, Store_Phone1); distanceList.add(sortBean);
			 * Collections.sort(distanceList, new Comparator<SortBean>() {
			 * public int compare(SortBean sortBean1, SortBean sortBean2) {
			 * return sortBean1.distance.compareTo(sortBean2.distance); } });
			 */
			// Log.e(TAG, "distanceList.size()----------------" +
			// distanceList.size());
			if (distanceList.size() == 0) {
				resultString = false;

			} else {
				resultString = true;
				Store_Id = distanceList.get(0).store_id;
				Store_Name = distanceList.get(0).store_name;
				Store_Add = distanceList.get(0).store_add;
				Store_Phone = distanceList.get(0).store_phone;
			}

		}

		return resultString;
	}

	public double distance(double lat1, double lon1, double lat2, double lon2) {
		double theta = lon1 - lon2;
		double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1))
				* Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
		dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = dist * 60 * 1.1515;
		String dst = "" + dist;

		String dst1 = "0.0";
		if (dst.length() > 4) {
			dst1 = dst.substring(0, dst.indexOf(".") + 3);
			System.out.print("1st---->" + dst1);
		} else if (dst.length() == 3) {
			dst1 = dst.substring(0, dst.indexOf(".") + 2);
			System.out.print("2nd---->" + dst1);
		} else if (dst.length() == 4) {
			dst1 = dst.substring(0, dst.indexOf(".") + 3);
			System.out.print("3rd---->" + dst1);
		}

		// String dst1 = dst.substring(0, dst.indexOf(".") + 6);

		double distanceval = Double.parseDouble(dst1);
		return (distanceval);
	}

	private double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}

	private double rad2deg(double rad) {
		return (rad * 180.0 / Math.PI);
	}

	@SuppressLint("InlinedApi")
	private void showAlertDialog(String message, String message1) {

		try {

			final Dialog alertDialog = new Dialog(Clickheretoparticipate.this);

			alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
			alertDialog.setContentView(R.layout.clickwindow);
			alertDialog.setCancelable(true);
			Typeface type = Typeface.createFromAsset(getAssets(), "Candara Bold Italic.ttf");
			Button no = (Button) alertDialog.findViewById(R.id.no);
			Button yes = (Button) alertDialog.findViewById(R.id.yes);
			TextView aboutprogram = (TextView) alertDialog.findViewById(R.id.aboutprogram);

			TextView alerttxt = (TextView) alertDialog.findViewById(R.id.aler12);
			alerttxt.setTypeface(type);
			aboutprogram.setTypeface(type);
			yes.setTypeface(type);
			no.setTypeface(type);

			aboutprogram.setText(message1);
			alerttxt.setText(message);

			no.setOnClickListener(new Button.OnClickListener() {

				@Override
				public void onClick(View v) {
					alertDialog.dismiss();
					new GetRegInfoNo().execute();

				}
			});

			yes.setOnClickListener(new Button.OnClickListener() {

				@Override
				public void onClick(View v) {
					alertDialog.dismiss();
					new GetRegInfo().execute();

				}
			});

			alertDialog.getWindow().setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			alertDialog.getWindow().setGravity(Gravity.CENTER);
			alertDialog.show();
		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	public class GetRegInfo extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			prgLoading.setVisibility(View.VISIBLE);
		}

		@Override
		protected Void doInBackground(Void... params) {
			getUserReginfoBYDevice_UID();
			// Log.e(TAG, "Result-------" + Result);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			new SaveProgramParticipationByDevice().execute();
		}
	}

	public class GetRegInfoNo extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {

			prgLoading.setVisibility(View.VISIBLE);
		}

		@Override
		protected Void doInBackground(Void... params) {
			getUserReginfoBYDevice_UID();
			// Log.e(TAG, "Result-------" + Result);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			new SaveProgramParticipationByDeviceNo().execute();
		}
	}

	public void getUserGradeinfoByDevice_UID() {
		JSONArray result = null;
		try {
			JSONObject json = jsonParser.getJSONFromUrl(Constant.SERVER_URL
					+ "GetUserGradeinfoByDevice_UID"
					+ "?Key=123456&Device_UID=" + device_idString);

//			Log.d(TAG,	"-GetUserGradeinfoByDevice_UID----GetUserGradeinfoByDevice_UIDResult-------"
//							+ json);
			JSONObject GetUserGradeinfoByDevice_UIDResult = json
					.getJSONObject("GetUserGradeinfoByDevice_UIDResult");

			result = GetUserGradeinfoByDevice_UIDResult
					.getJSONArray("UserRegGradeInfo");
		} catch (Exception e) {

			e.printStackTrace();
		}
		for (int i = 0; i < result.length(); i++) {
			try {
				JSONObject c = result.getJSONObject(i);
				Grade_Id = c.getString("Grade_Id");
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("Grade_Id", Grade_Id);
				getUserGradeinfo.add(map);

			} catch (Exception e) {

				e.printStackTrace();
			}
		}
	}

	public void getUserReginfoBYDevice_UID() {

		try {
			JSONArray result = null;

			JSONObject json = jsonParser.getJSONFromUrl(Constant.SERVER_URL + "GetUserReginfoBYDevice_UID"
					+ "?Device_UID=" + device_idString);
			if (json.length() == 0) {
				showMesgDlg("Server not found.Please try again later.");
			}
			// Log.d("-GetUserReginfoBYDevice_UID----------->", "-----------" +
			// json);
			JSONObject GetUserReginfoBYDevice_UIDResult = json.getJSONObject("GetUserReginfoBYDevice_UIDResult");

			result = GetUserReginfoBYDevice_UIDResult.getJSONArray("UserRegInfo");

			for (int i = 0; i < result.length(); i++) {

				JSONObject c = result.getJSONObject(i);
				String Device_Info_Id2 = c.getString("Device_Info_Id");
				String School_Id2 = c.getString("School_Id");
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("School_Id", School_Id2);
				map.put("Device_Info_Id2", Device_Info_Id2);
				Device_Info_Id = Device_Info_Id2;
				School_Id = School_Id2;
//				Log.d(TAG, "-School_Id----" + School_Id + "------School_Id2-------" + School_Id2);
				getUserReginfo.add(map);
			}
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	public class SaveProgramParticipationByDevice extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {

			prgLoading.setVisibility(View.VISIBLE);
		}

		@Override
		protected Void doInBackground(Void... params) {

			saveProgramParticipationByDevice();

//			Log.e(TAG, "Result-------" + Result);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			prgLoading.setVisibility(View.GONE);
			showMesgDlg1("Thank you for participating in the School Give Back Program.");
		}
	}

	public class SaveProgramParticipationByDeviceNo extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			prgLoading.setVisibility(View.VISIBLE);
		}

		@Override
		protected Void doInBackground(Void... params) {
			saveProgramParticipationByDeviceNo();
//			Log.e(TAG, "Result-------" + Result);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			prgLoading.setVisibility(View.GONE);
			showMesgDlg1("You selected not to participate in the School Give Back Program.");
		}
	}

	public void saveProgramParticipationByDeviceNo() {

		try {
			JSONObject json = jsonParser.getJSONFromUrl(Constant.SERVER_URL + "SaveProgramParticipationByDevice"
					+ "?Key=" + "123456" + "&Device_Info_Id=" + Device_Info_Id + "&Device_UID=" + device_idString
					+ "&School_Id=" + School_Id + "&Store_Id=" + Store_Id + "&Participation_DateTime=" + Current_date
					+ "&Clicked_To_Participate=" + "false");
//			Log.d(TAG, "-saveUserGrade-----------" + json);
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	public void saveProgramParticipationByDevice() {

		try {

			String uuurrl = Constant.SERVER_URL + "SaveProgramParticipationByDevice" + "?Key=" + "123456"
					+ "&Device_Info_Id=" + Device_Info_Id + "&Device_UID=" + device_idString + "&School_Id="
					+ School_Id + "&Store_Id=" + Store_Id + "&Participation_DateTime=" + Current_date
					+ "&Clicked_To_Participate=" + "true";

			Log.e("SaveProgramParticipationByDevice", "---->" + uuurrl);

			JSONObject json = jsonParser.getJSONFromUrl(uuurrl);
//			Log.d(TAG, "-saveUserGrade-----------" + json);
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	public void showMesgDlg1(String msg) {
		try {
			final Dialog alertDialog = new Dialog(Clickheretoparticipate.this);

			alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
			alertDialog.setContentView(R.layout.clickpopup);
			alertDialog.setCancelable(true);
			Typeface type = Typeface.createFromAsset(getAssets(), "Candara Bold Italic.ttf");
			Button btnDismiss = (Button) alertDialog.findViewById(R.id.okmsg);
			TextView aboutprogram = (TextView) alertDialog.findViewById(R.id.aboutprogram);

			TextView alerttxtwindow = (TextView) alertDialog.findViewById(R.id.alerttxtpopup);
			alerttxtwindow.setText("Participate");

			aboutprogram.setText(msg);
			alerttxtwindow.setTypeface(type);
			aboutprogram.setTypeface(type);
			btnDismiss.setTypeface(type);
			btnDismiss.setOnClickListener(new Button.OnClickListener() {

				@Override
				public void onClick(View v) {
					alertDialog.dismiss();

					startActivity(new Intent(Clickheretoparticipate.this, MainActivity.class));
					finish();

				}
			});
			alertDialog.getWindow().setGravity(Gravity.CENTER);
			alertDialog.show();
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	public void showMesgDlg(String msg) {
		try {

			final Dialog alertDialog = new Dialog(Clickheretoparticipate.this);

			alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
			alertDialog.setContentView(R.layout.popupwindow);
			alertDialog.setCancelable(true);
			Typeface type = Typeface.createFromAsset(getAssets(), "Candara Bold Italic.ttf");
			Button btnDismiss = (Button) alertDialog.findViewById(R.id.dismiss);
			TextView aboutprogram = (TextView) alertDialog.findViewById(R.id.aboutprogram);

			TextView alerttxtwindow = (TextView) alertDialog.findViewById(R.id.alerttxtwindow);
			alerttxtwindow.setText("Participate");

			aboutprogram.setText(msg);
			alerttxtwindow.setTypeface(type);
			aboutprogram.setTypeface(type);
			btnDismiss.setTypeface(type);
			btnDismiss.setOnClickListener(new Button.OnClickListener() {

				@Override
				public void onClick(View v) {
					alertDialog.dismiss();
					prgLoading.setVisibility(View.GONE);
				}
			});
			alertDialog.getWindow().setGravity(Gravity.CENTER);
			alertDialog.show();
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

}
