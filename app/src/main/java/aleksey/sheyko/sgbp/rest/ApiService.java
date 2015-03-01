package aleksey.sheyko.sgbp.rest;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;

public interface ApiService {

    @GET("/GetSchoolName")
    void listSchools(Callback<Response> callback);

    @GET("/GetStoresList")
    void listStores(Callback<Response> callback);

    @FormUrlEncoded
    @POST("/SaveTeamMemberInfo")
    void register(@Field("Key") String key, @Field("Device_Info_Id") int deviceId,
                  @Field("First_Name") String firstName, @Field("Last_Name") String lastName,
                  @Field("Device_UID") String userId, @Field("School_Id") int schoolId,
                  @Field("User_Email") String email, @Field("User_Type") int userType,
                  @Field("Has_Multiple_Child") boolean multiGrade, @Field("Is_User_Registered") boolean isRegistered,
                  @Field("Is_Coupon_Allowed") boolean receiveCoupons, @Field("Is_Notification_Allowed") boolean getNotifications,
                  @Field("Is_Location_Service_Allowed") boolean trackLocation, @Field("Is_User_Over_18_Year") boolean is18,
                  @Field("Is_Device_Registered") boolean isDeviceRegistered, Callback<Response> callback);
}
