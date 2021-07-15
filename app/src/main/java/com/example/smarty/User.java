package com.example.smarty;

public class User {
    public String FirstName,LastName,Email,Password;
    public User(){

    }
    public User(String Email,String Password,String FirstName,String LastName){
        this.Email=Email;
        this.Password=Password;
        this.FirstName=FirstName;
        this.LastName=LastName;
    }



}
