package com.widevision.sgbp;

public class SortBean {

	   public final Double distance,distancemeter1,distancemeter2,distancemeter3,distancemeter4;
	   public final String store_id;
	   public final String store_name;
	   public final String store_add;
	   public final String store_phone;
	   

	   public SortBean(Double distance,Double distancemeter1,Double distancemeter2,Double distancemeter3,Double distancemeter4,String store_id,String store_name,String store_add,String store_phone) {
		   this.store_id = store_id;
	      this.distance = distance;
	      this.distancemeter1=distancemeter1;
	      this.distancemeter2=distancemeter2;
	      this.distancemeter3=distancemeter3;
	      this.distancemeter4=distancemeter4;
	      this.store_name=store_name;
	      this.store_add=store_add;
	      this.store_phone=store_phone;
	   }
	   
	   

}
