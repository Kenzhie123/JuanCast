package com.JuanCast.myapplication.models;

import com.google.firebase.Timestamp;

public class PurchaseTransaction {

    private String transactionID;
    private String amountCharged;
    private String transactionType;
    private String referenceNumber;
    private Long starAmount;
    private Timestamp timeStamp;
    private String powerupName;


    public PurchaseTransaction(String transactionID,String amountCharged,String transactionType, String referenceNumber, Long starAmount, Timestamp timeStamp) {
        this.transactionID = transactionID;
        this.transactionType = transactionType;
        this.amountCharged = amountCharged;
        this.referenceNumber = referenceNumber;
        this.starAmount = starAmount;
        this.timeStamp = timeStamp;
    }

    public PurchaseTransaction(String transactionID,String amountCharged,String transactionType, String referenceNumber, Timestamp timeStamp) {
        this.transactionID = transactionID;
        this.transactionType = transactionType;
        this.amountCharged = amountCharged;
        this.referenceNumber = referenceNumber;
        this.timeStamp = timeStamp;
    }

    public String getTransactionID() {
        return transactionID;
    }

    public String getAmountCharged() {
        return amountCharged;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public String getPowerupName() {
        return powerupName;
    }

    public Long getStarAmount() {
        return starAmount;
    }

    public Timestamp getTimeStamp() {
        return timeStamp;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public void setAmountCharged(String amountCharged) {
        this.amountCharged = amountCharged;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public void setStarAmount(Long starAmount) {
        this.starAmount = starAmount;
    }

    public void setTimeStamp(Timestamp timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public void setPowerupName(String powerupName) {
        this.powerupName = powerupName;
    }
}
