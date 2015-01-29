package com.widevision.sgbp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.os.StrictMode;
import android.util.Log;

import com.widevision.sgbp.util.Constant;
import com.widevision.sgbp.util.Get_Deviceid;
import com.widevision.sgbp.util.JSONParser;

public class Participation_service extends Service {
	AsyncTask<Void, Void, Void> mRegisterTask;
	Intent intent;
	private final Handler handler = new Handler();
	private JSONParser jsonParser;
	String TAG = "Participation_service", Store_Id, Store_Name, Store_Add, Store_Phone, UserId;;
	ArrayList<SortBean> distanceList = new ArrayList<SortBean>();
	double latitude, storelatitude, storeCorner1latitude, storeCorner2latitude, storeCorner3latitude, storeCorner4latitude;
	double longitude, storelongitude, storeCorner1longitude, storeCorner2longitude, storeCorner3longitude, storeCorner4longitude;
	double distance, mindistance, distancemeter1, distancemeter2, distancemeter3, distancemeter4;
	private String device_idString;
	Get_Deviceid get_dv_id;

	ArrayList<HashMap<String, String>> getUserReginfo = new ArrayList<HashMap<String, String>>();

	@Override
	public void onCreate() {
		super.onCreate();

		jsonParser = new JSONParser();
		get_dv_id = new Get_Deviceid(getApplicationContext());

		device_idString = get_dv_id.get_unique_deviceid();

		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		// intent = new Intent(BROADCAST_ACTION);
	}

	@Override
	public void onStart(Intent intent, int startId) {
		handler.removeCallbacks(sendUpdatesToUI);
//		25 minute 
//		handler.postDelayed(sendUpdatesToUI, 1500000); 
		
//		5 minute 
//		handler.postDelayed(sendUpdatesToUI, 300000); 
//		10 second
		handler.postDelayed(sendUpdatesToUI, 10000); 

	}

	private Runnable sendUpdatesToUI = new Runnable() {

		private String TAG = "Participation_service";
		// private String address="";
		private JSONObject json;
		private String city = "";
		private String Is_Location_Service_Allowed;

		public void run() {
			DoYourWorking();
//			25 minute
			handler.postDelayed(this, 1500000);
//			3 minute 
//			handler.postDelayed(this, 180000);
		}

		private void DoYourWorking() {

			Log.w("Participation_service starting", "----------------------------------->");

			try {

				My_Current_Location gpsTracker = new My_Current_Location(getApplicationContext());

				if (gpsTracker.canGetLocation()) {
					Location location1 = gpsTracker.getLocation();
//					Log.d("location----------", "" + location1);
					
					 latitude = gpsTracker.getLocation().getLatitude();
					 longitude = gpsTracker.getLocation().getLongitude();

					// latitude = 22.75071;
					// longitude = 75.89542;

//					 latitude = 39.162461;
//					 longitude = -119.761963;

					// Reno City
//					 latitude = 39.529633;
//					 longitude = -119.813803;

					Log.e(TAG, "latitude-----" + latitude);
					Log.e(TAG, "longitude-----" + longitude);
					// Toast.makeText(getApplicationContext(),
					// "latitude->"+latitude +"\n" + "longitude->"+longitude,
					// 7000).show();
				} else {
					gpsTracker.showSettingsAlert();
				}

				Is_Location_Service_Allowed = PreferenceConnector.readString(getApplicationContext(), PreferenceConnector.Is_Location_Service_Allowed, "false");

				if (Is_Location_Service_Allowed.equalsIgnoreCase("true")) {
					new GetDatatoFindStoreId().execute();
				} else {
					// showAlertDialog();

				}

				// new GetLocationConsent().execute();

			} catch (Exception e) {

				e.printStackTrace();
			}

		}
	};
	private String add;
	public boolean getstoreid;
	private double dis_in_mile;
	private String Device_Info_Id;
	private String School_Id;

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

	public class GetDatatoFindStoreId extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {

			// prgLoading.setVisibility(View.VISIBLE);
		}

		@Override
		protected Void doInBackground(Void... params) {

			try {
				getstoreid = getDatatoFindStoreId();
//				Log.e(TAG, "getstoreid-------" + getstoreid);
			
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// prgLoading.setVisibility(View.GONE);
			if (getstoreid) {
				// showAlertDialog(
				// "Participate",
				// "Please confirm by clicking on the button below to participate in the the School Give Back Program for'"
				// + Store_Name + "'.");
				Log.e("Participating ", "----------------------------------->");
				new GetRegInfo().execute();

			} else {
				// showMesgDlg1(Constant.storeparticipatemsg);
				Log.e("Not Participating", "----------------------------------->");
			}
		}
	}

	public class GetRegInfo extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			// prgLoading.setVisibility(View.VISIBLE);
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

	public class SaveProgramParticipationByDevice extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {

			// prgLoading.setVisibility(View.VISIBLE);
		}

		@Override
		protected Void doInBackground(Void... params) {

			saveProgramParticipationByDevice();

			// Log.e(TAG, "Result-------" + Result);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// prgLoading.setVisibility(View.GONE);
			// showMesgDlg1("Thank you for participating in the School Give Back Program.");
			
//			Log.e(TAG, "-------Thank you for participating in the School Give Back Program.");

		}
	}

	public void saveProgramParticipationByDevice() {
		
		
		final Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);

		int month1 = month + 1;

		String Current_date = month1 + "/" + day + "/" + year;
	
//		Log.w(TAG, "-Current_Time-----------" + c.getTime());

		try {
			JSONObject json = jsonParser.getJSONFromUrl(Constant.SERVER_URL + "SaveProgramParticipationByDevice" + "?Key=" + "123456" + "&Device_Info_Id="
					+ Device_Info_Id + "&Device_UID=" + device_idString + "&School_Id=" + School_Id + "&Store_Id=" + Store_Id + "&Participation_DateTime="
					+ Current_date + "&Clicked_To_Participate=" + "true");
//			Log.d(TAG, "-saveUserGrade-----------" + json);
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	public void getUserReginfoBYDevice_UID() {

		try {
			JSONArray result = null;

			JSONObject json = jsonParser.getJSONFromUrl(Constant.SERVER_URL + "GetUserReginfoBYDevice_UID" + "?Device_UID=" + device_idString);

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
				getUserReginfo.add(map);
			}
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	public boolean getDatatoFindStoreId() {

		boolean resultString = false;

		JSONObject json = jsonParser.getJSONFromUrl(Constant.SERVER_URL + "GetDatatoFindStoreId");
//		Log.d("GetDatatoFindStoreId--------", "" + json);

		JSONArray result = null;
		JSONObject GetDatatoFindStoreIdResult = null;
		try {
			GetDatatoFindStoreIdResult = json.getJSONObject("GetDatatoFindStoreIdResult");

			dis_in_mile = Double.parseDouble(GetDatatoFindStoreIdResult.getString("Distance"));

			Log.e(TAG, "distanse admin---------->" + dis_in_mile);
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
				if (!c.getString("Store_Corner1_Latitude").toString().equals("")) {
					Store_Corner1_Latitude = c.getString("Store_Corner1_Latitude");
					storeCorner1latitude = Double.parseDouble(Store_Corner1_Latitude);
				}
				if (!c.getString("Store_Corner1_Longitude").toString().equals("")) {
					Store_Corner1_Longitude = c.getString("Store_Corner1_Longitude");

					storeCorner1longitude = Double.parseDouble(Store_Corner1_Longitude);
				}

			} catch (Exception e) {

				e.printStackTrace();
			}

			String Store_Corner2_Latitude = null;
			String Store_Corner2_Longitude = null;
			try {
				if (!c.getString("Store_Corner2_Latitude").toString().equals("")) {
					Store_Corner2_Latitude = c.getString("Store_Corner2_Latitude");
					storeCorner2latitude = Double.parseDouble(Store_Corner2_Latitude);
				}
				if (!c.getString("Store_Corner2_Longitude").toString().equals("")) {
					Store_Corner2_Longitude = c.getString("Store_Corner2_Longitude");

					storeCorner2longitude = Double.parseDouble(Store_Corner2_Longitude);
				}
			} catch (Exception e) {

				e.printStackTrace();
			}

			String Store_Corner3_Latitude = null;
			String Store_Corner3_Longitude = null;
			try {
				if (!c.getString("Store_Corner3_Latitude").toString().equals("")) {
					Store_Corner3_Latitude = c.getString("Store_Corner3_Latitude");
					storeCorner3latitude = Double.parseDouble(Store_Corner3_Latitude);
				}
				if (!c.getString("Store_Corner3_Longitude").toString().equals("")) {
					Store_Corner3_Longitude = c.getString("Store_Corner3_Longitude");

					storeCorner3longitude = Double.parseDouble(Store_Corner3_Longitude);
				}
			} catch (Exception e) {

				e.printStackTrace();
			}

			String Store_Corner4_Latitude = null;
			String Store_Corner4_Longitude = null;
			try {
				if (!c.getString("Store_Corner4_Latitude").toString().equals("")) {
					Store_Corner4_Latitude = c.getString("Store_Corner4_Latitude");
					storeCorner4latitude = Float.parseFloat(Store_Corner4_Latitude);
				}
				if (!c.getString("Store_Corner4_Longitude").toString().equals("")) {
					Store_Corner4_Longitude = c.getString("Store_Corner4_Longitude");

					storeCorner4longitude = Double.parseDouble(Store_Corner4_Longitude);
				}
			} catch (Exception e) {

				e.printStackTrace();
			}

			String Store_Address_Latitude = null;
			String Store_Address_Longitude = null;
			try {
				if (!c.getString("Store_Address_Latitude").toString().equals("")) {
					Store_Address_Latitude = c.getString("Store_Address_Latitude");
				}
				storelatitude = Double.parseDouble(Store_Address_Latitude);

				if (!c.getString("Store_Address_Longitude").toString().equals("")) {
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
//			Log.e(TAG, "storelatitude=------------" + storelatitude);
//			Log.e(TAG, "storelongitude=------------" + storelongitude);
//			Log.e(TAG, "latitude=------------" + latitude);
//			Log.e(TAG, "longitude=------------" + longitude);

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
				SortBean sortBean = new SortBean(distance, distancemeter1, distancemeter2, distancemeter3, distancemeter4, Store_Id1, Store_Name1, Store_Add1,
						Store_Phone1);

				distanceList.add(sortBean);
			} else if (distancemeter2 < dis_in_mile) {
				SortBean sortBean = new SortBean(distance, distancemeter1, distancemeter2, distancemeter3, distancemeter4, Store_Id1, Store_Name1, Store_Add1,
						Store_Phone1);

				distanceList.add(sortBean);
			} else if (distancemeter3 < dis_in_mile) {
				SortBean sortBean = new SortBean(distance, distancemeter1, distancemeter2, distancemeter3, distancemeter4, Store_Id1, Store_Name1, Store_Add1,
						Store_Phone1);

				distanceList.add(sortBean);
			} else if (distancemeter4 < dis_in_mile) {
				SortBean sortBean = new SortBean(distance, distancemeter1, distancemeter2, distancemeter3, distancemeter4, Store_Id1, Store_Name1, Store_Add1,
						Store_Phone1);

				distanceList.add(sortBean);
			} else if (distance < dis_in_mile) {
				SortBean sortBean = new SortBean(distance, distancemeter1, distancemeter2, distancemeter3, distancemeter4, Store_Id1, Store_Name1, Store_Add1,
						Store_Phone1);
				// Log.d(TAG, "distance---------" +
				// distance+"--------Store_Name1-------"+Store_Name1);
				// Log.d(TAG, "storelatitude=------------" + storelatitude);
				// Log.d(TAG, "storelongitude=------------" + storelongitude);
//				Log.d(TAG, "latitude=------------" + latitude);
//				Log.d(TAG, "longitude=------------" + longitude);
				distanceList.add(sortBean);
			}
			Log.e(TAG, "--------Store_Name1-------" + Store_Name1 +" distance---------" + distance  );
//			Log.d(TAG, "storelatitude=------------" + storelatitude);
//			Log.d(TAG, "storelongitude=------------" + storelongitude);

			Collections.sort(distanceList, new Comparator<SortBean>() {
				public int compare(SortBean sortBean1, SortBean sortBean2) {
					return sortBean1.distance.compareTo(sortBean2.distance);
				}
			});
			/*
			 * SortBean sortBean = new SortBean(distance,distancemeter1,distancemeter2 ,distancemeter3,distancemeter4, Store_Id1, Store_Name1, Store_Add1,
			 * Store_Phone1); distanceList.add(sortBean); Collections.sort(distanceList, new Comparator<SortBean>() { public int compare(SortBean sortBean1,
			 * SortBean sortBean2) { return sortBean1.distance.compareTo(sortBean2.distance); } });
			 */
//			Log.e(TAG, "distanceList.size()----------------" + distanceList.size());
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
		double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
		dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = dist * 60 * 1.1515;
		String dst = "" + dist;
		
		String dst1 = "0.0";
		if(dst.length() > 4){
			 dst1 = dst.substring(0, dst.indexOf(".") + 3);
			System.out.print("1st---->"+dst1);
		}else if (dst.length() == 3) {
			 dst1 = dst.substring(0,dst.indexOf(".") + 2);
			System.out.print("2nd---->"+ dst1);
		}else if (dst.length() == 4) {
			 dst1 =dst.substring(0, dst.indexOf(".") + 3);
			System.out.print("3rd---->"+ dst1);
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

}
