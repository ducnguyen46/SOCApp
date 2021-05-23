package com.ducnguyen46.soc.service;

import com.ducnguyen46.soc.model.Product;
import com.ducnguyen46.soc.model.ProductAddCart;
import com.ducnguyen46.soc.model.ProductInCart;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.*;

public interface ProductCartRestService {


    @POST("/productInCart")
    Call<Void> addProductToCart(@Header("Authorization") String token, @Body ProductAddCart productAddCart);


    @GET("/productInCart")
    Call<List<ProductInCart>> getAllProductInCart(@Header("Authorization") String token);

    //change quantity of product in cart
    // id product
    @PUT("/productInCart")
    Call<Void> updateProductInCartQuantity(@Header("Authorization") String token, @Body ProductAddCart productAddCart);

    // id product
    @DELETE("/productInCart/{id}")
    Call<Void> deleteProductInCart(@Header("Authorization") String token, @Path("id") long id);
}
