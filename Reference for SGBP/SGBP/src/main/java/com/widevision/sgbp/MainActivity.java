package com.widevision.sgbp;

import static com.widevision.sgbp.CommonUtilities.DISPLAY_MESSAGE_ACTION;
import static com.widevision.sgbp.CommonUtilities.SENDER_ID;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gcm.GCMRegistrar;
import com.widevision.sgbp.util.AlertDialogManager;
import com.widevision.sgbp.util.ConnectionDetector;
import com.widevision.sgbp.util.Constant;
import com.widevision.sgbp.util.Get_Deviceid;
import com.widevision.sgbp.util.JSONParser;

@SuppressLint("InlinedApi")
public class MainActivity extends Activity {
	Button clickheretoparticipate, findparticipatingbusiness, notificationncoupans, regnmanageaccnt, aboutparogram;

	JSONParser jsonParser = new JSONParser();
	SharedPreferences prefs;
	String checkDeviceResult, device_idString, TAG = "MainActivity", Status, aboutprogramstr, device_NameString,
			device_TypeString, Resultdevice, Messagedevice, Device_UID, Device_Info_Id;
	ProgressBar prgLoading;
	int count;
	String schlnameliststatus;
	ArrayList<HashMap<String, String>> getschoolnamelistdata = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> getAutoCheckFlagdata = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> deviceinfodata = new ArrayList<HashMap<String, String>>();
	Dialog dialog3;

	Bundle bundle;
	TextView title;
	String regId;

	Get_Deviceid get_dv_id;
	// Asyntask
	AsyncTask<Void, Void, Void> mRegisterTask;

	// Alert dialog manager
	AlertDialogManager alert = new AlertDialogManager();
	//
	// Connection detector
	ConnectionDetector cd;
	ArrayList<String> device_uid = new ArrayList<String>();
	Bundle savedInstanceState1;

	PreferenceConnector preference_connector;

	SharedPreferences pref, userdpref, deviceinfoidpref, onepref;

	boolean one;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		savedInstanceState1 = savedInstanceState;
		setContentView(R.layout.activity_main);

		// preference_connector = new PreferenceConnector();

		get_dv_id = new Get_Deviceid(getApplicationContext());

		device_idString = get_dv_id.get_unique_deviceid();

		// TelephonyManager telephonyManager = (TelephonyManager)
		// getSystemService(Context.TELEPHONY_SERVICE);
		// device_idString = telephonyManager.getDeviceId() + "12345";

		GCM_REGISTRATION(device_idString);

		// regId= PreferenceConnector.readString(getApplicationContext(),
		// PreferenceConnector.GCM_ID, "");

		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		prgLoading = (ProgressBar) findViewById(R.id.prgLoading);
		clickheretoparticipate = (Button) findViewById(R.id.clickheretoparticipate);
		findparticipatingbusiness = (Button) findViewById(R.id.findparticipatingbusiness);
		notificationncoupans = (Button) findViewById(R.id.notificationncoupans);
		regnmanageaccnt = (Button) findViewById(R.id.regnmanageaccnt);
		aboutparogram = (Button) findViewById(R.id.aboutparogram);
		title = (TextView) findViewById(R.id.title);
		Typeface type = Typeface.createFromAsset(getAssets(), "Candara Bold Italic.ttf");
		title.setTypeface(type);

		// oitu-0y9ur-tiygkiyuhkh
		// 6847538697398738
		// device_idString = "00000046546435300006378647";
		device_NameString = getDeviceName();

		device_TypeString = android.os.Build.DEVICE;

		// device_TypeString1111 = android.os.Build. ;

		prefs = getSharedPreferences("device", Context.MODE_PRIVATE);

		Editor editor = prefs.edit();
		editor.putString("device_idString", device_idString);
		editor.commit();
		userdpref = getSharedPreferences("userid", Context.MODE_PRIVATE);
		deviceinfoidpref = getSharedPreferences("deviceinfoid", Context.MODE_PRIVATE);
		onepref = getSharedPreferences("one", Context.MODE_PRIVATE);
		Log.e(TAG, "device_idString-------" + device_idString);
		one = onepref.getBoolean("register", false);
		
		
		
		if (one) {
			clickheretoparticipate.setEnabled(true);
			findparticipatingbusiness.setEnabled(true);
			notificationncoupans.setEnabled(true);
			aboutparogram.setEnabled(true);
			regnmanageaccnt.setBackgroundResource(R.drawable.manageaccount);

//			Intent intent1 = new Intent(MainActivity.this, Participation_service.class);
//			startService(intent1);

			boolean is_Service_Running = isServiceRunning();

			if (is_Service_Running) {

				Log.e(TAG, "Auto_flag_status_check_service Already Runnning-------");

			} else {
				
				Log.e(TAG, "Auto_flag_status_check_service  started now-------");

				Intent intent2 = new Intent(MainActivity.this, Auto_flag_status_check_service.class);
				startService(intent2);

			}

		} else {
			// float alpha = 0.45f;
			// AlphaAnimation alphaUp = new AlphaAnimation(alpha,
			// alpha);
			// alphaUp.setFillAfter(true);
			clickheretoparticipate.setEnabled(false);
			findparticipatingbusiness.setEnabled(false);
			notificationncoupans.setEnabled(false);
			aboutparogram.setEnabled(false);
			// clickheretoparticipate.startAnimation(alphaUp);
			// findparticipatingbusiness.startAnimation(alphaUp);
			// notificationncoupans.startAnimation(alphaUp);
			// aboutparogram.startAnimation(alphaUp);
		}
		notificationncoupans.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				startActivity(new Intent(MainActivity.this, NotificationsnCoupans.class));
				finish();

			}
		});
		findparticipatingbusiness.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				startActivity(new Intent(MainActivity.this, Findparticipatingbusiness.class));

				finish();
			}
		});
		clickheretoparticipate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// String status =
				// PreferenceConnector.readString(getApplicationContext(),
				// PreferenceConnector.AUTOMATIC_CHECK_FLAG, "true");

				GetAutoCheckFlagStatus bb = new GetAutoCheckFlagStatus();
				bb.execute();

				// if (status.equalsIgnoreCase("true")) {
				//
				// startActivity(new Intent(MainActivity.this,
				// Mobile_store_to_participate.class));
				//
				// finish();
				//
				// } else {
				//
				// startActivity(new Intent(MainActivity.this,
				// Click_here_to_participate_option.class));
				//
				// finish();
				//
				// }

			}
		});
		regnmanageaccnt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (checkDeviceResult.equalsIgnoreCase("true")) {
					startActivity(new Intent(MainActivity.this, ManageRegistration.class).putExtra(
							"Device_Info_Id_saved", Device_Info_Id));
					finish();

				} else {
					startActivity(new Intent(MainActivity.this, RegistertoParticipate.class).putExtra(
							"getschoolnamelistdata", getschoolnamelistdata).putExtra("Device_Info_Id_saved",
							Device_Info_Id));
					finish();
				}
			}
		});
		aboutparogram.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				startActivity(new Intent(MainActivity.this, AboutProgram.class));
				finish();
			}
		});
		cd = new ConnectionDetector(getApplicationContext());

		// Check if Internet present
		if (!cd.isConnectingToInternet()) {
			// Internet Connection is not present
			alert.showAlertDialog(MainActivity.this, "Oops!!",
					"Internet Connection seems to offline.Please check your network.", false);
			// stop executing code by return
			return;
		} else {
			new CheckDeviceRegData().execute();
		}
	}

	private String CheckDeviceReg() {
		String resultString = "";

		JSONObject json = null;
		try {
			json = jsonParser.getJSONFromUrl(Constant.SERVER_URL + "CheckDeviceReg" + "?DeviceID=" + device_idString);

			// Log.d(TAG, "CheckDeviceReg-------" + json);
			// if (json.length() == 0) {
			// showMesgDlg("Server not found.Please try again later.");
			// }
		} catch (Exception e) {
			e.printStackTrace();
			// Log.d(TAG, "CheckDeviceReg----Exception---" + json);
		}

		JSONObject ja = null;
		try {
			ja = json.getJSONObject("CheckDeviceRegResult");

			String Is_User_Registered = null;
			try {
				Is_User_Registered = ja.getString("Is_User_Registered");
			} catch (Exception e) {

				e.printStackTrace();
			}

			String UserId = null;
			try {
				UserId = ja.getString("User_Reg_Info_Id").toString();
			} catch (Exception e) {

				e.printStackTrace();
			}

			// Log.d(TAG, "UserId----------" + UserId);
			Editor editor = userdpref.edit();
			editor.putString("UserId", UserId);
			editor.commit();
			String Is_Coupon_Allowed2 = null;
			try {
				Is_Coupon_Allowed2 = ja.getString("Is_Coupon_Allowed");
			} catch (Exception e) {

				e.printStackTrace();
			}
			String Is_Location_Service_Allowed2 = null;
			try {
				Is_Location_Service_Allowed2 = ja.getString("Is_Location_Service_Allowed");
			} catch (Exception e) {

				e.printStackTrace();
			}
			String Is_Notification_Allowed2 = null;
			try {
				Is_Notification_Allowed2 = ja.getString("Is_Notification_Allowed");
			} catch (Exception e) {

				e.printStackTrace();
			}
			/*
			 * String Is_User_Over_18_Year2 = null; try { Is_User_Over_18_Year2
			 * = ja.getString("Is_User_Over_18_Year"); } catch (Exception e) {
			 * e.printStackTrace(); }
			 */
			PreferenceConnector.writeString(getApplicationContext(), PreferenceConnector.Is_Location_Service_Allowed,
					Is_Location_Service_Allowed2);
			PreferenceConnector.writeString(getApplicationContext(), PreferenceConnector.Is_Coupon_Allowed,
					Is_Coupon_Allowed2);
			PreferenceConnector.writeString(getApplicationContext(), PreferenceConnector.Is_Notification_Allowed,
					Is_Notification_Allowed2);
			/*
			 * PreferenceConnector .writeString(getApplicationContext(),
			 * PreferenceConnector.Is_User_Over_18_Year, Is_User_Over_18_Year2);
			 */

			resultString = Is_User_Registered;

		} catch (Exception e) {
			showMesgDlg("Server not found.Please try again later.");
			e.printStackTrace();
		}

		return resultString;
	}

	public class CheckDeviceRegData extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			clickheretoparticipate.setEnabled(false);
			findparticipatingbusiness.setEnabled(false);
			notificationncoupans.setEnabled(false);
			regnmanageaccnt.setEnabled(false);
			aboutparogram.setEnabled(false);

			// Toast.makeText(getApplicationContext(), ""+regId,
			// Toast.LENGTH_LONG).show();
		}

		@Override
		protected Void doInBackground(Void... params) {

			checkDeviceResult = CheckDeviceReg();
			// Log.d(TAG, "checkDeviceResult---------------" +
			// checkDeviceResult);

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			try {
				// if (schlnameliststatus.equals("true")) {
				if (checkDeviceResult.equalsIgnoreCase("false")) {

					float alpha = 0.45f;
					AlphaAnimation alphaUp = new AlphaAnimation(alpha, alpha);
					alphaUp.setFillAfter(true);
					Editor editor = onepref.edit();
					editor.putBoolean("register", false);
					editor.commit();
					clickheretoparticipate.setEnabled(false);
					findparticipatingbusiness.setEnabled(false);
					notificationncoupans.setEnabled(false);
					aboutparogram.setEnabled(false);
					clickheretoparticipate.startAnimation(alphaUp);
					findparticipatingbusiness.startAnimation(alphaUp);
					notificationncoupans.startAnimation(alphaUp);
					aboutparogram.startAnimation(alphaUp);
					// Toast.makeText(getApplicationContext(), ""+regId,
					// Toast.LENGTH_LONG).show();
					Resultdevice = saveDeviceInfo("123456", device_idString, device_NameString, device_TypeString);
					// Log.d(TAG, "Resultdevice---------------" + Resultdevice);
					schlnameliststatus = getSchoolNameList();

					// if (Resultdevice.equalsIgnoreCase("true")) {
					//
					firstshowMesgDlg();
					//
					// } else {
					//
					// // showdialog_p(Messagedevice);
					//
					// }

				} else {

					clickheretoparticipate.setEnabled(true);
					findparticipatingbusiness.setEnabled(true);
					notificationncoupans.setEnabled(true);
					aboutparogram.setEnabled(true);
					regnmanageaccnt.setBackgroundResource(R.drawable.manageaccount);
					Editor editor = onepref.edit();
					editor.putBoolean("register", true);
					editor.commit();
				}
				prgLoading.setVisibility(View.GONE);
				regnmanageaccnt.setEnabled(true);
				// }
			} catch (Exception e) {

				e.printStackTrace();
			}

		}

		/*
		 * private void showdialog_p(String messagedevice) { try { final Dialog
		 * alertDialog = new Dialog(MainActivity.this);
		 * alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		 * alertDialog.getWindow().setBackgroundDrawable( new
		 * ColorDrawable(android.graphics.Color.TRANSPARENT));
		 * alertDialog.setContentView(R.layout.popupwindow);
		 * alertDialog.setCancelable(true); Typeface type =
		 * Typeface.createFromAsset(getAssets(), "Candara Bold Italic.ttf");
		 * Button btnDismiss = (Button) alertDialog .findViewById(R.id.dismiss);
		 * TextView aboutprogram = (TextView) alertDialog
		 * .findViewById(R.id.aboutprogram);
		 * aboutprogram.setText(messagedevice); aboutprogram.setTypeface(type);
		 * btnDismiss.setTypeface(type); btnDismiss.setOnClickListener(new
		 * Button.OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { alertDialog.dismiss(); //
		 * finish(); } });
		 * alertDialog.getWindow().setLayout(LayoutParams.WRAP_CONTENT,
		 * LayoutParams.WRAP_CONTENT);
		 * alertDialog.getWindow().setGravity(Gravity.CENTER);
		 * alertDialog.show(); } catch (Exception e) { e.printStackTrace(); } }
		 */
	}

	public String saveDeviceInfo(String keyString2, String device_idString2, String device_NameString2,
			String device_TypeString2) {

		String resultString = null;
		// Log.d(TAG, "device_idString2------------" + device_idString2);
		// Log.d(TAG, "device_NameString2------------" + device_NameString2);
		// Log.d(TAG, "device_TypeString2------------" + device_TypeString2);
		String URL = Constant.SERVER_URL
				+ "SaveDeviceInfo?Key=123456&Device_UID="
				+ device_idString2
				+ "&Device_Name="
				+ device_NameString2
				+ "&Device_Type="
				+ device_TypeString2
				+ "&Device_Manufacturer_Name=abc&Device_Model_Name=android&Device_Model_Number=123abc&Device_System_Name=abc22&Device_System_Version=v1&Device_Software_Version=v2&Device_Platform_Version=ab2&Device_Firmware_Version=ab222&Device_OS=aa&Device_Timezone=12&Language_Used_On_Device=english&Has_Camera=true&Is_Backlight_On=true&Is_Battery_Removable=false";
		// &UserId=
		JSONArray result = null;
		JSONObject SaveDeviceInfoResult = null;
		try {

			JSONObject SaveDeviceInfo = jsonParser.getJSONFromUrl(URL);

			// Log.d("Saved Device info response", ">>>>>>>>>>>>>>>>>   " +
			// SaveDeviceInfo);
			// JSONObject SaveDeviceInfo = userfunction.deviceregistration(
			// device_idString2, device_NameString2, device_TypeString2,
			// UserId2);
			// if (SaveDeviceInfo.length() == 0) {
			// showMesgDlg("Server not found.Please try again later.");
			// }
			// Log.d(TAG, "SaveDeviceInfo------------" + SaveDeviceInfo);
			try {
				SaveDeviceInfoResult = SaveDeviceInfo.getJSONObject("SaveDeviceInfoResult");
			} catch (Exception e1) {

				showMesgDlg("Server not found.Please try again later.");
				e1.printStackTrace();
			}
			if (SaveDeviceInfoResult != null) {
				try {
					result = SaveDeviceInfoResult.getJSONArray("DeviceInfo");
					for (int i = 0; i < result.length(); i++) {

						JSONObject c = result.getJSONObject(i);
						// String UserId = c.getString("UserId");
						String Device_Info_Id2 = c.getString("Device_Info_Id");
						// Log.d(TAG, "Device_Info_Id2" + Device_Info_Id2);
						// HashMap<String, String> map = new HashMap<String,
						// String>();
						// map.put("UserId", UserId);
						// map.put("Device_Info_Id", Device_Info_Id2);

						Device_Info_Id = Device_Info_Id2;
						// Editor editor = deviceinfoidpref.edit();
						// editor.putString("Device_Info_Id", Device_Info_Id2);
						// editor.commit();
						// deviceinfodata.add(map);

					}
				} catch (Exception e) {

					e.printStackTrace();
				}

			} else {
				// Log.d("Response", "Json is null");
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		try {
			Messagedevice = SaveDeviceInfoResult.getString("Message");
			resultString = SaveDeviceInfoResult.getString("Status");
		} catch (Exception e) {

			e.printStackTrace();
		}
		// Log.d(TAG, "SaveDeviceInfo-----resultString-----Message--" +
		// resultString + "-------------" + Messagedevice);
		return resultString;

	}

	public String getDeviceName() {
		String manufacturer = Build.MANUFACTURER;
		String model = Build.MODEL;
		if (model.startsWith(manufacturer)) {
			return capitalize(model);
		} else {
			return capitalize(manufacturer) + " " + model;
		}
	}

	private String capitalize(String s) {
		if (s == null || s.length() == 0) {
			return "";
		}
		char first = s.charAt(0);
		if (Character.isUpperCase(first)) {
			return s;
		} else {
			return Character.toUpperCase(first) + s.substring(1);
		}
	}

	public void getUserReginfoBYDevice_UID() {
		// boolean IsCouponAllowedreturn = false, IsNotificationAllowedreturn;
		JSONArray result = null;
		try {
			JSONObject json = jsonParser.getJSONFromUrl(Constant.SERVER_URL + "GetUserReginfoBYDevice_UID"
					+ "?Device_UID=" + device_idString);
			if (json.length() == 0) {
				showMesgDlg("Server not found.Please try again later.");
			}
			// Log.d(TAG, "---GetUserReginfoBYDevice_UID--------" + json);
			JSONObject GetUserReginfoBYDevice_UIDResult = json.getJSONObject("GetUserReginfoBYDevice_UIDResult");

			result = GetUserReginfoBYDevice_UIDResult.getJSONArray("UserRegInfo");

		} catch (Exception e) {

			e.printStackTrace();
		}
		for (int i = 0; i < result.length(); i++) {
			try {
				JSONObject c = result.getJSONObject(i);
				String UserId = c.getString("User_Reg_Info_Id").toString();

				// Log.d(TAG, "UserId----------" + UserId);
				Editor editor = userdpref.edit();
				editor.putString("UserId", UserId);
				editor.commit();
				String Is_Coupon_Allowed2 = c.getString("Is_Coupon_Allowed");
				String Is_Location_Service_Allowed2 = c.getString("Is_Location_Service_Allowed");
				String Is_Notification_Allowed2 = c.getString("Is_Notification_Allowed");
				String Is_User_Over_18_Year2 = c.getString("Is_User_Over_18_Year");
				PreferenceConnector.writeString(getApplicationContext(),
						PreferenceConnector.Is_Location_Service_Allowed, Is_Location_Service_Allowed2);
				PreferenceConnector.writeString(getApplicationContext(), PreferenceConnector.Is_Coupon_Allowed,
						Is_Coupon_Allowed2);
				PreferenceConnector.writeString(getApplicationContext(), PreferenceConnector.Is_Notification_Allowed,
						Is_Notification_Allowed2);
				PreferenceConnector.writeString(getApplicationContext(), PreferenceConnector.Is_User_Over_18_Year,
						Is_User_Over_18_Year2);

				// HashMap<String, String> map = new HashMap<String, String>();
				// adding each child node to HashMap key => value

				/*
				 * map.put("Has_Multiple_Child", Has_Multiple_Child2);
				 * map.put("Is_Coupon_Allowed", Is_Coupon_Allowed2);
				 * map.put("Is_Location_Service_Allowed",
				 * Is_Location_Service_Allowed2);
				 * map.put("Is_Notification_Allowed", Is_Notification_Allowed2);
				 * map.put("Is_User_Over_18_Year", Is_User_Over_18_Year2);
				 * map.put("Is_User_Registered", Is_User_Registered2);
				 */

				/*
				 * Has_Multiple_Child = Has_Multiple_Child2; Is_Coupon_Allowed =
				 * Is_Coupon_Allowed2; Is_Location_Service_Allowed =
				 * Is_Location_Service_Allowed2; Is_Notification_Allowed =
				 * Is_Notification_Allowed2; Is_User_Over_18_Year =
				 * Is_User_Over_18_Year2; Is_User_Registered =
				 * Is_User_Registered2; getUserReginfo.add(map); Log.d(TAG,
				 * "-Is_Coupon_Allowed-----------" + Is_Coupon_Allowed);
				 * IsCouponAllowedreturn = Boolean.valueOf(Is_Coupon_Allowed);
				 * IsNotificationAllowedreturn = Boolean
				 * .valueOf(Is_Notification_Allowed); IsCouponAllowed =
				 * IsCouponAllowedreturn; IsNotificationAllowed =
				 * IsNotificationAllowedreturn; Log.d(TAG,
				 * "-IsCouponAllowedreturn-----------" + IsCouponAllowedreturn);
				 */
			} catch (Exception e) {

				e.printStackTrace();
			}
		}
	}

	/*
	 * public void getGetAutoCheckFlag() { try { JSONObject json =
	 * jsonParser.getJSONFromUrl(Constant.SERVER_URL + "GetAutoCheckFlag");
	 * JSONArray result = null; Log.d(TAG, "-GetAutoCheckFlag-----------" +
	 * json); JSONObject GetSchoolNameResult = json
	 * .getJSONObject("GetAutoCheckFlagResult"); result =
	 * GetSchoolNameResult.getJSONArray("AutomaticCheckFlag"); for (int i = 0; i
	 * < result.length(); i++) { try { JSONObject c = result.getJSONObject(i);
	 * String Automatic_Check_Flag = c .getString("Automatic_Check_Flag");
	 * String Automatic_Check_Flag_Id = c .getString("Automatic_Check_Flag_Id");
	 * String UserId = c.getString("UserId"); HashMap<String, String> map = new
	 * HashMap<String, String>(); // adding each child node to HashMap key =>
	 * value map.put("Automatic_Check_Flag", Automatic_Check_Flag);
	 * map.put("Automatic_Check_Flag_Id", Automatic_Check_Flag_Id);
	 * map.put("UserId", UserId); getAutoCheckFlagdata.add(map); } catch
	 * (Exception e) { e.printStackTrace(); } } } catch (Exception e) {
	 * e.printStackTrace(); } }
	 */

	public String getSchoolNameList() {

		String schlnameliststatus = null;
		JSONArray result = null;
		JSONObject json = null;
		try {

			json = jsonParser.getJSONFromUrl(Constant.SERVER_URL + "GetSchoolName");
			if (json.length() == 0) {
				showMesgDlg("Server not found.Please try again later.");
			}
			// Log.d(TAG, "-GetSchoolName-----------" + json);
			JSONObject GetSchoolNameResult = json.getJSONObject("GetSchoolNameResult");

			result = GetSchoolNameResult.getJSONArray("SchoolInfo");
			schlnameliststatus = GetSchoolNameResult.getString("Status");
			for (int i = 0; i < result.length(); i++) {
				try {
					JSONObject c = result.getJSONObject(i);
					String School_Name = c.getString("School_Name");
					String School_Id = c.getString("School_Id");
					HashMap<String, String> map = new HashMap<String, String>();
					// adding each child node to HashMap key => value
					map.put("School_Name", School_Name);
					map.put("School_Id", School_Id);
					if (getschoolnamelistdata.contains(School_Id)) {

					} else {
						getschoolnamelistdata.add(map);
					}

				} catch (Exception e) {

					e.printStackTrace();
				}
			}

		} catch (Exception e) {

			e.printStackTrace();
		}
		return schlnameliststatus;

	}

	public void showMesgDlg(String msg) {

		try {
			final Dialog alertDialog = new Dialog(MainActivity.this);

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
					finish();
				}
			});

			alertDialog.getWindow().setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			alertDialog.getWindow().setGravity(Gravity.CENTER);
			alertDialog.show();
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	@Override
	public void onBackPressed() {

		finish();
		super.onBackPressed();
	}

	public void firstshowMesgDlg() {

		try {
			final Dialog alertDialog = new Dialog(MainActivity.this);

			alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
			alertDialog.setContentView(R.layout.firstpopup);
			alertDialog.setCancelable(true);
			Typeface type = Typeface.createFromAsset(getAssets(), "Candara Bold Italic.ttf");
			Button ok = (Button) alertDialog.findViewById(R.id.ok);
			Button cancel = (Button) alertDialog.findViewById(R.id.cancel);
			TextView alert = (TextView) alertDialog.findViewById(R.id.alert);
			TextView program = (TextView) alertDialog.findViewById(R.id.program);
			program.setTypeface(type);
			alert.setTypeface(type);
			ok.setTypeface(type);
			cancel.setTypeface(type);
			ok.setOnClickListener(new Button.OnClickListener() {

				@Override
				public void onClick(View v) {
					alertDialog.dismiss();
					startActivity(new Intent(MainActivity.this, RegistertoParticipate.class).putExtra(
							"getschoolnamelistdata", getschoolnamelistdata).putExtra("Device_Info_Id_saved",
							Device_Info_Id));
					finish();
				}
			});

			cancel.setOnClickListener(new Button.OnClickListener() {

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

	public void GCM_REGISTRATION(final String imeistring2) {

		// Make sure the device has the proper dependencies.
		GCMRegistrar.checkDevice(this);
		GCMRegistrar.checkManifest(this);

		registerReceiver(mHandleMessageReceiver, new IntentFilter(DISPLAY_MESSAGE_ACTION));

		// Get GCM registration id
		regId = GCMRegistrar.getRegistrationId(this);

		// userFunction.gcmregister(regId);

		Log.e("GCM_REGISTRATION Regitered id>>>>>>>>>>", "--->" + regId);
		/*
		 * PreferenceConnector.readString( getApplicationContext(),
		 * PreferenceConnector.GCM_ID, regId);
		 */
		// Check if regid already presents
		if (regId.equals("")) {
			// Registration is not present, register now with GCM
			GCMRegistrar.register(this, SENDER_ID);

		} else {
			// Device is already registered on GCM
			if (GCMRegistrar.isRegisteredOnServer(this)) {
				Log.e("GCM Regitered id>>>>>>>>>>", "-if----------->" + regId);

			} else {
				Log.e("GCM Regitered id>>>>>>>>>>", "--else-------------->" + regId);
				// Try to register again, but not in the UI thread.
				// It's also necessary to cancel the thread onDestroy(),
				// hence the use of AsyncTask instead of a raw thread.
				final Context context = this;
				mRegisterTask = new AsyncTask<Void, Void, Void>() {

					@Override
					protected Void doInBackground(Void... params) {
						// Register on our server
						// On server creates a new user
						ServerUtilities.register(context, regId);
						return null;
					}

					@Override
					protected void onPostExecute(Void result) {

						// Intent l = new Intent(Splash.this,
						// MainActivity.class);
						//
						// PreferenceConnector.writeString(
						// getApplicationContext(),
						// PreferenceConnector.GCM_ID, regId);
						// startActivity(l);
						// finish();

						mRegisterTask = null;

					}

				};
				mRegisterTask.execute(null, null, null);
			}
		}

	}

	/**
	 * Receiving push messages
	 */
	private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			// String newMessage = intent.getExtras().getString("message");
			// Waking up mobile if it is sleeping
			WakeLocker.acquire(getApplicationContext());

			/**
			 * Take appropriate action on this message depending upon your app
			 * requirement For now i am just displaying it on the screen
			 */

			// Showing received message
			// lblMessage.append(newMessage + "\n");
			// Toast.makeText(getApplicationContext(),
			// "New Message: " + newMessage, Toast.LENGTH_LONG).show();

			// if (newMessage.equals("Your are registred successfully")) {
			//
			//
			// // Intent l = new Intent(Splash.this, MainActivity.class);
			// //
			// // PreferenceConnector.writeString(
			// // getApplicationContext(),
			// // PreferenceConnector.GCM_ID, regId);
			// // startActivity(l);
			// // finish();
			//
			// Toast.makeText(getApplicationContext(), "New Message: " +
			// newMessage, Toast.LENGTH_LONG).show();
			//
			// } else {
			//
			// Toast.makeText(getApplicationContext(), "New Message: " +
			// newMessage, Toast.LENGTH_LONG).show();
			//
			// }

			// Releasing wake lock
			WakeLocker.release();
		}
	};

	@Override
	protected void onDestroy() {
		if (mRegisterTask != null) {
			mRegisterTask.cancel(true);
		}
		try {
			unregisterReceiver(mHandleMessageReceiver);
			GCMRegistrar.onDestroy(this);
		} catch (Exception e) {
			Log.e("UnRegister Receiver Error", "> " + e.getMessage());
		}
		super.onDestroy();
	}

	private boolean isServiceRunning() {
		ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
			if ("com.widevision.sgbp.Auto_flag_status_check_service".equals(service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}

	private boolean isParticipationServiceRunning() {
		ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
			if ("com.widevision.sgbp.Participation_service".equals(service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}

	public class GetAutoCheckFlagStatus extends AsyncTask<Void, Void, String> {

		@Override
		protected void onPreExecute() {

			// prgLoading.setVisibility(View.VISIBLE);
		}

		@Override
		protected String doInBackground(Void... params) {
			String Automatic_Check_Flag_Status = getAutoCheckFlag();
			Log.e("24 hours service response>>>>>>", "-------" + Automatic_Check_Flag_Status);
			return Automatic_Check_Flag_Status;
		}

		@Override
		protected void onPostExecute(String result) {

			PreferenceConnector.writeString(getApplicationContext(), PreferenceConnector.AUTOMATIC_CHECK_FLAG, result);
			if (result.equalsIgnoreCase("true")) {

				boolean is_Participation_Service_Running = isParticipationServiceRunning();

				if (is_Participation_Service_Running) {

					Log.w("Participation_service--->", "Participation_service Already Runnning-------");

				} else {
					Log.w("Participation_service--->", "Participation_service  started now-------");

					Intent intent2 = new Intent(getApplicationContext(), Participation_service.class);
					startService(intent2);
					Log.e("Participation_service---->>>>>>", "" + result);
				}
				
				startActivity(new Intent(MainActivity.this, Mobile_store_to_participate.class));

				finish();

				// try {
				// Intent intent2 = new Intent(getApplicationContext(),
				// Participation_service.class);
				// startService(intent2);
				// } catch (Exception e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }

			} else {

				try {
					Intent intent1 = new Intent(getApplicationContext(), Participation_service.class);
					stopService(intent1);
				} catch (Exception e) {

					e.printStackTrace();
				}

				startActivity(new Intent(MainActivity.this, Click_here_to_participate_option.class));

				finish();

			}

		}
	}

	public String getAutoCheckFlag() {

		String resultString = "";
		JSONObject json = jsonParser.getJSONFromUrl(Constant.SERVER_URL + "GetAutoCheckFlag");
		// Log.d("GetAutoCheckFlag------>>>>>>>>", "------------" + json);
		try {
			JSONArray AutomaticCheckFlag = json.getJSONObject("GetAutoCheckFlagResult").getJSONArray(
					"AutomaticCheckFlag");
			for (int i = 0; i < AutomaticCheckFlag.length(); i++) {
				JSONObject jo = AutomaticCheckFlag.getJSONObject(i);
				resultString = jo.getString("Automatic_Check_Flag");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultString;

	}

}
