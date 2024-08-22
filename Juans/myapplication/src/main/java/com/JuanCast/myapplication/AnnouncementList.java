package com.JuanCast.myapplication;

import com.google.firebase.Timestamp;

public class AnnouncementList {
    private String text;
    private String date;
    private String time;
    private Timestamp timestamp;



    // Empty constructor needed for Firestore
    public AnnouncementList() {
    }

    public AnnouncementList(String text, String date, String time, Timestamp timestamp) {
        this.text = text;
        this.date = date;
        this.time = time;
        this.timestamp = timestamp;

    }



    public String getText() {
        return text;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
