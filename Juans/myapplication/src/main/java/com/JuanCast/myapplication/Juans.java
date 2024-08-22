package com.JuanCast.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.JuanCast.myapplication.databinding.ActivityJuansBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Juans extends AppCompatActivity {

    ActivityJuansBinding binding;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityJuansBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(this);

        TextInputEditText confirmEditText = findViewById(R.id.password_input);
        final TextInputLayout confirmpasswordInputLayout = findViewById(R.id.confirm_input_layout);

        confirmEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // Kapag nawala ang focus, tingnan kung may laman
                if (!hasFocus) {
                    if (confirmEditText.getText().length() == 0) {
                        confirmpasswordInputLayout.setHint("Enter your password...");
                    }
                } else {
                    // Kapag may focus, siguraduhing walang hint
                    confirmpasswordInputLayout.setHint(null);
                }
            }
        });

// TextWatcher para mag-update ng hint habang binabago ang text
        confirmEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Alisin ang hint kung may laman ang text
                if (s.length() > 0) {
                    confirmpasswordInputLayout.setHint(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // I-restore ang hint kung walang laman ang text
                if (s.length() == 0) {

                }
            }
        });



        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.usernameInput.getText().toString().trim();
                String password = binding.passwordInput.getText().toString().trim();
                boolean rememberMe = binding.rememberMeCheckBox.isChecked(); // Check if "Remember Me" is checked

                boolean valid = true;
                Drawable errorIcon = getResources().getDrawable(R.drawable.ic_error_red);
                errorIcon.setBounds(0, 0, errorIcon.getIntrinsicWidth(), errorIcon.getIntrinsicHeight());

                Drawable errorIconPadded = getResources().getDrawable(R.drawable.ic_error_red_padded);
                errorIconPadded.setBounds(0, 0, errorIconPadded.getIntrinsicWidth(), errorIconPadded.getIntrinsicHeight());

                if (email.isEmpty()) {
                    binding.usernameInput.setError("Required", errorIcon);
                    valid = false;
                } else {
                    binding.usernameInput.setError(null);  // Clear error if not empty
                }

                if (password.isEmpty()) {
                    binding.passwordInput.setError("Required", errorIconPadded);
                    valid = false;
                } else {
                    binding.passwordInput.setError(null);  // Clear error if not empty
                }

                if (!valid) {
                    showCustomToast("Please fill out all fields", R.drawable.juanscast);
                    return;
                }

                progressDialog.show();

                firebaseAuth.signInWithEmailAndPassword(email, password)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                FirebaseUser user = firebaseAuth.getCurrentUser();
                                if (user != null && user.isEmailVerified()) {
                                    progressDialog.dismiss();
                                    showCustomToast("Login Successful", R.drawable.juanscast);

                                    // Save credentials if "Remember Me" is checked
                                    SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = prefs.edit();
                                    if (rememberMe) {
                                        editor.putString("email", email);
                                        editor.putString("password", password); // Consider security implications of storing passwords
                                    } else {
                                        editor.remove("email");
                                        editor.remove("password");
                                    }
                                    editor.apply();

                                    String userId = user.getUid();
                                    fetchAndStoreProfileImageUrl(userId);

                                    firebaseFirestore.collection("User")
                                            .document(userId)
                                            .get()
                                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                @Override
                                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                    if (documentSnapshot.exists()) {
                                                        String username = documentSnapshot.getString("username");
                                                        Number votingPoints = documentSnapshot.getDouble("votingPoints");

                                                        Intent intent = new Intent(Juans.this, Homepage.class);
                                                        intent.putExtra("USERNAME", username);
                                                        intent.putExtra("VotingPoints", votingPoints.doubleValue());
                                                        startActivity(intent);
                                                        finish();
                                                    } else {
                                                        Toast.makeText(Juans.this, "User document does not exist", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(Juans.this, "Failed to fetch user details: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                } else {
                                    progressDialog.dismiss();
                                    showCustomToast("Please verify your email first", R.drawable.juanscast);
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(Juans.this, "Login failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

// On activity creation or resume, check if there's a saved email and password
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String savedEmail = prefs.getString("email", "");
        String savedPassword = prefs.getString("password", "");
        if (!savedEmail.isEmpty()) {
            binding.usernameInput.setText(savedEmail);
            binding.passwordInput.setText(savedPassword);
            binding.rememberMeCheckBox.setChecked(true);
        }


        binding.goToForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.usernameInput.getText().toString().trim();

                if (email.isEmpty()) {
                    showCustomToast("Please enter your email first", R.drawable.juanscast);
                    return;
                }

                progressDialog.setTitle("Sending Mail");
                progressDialog.show();

                firebaseAuth.sendPasswordResetEmail(email)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                progressDialog.dismiss();
                                showCustomToast("Password reset email sent!", R.drawable.juanscast);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                showCustomToast("Failed to send password reset email: " + e.getMessage(), R.drawable.juanscast);
                            }
                        });
            }
        });

        binding.SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Juans.this, SignUp.class);
                startActivity(intent);
                overridePendingTransition(0, 0); // Walang animation
            }

        });
    }

    private void showCustomToast(String message, int imageResId) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.imagetoast, (ViewGroup) findViewById(R.id.custom_toast_container));

        ImageView imageView = layout.findViewById(R.id.imageView);
        imageView.setImageResource(imageResId);

        TextView textView = layout.findViewById(R.id.textView);
        textView.setText(message);

        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    private void fetchAndStoreProfileImageUrl(String userId) {
        firebaseFirestore.collection("users").document(userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        String profileImageUrl = task.getResult().getString("profileImageUrl");
                        if (profileImageUrl != null) {
                            SharedPreferences preferences = getSharedPreferences("profile_prefs", MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("profileImageUrl", profileImageUrl);
                            editor.apply();
                        }
                    }
                });
    }
}
