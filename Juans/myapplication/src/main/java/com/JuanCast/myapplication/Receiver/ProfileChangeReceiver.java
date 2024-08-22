package com.JuanCast.myapplication.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;

import com.JuanCast.myapplication.Profile;

public class ProfileChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        boolean isConnected = isNetworkConnected(context);
        if (context instanceof Profile) {
            Profile activity = (Profile) context;
            if (isConnected) {
                activity.getNoInternetLayout().setVisibility(View.GONE);
            } else {
                activity.getNoInternetLayout().setVisibility(View.VISIBLE);
            }
        }
    }

    private boolean isNetworkConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}
