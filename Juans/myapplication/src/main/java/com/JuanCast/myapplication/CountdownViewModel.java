package com.JuanCast.myapplication;

// CountdownViewModel.java
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CountdownViewModel extends ViewModel {
    private final MutableLiveData<Long> remainingTime = new MutableLiveData<>();

    public LiveData<Long> getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(long time) {
        remainingTime.setValue(time);
    }

    public void updateCountdown(long elapsedTime, long cooldownMillis) {
        long remaining = cooldownMillis - elapsedTime;
        if (remaining < 0) remaining = 0;
        setRemainingTime(remaining);
    }
}

