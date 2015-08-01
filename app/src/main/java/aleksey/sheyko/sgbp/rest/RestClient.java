package aleksey.sheyko.sgbp.rest;

import retrofit.RestAdapter;

import static retrofit.RestAdapter.LogLevel.FULL;

public class RestClient {

    private ApiService mApiService;

    public RestClient() {
        final String baseUrl = "http://test.sgbp.info/SGBPWS.asmx";

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(FULL)
                .setEndpoint(baseUrl)
                .build();

        mApiService = restAdapter.create(ApiService.class);
    }

    public ApiService getApiService() {
        return mApiService;
    }
}
