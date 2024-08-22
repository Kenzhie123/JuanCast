package com.JuanCast.myapplication;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.JuanCast.myapplication.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class SignUp extends AppCompatActivity {

    ActivitySignUpBinding binding;
    ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    EditText birthdateEditText;
    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());

    EditText passwordEditText, passwordConfirmEditText;
    TextView passwordErrorTextView;

    private TextInputLayout passwordInputLayout;
    private TextInputEditText PasswordEditText;

    private CheckBox rbMale;
    private CheckBox rbFemale;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(this);

        passwordEditText = findViewById(R.id.password);
        passwordConfirmEditText = findViewById(R.id.password_confirm);
        passwordErrorTextView = findViewById(R.id.password_error);

        rbMale = findViewById(R.id.rbMale);
        rbFemale = findViewById(R.id.rbFemale);


        TextInputEditText passwordEditText = findViewById(R.id.password);
        final TextInputLayout passwordInputLayout = findViewById(R.id.password_input_layout);

        TextInputEditText confirmEditText = findViewById(R.id.password_confirm);
        final TextInputLayout confirmpasswordInputLayout = findViewById(R.id.confirm_input_layout);

        passwordEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // Kapag nawala ang focus, i-check kung may text na
                    if (passwordEditText.getText().length() > 0) {
                        passwordInputLayout.setHint(null); // I-set ang hint sa null para mawala
                    } else {
                        passwordInputLayout.setHint("Enter your password..."); // Kung walang text, i-restore ang original hint
                    }
                } else {
                    // Kapag nagkaroon ng focus, siguraduhing walang hint
                    passwordInputLayout.setHint(null);
                }
            }
        });
        confirmEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // Kapag nawala ang focus, i-check kung may text na
                    if (confirmEditText.getText().length() > 0) {
                        confirmpasswordInputLayout.setHint(null); // I-set ang hint sa null para mawala
                    } else {
                        confirmpasswordInputLayout.setHint("Enter your password..."); // Kung walang text, i-restore ang original hint
                    }
                } else {
                    // Kapag nagkaroon ng focus, siguraduhing walang hint
                    confirmpasswordInputLayout.setHint(null);
                }
            }
        });


        CompoundButton.OnCheckedChangeListener listener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (buttonView == rbMale) {
                        rbFemale.setChecked(false);
                    } else if (buttonView == rbFemale) {
                        rbMale.setChecked(false);
                    }
                }
            }
        };

        rbMale.setOnCheckedChangeListener(listener);
        rbFemale.setOnCheckedChangeListener(listener);


        binding.SignupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.email.getText().toString();
                String number = binding.number.getText().toString();
                String fullName = binding.fullname.getText().toString();
                String username = binding.username.getText().toString().trim();
                String password = binding.password.getText().toString();
                String confirmPassword = passwordConfirmEditText.getText().toString();
                String birthdate = binding.birthdate.getText().toString().trim();

                boolean valid = true;
                Drawable errorIcon = getResources().getDrawable(R.drawable.ic_error_red);
                errorIcon.setBounds(0, 0, errorIcon.getIntrinsicWidth(), errorIcon.getIntrinsicHeight());
                Drawable errorIconPadded = getResources().getDrawable(R.drawable.ic_error_red_padded);
                errorIconPadded.setBounds(0, 0, errorIconPadded.getIntrinsicWidth(), errorIconPadded.getIntrinsicHeight());

                // Get references to CheckBox and TextView
                CheckBox termsCheckBox = findViewById(R.id.terms);
                TextView termsTextView = findViewById(R.id.termsTextView);

                // Check if gender is selected
                if (!binding.rbMale.isChecked() && !binding.rbFemale.isChecked()) {
                    showCustomToast("Please Input all fields", R.drawable.juanscast);
                    valid = false;
                }

                if (email.isEmpty()) {
                    binding.email.setError("Required", errorIcon);
                    valid = false;
                } else {
                    binding.email.setError(null);  // Clear error if not empty
                }

                if (number.isEmpty()) {
                    binding.number.setError("Required", errorIcon);
                    valid = false;
                } else {
                    binding.number.setError(null);  // Clear error if not empty
                }

                if (fullName.isEmpty()) {
                    binding.fullname.setError("Required", errorIcon);
                    valid = false;
                } else {
                    binding.fullname.setError(null);  // Clear error if not empty
                }

                if (username.isEmpty()) {
                    binding.username.setError("Required", errorIcon);
                    valid = false;
                } else {
                    binding.username.setError(null);  // Clear error if not empty
                }

                if (password.isEmpty()) {
                    binding.password.setError("Required", errorIconPadded);
                    valid = false;
                } else {
                    binding.password.setError(null);  // Clear error if not empty
                }

                if (birthdate.isEmpty()) {
                    binding.birthdate.setError("Required", errorIcon);
                    valid = false;
                } else {
                    binding.birthdate.setError(null);  // Clear error if not empty
                }

                // Check if terms and conditions checkbox is checked
                if (!termsCheckBox.isChecked()) {
                    termsTextView.setTextColor(Color.RED);  // Change text color to red
                    valid = false;
                } else {
                    termsTextView.setTextColor(getResources().getColor(R.color.white));  // Change text color back to white
                }

                if (!valid) {
                    return;
                }

                // Check if passwords match
                if (!password.equals(confirmPassword)) {
                    passwordErrorTextView.setVisibility(View.VISIBLE);
                    return;
                } else {
                    passwordErrorTextView.setVisibility(View.GONE);
                }

                progressDialog.setMessage("Signing Up...");
                progressDialog.show();

                // Get selected gender
                String gender = binding.rbMale.isChecked() ? "Male" : "Female";

                // Register user with Firebase Authentication
                registerUser(email, password, fullName, username, gender, birthdate);
            }
        });



        binding.LogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Proceed to login activity
                Intent intent = new Intent(SignUp.this, Juans.class);
                startActivity(intent);
                overridePendingTransition(0, 0); // Walang animation
            }
        });

        binding.learnMORE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUp.this, LearnMore.class);
                startActivity(intent);
                overridePendingTransition(0, 0); // Walang animation
            }
        });

        binding.Datapolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUp.this, LearnMore.class);
                startActivity(intent);
                overridePendingTransition(0, 0); // Walang animation
            }
        });

        birthdateEditText = findViewById(R.id.birthdate);
        birthdateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
    }




    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR) - 18;
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar selectedDate = Calendar.getInstance();
                        selectedDate.set(year, monthOfYear, dayOfMonth);
                        birthdateEditText.setText(dateFormat.format(selectedDate.getTime()));
                    }
                },
                year, month, dayOfMonth);
        datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis()); // Restrict future dates
        datePickerDialog.show();
    }

    private void registerUser(String email, String password, String fullName, String username, String gender, String birthdate) {
        // Parse birthdate to calculate age
        Calendar dob = Calendar.getInstance();
        try {
            dob.setTime(dateFormat.parse(birthdate));
            if (!isAgeAbove18(dob)) {
                showCustomToast("You must be 18 years or older to sign up.", R.drawable.juanscast);
                progressDialog.dismiss();
                return;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            showCustomToast("Invalid birthdate format.", R.drawable.juanscast);
            progressDialog.dismiss();
            return;
        }

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // User signed up successfully
                            FirebaseUser user = firebaseAuth.getCurrentUser();

                            // Send verification email
                            if (user != null) {
                                user.sendEmailVerification()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> emailTask) {
                                                if (emailTask.isSuccessful()) {
                                                    // Email sent successfully
                                                    showCustomToast("Verification email sent to " + email, R.drawable.juanscast);

                                                    // Proceed with adding user details to Firestore
                                                    String userId = user.getUid();
                                                    addUserDetailsToFirestore(userId, email, fullName, username, gender, birthdate, user.getPhoneNumber());

                                                    progressDialog.dismiss();

                                                    // Go to Login activity
                                                    startActivity(new Intent(SignUp.this, Juans.class));
                                                    finish();
                                                } else {
                                                    // Email not sent, show error
                                                    showCustomToast("Failed to send verification email: " + emailTask.getException().getMessage(), R.drawable.juanscast);
                                                    progressDialog.dismiss();
                                                }
                                            }
                                        });
                            }
                        } else {
                            // Signup failed
                            showCustomToast("Signup failed: " + task.getException().getMessage(), R.drawable.juanscast);
                            progressDialog.dismiss();
                        }
                    }
                });
    }

    private boolean isAgeAbove18(Calendar dob) {
        Calendar now = Calendar.getInstance();
        int age = now.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
        if (dob.get(Calendar.MONTH) > now.get(Calendar.MONTH) ||
                (dob.get(Calendar.MONTH) == now.get(Calendar.MONTH) && dob.get(Calendar.DAY_OF_MONTH) > now.get(Calendar.DAY_OF_MONTH))) {
            age--;
        }
        return age >= 18;
    }

    private void addUserDetailsToFirestore(String userId, String email, String fullName, String username, String gender, String birthdate, String number) {
        // Get current timestamp in milliseconds
        long timestamp = System.currentTimeMillis();
        // Convert timestamp to date string or any desired format
        String signUpDate = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault()).format(new Date(timestamp));

        // Create user map
        Map<String, Object> user = new HashMap<>();
        user.put("email", email);
        user.put("number", number);
        user.put("fullName", fullName);
        user.put("username", username);
        user.put("votingPoints", 10); // Initial voting points (assuming default value)
        user.put("sunvotingpoints", 0); // Initial sunvoting points (assuming default value)
        user.put("signUpDate", signUpDate); // Signup date
        user.put("gender", gender); // Store gender
        user.put("emailVerified", false); // Add email verification status
        user.put("birthdate", birthdate); // Add birthdate

        // Add user to Firestore
        firebaseFirestore.collection("User")
                .document(userId)
                .set(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // User data added to Firestore successfully
                            showCustomToast("User signed up successfully. Please verify your email before logging in.", R.drawable.juanscast);
                        } else {
                            // Error adding user to Firestore
                            showCustomToast("Error adding user to Firestore: " + task.getException().getMessage(), R.drawable.juanscast);
                        }
                        progressDialog.dismiss();
                    }
                });
    }

    private void showCustomToast(String message, int imageResId) {
        // Create LayoutInflater object
        LayoutInflater inflater = getLayoutInflater();

        // Inflate custom layout
        View layout = inflater.inflate(R.layout.imagetoast,
                (ViewGroup) findViewById(R.id.custom_toast_container));

        // Set image and text in custom layout
        ImageView imageView = layout.findViewById(R.id.imageView);
        imageView.setImageResource(imageResId); // Set image based on the given image resource ID

        TextView textView = layout.findViewById(R.id.textView);
        textView.setText(message);

        // Create Toast object
        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

}
