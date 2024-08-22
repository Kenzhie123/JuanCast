package com.JuanCast.myapplication;

public class UserModel {

    private String email, fullName, username;
    public int votingPoints;

    public UserModel() {

    }

    public UserModel(String email, String fullName, String username) {
        this.email = email;
        this.fullName = fullName;
        this.username = username;

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
