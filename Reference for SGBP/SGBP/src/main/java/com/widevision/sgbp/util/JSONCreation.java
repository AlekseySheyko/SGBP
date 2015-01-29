package com.widevision.sgbp.util;

import org.json.JSONArray;
import org.json.JSONObject;


public class JSONCreation
{
	
	private JSONObject jsonObject,jsonObjectsave,jsonObjectupdate,grandparent,grandparentsave,grandparentupdate;
	private JSONArray parent,parentsave,parentupdate;

	public JSONCreation(){
		grandparent = new JSONObject();
		parent = new JSONArray();
		grandparentsave = new JSONObject();
		parentsave = new JSONArray();
		grandparentupdate = new JSONObject();
		parentupdate = new JSONArray();
	}
	
	public void jsonget(String lat,String lng,String name,String phone,String add,int row, String add2, String is_storelocation_Physical) 
	{
		
        try {
        	
        	jsonObject = new JSONObject();
			jsonObject.put("lat", lat);
			jsonObject.put("lng", lng);
			jsonObject.put("name", name);
			jsonObject.put("phone", phone);
			jsonObject.put("add", add);
			jsonObject.put("add2", add2);
			jsonObject.put("is_storelocation_Physical", is_storelocation_Physical);
			parent.put(jsonObject);
			
			grandparent.put("storedata",parent);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//			return grandparent;
	
		}
	
	public void savemultigrade(String School_Id,String Grade_Id,String Device_UID,String Grade_Info_Id,String Device_Info_Id){
		
		
		try {
			
			jsonObjectsave = new JSONObject();
//			grandparentsave = new JSONObject();
//			parentsave = new JSONArray();
			jsonObjectsave.put("Device_Info_Id", Device_Info_Id);
			jsonObjectsave.put("Device_UID", Device_UID);
			jsonObjectsave.put("School_Id", School_Id);
			jsonObjectsave.put("Grade_Info_Id", Grade_Info_Id);
			jsonObjectsave.put("Grade_Id", Grade_Id);
			
//			User_Reg_Info_Id
//			User_Reg_Grade_Info_Id
			
			/*"Device_Info_Id": 1,
	        "Device_UID": "1234567890",
	        "School_Id": 1,
	        "Grade_Info_Id": 1,
	        "Grade_Id": 1*/
			parentsave.put(jsonObjectsave);
//			grandparent1.put("Key",Key);
//			grandparent1.put("UserID",UserId);
			grandparentsave.put("",parentsave);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void updatemultigrade(String User_Reg_Grade_Info_Id,String User_Reg_Info_Id,String School_Id,String Grade_Id,String Device_UID,String Grade_Info_Id,String Device_Info_Id){
		
		
		try {
			
			jsonObjectupdate = new JSONObject();
			jsonObjectupdate.put("User_Reg_Grade_Info_Id", User_Reg_Grade_Info_Id);
		
			jsonObjectupdate.put("User_Reg_Info_Id", User_Reg_Info_Id);
			
			jsonObjectupdate.put("Device_Info_Id", Device_Info_Id);
			jsonObjectupdate.put("Device_UID", Device_UID);
			jsonObjectupdate.put("School_Id", School_Id);
			jsonObjectupdate.put("Grade_Info_Id", Grade_Info_Id);
			jsonObjectupdate.put("Grade_Id", Grade_Id);
			
			 /*http://demo.myfirstland.com/SGBP/SGBPWSJSON.svc/UpdateUserGrade?Key=1&UserID=172&
				 InpupGradeUpdateList=
				 [{"User_Reg_Grade_Info_Id":1,
					 "User_Reg_Info_Id":123,
					 "Device_Info_Id":1,
					 "Device_UID":"1234567890",
					 "School_Id":1,
					 "Grade_Info_Id":1,
					 "Grade_Id":1},
					 
					 *
					 *
					 * "Grade_Info_Id": "35",
        "User_Reg_Grade_Info_Id": "118",
        "Grade_Id": "11",
        "Device_UID": "00878796787696979",
        "User_Reg_Info_Id": "257",
        "Device_Info_Id": "0"*/
					
			parentupdate.put(jsonObjectupdate);
//			grandparent1.put("Key",Key);
//			grandparent1.put("UserID",UserId);
			grandparentupdate.put("",parentupdate);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public JSONObject savemultigrade(){
		return grandparentsave;
	}
	public JSONObject updatemultigrade(){
		return grandparentupdate;
	}
	public JSONObject jsobparse(){
		return grandparent;
	}
	
	}

	

