package com.JuanCast.myapplication;

import java.util.Date;

public class Post {

    private String userId;
    private String postId; // Unique ID for the post
    private String content;
    private Date timestamp;
    private int likesCount;
    private boolean likedByCurrentUser;
    private String profileImageUrl;
    private String username;
    private boolean contentExpanded;
    private Long likeCount;

    public Post() {
        // Required empty public constructor for Firestore to deserialize
    }

    public Post(String userId, String postId, String content, Date timestamp, String profileImageUrl) {
        this.userId = userId;
        this.postId = postId;
        this.content = content;
        this.timestamp = timestamp;
        this.likesCount = 0; // Initial likes count
        this.likedByCurrentUser = false; // Initially not liked by current user
        this.profileImageUrl = profileImageUrl; // Initialize profile image URL
        this.username = username;
   
    }

    // Getters and setters

    public Long getLikeCount() {
        return likeCount;
    }

    // Setter for likeCount
    public void setLikeCount(Long likeCount) {
        this.likeCount = likeCount;
    }

    public boolean isContentExpanded() {
        return contentExpanded;
    }

    public void setContentExpanded(boolean contentExpanded) {
        this.contentExpanded = contentExpanded;
    }

    public String getUsername(){
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    public boolean isLikedByCurrentUser() {
        return likedByCurrentUser;
    }

    public void setLikedByCurrentUser(boolean likedByCurrentUser) {
        this.likedByCurrentUser = likedByCurrentUser;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
}