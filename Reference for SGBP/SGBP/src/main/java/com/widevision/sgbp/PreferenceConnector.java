package com.widevision.sgbp;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
/**
 * The class PreferenceConnector is a class useful to
 * simplify you the interaction with your app preferences.
 * In fact it has methods that interact with the basical features
 * of SharedPreferences but still the possibility to obtain
 * preferences.
 * 
 * @author Simone Casagranda
 *
 */
public class PreferenceConnector{
	public static final String PREF_NAME = "CASHONE";
	public static final int MODE = Context.MODE_WORLD_WRITEABLE;
	
	public static final String Is_Notification_Allowed = "Is_Notification_Allowed";
	public static final String Is_Location_Service_Allowed = "Is_Location_Service_Allowed";
	public static final String Is_User_Over_18_Year = "Is_User_Over_18_Year";
	public static final String Is_Coupon_Allowed = "Is_Coupon_Allowed";
	public static final String GCM_ID = "GCM_ID";
	public static final String AUTOMATIC_CHECK_FLAG = "AUTOMATIC_CHECK_FLAG";
//	public static final String DEVICE_INFO_ID = "DEVICE_INFO_ID";


//	{"isFirstCashAdvance":false,
//		"SessionId":"6ffddc07-0872-481f-aa99-1f3520627cd3",
//		"LoanID":7,
//		"IsSecurity":true,
//		"IsEsign":false,
//		"StoreID":21,
//		"CustomerID":252535}
	
	
	
	public static void writeBoolean(Context context, String key, boolean value) {
		getEditor(context).putBoolean(key, value).commit();
	}

	public static boolean readBoolean(Context context, String key, boolean defValue) {
		return getPreferences(context).getBoolean(key, defValue);
	}

	public static void writeInteger(Context context, String key, int value) {
		getEditor(context).putInt(key, value).commit();

	}

	public static int readInteger(Context context, String key, int defValue) {
		return getPreferences(context).getInt(key, defValue);
	}

	public static void writeString(Context context, String key, String value) {
		getEditor(context).putString(key, value).commit();

	}
	
	public static String readString(Context context, String key, String defValue) {
		return getPreferences(context).getString(key, defValue);
	}
	
	public static void writeFloat(Context context, String key, float value) {
		getEditor(context).putFloat(key, value).commit();
	}

	public static float readFloat(Context context, String key, float defValue) {
		return getPreferences(context).getFloat(key, defValue);
	}
	
	public static void writeLong(Context context, String key, long value) {
		getEditor(context).putLong(key, value).commit();
	}

	public static long readLong(Context context, String key, long defValue) {
		return getPreferences(context).getLong(key, defValue);
	}

	public static SharedPreferences getPreferences(Context context) {
		return context.getSharedPreferences(PREF_NAME, MODE);
	}

	public static Editor getEditor(Context context) {
		return getPreferences(context).edit();
	}

}
