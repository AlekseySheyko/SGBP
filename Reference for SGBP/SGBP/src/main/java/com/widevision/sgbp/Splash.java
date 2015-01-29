package com.widevision.sgbp;

import static com.widevision.sgbp.CommonUtilities.DISPLAY_MESSAGE_ACTION;
import static com.widevision.sgbp.CommonUtilities.SENDER_ID;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gcm.GCMRegistrar;

public class Splash extends Activity {

	private String deviceregistered;
	private String json, result;
	public boolean Is_Device_Registered;
	private String imeistring = null;
	SharedPreferences  onepref;

	// Asyntask
	AsyncTask<Void, Void, Void> mRegisterTask;
	String regId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.splash);

		System.out.println("SGBP Splash Screen ------------------------------------->");
		
		TelephonyManager telephonyManager;

		telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		imeistring = telephonyManager.getDeviceId()+"0";
		System.out.println(imeistring);
		
		onepref = getSharedPreferences("one", Context.MODE_PRIVATE);
		Editor editor = onepref.edit();
		editor.putBoolean("register", false);
		editor.commit();
		
		
		GCM_REGISTRATION(imeistring);

		// userFunction = new Userfunction();

		// lat_long();

		// class Asedit extends AsyncTask<Void, Void, String> {
		// ProgressDialog dialog = new ProgressDialog(Splash.this);
		//
		// @Override
		// protected String doInBackground(Void... params) {
		//
		// TelephonyManager telephonyManager;
		//
		// telephonyManager = (TelephonyManager)
		// getSystemService(Context.TELEPHONY_SERVICE);
		// imeistring = telephonyManager.getDeviceId();
		// System.out.println(imeistring);
		//
		// GCM_REGISTRATION(imeistring);
		//
		// return result;
		//
		// }
		//
		// protected void onPostExecute(String result) {
		//
		// try {
		// dialog.cancel();
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		//
		//
		//
		// if (regId.equals("")) {
		//
		//
		//
		// } else {
		//
		// Intent l = new Intent(Splash.this, MainActivity.class);
		// startActivity(l);
		// finish();
		//
		// // Intent l = new Intent(Splash.this, LoginActivity.class);
		// // startActivity(l);
		// // finish();
		// }
		//
		// }
		//
		// protected void onPreExecute() {
		// super.onPreExecute();
		// dialog.setMessage("Please Wait... ");
		//
		// dialog.setCancelable(true);
		//
		// }
		//
		// }

		// Asedit ae = new Asedit();
		// ae.execute();

	}

	public void GCM_REGISTRATION(final String imeistring2) {

		// Make sure the device has the proper dependencies.
		GCMRegistrar.checkDevice(this);
		GCMRegistrar.checkManifest(this);

		registerReceiver(mHandleMessageReceiver, new IntentFilter(DISPLAY_MESSAGE_ACTION));

		// Get GCM registration id
		regId = GCMRegistrar.getRegistrationId(this);

		// userFunction.gcmregister(regId);

		Log.d("Regiter id>>>>>>>>>>", "--->" + regId);
		/*PreferenceConnector.readString(
				getApplicationContext(),
				PreferenceConnector.GCM_ID, regId);*/
		// Check if regid already presents
		if (regId.equals("")) {
			// Registration is not present, register now with GCM
			GCMRegistrar.register(this, SENDER_ID);
			
			

		} else {
			// Device is already registered on GCM
			if (GCMRegistrar.isRegisteredOnServer(this)) {
				Log.d("Regiter id>>>>>>>>>>", "-if----------->" + regId);
				Intent l = new Intent(Splash.this, MainActivity.class);

				PreferenceConnector.writeString(
						getApplicationContext(),
						PreferenceConnector.GCM_ID, regId);
				startActivity(l);
				finish();

			} else {
				Log.d("Regiter id>>>>>>>>>>", "--else-------------->" + regId);
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
					

						Intent l = new Intent(Splash.this, MainActivity.class);

						PreferenceConnector.writeString(
								getApplicationContext(),
								PreferenceConnector.GCM_ID, regId);
						startActivity(l);
						finish();
						
						mRegisterTask = null;

					}

				};
				mRegisterTask.execute(null, null, null);
			}
		}

	}

	/**
	 * Receiving push messages
	 * */
	private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			 String newMessage = intent.getExtras().getString("message");
			// Waking up mobile if it is sleeping
			WakeLocker.acquire(getApplicationContext());

			/**
			 * Take appropriate action on this message depending upon your app
			 * requirement For now i am just displaying it on the screen
			 * */

			// Showing received message
			// lblMessage.append(newMessage + "\n");
//			 Toast.makeText(getApplicationContext(), "New Message: " +
//			 newMessage, Toast.LENGTH_LONG).show();
			 
			 if (newMessage.equals("Your are registred successfully")) {
				 
				 
					Intent l = new Intent(Splash.this, MainActivity.class);

					PreferenceConnector.writeString(
							getApplicationContext(),
							PreferenceConnector.GCM_ID, regId);
					startActivity(l);
					finish();
				 
				 
				
				} else {
					
					Toast.makeText(getApplicationContext(), "New Message: " +
							 newMessage, Toast.LENGTH_LONG).show();
	
				}
			 
			 

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

}
