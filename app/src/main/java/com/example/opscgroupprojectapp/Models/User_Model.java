package com.example.opscgroupprojectapp.Models;

// This is the user model class ()
public class User_Model {

    //Declaration of variables ()
    private String Username;
    private String Password;
    private String Email;

    // The code below is a constructor that will take two arguments ()
    public User_Model(String email, String password) {
        Email = email;
        Password = password;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    // The following are setters and getters for the username and password variables  ()
    // Get username method ()
    public String getUsername() {
        return Username;
    }

    // set username method ()
    public void setUsername(String username) {
        Username = username;
    }

    // get password method ()
    public String getPassword() {
        return Password;
    }

    // set password method ()
    public void setPassword(String password) {
        Password = password;
    }

}
