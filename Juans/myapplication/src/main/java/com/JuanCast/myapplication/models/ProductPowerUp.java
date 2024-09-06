package com.JuanCast.myapplication.models;

import com.android.billingclient.api.ProductDetails;

public class ProductPowerUp {
    private String productID;
    private long durationMinutes;
    private ProductDetails productDetails;

    public ProductPowerUp(String productID, long durationMinutes) {
        this.productID = productID;
        this.durationMinutes = durationMinutes;
    }

    public ProductPowerUp(String productID, long durationMinutes, ProductDetails productDetails) {
        this.productID = productID;
        this.durationMinutes = durationMinutes;
        this.productDetails = productDetails;
    }

    public String getProductID() {
        return productID;
    }

    public long getDurationMinutes() {
        return durationMinutes;
    }

    public ProductDetails getProductDetails() {
        return productDetails;
    }

    public String getFormattedPrice()
    {
        return productDetails.getOneTimePurchaseOfferDetails().getFormattedPrice();
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public void setProductDetails(ProductDetails productDetails) {
        this.productDetails = productDetails;
    }
}
