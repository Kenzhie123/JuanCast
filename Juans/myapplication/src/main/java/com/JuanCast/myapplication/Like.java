package com.JuanCast.myapplication;

public class Like {

    private String userId;

    public Like() {
        // Required empty public constructor for Firestore to deserialize
    }

    public Like(String userId) {
        this.userId = userId;
    }

    // Getter and setter
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}