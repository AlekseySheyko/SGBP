package com.widevision.sgbp;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import com.widevision.sgbp.util.Constant;
import com.widevision.sgbp.util.JSONParser;

public class Splash_old extends Activity {

	String TAG = "Splash", Result, device_idString, Device_UID;
	JSONParser jsonParser = new JSONParser();
	SharedPreferences pref;
	boolean Is_Coupon_Allowed, Is_Device_Registered, Is_Notification_Allowed,
			Is_User_Registered;
	int device_id;
//	private static String URL = "http://demo.myfirstland.com/SGBP/sgbpwsjson.svc/";

	ArrayList<HashMap<String, Boolean>> checkDeviceRegdata = new ArrayList<HashMap<String, Boolean>>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.splash);
		pref = getSharedPreferences("device", Context.MODE_PRIVATE);

		device_idString = pref.getString("device_idString", null);
//		TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//
//		device_idString = telephonyManager.getDeviceId()+"99999";
		// device_id=Integer.parseInt(device_idString);
		Log.e(TAG, "device_idString-------" + device_idString);
		Log.e(TAG, "device_id-------" + device_id);
		// new sendData().execute();
		new sendDataTeammeber().execute();

		Thread t = new Thread() {
			public void run() {
				try {
					sleep(3000);
				} catch (Exception e) {

				} finally {
					/*Intent i = new Intent(Splash.this, MainActivity.class);
					i.putExtra("checkDeviceResult", Result);
					Log.e(TAG, "Result-------" + Result);
//					i.putExtra("checkDeviceRegdata", checkDeviceRegdata);
					startActivity(i);
					finish();*/
				}

			}

		};
		t.start();

	}

	/*public class sendData extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {

		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			Result = checkDeviceReg();

			Log.e(TAG, "Result-------" + Result);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

		}
	}*/

	public class sendDataTeammeber extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {

		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			Result = getTeammemberList();

			Log.e(TAG, "getTeammemberList-------" + Result);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			Intent i = new Intent(Splash_old.this, MainActivity.class);
			i.putExtra("checkDeviceResult", Result);
			Log.e(TAG, "Result-------" + Result);
//			i.putExtra("checkDeviceRegdata", checkDeviceRegdata);
			startActivity(i);
			finish();
		}
	}

	/*private String checkDeviceReg() {

		String resultString = null;
		
		 * JSONObject json = jsonParser.getJSONFromUrl(URL + "CheckDeviceReg" +
		 * "?DeviceID=" + device_idString); Log.d(TAG,
		 * "CheckDeviceReg------------" + json); try { JSONObject c =
		 * json.getJSONObject("CheckDeviceRegResult"); Is_Coupon_Allowed=
		 * c.getBoolean("Is_Coupon_Allowed"); Is_Device_Registered=
		 * c.getBoolean("Is_Device_Registered"); Is_Notification_Allowed=
		 * c.getBoolean("Is_Notification_Allowed"); Is_User_Registered=
		 * c.getBoolean("Is_User_Registered"); HashMap<String, Boolean> map =
		 * new HashMap<String, Boolean>(); // adding each child node to HashMap
		 * key => value map.put("Is_Coupon_Allowed", Is_Coupon_Allowed);
		 * map.put("Is_Device_Registered", Is_Device_Registered);
		 * map.put("Is_Notification_Allowed", Is_Notification_Allowed);
		 * map.put("Is_User_Registered", Is_User_Registered);
		 * checkDeviceRegdata.add(map);
		 * 
		 * // resultString = c.getString("Status"); resultString="true"; }
		 * 
		 * catch (Exception e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 

		return resultString;
	}*/

	private String getTeammemberList() {
		String resultString = "";
		JSONObject json = jsonParser.getJSONFromUrl(Constant.SERVER_URL + "GetTeamMembersList");
		JSONArray result = null;
		Log.d(TAG, "GetTeamMembersListt-------" + json);
		try {
			JSONObject GetTeamMembersListResult = json.getJSONObject("GetTeamMembersListResult");

			result = GetTeamMembersListResult.getJSONArray("UserRegInfo");
			Log.d(TAG, "result-------" + result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (int i = 0; i < result.length(); i++) {
			try {
				JSONObject c = result.getJSONObject(i);
				Device_UID = c.getString("Device_UID");
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("Device_UID", Device_UID);
				// getUserGradeinfo.add(map);
				if(device_idString.equalsIgnoreCase(Device_UID)){
					resultString="true";
				}
				else {
					resultString="false";
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return resultString;
	}

}
