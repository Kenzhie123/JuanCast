package com.JuanCast.myapplication.models;

import com.android.billingclient.api.ProductDetails;

public class ProductStar {
    private String productID;
    private int starAmount;
    private ProductDetails productDetails;

    public ProductStar(String productID, int starAmount) {
        this.productID = productID;
        this.starAmount = starAmount;
    }

    public ProductStar(String productID, int starAmount, ProductDetails productDetails) {
        this.productID = productID;
        this.starAmount = starAmount;
        this.productDetails = productDetails;
    }

    public String getProductID() {
        return productID;
    }

    public int getStarAmount() {
        return starAmount;
    }

    public String getFormattedPrice()
    {
        return productDetails.getOneTimePurchaseOfferDetails().getFormattedPrice();
    }

    public ProductDetails getProductDetails() {
        return productDetails;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public void setStarAmount(int starAmount) {
        this.starAmount = starAmount;
    }

    public void setProductDetails(ProductDetails productDetails) {
        this.productDetails = productDetails;
    }

    @Override
    public String toString() {
        return "ProductStar{" +
                "productID='" + productID + '\'' +
                ", starAmount=" + starAmount +
                '}';
    }
}
