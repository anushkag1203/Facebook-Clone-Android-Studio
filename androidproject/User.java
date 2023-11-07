package com.example.androidproject;


public class User {
    private String firstName;
    private String phoneNumber;

    private String email;
    private String password;
    private String birthDate;
    private String profile_image;

    private  String cover_image;

    private String userID;
    private  int followerCount;
    public User() {
        // Default constructor required for Firebase Realtime Database
    }

    public User(String firstName, String birthDate,String phoneNumber,String email,String password) {
        this.firstName = firstName;
        this.birthDate=birthDate;
        this.phoneNumber = phoneNumber;
        this.email=email;
        this.password=password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String password) {
        this.birthDate = birthDate;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public String getCover_image() {
        return cover_image;
    }

    public void setCover_image(String cover_image) {
        this.cover_image = cover_image;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public int getFollowerCount() {
        return followerCount;
    }

    public void setFollowerCount(int followerCount) {
        this.followerCount = followerCount;
    }
}
