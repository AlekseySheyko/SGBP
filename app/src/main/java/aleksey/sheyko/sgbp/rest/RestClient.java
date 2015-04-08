package aleksey.sheyko.sgbp.rest;

import com.squareup.okhttp.OkHttpClient;

import retrofit.RestAdapter;
import retrofit.client.OkClient;

import static retrofit.RestAdapter.LogLevel.FULL;

public class RestClient {

    private ApiService mApiService;

    public RestClient() {
        String schoolsUrl = "http://test.sgbp.info/SGBPWS.asmx";

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(FULL)
                .setClient(new OkClient(new OkHttpClient()))
                .setEndpoint(schoolsUrl)
                .build();

        mApiService = restAdapter.create(ApiService.class);
    }

    public ApiService getApiService() {
        return mApiService;
    }
}
