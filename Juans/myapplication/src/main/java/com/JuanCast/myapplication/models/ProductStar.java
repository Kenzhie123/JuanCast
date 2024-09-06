package com.JuanCast.myapplication.models;

public class ProductStar {
    private String productID;
    private int starAmount;

    public ProductStar(String productID, int starAmount) {
        this.productID = productID;
        this.starAmount = starAmount;
    }

    public String getProductID() {
        return productID;
    }

    public int getStarAmount() {
        return starAmount;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public void setStarAmount(int starAmount) {
        this.starAmount = starAmount;
    }
}
