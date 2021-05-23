package com.ducnguyen46.soc.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ProductInCart implements Serializable {


    @SerializedName("id")
    @Expose
    private long id;
    @SerializedName("productDTO")
    @Expose
    private Product product;
    @SerializedName("quantity")
    @Expose
    private int quantity;;

    public ProductInCart() {
    }

    public ProductInCart(long id, Product product, int quantity) {
        this.id = id;
        this.product = product;
        this.quantity = quantity;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
