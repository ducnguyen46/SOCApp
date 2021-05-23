package com.ducnguyen46.soc.service;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

import okhttp3.Authenticator;
import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.OkHttp;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import static com.ducnguyen46.soc.MainActivity.*;
import static com.ducnguyen46.soc.constant.Constant.*;

public class RetrofitClient {
    private static Retrofit retrofit = null;
    public static Retrofit getClient(String baseUrl) {
        if (retrofit==null) {
            Gson gson = new GsonBuilder().setLenient().create();
//            ServiceInterceptor interceptor=new ServiceInterceptor();
//
//            OkHttpClient client = new OkHttpClient.Builder()
//                    .addInterceptor(interceptor).build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }
}
class ServiceInterceptor implements Interceptor {

     @NotNull
     @Override
     public Response intercept(@NotNull Chain chain) throws IOException {
         Request request = chain.request();
         if(request.header("No-Authentication") == null){

             SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());

             String token = sharedPreferences.getString(TOKEN_USER, "");

             request = request.newBuilder()
                     .addHeader("Authorization", "Bearer " + token)
                     .build();
         }
         return chain.proceed(request);
     }
 }