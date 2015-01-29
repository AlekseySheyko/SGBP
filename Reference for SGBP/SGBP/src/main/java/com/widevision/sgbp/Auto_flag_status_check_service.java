package com.widevision.sgbp;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.widevision.sgbp.util.Constant;
import com.widevision.sgbp.util.JSONParser;

public class Auto_flag_status_check_service extends Service {
	AsyncTask<Void, Void, Void> mRegisterTask;
	Intent intent;
	private final Handler handler = new Handler();
	public JSONParser jsonParser;
	String Automatic_Check_Flag_Status;

	@Override
	public void onCreate() {
		super.onCreate();
		// intent = new Intent(BROADCAST_ACTION);
		jsonParser = new JSONParser();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		handler.removeCallbacks(sendUpdatesToUI);
		handler.postDelayed(sendUpdatesToUI, 5000);

	}

	private Runnable sendUpdatesToUI = new Runnable() {
		public void run() {
			DoYourWorking();
			handler.postDelayed(this, 86400000);
		}

		private void DoYourWorking() {

			sendaboutprogramData bb = new sendaboutprogramData();
			bb.execute();
			Log.e("Auto_flag_status_check_service starting", "----------------------------------->");
		}
	};

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		handler.removeCallbacks(sendUpdatesToUI);

	}

	public class sendaboutprogramData extends AsyncTask<Void, Void, String> {

		@Override
		protected void onPreExecute() {

			// prgLoading.setVisibility(View.VISIBLE);
		}

		@Override
		protected String doInBackground(Void... params) {
			Automatic_Check_Flag_Status = getAutoCheckFlag();
			Log.e("24 hours service response>>>>>>", "-------" + Automatic_Check_Flag_Status);
			return Automatic_Check_Flag_Status;
		}

		@Override
		protected void onPostExecute(String result) {

			PreferenceConnector.writeString(getApplicationContext(), PreferenceConnector.AUTOMATIC_CHECK_FLAG, result);

			if (result.equals("true")) {

				boolean is_Service_Running = isParticipationServiceRunning();

				if (is_Service_Running) {

					Log.e("AUTOMATIC_CHECK_FLAG--->", "Participation_service Already Runnning-------");

				} else {
					Log.e("AUTOMATIC_CHECK_FLAG--->", "Participation_service  started now-------");

					Intent intent2 = new Intent(getApplicationContext(), Participation_service.class);
					startService(intent2);
					Log.e("AUTOMATIC_CHECK_FLAG---->>>>>>", "" + result);
				}
 
			} else {

				try {
					Intent intent1 = new Intent(getApplicationContext(), Participation_service.class);
					stopService(intent1);
				} catch (Exception e) {

					e.printStackTrace();
				}

				Log.e("AUTOMATIC_CHECK_FLAG>>>>>>", "" + result);
			}
		}
	}

	public String getAutoCheckFlag() {

		String resultString = "";
		JSONObject json = jsonParser.getJSONFromUrl(Constant.SERVER_URL + "GetAutoCheckFlag");
		Log.e("GetAutoCheckFlag------>>>>>>>>", "------------" + json);
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

	private boolean isParticipationServiceRunning() {
		ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
			if ("com.widevision.sgbp.Participation_service".equals(service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}

}
