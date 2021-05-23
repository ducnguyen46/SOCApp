package com.ducnguyen46.soc.service;

import com.ducnguyen46.soc.model.Bill;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface BillRestService {
    //tiến hành thanh toán
    @POST("/bill")
    Call<Void> createBill(
            @Header("Authorization") String token,
            @Body Bill address);

    @GET("/bills")
    Call<List<Bill>> getAllBill(
            @Header("Authorization") String token);

}
