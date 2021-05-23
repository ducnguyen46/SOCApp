package com.ducnguyen46.soc.service;

import com.ducnguyen46.soc.model.ChangePassword;
import com.ducnguyen46.soc.model.LoginUser;
import com.ducnguyen46.soc.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserRestService {
    //login
    @POST("/login")
    @Headers("No-Authentication: true")
    Call<String> login(@Body LoginUser user);

    //update user password
    @PUT("/user")
    Call<Void> updateUserPassword(@Header("Authorization") String token, @Body ChangePassword password);

    @POST("/register")
    @Headers("No-Authentication: true")
    Call<Void> register(@Body User user);

    //lấy thông tin của chính user
    @GET("/userInfo")
    Call<User> getUserInfo(@Header("Authorization") String token);
}
