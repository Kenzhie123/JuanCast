package com.JuanCast.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.List;

public class PromoCode extends AppCompatActivity {

    private EditText editTextPromoCode;
    private Button buttonApplyPromoCode;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promo_code);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        editTextPromoCode = findViewById(R.id.editTextPromoCode);
        buttonApplyPromoCode = findViewById(R.id.buttonApplyPromoCode);

        back = findViewById(R.id.back);
        back.setOnClickListener(v -> {
            Intent intent = new Intent(PromoCode.this, Profile.class);
            startActivity(intent);
            overridePendingTransition(0, 0); // No animation
        });


        buttonApplyPromoCode.setOnClickListener(v -> {
            // Tawagin ang method para mag-apply ng promo code
            applyPromoCode();

            // I-clear ang input field pagkatapos i-apply ang promo code
            editTextPromoCode.setText("");
        });

    }

    @Override
    public void onBackPressed() {
        // Perform custom action before calling the default behavior
        Intent intent = new Intent(this, Profile.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);

        // Use overridePendingTransition after startActivity
        overridePendingTransition(0, 0);

        // Call the default back behavior
        super.onBackPressed();
    }



    //*******************************************Promo Code************************************************************
    private void applyPromoCode() {
        String promoCode = editTextPromoCode.getText().toString().trim();
        if (!promoCode.isEmpty()) {
            checkPromoCode(promoCode);
        } else {
            Toast.makeText(this, "Please enter a promo code", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkPromoCode(String promoCode) {
        DocumentReference promoCodeRef = firebaseFirestore.collection("promoCodes").document(promoCode);

        promoCodeRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document != null && document.exists() && document.getBoolean("isActive")) {
                    int points = document.getLong("points").intValue();
                    FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                    if (currentUser != null) {
                        String userId = currentUser.getUid();
                        checkIfCodeUsed(userId, promoCode, points);
                    } else {
                        Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Invalid or inactive promo code", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Error checking promo code", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkIfCodeUsed(String userId, String promoCode, int points) {
        DocumentReference userRef = firebaseFirestore.collection("User").document(userId);

        userRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot snapshot = task.getResult();
                if (snapshot != null && snapshot.exists()) {
                    List<String> usedCodes = (List<String>) snapshot.get("usedPromoCodes");
                    if (usedCodes != null && usedCodes.contains(promoCode)) {
                        Toast.makeText(this, "Promo code already used", Toast.LENGTH_SHORT).show();
                    } else {
                        awardPointsToUser(userId, points, promoCode);
                    }
                } else {
                    Toast.makeText(this, "User document does not exist", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Error checking user data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void awardPointsToUser(String userId, int points, String promoCode) {
        DocumentReference userRef = firebaseFirestore.collection("User").document(userId);

        firebaseFirestore.runTransaction(transaction -> {
            DocumentSnapshot snapshot = transaction.get(userRef);
            if (snapshot.exists()) {
                long currentPoints = snapshot.contains("votingPoints") ? snapshot.getLong("votingPoints") : 0;
                transaction.update(userRef, "votingPoints", currentPoints + points);

                List<String> usedCodes = (List<String>) snapshot.get("usedPromoCodes");
                if (usedCodes == null) {
                    usedCodes = new ArrayList<>();
                }
                usedCodes.add(promoCode);
                transaction.update(userRef, "usedPromoCodes", usedCodes);
            } else {
                throw new FirebaseFirestoreException("User document does not exist", FirebaseFirestoreException.Code.NOT_FOUND);
            }
            return points; // Return the number of points awarded
        }).addOnSuccessListener(awardedPoints -> {
            // Show dialog box upon successful points awarding with the points received
            showSuccessDialog(awardedPoints);
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Error awarding points: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void showSuccessDialog(int points) {
        // Inflate the custom layout
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_success, null);

        // Find views in the custom layout and set data
        ImageView successIcon = dialogView.findViewById(R.id.success_icon);
        TextView successMessage = dialogView.findViewById(R.id.success_message);
        TextView pointsAwarded = dialogView.findViewById(R.id.points_awarded);

        // Update the message and points awarded
        successMessage.setText("You have been awarded points!");
        pointsAwarded.setText(points + " Points");

        // Build and show the dialog with the custom view
        new AlertDialog.Builder(this)
                .setView(dialogView)
                .setPositiveButton(android.R.string.ok, null)
                .show();
    }



    //*******************************************************************************************************
}