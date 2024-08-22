package com.JuanCast.myapplication;


import com.google.firebase.Timestamp;

public class Reward {
    private String date;
    private int rewardPoints;
    private String time;
    private String userId;
    private Timestamp timestamp; // Field for sorting by date

    public Reward() {
        // Default constructor required for calls to DataSnapshot.getValue(Reward.class)
    }

    public Reward(String date, int rewardPoints, String time, String userId, Timestamp timestamp) {
        this.date = date;
        this.rewardPoints = rewardPoints;
        this.time = time;
        this.userId = userId;
        this.timestamp = timestamp;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getRewardPoints() {
        return rewardPoints ;
    }

    // Method to get formatted reward points
    public String getFormattedRewardPoints() {
        return"Rewards: "+""+ "+" + rewardPoints + " Star";
    }

    public void setRewardPoints(int rewardPoints) {
        this.rewardPoints = rewardPoints;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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
