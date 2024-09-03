package com.JuanCast.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.JuanCast.myapplication.Receiver.PostChangeReceiver;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PostActivity extends AppCompatActivity {

    private ImageView profileImageView;
    private EditText postContentEditText;
    private Button addPostImageView;
    private ProgressBar progressBar;
    private boolean isPosting = false;

    private ImageView logo;
    private SwipeRefreshLayout swipeRefreshLayout; // Declare SwipeRefreshLayout

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private String currentUserId;

    private RecyclerView postsRecyclerView;
    private List<Post> postList;
    private PostAdapter postAdapter;



    private ImageView profile;
    private ImageView Store;
    private ImageView Storetop;
    private ImageView Cast;
    private ImageView home;
    private ImageView Community;


    private RelativeLayout noInternetLayout;
    private PostChangeReceiver networkChangeReceiver;


    // Getter para sa noInternetLayout
    public RelativeLayout getNoInternetLayout() {
        return noInternetLayout;
    }

    private String userProfileImageUrl; // Your profile image URL or resource ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        logo = findViewById(R.id.logo);
        postContentEditText = findViewById(R.id.postContentEditText);
        addPostImageView = findViewById(R.id.addPostImageView);
        postsRecyclerView = findViewById(R.id.postsRecyclerView);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout); // Initialize SwipeRefreshLayout

        postsRecyclerView.setVerticalScrollBarEnabled(false);
        postsRecyclerView.setHorizontalScrollBarEnabled(false);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        currentUserId = firebaseAuth.getCurrentUser().getUid();

        // Initialize RecyclerView
        postsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        postList = new ArrayList<>();
        postAdapter = new PostAdapter(postList); // Initialize PostAdapter without userProfileImageUrl
        postsRecyclerView.setAdapter(postAdapter);

        progressBar = findViewById(R.id.progressBar);

        //navvar
        profile = findViewById(R.id.profile);
        Store = findViewById(R.id.Store);
        Storetop = findViewById(R.id.notification_icon);
        Cast = findViewById(R.id.Cast);
        home = findViewById(R.id.home);
        Community = findViewById(R.id.Community);

        // Load profile image and username
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PostActivity.this, Homepage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                overridePendingTransition(0, 0); // No animation
                finish();
            }
        });

        // Handle adding a new post
        addPostImageView.setOnClickListener(v -> {
            String content = postContentEditText.getText().toString().trim();
            if (!content.isEmpty()) {
                addPost(content);
            } else {
                showCustomToast("Please enter some content", R.drawable.juanscast);
            }
        });

        // Set up SwipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener(this::refreshPosts);

        // Automatically trigger refresh
        swipeRefreshLayout.post(() -> {
            swipeRefreshLayout.setRefreshing(true); // Show refresh indicator
            refreshPosts(); // Reload posts
        });

        // Navigation variables
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PostActivity.this, Profile.class);
                startActivity(intent);
                overridePendingTransition(0, 0); // No animation
            }
        });

        // Navigation variables
        Community.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PostActivity.this, PostActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0); // No animation
            }
        });


        Store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PostActivity.this, StarStore.class);
                startActivity(intent);
                overridePendingTransition(0, 0); // No animation
            }
        });

        Storetop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PostActivity.this, StarStore.class);
                startActivity(intent);
                overridePendingTransition(0, 0); // No animation
            }
        });

        Cast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PostActivity.this, Voting.class);
                startActivity(intent);
                overridePendingTransition(0, 0); // No animation
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PostActivity.this, Homepage.class);
                startActivity(intent);
                overridePendingTransition(0, 0); // No animation
            }
        });

        noInternetLayout = findViewById(R.id.noInternetLayout);
        networkChangeReceiver = new PostChangeReceiver();

        // Initial check for internet connectivity
        if (!isConnected()) {
            noInternetLayout.setVisibility(View.VISIBLE);
        } else {
            noInternetLayout.setVisibility(View.GONE);
        }


    }


    @Override
    protected void onResume() {
        super.onResume();

        // Register receiver to listen for network changes
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Unregister receiver to avoid memory leaks
        unregisterReceiver(networkChangeReceiver);
    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }


    private void refreshPosts() {
        loadPosts();
    }

    private void loadPosts() {
        firebaseFirestore.collection("posts")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        postList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Post post = document.toObject(Post.class);
                            postList.add(post);
                        }
                        postAdapter.notifyDataSetChanged();
                    } else {
                        showCustomToast("Error getting posts: " + task.getException().getMessage(), R.drawable.juanscast);
                    }
                    // Hide the refreshing indicator when done
                    swipeRefreshLayout.setRefreshing(false);
                });
    }

    private void addPost(String content) {
        if (isPosting) {
            // Prevent adding another post if one is already in progress
            return;
        }

        isPosting = true; // Set to true when posting starts

        String postId = firebaseFirestore.collection("posts").document().getId();
        String user = firebaseFirestore.collection("User").document().getId();

        // Show ProgressBar
        progressBar.setVisibility(View.VISIBLE);

        firebaseFirestore.collection("User").document(currentUserId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String profileImageUrl = documentSnapshot.getString("profileImageUrl");

                        Post post = new Post(currentUserId, postId, content, new Date(), profileImageUrl);

                        firebaseFirestore.collection("posts").document(postId)
                                .set(post)
                                .addOnSuccessListener(aVoid -> {
                                    // Hide ProgressBar
                                    progressBar.setVisibility(View.GONE);

                                    isPosting = false; // Reset posting flag

                                    showCustomToast("Post added successfully", R.drawable.juanscast);
                                    postList.add(0, post); // Add post to the beginning of the list
                                    postAdapter.notifyDataSetChanged();
                                    postContentEditText.setText(""); // Clear input field after posting
                                })
                                .addOnFailureListener(e -> {
                                    // Hide ProgressBar
                                    progressBar.setVisibility(View.GONE);

                                    isPosting = false; // Reset posting flag

                                    Toast.makeText(PostActivity.this, "Error adding post: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                    }
                })
                .addOnFailureListener(e -> {
                    // Hide ProgressBar
                    progressBar.setVisibility(View.GONE);

                    isPosting = false; // Reset posting flag

                    Toast.makeText(PostActivity.this, "Error retrieving profile image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }



    private void showCustomToast(String message, int imageResId) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.imagetoast,
                (ViewGroup) findViewById(R.id.custom_toast_container));

        ImageView imageView = layout.findViewById(R.id.imageView);
        imageView.setImageResource(imageResId);

        TextView textView = layout.findViewById(R.id.textView);
        textView.setText(message);

        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }
}
