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

    @GET("/GetGradebySchool")
    void getGrade(@Query("Key") String key, @Query("School_Id") int schoolId, Callback<Response> callback);

    @GET("/GetStoresList")
    void listAllStores(Callback<Response> callback);

    @GET("/GetCouponCodeDetails")
    void listCoupons(Callback<Response> callback);

    @FormUrlEncoded
    @POST("/SaveDeviceInfo")
    void registerDevice(@Field("Key") String key, @Field("Device_UID") String deviceId,
                  @Field("Device_Name") String deviceName, @Field("Device_Type") String deviceType,
                  @Field("Device_Manufacturer_Name") String manufacturerName, @Field("Device_Model_Name") String modelName,
                  @Field("Device_Model_Number") String modelNumber, @Field("Device_System_Name") String systemName,
                  @Field("Device_System_Version") String systemVersion, @Field("Device_Software_Version") String softwareVersion,
                  @Field("Device_Platform_Version") String platformVersion, @Field("Device_Firmware_Version") String firmwareVersion,
                  @Field("Device_OS") String operatingSystem, @Field("Device_Timezone") String timezone,
                  @Field("Language_Used_On_Device") String language, @Field("Has_Camera") boolean hasCamera,
                  @Field("Is_Backlight_On") boolean backlightOn, @Field("Is_Battery_Removable") boolean batteryRemovable,
                  @Field("UserId") String userId, @Field("Is_Device_Registered") boolean isDeviceRegistered,
                  @Field("Device_Token_Registration_Id") String registrationId, Callback<Response> callback);

    @FormUrlEncoded
    @POST("/SaveTeamMemberInfo")
    void registerUser(@Field("Key") String key, @Field("Device_Info_Id") int deviceInfoId,
                      @Field("First_Name") String firstName, @Field("Last_Name") String lastName,
                      @Field("Device_UID") String deviceId, @Field("School_Id") int schoolId,
                      @Field("User_Email") String email, @Field("User_Type") int userType,
                      @Field("Has_Multiple_Child") boolean multiGrade, @Field("Is_User_Registered") boolean isRegistered,
                      @Field("Is_Coupon_Allowed") boolean receiveCoupons, @Field("Is_Notification_Allowed") boolean getNotifications,
                      @Field("Is_Location_Service_Allowed") boolean trackLocation, @Field("Is_User_Over_18_Year") boolean is18,
                      @Field("Is_Device_Registered") boolean isDeviceRegistered, Callback<Response> callback);
    @FormUrlEncoded
    @POST("/SaveGradeInfo")
    void saveGrade(@Field("Key") String key, @Field("Grade_Id") int gradeId,
                      @Field("School_Id") int schoolId, @Field("Grade_Name") String gradeName,
                      @Field("Eff_Date") String startDate, @Field("End_Date") String endDate, Callback<Response> callback);

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
