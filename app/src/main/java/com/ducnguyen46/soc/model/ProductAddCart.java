package com.ducnguyen46.soc.model;
import com.google.gson.annotations.SerializedName;

public class ProductAddCart {

    @SerializedName("idProduct")
    private long idProduct;

    @SerializedName("quantity")
    private int quantity;

    public ProductAddCart() {
    }

    public ProductAddCart(long idProduct, int quantity) {
        this.idProduct = idProduct;
        this.quantity = quantity;
    }

    public long getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(long idProduct) {
        this.idProduct = idProduct;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
