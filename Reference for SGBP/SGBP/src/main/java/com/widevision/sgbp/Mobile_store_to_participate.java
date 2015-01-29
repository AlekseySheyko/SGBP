package com.widevision.sgbp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
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
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.widevision.sgbp.util.AlertDialogManager;
import com.widevision.sgbp.util.ConnectionDetector;
import com.widevision.sgbp.util.Constant;
import com.widevision.sgbp.util.Get_Deviceid;
import com.widevision.sgbp.util.JSONCreation;
import com.widevision.sgbp.util.JSONParser;

@SuppressLint("NewApi")
public class Mobile_store_to_participate extends FragmentActivity {
	Dialog dialog1;
	Button home;

	// static final LatLng HAMBURG = new LatLng(53.558, 9.927);
	// static final LatLng KIEL = new LatLng(53.551, 9.993);

	boolean IsLocationServiceAllowed;

	TextView titleView;

	ListView storelistview;

	ProgressBar prgLoading;

	String TAG = "Mobile_store_to_participate", Result, title, phone, UserId, device_idString;
	// miles = "2";
	int UserIdint;
	JSONParser jsonParser = new JSONParser();
	JSONCreation jsonCreation;
	boolean IsStorePaticipating;
	ArrayList<HashMap<String, String>> getParticipatingBusinessInfo;

	double latitude, storelatitude;
	double longitude, storelongitude, distance, mindistance;
	ArrayList<Double> distanceList = new ArrayList<Double>();

	ArrayList<String> storeidlist = new ArrayList<String>();
	ArrayList<String> storenamelist = new ArrayList<String>();

	Typeface type;
	Get_Deviceid get_dv_id;
	StoreBean bean;
	StoreListAdapter storeListAdapter;

	// Asyntask
	AsyncTask<Void, Void, Void> mRegisterTask;

	// Alert dialog manager
	AlertDialogManager alert = new AlertDialogManager();

	// Connection detector
	ConnectionDetector cd;

	ArrayList<Object> storelistArrayList = new ArrayList<Object>();
	SharedPreferences pref, userdpref, prefd;
	private String Is_Location_Service_Allowed;
	private String Device_Info_Id;
	private String School_Id;
	String Store_Id, Current_date;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mobile_store_to_participate);

		titleView = (TextView) findViewById(R.id.title);
		storelistview = (ListView) findViewById(R.id.storelistview);
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		prefd = getSharedPreferences("device", Context.MODE_PRIVATE);

		get_dv_id = new Get_Deviceid(getApplicationContext());

		device_idString = get_dv_id.get_unique_deviceid();

		pref = getSharedPreferences("milevalue", Context.MODE_PRIVATE);
		userdpref = getSharedPreferences("userid", Context.MODE_PRIVATE);
		UserId = userdpref.getString("UserId", null);

		try {
			UserIdint = Integer.parseInt(UserId);
		} catch (Exception e1) {

			e1.printStackTrace();
		}

		final Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);

		int month1 = month + 1;

		Current_date = month1 + "/" + day + "/" + year;

		My_Current_Location gpsTracker = new My_Current_Location(Mobile_store_to_participate.this);

		if (gpsTracker.canGetLocation()) {
			try {
				Location location1 = gpsTracker.getLocation();
//				Log.d("location----------", "" + location1);

				 latitude = gpsTracker.getLocation().getLatitude();
				 longitude = gpsTracker.getLocation().getLongitude();

				// latitude = 22.75071;
				// longitude = 75.89542;

//				 latitude = 39.162461;
//				 longitude = -119.761963;

				// Reno City
				// latitude = 39.529633;
				// longitude = -119.813803;

				Log.d(TAG, "latitude---------" + latitude);
				Log.d(TAG, "longitude-------" + longitude);
				// Toast.makeText(getApplicationContext(), "latitude->"+latitude
				// +"\n" + "longitude->"+longitude, 7000).show();
			} catch (Exception e) {

				e.printStackTrace();
			}
		} else {
			try {
				gpsTracker.showSettingsAlert();
			} catch (Exception e) {

				e.printStackTrace();
			}
		}

		type = Typeface.createFromAsset(getAssets(), "Candara Bold Italic.ttf");
		titleView.setTypeface(type);
		titleView.setText("  MOBILE BUSINESSES ");

		home = (Button) findViewById(R.id.home);

		prgLoading = (ProgressBar) findViewById(R.id.prgLoading);

		home.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				startActivity(new Intent(Mobile_store_to_participate.this, MainActivity.class));
				finish();
			}
		});

		storelistview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

				Store_Id = storeidlist.get(arg2);

//				Log.d("Store_Id----------", "" + Store_Id);

				String stname = storenamelist.get(arg2);
//				Log.d("store_name----------", "" + stname);

				showAlertDialog("Participate",
						"Please confirm by clicking on the button below to participate in the the School Give Back Program for'"
								+ stname + "'.");

				// new SaveProgramParticipationByDevice().execute();

			}
		});

		cd = new ConnectionDetector(getApplicationContext());

		// Check if Internet present
		if (!cd.isConnectingToInternet()) {
			// Internet Connection is not present
			alert.showAlertDialog(Mobile_store_to_participate.this, "Oops!!",
					"Internet Connection seems to offline.Please check your network.", false);
			// stop executing code by return
			return;
		} else {
			try {
				// new GetLocationConsent().execute();

				Is_Location_Service_Allowed = PreferenceConnector.readString(getApplicationContext(),
						PreferenceConnector.Is_Location_Service_Allowed, "false");

				if (Is_Location_Service_Allowed.equalsIgnoreCase("true")) {
					new GetParticipatingBusinessInfo().execute();

				} else {
					showAlertDialog();
				}
			} catch (Exception e) {

				e.printStackTrace();
			}
		}

	}

	private void showAlertDialog() {

		try {

			final Dialog alertDialog = new Dialog(Mobile_store_to_participate.this);

			alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
			alertDialog.setContentView(R.layout.clickhere);
			alertDialog.setCancelable(true);
			Typeface type = Typeface.createFromAsset(getAssets(), "Candara Bold Italic.ttf");
			Button dontallow = (Button) alertDialog.findViewById(R.id.dontallow);
			Button allow = (Button) alertDialog.findViewById(R.id.allow);

			TextView program = (TextView) alertDialog.findViewById(R.id.program);
			TextView alerttxtclick = (TextView) alertDialog.findViewById(R.id.alerttxtclick);
			program.setText("The Location Service feature should be enabled to participate in the School Give Back Program. Do you want to allow application to use the Location Service?");
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
					startActivity(new Intent(Mobile_store_to_participate.this, MainActivity.class));

					finish();
				}
			});

			allow.setOnClickListener(new Button.OnClickListener() {

				@Override
				public void onClick(View v) {

					try {
						new GetParticipatingBusinessInfo().execute();
						new UpdateLocationConsent().execute();

						/*
						 * My_Current_Location gpsTracker = new
						 * My_Current_Location( Findparticipatingbusiness.this);
						 * if (gpsTracker.canGetLocation()) { Location location1
						 * = gpsTracker.getLocation();
						 * Log.d("location----------", "" + location1); latitude
						 * = gpsTracker.getLocation().getLatitude(); longitude =
						 * gpsTracker.getLocation().getLongitude(); } else {
						 * gpsTracker.showSettingsAlert(); }
						 */
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

	public class UpdateLocationConsent extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {

			prgLoading.setVisibility(View.VISIBLE);
		}

		@Override
		protected Void doInBackground(Void... params) {

			String status = UpdateLocationConsent();
//			Log.e(TAG, "status-------" + status);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			prgLoading.setVisibility(View.GONE);
			PreferenceConnector.writeString(getApplicationContext(), PreferenceConnector.Is_Location_Service_Allowed,
					"true");
		}
	}

	private String UpdateLocationConsent() {

		String status = null;
		try {
			JSONObject json = jsonParser.getJSONFromUrl(Constant.SERVER_URL + "UpdateLocationConsent" + "?User_Id="
					+ UserIdint + "&Is_Location_Service_Allowed=true");
//			Log.d(TAG, "UpdateLocationConsent---------" + json);
			JSONObject UpdateLocationConsentResult = json.getJSONObject("UpdateLocationConsentResult");
			status = UpdateLocationConsentResult.getString("Status");
		} catch (Exception e) {

			e.printStackTrace();
		}
		return status;
	}

	/**/

	@Override
	public void onBackPressed() {

		startActivity(new Intent(Mobile_store_to_participate.this, MainActivity.class));

		finish();
	}

	public static int getPixelsFromDp(Context context, float dp) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dp * scale + 0.5f);
	}

	public class GetParticipatingBusinessInfo extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {

			prgLoading.setVisibility(View.VISIBLE);

			jsonCreation = new JSONCreation();
		}

		@Override
		protected Void doInBackground(Void... params) {

			getParticipatingBusinessInfo();

			getUserReginfoBYDevice_UID();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			prgLoading.setVisibility(View.GONE);

			storeListAdapter = new StoreListAdapter(Mobile_store_to_participate.this, getParticipatingBusinessInfo);
			storelistview.setAdapter(storeListAdapter);

		}
	}

	public void getParticipatingBusinessInfo() {

		getParticipatingBusinessInfo = new ArrayList<HashMap<String, String>>();
		JSONObject json = jsonParser.getJSONFromUrl(Constant.SERVER_URL + "GetAllNonPhysicalStore");

		JSONArray result = null;
//		Log.d(TAG, "GetAllNonPhysicalStore-------" + json);

		JSONObject GetParticipatingBusinessInfoResult = null;
		JSONObject c = null;
		try {
			GetParticipatingBusinessInfoResult = json.getJSONObject("GetAllNonPhysicalStoreResult");
		} catch (Exception e2) {

			e2.printStackTrace();
		}

		try {
			result = GetParticipatingBusinessInfoResult.getJSONArray("StoreInfo");
		} catch (Exception e3) {

			e3.printStackTrace();
		}
		for (int i = 0; i < result.length(); i++) {

			try {
				c = result.getJSONObject(i);
			} catch (Exception e2) {

				e2.printStackTrace();
			}

			try {
//				Log.d(TAG,
//						"--Store_Name-------" + c.getString("Store_Name") + "----Is_Store_Participating----"
//								+ c.getString("Is_Store_Location_Physical"));
			} catch (Exception e2) {

				e2.printStackTrace();
			}

			String IsStorePaticipating = null;
			try {
				IsStorePaticipating = c.getString("Is_Store_Location_Physical");
			} catch (Exception e2) {

				e2.printStackTrace();
			}

			// change by pankaj below if
			// (IsStorePaticipating.equalsIgnoreCase("true"))

			if (IsStorePaticipating.equalsIgnoreCase("false")) {

				String Store_Id1 = null;
				try {
					Store_Id1 = c.getString("Store_Id");
					storeidlist.add(Store_Id1);
				} catch (Exception e) {

					e.printStackTrace();
				}

				String Store_Phone = null;
				try {

					Store_Phone = c.getString("Store_Phone");

				} catch (Exception e1) {

					e1.printStackTrace();
				}
				String Store_Name = null;
				try {

					Store_Name = c.getString("Store_Name");

					storenamelist.add(Store_Name);

				} catch (Exception e1) {

					e1.printStackTrace();
				}
				String Store_Address_Latitude = "";
				try {

					Store_Address_Latitude = c.getString("Store_Address_Latitude");
					if (!Store_Address_Latitude.equals("")) {
						storelatitude = Double.parseDouble(Store_Address_Latitude);
					} else {
						storelatitude = 0;
					}

				} catch (Exception e1) {

					e1.printStackTrace();
				}
				String Store_Address_Longitude = "";
				try {

					Store_Address_Longitude = c.getString("Store_Address_Longitude");

					if (!Store_Address_Longitude.equals("")) {
						storelongitude = Double.parseDouble(Store_Address_Longitude);
					} else {
						storelongitude = 0;
					}

				} catch (Exception e1) {

					e1.printStackTrace();
				}

				String Store_Address_Line1 = "";
				try {

					Store_Address_Line1 = c.getString("Store_Address_Line1");

				} catch (Exception e1) {

					e1.printStackTrace();
				}

				String Store_City = null;
				try {

					Store_City = c.getString("Store_City");

				} catch (Exception e1) {

					e1.printStackTrace();
				}

				String Store_State = null;
				try {

					Store_State = c.getString("Store_State");

				} catch (Exception e1) {

					e1.printStackTrace();
				}

				String Store_Zip = null;
				try {

					Store_Zip = c.getString("Store_Zip");

				} catch (Exception e1) {

					e1.printStackTrace();
				}

				distance = distance(latitude, longitude, storelatitude, storelongitude);
				Double newdistance = null;
//				Log.d(TAG, "newdistance-------" + newdistance);

//				Log.d("distance----------", Store_Name + "---------" + distance);
				newdistance = distance;
				// if (distance > Float.parseFloat(miles)) {
				// } else {

//				Log.d("distance----------", "" + distance);
				newdistance = distance;
				distanceList.add(newdistance);

				jsonCreation.jsonget(Store_Address_Latitude, Store_Address_Longitude, Store_Name, Store_Phone,
						Store_Address_Line1, i, Store_City + "," + Store_State + " " + Store_Zip, "");
				float store_lat = 0;
				float store_lon = 0;
				try {
					store_lat = Float.parseFloat(Store_Address_Latitude);
					store_lon = Float.parseFloat(Store_Address_Longitude);
				} catch (Exception e) {

					e.printStackTrace();
				}
//				Log.d(TAG, "Store_Name--------Store_Address_Latitude------Store_Address_Longitude" + Store_Name
//						+ "----------" + store_lat + "Store_Address_Longitude-----------" + store_lon);
				HashMap<String, String> map = new HashMap<String, String>();
				try {
					if (!c.getString("Store_Name").equals("null")) {
						map.put("Store_Name", Store_Name);
					}
				} catch (Exception e) {

					e.printStackTrace();
				}

				map.put("Distance", "" + newdistance);

				try {
					if (!c.getString("Store_Phone").equals("null")) {
						map.put("Store_Phone", Store_Phone);
					}
				} catch (Exception e) {

					e.printStackTrace();
				}
				map.put("Store_Address_Latitude", Store_Address_Latitude);
				map.put("Store_Address_Longitude", Store_Address_Longitude);
				try {
					if (!c.getString("Store_Address_Line1").equals("null")) {
						map.put("Store_Address_Line1", Store_Address_Line1);
					}
				} catch (Exception e) {

					e.printStackTrace();
				}

				try {
					if (!c.getString("Store_City").equals("null")) {
						map.put("Store_City_state_zip", Store_City + "," + Store_State + " " + Store_Zip);
					}
				} catch (Exception e) {

					e.printStackTrace();
				}

				getParticipatingBusinessInfo.add(map);

				// Collections.sort(getParticipatingBusinessInfo,
				// new Comparator<HashMap<String, String>>() {
				// @Override
				// public int compare(HashMap<String, String> a,
				// HashMap<String, String> b) {
				// return a.get("Store_Name").compareTo(b.get("Store_Name"));
				// }
				// });

				// }
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
//			Log.d("-GetUserReginfoBYDevice_UID----------->", "-----------" + json);
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
				// getUserReginfo.add(map);
			}
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	public class StoreListAdapter extends BaseAdapter {
		Context ctx;
		LayoutInflater lInflater;
		ArrayList<HashMap<String, String>> data;
		String miles;

		StoreListAdapter(Context context, ArrayList<HashMap<String, String>> data) {
			ctx = context;
			this.data = data;
			// this.miles = miles;
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
		public View getView(final int position, View convertView, ViewGroup parent) {
			final String address_line1, address_line2;

			View view = convertView;
			view = lInflater.inflate(R.layout.mobile_storelistitem, parent, false);

			Typeface type = Typeface.createFromAsset(ctx.getAssets(), "Candara Bold Italic.ttf");
			Typeface type1 = Typeface.createFromAsset(ctx.getAssets(), "Papyrus-Regular.ttf");

			TextView store_name, store_phn;
			final TextView store_add;

			store_name = (TextView) view.findViewById(R.id.store_name);
			store_add = (TextView) view.findViewById(R.id.store_add);
			store_phn = (TextView) view.findViewById(R.id.store_phn);

			store_name.setTypeface(type);
			store_add.setTypeface(type);
			store_phn.setTypeface(type);

//			Log.d("data---------", "" + data.size() + "-----------" + data.get(position).get("Store_Name"));

			store_name.setText(data.get(position).get("Store_Name"));

			address_line1 = data.get(position).get("Store_Address_Line1").toString();
			address_line2 = data.get(position).get("Store_City_state_zip").toString();

			store_add.setText(address_line1 + "\n" + address_line2);
			float textWidth = store_add.getPaint().measureText(address_line1);

			store_phn.setText(data.get(position).get("Store_Phone"));

			Display display = getWindowManager().getDefaultDisplay();
			@SuppressWarnings("deprecation")
			int width = display.getWidth();
//			Log.d(TAG, "textWidth-------------" + textWidth + "width-----------" + width);

			return view;
		}

		// protected void arroewDlg(String store_add) {
		//
		// try {
		// final Dialog alertDialog = new
		// Dialog(Mobile_store_to_participate.this);
		//
		// alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// alertDialog.getWindow().setBackgroundDrawable(new
		// ColorDrawable(android.graphics.Color.TRANSPARENT));
		// alertDialog.setContentView(R.layout.arrowpopup);
		// alertDialog.setCancelable(true);
		// type = Typeface.createFromAsset(getAssets(),
		// "Candara Bold Italic.ttf");
		// Button okmsg = (Button) alertDialog.findViewById(R.id.okmsg);
		// TextView aboutprogram = (TextView)
		// alertDialog.findViewById(R.id.aboutprogram);
		// aboutprogram.setText(store_add);
		// aboutprogram.setTypeface(type);
		// okmsg.setTypeface(type);
		// okmsg.setOnClickListener(new Button.OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// alertDialog.dismiss();
		// }
		// });
		// alertDialog.getWindow().setGravity(Gravity.CENTER);
		// alertDialog.show();
		// } catch (Exception e) {
		//
		// e.printStackTrace();
		// }
		// }
	}

	public double distance(double lat1, double lon1, double lat2, double lon2) {
		double theta = lon1 - lon2;
		double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1))
				* Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
		dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = (dist * 60 * 1.1515);

//		Log.e("dist", "--->" + dist);
		// dist=dist*1.609344;// just for testing

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

		// String dst1 = dst.substring(0, dst.indexOf(".") + 3);

		double distanceval = Double.parseDouble(dst1);
//		Log.d(TAG, "distanceval-------" + distanceval);
		return (distanceval);
	}

	private double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}

	private double rad2deg(double rad) {
		return (rad * 180.0 / Math.PI);
	}

	@SuppressLint("InlinedApi")
	public void showMesgDlg(String msg) {
		try {
			final Dialog alertDialog = new Dialog(Mobile_store_to_participate.this);

			alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
			alertDialog.setContentView(R.layout.popupwindow);
			alertDialog.setCancelable(true);
			Typeface type = Typeface.createFromAsset(getAssets(), "Candara Bold Italic.ttf");
			Button btnDismiss = (Button) alertDialog.findViewById(R.id.dismiss);
			TextView aboutprogram = (TextView) alertDialog.findViewById(R.id.aboutprogram);
			TextView alerttxtwindow = (TextView) alertDialog.findViewById(R.id.alerttxtwindow);
			aboutprogram.setText(msg);
			aboutprogram.setTypeface(type);
			alerttxtwindow.setTypeface(type);
			btnDismiss.setTypeface(type);
			btnDismiss.setOnClickListener(new Button.OnClickListener() {

				@Override
				public void onClick(View v) {
					alertDialog.dismiss();

				}
			});
			alertDialog.getWindow().setGravity(Gravity.CENTER);
			alertDialog.show();
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

	public void saveProgramParticipationByDevice() {

		try {

			String uuurrrll = Constant.SERVER_URL + "SaveProgramParticipationByDevice" + "?Key=" + "123456"
					+ "&Device_Info_Id=" + Device_Info_Id + "&Device_UID=" + device_idString + "&School_Id="
					+ School_Id + "&Store_Id=" + Store_Id + "&Participation_DateTime=" + Current_date
					+ "&Clicked_To_Participate=" + "true";

			Log.e("Mobile store SaveProgramParticipationByDevice", "---->" + uuurrrll);

			JSONObject json = jsonParser.getJSONFromUrl(Constant.SERVER_URL + "SaveProgramParticipationByDevice"
					+ "?Key=" + "123456" + "&Device_Info_Id=" + Device_Info_Id + "&Device_UID=" + device_idString
					+ "&School_Id=" + School_Id + "&Store_Id=" + Store_Id + "&Participation_DateTime=" + Current_date
					+ "&Clicked_To_Participate=" + "true");
//			Log.d(TAG, "-saveUserGrade-----------" + json);
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	public void showMesgDlg1(String msg) {
		try {
			final Dialog alertDialog = new Dialog(Mobile_store_to_participate.this);

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

					startActivity(new Intent(Mobile_store_to_participate.this, MainActivity.class));
					finish();

				}
			});
			alertDialog.getWindow().setGravity(Gravity.CENTER);
			alertDialog.show();
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	@SuppressLint("InlinedApi")
	private void showAlertDialog(String message, String message1) {

		try {

			final Dialog alertDialog = new Dialog(Mobile_store_to_participate.this);

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
					// new GetRegInfoNo().execute();
					showMesgDlg1("You selected not to participate in the School Give Back Program.");

				}
			});

			yes.setOnClickListener(new Button.OnClickListener() {

				@Override
				public void onClick(View v) {
					alertDialog.dismiss();
					// new GetRegInfo().execute();

					new SaveProgramParticipationByDevice().execute();

				}
			});

			alertDialog.getWindow().setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			alertDialog.getWindow().setGravity(Gravity.CENTER);
			alertDialog.show();
		} catch (Exception e) {

			e.printStackTrace();
		}

	}

}
