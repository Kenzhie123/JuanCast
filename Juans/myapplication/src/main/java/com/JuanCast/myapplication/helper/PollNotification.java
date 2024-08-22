package com.JuanCast.myapplication.helper;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;

import com.JuanCast.myapplication.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class PollNotification extends Service {

    FirebaseFirestore db;
    ArrayList<String> pollIDs;
    String notificationTitle = "";
    String notificationMessage = "";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        db = FirebaseFirestore.getInstance();
        pollIDs = new ArrayList<>();

        waitForNotification();
        String channelID = "New Poll";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelID,
                    channelID,
                    NotificationManager.IMPORTANCE_HIGH

            );
            getSystemService(NotificationManager.class).createNotificationChannel(channel);
            Notification.Builder notification = new Notification.Builder(PollNotification.this, channelID)
                    .setContentText("")
                    .setContentTitle("Juan Cast Polls")
                    .setSmallIcon(R.drawable.juanscast);
            startForeground(2024,notification.build());
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public PollNotification() {

    }

    private void waitForNotification() {
        db.collection("voting_polls")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @SuppressLint("ForegroundServiceType")
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (pollIDs.size() == 0) {

                            for (DocumentSnapshot document : value.getDocuments()) {
                                pollIDs.add(document.getId());
                            }
                            Log.d("DATA2TAG","NO ITEMS");
                        } else {

                            for (DocumentSnapshot document : value.getDocuments()) {

                                if (!pollIDs.contains(document.getId())) {
                                    Map<String, Object> data =document.getData();
                                    notificationTitle = "The poll for " + data.get("poll_title") + " has started!";
                                    notificationMessage = (String) data.get("note");
                                    pollIDs.add(document.getId());
                                    showNotification(notificationTitle,notificationMessage);
                                    Log.d("DATA2TAG","NOTIFICATIONSHOWED");
                                }
                            }

                        }


                    }
                });
    }

    @SuppressLint("ForegroundServiceType")
    private void showNotification(String title, String message) {
        String channelID = "New Poll";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {


            Notification.Builder notification = new Notification.Builder(PollNotification.this, channelID)
                    .setContentText(message)
                    .setContentTitle(title)
                    .setSmallIcon(R.drawable.juanscast);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                notificationManager.notify(2, notification.build());

            }
            else
            {
                Log.d("DATA2TAG","No Permission");
                Log.d("DATA2TAG", String.valueOf(ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)));

            }


        }
        else
        {
            Log.d("DATA2TAG","SDK INT");
        }
    }
}
