package com.ducnguyen46.soc.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductSold {

    public ProductSold(ProductInCart productInCart) {
        this.id = productInCart.getId();
        this.name = productInCart.getProduct().getName();
        this.brand = productInCart.getProduct().getBrand();
        this.madein = productInCart.getProduct().getMadein();
        this.price = productInCart.getProduct().getPrice();
        this.quantity = productInCart.getQuantity();
    }

    public ProductSold(){}

    @SerializedName("id")
    @Expose
    private long id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("brand")
    @Expose
    private String brand;

    @SerializedName("madein")
    @Expose
    private String madein;

    @SerializedName("price")
    @Expose
    private double price;

    @SerializedName("quantity")
    @Expose
    private int quantity;

    @SerializedName("imageURL")
    @Expose
    private String imageURL;

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getMadein() {
        return madein;
    }

    public void setMadein(String madein) {
        this.madein = madein;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
