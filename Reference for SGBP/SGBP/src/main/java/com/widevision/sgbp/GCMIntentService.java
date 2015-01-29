package com.widevision.sgbp;

import static com.widevision.sgbp.CommonUtilities.SENDER_ID;
import static com.widevision.sgbp.CommonUtilities.displayMessage;
import static com.widevision.sgbp.CommonUtilities.displayMessage1;

import java.util.Calendar;

import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;


public class GCMIntentService extends GCMBaseIntentService {

	private static final String TAG = "GCMIntentService";
	
	private static JSONObject json;
	static TelephonyManager    telephonyManager;  
	public static String imeistring;
	Notification notification;
	

    public GCMIntentService() {
        super(SENDER_ID);
       
    }

    /**
     * Method called on device registered
     **/
    protected void onRegistered(Context context, String registrationId) {
        Log.i(TAG, "Device registered: regId = " + registrationId);
        displayMessage(context, "Your are registred successfully");
        ServerUtilities.register(context , registrationId);
    }

    /**
     * Method called on device un registred
     * */
    protected void onUnregistered(Context context, String registrationId) {
        Log.i(TAG, "Device unregistered");
        displayMessage(context, getString(R.string.gcm_unregistered));
//        ServerUtilities.unregister(context, registrationId);
    }

    /**
     * Method called on Receiving a new message
     * */
    protected void onMessage(Context context, Intent intent) {
        Log.i(TAG, "Received message");
        String message = intent.getExtras().getString("message");
//      String msg_id = intent.getExtras().getString("msg_id");
//      Log.i("msg_id------>", "Received onMessage" + msg_id);
        
        displayMessage1(context, message,"");
        // notifies user
        generateNotification_1(context, message,"");
    }

 



	/**
     * Method called on receiving a deleted message
     * */
    protected void onDeletedMessages(Context context, int total) {
        Log.i(TAG, "Received deleted messages notification");
        String message = getString(R.string.gcm_deleted, total);
        displayMessage(context, message);
        // notifies user
//        generateNotification(context, message);
    }

    /**
     * Method called on Error
     * */
    public void onError(Context context, String errorId) {
        Log.i(TAG, "Received error: " + errorId);
        displayMessage(context, getString(R.string.gcm_error, errorId));
    }

    protected boolean onRecoverableError(Context context, String errorId) {
        // log message
        Log.i(TAG, "Received recoverable error: " + errorId);
        displayMessage(context, getString(R.string.gcm_recoverable_error,
                errorId));
        return super.onRecoverableError(context, errorId);
    }

    /**
     * Issues a notification to inform the user that server has sent a message.
     */
//    private static void generateNotification(Context context, String message) {
//        int icon = R.drawable.star;
//        long when = System.currentTimeMillis();
//        NotificationManager notificationManager = (NotificationManager)
//                context.getSystemService(Context.NOTIFICATION_SERVICE);
//        Notification notification = new Notification(icon, message, when);
//        
//        String title = context.getString(R.string.app_name);
//        
//        String d_id= PreferenceConnector.readString(context, PreferenceConnector.DEVICE_ID , "Wrong device id");
//    
//		
//        Intent notificationIntent = new Intent(context, LoginActivity.class);
//        // set intent so it does not start a new activity
//        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
//                Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        PendingIntent intent =
//                PendingIntent.getActivity(context, 0, notificationIntent, 0);
//        notification.setLatestEventInfo(context, title, message, intent);
//        notification.flags |= Notification.FLAG_AUTO_CANCEL;
//        
//        // Play default notification sound
//        notification.defaults |= Notification.DEFAULT_SOUND;
//        
//        //notification.sound = Uri.parse("android.resource://" + context.getPackageName() + "your_sound_file_name.mp3");
//        
//        // Vibrate if vibrate is enabled
//        notification.defaults |= Notification.DEFAULT_VIBRATE;
//        notificationManager.notify(0, notification);      
//
//    }
    
    
	private void generateNotification_1(Context context, String message,String msg_id2) {
		
		
		
		final Calendar c = Calendar.getInstance();
	
		Log.w(TAG, "-Current_Time-----------" + c.getTime());
		
	
		    telephonyManager  = ( TelephonyManager )context.getSystemService( Context.TELEPHONY_SERVICE );
	        
	        imeistring = telephonyManager.getDeviceId();
		
		int icon = R.drawable.notification_icon;
        long when = System.currentTimeMillis();
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        
        if (this.notification == null)
            this.notification = new Notification(icon, message, when);
        
//        Notification notification = new Notification(icon, message, when);
        
        String title = context.getString(R.string.app_name);
             
               
    	Log.e(TAG, "-------"+message +" & Time--->"+c.getTime()); 
        
        
//        try {
//			json = userFunction.readnotification(imeistring,msg_id2);
//			Log.d("readnotification json ","--------------"+json);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		
        Intent notificationIntent = new Intent(context, MainActivity.class);
        
        
        // set intent so it does not start a new activity
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        PendingIntent intent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
        this.notification.setLatestEventInfo(context, title, message, intent);
        this.notification.flags |= Notification.FLAG_AUTO_CANCEL;
        
        // Play default notification sound
        this.notification.defaults |= Notification.DEFAULT_SOUND;
//        this.notification.number += 1;
           
        //notification.sound = Uri.parse("android.resource://" + context.getPackageName() + "your_sound_file_name.mp3");
        
        // Vibrate if vibrate is enabled
        this.notification.defaults |= Notification.DEFAULT_VIBRATE;
        notificationManager.notify(0, notification);  
        
        
		
		
	}

}
