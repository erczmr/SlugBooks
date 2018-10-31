package com.example.slugbooks.slugbooks;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DataModel {

    public String username;
    public String userID;
    public String email;
    public String firstName;
    public String lastName;
    public String imageUrl;

    public DataModel() {
        // Default constructor required for calls to DataSnapshot.getValue(DataModel.class)
    }


    public DataModel(String userID, String username, String email, String firstName, String lastName, String imageUrl) {
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userID = userID;
        this.imageUrl = imageUrl;
    }

//getters
    public String getUsername() {
        return username;
    }

    public String getUserID() {
        return userID;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

//setters
    public void setUsername(String username) {
        this.username = username;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void displayTest() {

        System.out.println(firstName + " " + lastName + " " + username + " " + userID + " " + email + " " + imageUrl);
    }
}
