package aleksey.sheyko.sgbp.rest;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

public interface ApiService {

    @GET("/GetSchoolName")
    void listSchools(Callback<Response> callback);

    @GET("/GetGradesList")
    void listGrades(Callback<Response> callback);

    @GET("/GetStoresList")
    void listAllStores(Callback<Response> callback);

    @GET("/GetCouponCodeDetails")
    void listCoupons(Callback<Response> callback);

    @FormUrlEncoded
    @POST("/SaveTeamMemberInfo")
    void register(@Field("Key") String key, @Field("Device_Info_Id") int userId,
                  @Field("First_Name") String firstName, @Field("Last_Name") String lastName,
                  @Field("Device_UID") String deviceId, @Field("School_Id") int schoolId,
                  @Field("User_Email") String email, @Field("User_Type") int userType,
                  @Field("Has_Multiple_Child") boolean multiGrade, @Field("Is_User_Registered") boolean isRegistered,
                  @Field("Is_Coupon_Allowed") boolean receiveCoupons, @Field("Is_Notification_Allowed") boolean getNotifications,
                  @Field("Is_Location_Service_Allowed") boolean trackLocation, @Field("Is_User_Over_18_Year") boolean is18,
                  @Field("Is_Device_Registered") boolean isDeviceRegistered, Callback<Response> callback);

    @FormUrlEncoded
    @POST("/UpdateTeamMemberInfo")
    void update(@Field("Key") String key, @Field("First_Name") String firstName,
                @Field("Last_Name") String lastName, @Field("School_Id") int schoolId,
                @Field("Has_Multiple_Child") boolean multiGrade, @Field("Is_User_Registered") boolean isRegistered,
                @Field("Is_Coupon_Allowed") boolean receiveCoupons, @Field("Is_Notification_Allowed") boolean getNotifications,
                @Field("Is_Location_Service_Allowed") boolean trackLocation, @Field("Is_User_Over_18_Year") boolean is18,
                @Field("User_Reg_Info_Id") int userId, Callback<Response> callback);

    @GET("/GetUserReginfoBYDevice_UID")
    void checkRegistration(@Query("Device_UID") String deviceId, Callback<Response> callback);

    @FormUrlEncoded
    @POST("/SaveProgramParticipationByDevice")
    void participate(@Field("Key") String userId, @Field("Device_Info_Id") int deviceId,
                     @Field("Device_UID") String deviceId2, @Field("School_Id") int schoolId,
                     @Field("Store_Id") int storeId, @Field("Participation_DateTime") String dateTime,
                     @Field("Clicked_To_Participate") boolean isMobile, Callback<Response> callback);
}
