package com.ducnguyen46.soc.service;

import com.ducnguyen46.soc.model.Product;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ProductRestService {

    //get all products
    @GET("/products")
    Call<List<Product>> getAllProducts(@Header("Authorization") String token);

    @GET("/product/{id}")
    Call<Product> getProductById(@Header("Authorization") String token, @Path("id") long id);

    @GET("/productFindByName/{name}")
    Call<List<Product>> findProductByName(@Header("Authorization") String token, @Path("name") String name);
    
}
