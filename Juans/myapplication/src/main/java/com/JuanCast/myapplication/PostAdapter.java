package com.JuanCast.myapplication;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.BounceInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private List<Post> postList;
    private Context context;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    public PostAdapter(List<Post> postList) {
        this.postList = postList;
        this.firebaseFirestore = FirebaseFirestore.getInstance();
        this.firebaseAuth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = postList.get(position);

        // Set post content
        String content = post.getContent();
        holder.postContentTextView.setText(content);

        // Check if content exceeds 3 lines to show "See more" text
        holder.postContentTextView.post(() -> {
            Layout layout = holder.postContentTextView.getLayout();
            if (layout != null) {
                int lines = layout.getLineCount();
                if (lines > 3 || (lines == 3 && layout.getEllipsisCount(lines - 1) > 0)) {
                    holder.seeMoreTextView.setVisibility(View.VISIBLE);  // Show "See more" text
                } else {
                    holder.seeMoreTextView.setVisibility(View.GONE);  // Hide "See more" text if content is <= 3 lines
                }
            }
        });

        holder.postLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to navigate to DetailActivity
                Intent intent = new Intent(context, PostActivity.class);

                // Optional: Pass data to the next activity
                intent.putExtra("POST_ID", post.getPostId()); // Assuming post has a method getPostId()

                // Start the activity
                context.startActivity(intent);
            }
        });




        // Handle expanding and collapsing
        if (post.isContentExpanded()) {
            holder.postContentTextView.setMaxLines(Integer.MAX_VALUE);  // Expand text view
            holder.postContentTextView.setEllipsize(null);  // Remove ellipsis
            holder.seeMoreTextView.setText("Hide");  // Change text to "Hide" when content is expanded
            holder.seeMoreTextView.setVisibility(View.VISIBLE);  // Show "Hide" text
        } else {
            holder.postContentTextView.setMaxLines(3);  // Collapse to 3 lines
            holder.postContentTextView.setEllipsize(TextUtils.TruncateAt.END);  // Add ellipsis
            holder.seeMoreTextView.setText("See more");  // Change text to "See more" when content is collapsed
            holder.seeMoreTextView.setVisibility(View.VISIBLE);  // Show "See more" text
        }

        holder.seeMoreTextView.setOnClickListener(v -> togglePostContent(holder, post));

        // Other bindings
        holder.postTimestampTextView.setText(Utils.getTimeAgo(post.getTimestamp()));
        loadProfileImage(post.getUserId(), holder.profileImageView, holder.progressBar);
        loadUserName(post.getUserId(), holder.welcomeTextView);

        // Real-time listener for likes
        setupLikeListener(post.getPostId(), holder);

        holder.likeImageView.setOnClickListener(v -> {
            // Instantly change the image resource based on the current like state
            if (post.isLikedByCurrentUser()) {
                holder.likeImageView.setImageResource(R.drawable.baseline_favorite_border_24);
                unlikePost(post.getPostId());
            } else {
                holder.likeImageView.setImageResource(R.drawable.baseline_favorite_24);
                likePost(post.getPostId());
            }
        });




        // Set click listener for content text view and "See more" text view
        holder.postContentTextView.setOnClickListener(v -> togglePostContent(holder, post));
        holder.seeMoreTextView.setOnClickListener(v -> togglePostContent(holder, post));


    }



    @Override
    public int getItemCount() {
        return postList.size();
    }

    private void togglePostContent(PostViewHolder holder, Post post) {
        if (post.isContentExpanded()) {
            // Collapse the content
            post.setContentExpanded(false);
            holder.postContentTextView.setMaxLines(3);
            holder.postContentTextView.setEllipsize(TextUtils.TruncateAt.END);
            holder.seeMoreTextView.setVisibility(View.VISIBLE);
        } else {
            // Expand the content
            post.setContentExpanded(true);
            holder.postContentTextView.setMaxLines(Integer.MAX_VALUE);
            holder.postContentTextView.setEllipsize(null);
            holder.seeMoreTextView.setVisibility(View.GONE);
        }
        notifyItemChanged(holder.getAdapterPosition());
    }

    private void setupLikeListener(String postId, PostViewHolder holder) {
        String userId = firebaseAuth.getCurrentUser().getUid();

        // Set up Firestore listener
        firebaseFirestore.collection("posts").document(postId)
                .addSnapshotListener((snapshot, e) -> {
                    if (e != null) {
                        Toast.makeText(context, "Failed to load post: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (snapshot != null && snapshot.exists()) {
                        Long likesCount = snapshot.getLong("likesCount");
                        List<String> likes = (List<String>) snapshot.get("likes");
                        boolean isLiked = likes != null && likes.contains(userId);

                        // Immediate UI update
                        holder.likesCountTextView.post(() -> {
                            holder.likesCountTextView.setText(String.valueOf(likesCount != null ? likesCount : 0));
                            holder.likeImageView.setImageResource(isLiked ? R.drawable.baseline_favorite_24 : R.drawable.baseline_favorite_border_24);
                        });

                        // Update the Post object immediately
                        Post post = postList.get(holder.getAdapterPosition());
                        post.setLikedByCurrentUser(isLiked);
                        post.setLikeCount(likesCount != null ? likesCount : 0);
                    }
                });
    }

    //

    private void likePost(String postId) {
        String userId = firebaseAuth.getCurrentUser().getUid();
        DocumentReference postRef = firebaseFirestore.collection("posts").document(postId);

        firebaseFirestore.runTransaction(transaction -> {
            DocumentSnapshot snapshot = transaction.get(postRef);
            if (!snapshot.exists()) {
                throw new FirebaseFirestoreException("Post does not exist", FirebaseFirestoreException.Code.NOT_FOUND);
            }

            Long likesCount = snapshot.getLong("likesCount");
            if (likesCount == null) {
                likesCount = 0L;
            }

            // Check if the user has already liked the post
            List<String> likes = (List<String>) snapshot.get("likes");
            if (likes == null) {
                likes = new ArrayList<>();
            }

            if (likes.contains(userId)) {
                // User has already liked the post, do not increment the like count
                return null;
            } else {
                // User has not liked the post yet, increment the like count
                transaction.update(postRef, "likesCount", likesCount + 1);
                // Add the user to the list of likes
                likes.add(userId);
                transaction.update(postRef, "likes", likes);
                return null;
            }
        }).addOnSuccessListener(aVoid -> {
            // Success handling (optional, as updates are handled in the transaction)
        }).addOnFailureListener(e -> {
            Toast.makeText(context, "Failed to like post: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }


    private void unlikePost(String postId) {
        String userId = firebaseAuth.getCurrentUser().getUid();
        DocumentReference postRef = firebaseFirestore.collection("posts").document(postId);

        firebaseFirestore.runTransaction(transaction -> {
            DocumentSnapshot snapshot = transaction.get(postRef);
            if (!snapshot.exists()) {
                throw new FirebaseFirestoreException("Post does not exist", FirebaseFirestoreException.Code.NOT_FOUND);
            }

            Long likesCount = snapshot.getLong("likesCount");
            if (likesCount == null || likesCount <= 0) {
                // No likes to decrement
                return null;
            }

            // Get the list of likes
            List<String> likes = (List<String>) snapshot.get("likes");
            if (likes == null) {
                likes = new ArrayList<>();
            }

            if (likes.contains(userId)) {
                // User has liked the post; proceed with unlike operation
                transaction.update(postRef, "likesCount", likesCount - 1);
                likes.remove(userId); // Remove the user from the likes list
                transaction.update(postRef, "likes", likes);
            } else {
                // User has not liked the post; nothing to do
                return null;
            }

            return null;
        }).addOnSuccessListener(aVoid -> {
            // Success handling (optional, as updates are handled in the transaction)
        }).addOnFailureListener(e -> {
            Toast.makeText(context, "Failed to unlike post: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }


//




    private void loadUserName(String userId, TextView welcomeTextView) {
        firebaseFirestore.collection("User").document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String username = documentSnapshot.getString("username");
                        welcomeTextView.setText(username); // Set the retrieved username to the TextView
                    } else {
                        // Handle case where document does not exist
                        welcomeTextView.setText("User not found");
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle any errors
                    welcomeTextView.setText("Failed to load username");
                    Log.e(TAG, "Error fetching username: " + e.getMessage());
                });
    }

    private void loadProfileImage(String userId, ImageView imageView, ProgressBar progressBar) {
        progressBar.setVisibility(View.VISIBLE); // Show the ProgressBar before loading the image

        firebaseFirestore.collection("users").document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String profileImageUrl = documentSnapshot.getString("profileImageUrl");
                        if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
                            RequestOptions requestOptions = new RequestOptions()
                                    .placeholder(R.drawable.user)
                                    .error(R.drawable.user);

                            Glide.with(context)
                                    .load(profileImageUrl)
                                    .apply(requestOptions)
                                    .circleCrop()
                                    .transition(DrawableTransitionOptions.withCrossFade())
                                    .into(new CustomTarget<Drawable>() {
                                        @Override
                                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                            imageView.setImageDrawable(resource);
                                            progressBar.setVisibility(View.GONE); // Hide the ProgressBar when image is loaded
                                        }

                                        @Override
                                        public void onLoadCleared(@Nullable Drawable placeholder) {
                                            // Handle if needed
                                        }
                                    });
                        } else {
                            // Handle case when profileImageUrl is null or empty
                            loadDefaultImageBasedOnGender(userId, imageView, progressBar);
                        }
                    } else {
                        // Handle case when document for userId does not exist
                        loadDefaultImageBasedOnGender(userId, imageView, progressBar);
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Error loading profile image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    imageView.setImageResource(R.drawable.user); // Set default image in case of failure
                    progressBar.setVisibility(View.GONE); // Hide the ProgressBar in case of failure
                });
    }



    private void loadDefaultImageBasedOnGender(String userId, ImageView imageView, ProgressBar progressBar) {
        firebaseFirestore.collection("User").document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String gender = documentSnapshot.getString("gender");
                        if (gender != null) {
                            // Logging gender value for debugging
                            Log.d("GenderValue", "Gender: " + gender);
                            if (gender.equalsIgnoreCase("Male")) {
                                imageView.setImageResource(R.drawable.maledpc);
                            } else if (gender.equalsIgnoreCase("Female")) {
                                imageView.setImageResource(R.drawable.femaledp);
                            } else {
                                // Gender not specified or unknown, show default image
                                imageView.setImageResource(R.drawable.user);
                            }
                        } else {
                            // Gender field is null, show default image
                            Log.d("GenderValue", "Gender field is null");
                            imageView.setImageResource(R.drawable.user);
                        }
                    } else {
                        // Document for the user not found, show default image
                        Log.d("GenderValue", "User document not found");
                        imageView.setImageResource(R.drawable.user);
                    }
                    progressBar.setVisibility(View.GONE); // Hide the ProgressBar when loading default image
                })
                .addOnFailureListener(exception -> {
                    // Handle failure to retrieve user's gender
                    Log.e("GenderValue", "Error retrieving user document: " + exception.getMessage());
                    imageView.setImageResource(R.drawable.user);
                    progressBar.setVisibility(View.GONE); // Hide the ProgressBar in case of failure
                });
    }





    public static class PostViewHolder extends RecyclerView.ViewHolder {

        private ImageView profileImageView;
        private ProgressBar progressBar;
        private TextView welcomeTextView;
        private TextView postContentTextView;
        private TextView postTimestampTextView;
        private TextView seeMoreTextView; // New TextView for "See more"
        private ImageView likeImageView;
        private TextView likesCountTextView;
        LinearLayout postLayout; // Declare the LinearLayout

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);

            progressBar = itemView.findViewById(R.id.profileImageProgressBar);
            profileImageView = itemView.findViewById(R.id.profileImageView);
            welcomeTextView = itemView.findViewById(R.id.welcomeTextView);
            postContentTextView = itemView.findViewById(R.id.postContentTextView);
            postTimestampTextView = itemView.findViewById(R.id.postTimestampTextView);
            seeMoreTextView = itemView.findViewById(R.id.seeMoreTextView); // Initialize seeMoreTextView
            likeImageView = itemView.findViewById(R.id.likeImageView);
            likesCountTextView = itemView.findViewById(R.id.likesCountTextView);
            postLayout = itemView.findViewById(R.id.post);
        }
    }
}
