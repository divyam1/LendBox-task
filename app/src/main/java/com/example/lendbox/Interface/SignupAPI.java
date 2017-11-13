package com.example.lendbox.Interface;

import com.example.lendbox.Model.SignUpRequest;
import com.example.lendbox.Model.SignUpResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by DU on 11/12/2017.
 */

public interface SignupAPI {

    @POST("rest/v1/registerUser")
   /* @FormUrlEncoded*/
   // Call<SignUpResponse> getSignupacess(@Body SignUpRequest signUpRequest);

    Call<String> getSignupacess(@Body SignUpRequest signUpRequest);

   /* Call<String> getSignupacess(
    @Field("amount") int amount,
    @Field("fname") String fname,
    @Field("Lname") String lname,
    @Field("username") String username,
    @Field("password") String password,
    @Field("mobile") String mobile,
    @Field("title") String title,
    @Field("loanDuration") int loanDuration,
    @Field("roi") String roi,
    @Field("loanCity") String loanCity,
    @Field("loanPurpose") int loanPurpose,
    @Field("userType") String userType);*/

}
