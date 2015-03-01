package aleksey.sheyko.sgbp.rest;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.GET;

public interface ApiService {

    @GET("/GetSchoolName")
    void getSchoolsList(Callback<Response> callback);

    @GET("/GetGradesList")
    void getGradesList(Callback<Response> callback);
}
