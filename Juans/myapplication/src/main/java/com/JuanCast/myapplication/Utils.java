package com.JuanCast.myapplication;

import java.util.Date;

public class Utils {

    public static String getTimeAgo(long timestamp) {
        // Implement your logic to convert timestamp to time ago string
        // Example implementation:
        long now = System.currentTimeMillis();
        long diff = now - timestamp;

        if (diff < 60 * 1000) {
            return "just now";
        } else if (diff < 2 * 60 * 1000) {
            return "a minute ago";
        } else if (diff < 60 * 60 * 1000) {
            return diff / (60 * 1000) + " minutes ago";
        } else if (diff < 2 * 60 * 60 * 1000) {
            return "an hour ago";
        } else if (diff < 24 * 60 * 60 * 1000) {
            return diff / (60 * 60 * 1000) + " hours ago";
        } else {
            return diff / (24 * 60 * 60 * 1000) + " days ago";
        }
    }

    public static String getTimeAgo(Date date) {
        return getTimeAgo(date.getTime());
    }
}
