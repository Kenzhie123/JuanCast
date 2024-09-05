package com.JuanCast.myapplication;

import java.util.Date;

import com.google.firebase.Timestamp;

public class Redemption {
    private String userId;
    private String promoCode;
    private long pointsAwarded;
    private Timestamp date; // Use Firestore's Timestamp class

    // Default constructor required for calls to DataSnapshot.getValue(User.class)
    public Redemption() {
    }

    public Redemption(String userId, String promoCode, long pointsAwarded, Timestamp date) {
        this.userId = userId;
        this.promoCode = promoCode;
        this.pointsAwarded = pointsAwarded;
        this.date = date;
    }

    // Getters and setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }

    public long getPointsAwarded() {
        return pointsAwarded;
    }

    public void setPointsAwarded(long pointsAwarded) {
        this.pointsAwarded = pointsAwarded;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }
}


