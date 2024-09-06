package com.JuanCast.myapplication;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.WriteBatch;

public class ResetVotingPointsWorker extends Worker {

    public ResetVotingPointsWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        // Perform the reset operation
        resetVotingPoints();
        return Result.success();
    }

    private void resetVotingPoints() {
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("User").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        WriteBatch batch = firebaseFirestore.batch();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            batch.update(document.getReference(), "votingPoints", 0);
                        }
                        batch.commit().addOnCompleteListener(commitTask -> {
                            if (commitTask.isSuccessful()) {
                                // Successfully reset voting points
                                System.out.println("Voting points have been reset.");
                            } else {
                                // Handle error
                                System.err.println("Error resetting voting points: " + commitTask.getException().getMessage());
                            }
                        });
                    } else {
                        // Handle error
                        System.err.println("Error getting documents: " + task.getException().getMessage());
                    }
                });
    }
}

