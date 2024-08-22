package com.JuanCast.myapplication;

import com.google.firebase.Timestamp;

import java.util.Date;

import com.google.firebase.Timestamp;

public class Transaction {
    private String date;
    private String referenceNumber;
    private int star;
    private int sun;
    private String time;
    private String transactionType;
    private String userId;
    private Timestamp timestamp;

    // Constructor matching your usage
    public Transaction(String date, String referenceNumber, int star, int sun, String time, String transactionType, String userId, Timestamp timestamp) {
        this.date = date;
        this.referenceNumber = referenceNumber;
        this.star = star;
        this.sun = sun;
        this.time = time;
        this.transactionType = transactionType;
        this.userId = userId;
        this.timestamp = timestamp;
    }

    // Getters and setters for each field
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public int getSun() {
        return sun;
    }

    public void setSun(int sun) {
        this.sun = sun;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}




