package com.example.lendbox.Utils;

import com.example.lendbox.Interface.SignupAPI;

/**
 * Created by DU on 11/13/2017.
 */

public class ApiUtils {

    private ApiUtils() {}

    public static final String BASE_URL = "https://www.lendbox.in/";

    public static SignupAPI getAPIService() {

        return RetrofitClient.getClient(BASE_URL).create(SignupAPI.class);
    }
}
