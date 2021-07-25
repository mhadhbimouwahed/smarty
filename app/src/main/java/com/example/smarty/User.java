package com.example.smarty;

import com.google.firebase.auth.FirebaseUser;

public class User {
    public String FirstName,LastName,Email,Password;

    public User(FirebaseUser user){

    }
    public User(String Email,String Password,String FirstName,String LastName){
        this.Email=Email;
        this.Password=Password;
        this.FirstName=FirstName;
        this.LastName=LastName;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
