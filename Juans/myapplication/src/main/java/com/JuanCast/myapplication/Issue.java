package com.JuanCast.myapplication;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Issue extends AppCompatActivity {

    private static final int REQUEST_CODE_PICK_FILE = 1;

    private EditText subjectEditText;
    private EditText descriptionEditText;
    private Button uploadButton;
    private Button sendButton;
    private TextView fileNameTextView;
    private Uri fileUri;
    private ImageView back;


    //navvar
    private ImageView Community;
    private ImageView Store;
    private ImageView Cast;
    private ImageView home;
    private ImageView profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue);

        subjectEditText = findViewById(R.id.subjectEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        uploadButton = findViewById(R.id.uploadButton);
        sendButton = findViewById(R.id.sendButton);
        fileNameTextView = findViewById(R.id.fileNameTextView);

        home = findViewById(R.id.home);
        profile = findViewById(R.id.profile);
        Community = findViewById(R.id.Community);
        Store = findViewById(R.id.Store);
        Cast = findViewById(R.id.Cast);
        back = findViewById(R.id.back);

        uploadButton.setOnClickListener(v -> openFilePicker());

        sendButton.setOnClickListener(v -> sendIssueReport());


        //navvar

        back.setOnClickListener(v -> {
            Intent intent = new Intent(Issue.this, Profile.class);
            startActivity(intent);
            overridePendingTransition(0, 0); // No animation
        });

        Community.setOnClickListener(v -> {
            Intent intent = new Intent(Issue.this, PostActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0); // No animation
        });

        Store.setOnClickListener(v -> {
            Intent intent = new Intent(Issue.this, StarStore.class);
            startActivity(intent);
            overridePendingTransition(0, 0); // No animation
        });

        Cast.setOnClickListener(v -> {
            Intent intent = new Intent(Issue.this, Voting.class);
            startActivity(intent);
            overridePendingTransition(0, 0); // No animation
        });

        home.setOnClickListener(v -> {
            Intent intent = new Intent(Issue.this, Homepage.class);
            startActivity(intent);
            overridePendingTransition(0, 0); // No animation
        });

        profile.setOnClickListener(v -> {
            Intent intent = new Intent(Issue.this, Profile.class);
            startActivity(intent);
            overridePendingTransition(0, 0); // No animation
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

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(Intent.createChooser(intent, "Select a file"), REQUEST_CODE_PICK_FILE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_FILE && resultCode == RESULT_OK && data != null) {
            fileUri = data.getData();
            String fileName = getFileName(fileUri);
            fileNameTextView.setText(fileName != null ? "File selected: " + fileName : "No file selected");
        }
    }

    private String getFileName(Uri uri) {
        String[] projection = {MediaStore.Files.FileColumns.DISPLAY_NAME};
        try (Cursor cursor = getContentResolver().query(uri, projection, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                return cursor.getString(0);
            }
        }
        return null;
    }

    private void sendIssueReport() {
        String subject = subjectEditText.getText().toString();
        String description = descriptionEditText.getText().toString();

        if (subject.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Pakilagay ang lahat ng mga field", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"juancast.info@gmail.com"}); // Palitan ng iyong email address
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, description);

        if (fileUri != null) {
            emailIntent.putExtra(Intent.EXTRA_STREAM, fileUri);
            emailIntent.setType("application/pdf"); // Ayusin ayon sa uri ng file
        }

        emailIntent.setPackage("com.google.android.gm"); // Target ang Gmail

        try {
            startActivity(emailIntent);

            // Reset the EditText fields and TextView after successful email intent
            subjectEditText.setText("");
            descriptionEditText.setText("");
            fileNameTextView.setText("No file selected");
            fileUri = null; // Clear the fileUri if you want to reset it as well

        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "Walang Gmail na naka-install o hindi maaring hawakan ang request.", Toast.LENGTH_LONG).show();
        }
    }
}
