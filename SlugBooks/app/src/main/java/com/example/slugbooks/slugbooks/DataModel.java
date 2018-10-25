package com.example.slugbooks.slugbooks;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DataModel {

    public String username;
    public String email;
    public String firstName;
    public String lastName;

    public DataModel() {
        // Default constructor required for calls to DataSnapshot.getValue(DataModel.class)
    }

    public DataModel(String username, String email, String firstName, String lastName) {
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }


}
