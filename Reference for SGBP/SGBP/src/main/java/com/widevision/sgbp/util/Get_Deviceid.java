package com.widevision.sgbp.util;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;
 
public class Get_Deviceid {
     
    private Context _context;
	private String imeistring,deviceid;
     
    public Get_Deviceid(Context context){
        this._context = context;
    }
 
    public String get_unique_deviceid(){      
    	
    	 TelephonyManager telephonyManager  = ( TelephonyManager )_context.getSystemService( Context.TELEPHONY_SERVICE );
        
         imeistring = telephonyManager.getDeviceId();
     	                  
         Log.d(" imeistring---------->", imeistring);
        
         return imeistring;
    }
}
