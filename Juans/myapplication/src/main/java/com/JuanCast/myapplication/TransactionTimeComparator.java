package com.JuanCast.myapplication;

import java.util.Comparator;

public class TransactionTimeComparator implements Comparator<Transaction> {
    @Override
    public int compare(Transaction t1, Transaction t2) {
        // Assuming time is in a format that can be compared directly (e.g., ISO 8601 date-time)
        return t2.getTime().compareTo(t1.getTime()); // Descending order
    }
}
