package com.ducnguyen46.soc.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public class Bill {

    public Bill() {
    }

    public Bill(long id, String address, Date crateDate, double totalProductAmount, List<ProductSold> listProductHasSold) {
        this.id = id;
        this.address = address;
        this.crateDate = crateDate;
        this.totalProductAmount = totalProductAmount;
        this.listProductHasSold = listProductHasSold;
    }

    @SerializedName("id")
    @Expose
    private long id;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("crateDate")
    @Expose
    private Date crateDate;
    @SerializedName("totalProductAmount")
    @Expose
    private double totalProductAmount;
    @SerializedName("listProductHasSoldDTOs")
    @Expose
    private List<ProductSold> listProductHasSold;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getCrateDate() {
        return crateDate;
    }

    public void setCrateDate(Date crateDate) {
        this.crateDate = crateDate;
    }

    public double getTotalProductAmount() {
        return totalProductAmount;
    }

    public void setTotalProductAmount(double totalProductAmount) {
        this.totalProductAmount = totalProductAmount;
    }

    public List<ProductSold> getListProductHasSold() {
        return listProductHasSold;
    }

    public void setListProductHasSold(List<ProductSold> listProductHasSold) {
        this.listProductHasSold = listProductHasSold;
    }
}
