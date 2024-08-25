package com.JuanCast.myapplication.models;

import com.google.firebase.Timestamp;

public class PurchaseTransaction {

    private String transactionID;
    private String amountCharged;
    private String referenceNumber;
    private Long starAmount;
    private Timestamp timeStamp;

    public PurchaseTransaction(String transactionID,String amountCharged, String referenceNumber, Long starAmount, Timestamp timeStamp) {
        this.transactionID = transactionID;
        this.amountCharged = amountCharged;
        this.referenceNumber = referenceNumber;
        this.starAmount = starAmount;
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
}
