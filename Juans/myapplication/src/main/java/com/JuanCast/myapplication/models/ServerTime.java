package com.JuanCast.myapplication.models;

import android.util.Log;

import androidx.annotation.NonNull;

import com.JuanCast.myapplication.Tools;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class ServerTime {
    public ServerTime()
    {

    }

    public void addOnSuccessListener(ServerTimeSuccessListener<Date> serverTimeSuccessListener){
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        dbRef.child("timestamp").setValue(ServerValue.TIMESTAMP);
        dbRef.child("timestamp").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Long serverTime = snapshot.getValue(Long.class);
                if(serverTime != null)
                {
                    serverTimeSuccessListener.onSuccess(new Date(serverTime));
                }
                else {
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("TOOLSTAG", error.getMessage());
            }
        });
    }

    public interface ServerTimeSuccessListener<T>{
        void onSuccess(T serverTime);
    }
}
