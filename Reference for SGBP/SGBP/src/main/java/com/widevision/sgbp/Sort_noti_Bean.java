package com.widevision.sgbp;

public class Sort_noti_Bean {

//	   public final Double distance,distancemeter1,distancemeter2,distancemeter3,distancemeter4;
	   public final String Message;
	   public final String Notification_Date;
	   public final String Device_UID;
//	   public final String store_phone;
	   

	   public Sort_noti_Bean(String Message, String Notification_Date, String Device_UID){
		   this.Message = Message;
	      this.Notification_Date = Notification_Date;
	      this.Device_UID=Device_UID;
	  
	   }


	public String getMessage() {
		return Message;
	}


	public String getNotification_Date() {
		return Notification_Date;
	}


	public String getDevice_UID() {
		return Device_UID;
	}
	   
	   

}
