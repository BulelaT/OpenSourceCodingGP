package com.example.opscgroupprojectapp.Models;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.maps.model.Unit;
import com.google.maps.model.PlaceType;

import java.util.Base64;

// This is the user model class ()
public class User_Model {

    //Declaration of variables ()
    //(Iannone, Pecorelli, Nucci, Palomba and De Lucia, 2020)
    private String Username;
    private String Password;
    private String Email;
    private Unit measurementUnit;
    private PlaceType preferedLandmark;
    private String ePassword;



    // The code below is a constructor that will take two arguments ()
    @RequiresApi(api = Build.VERSION_CODES.O)
    public User_Model(String email, String password) {
        Email = email;
        Password = password;
        Password = User_Model.encryptPassword(Password);
        measurementUnit = Unit.METRIC;
        preferedLandmark = PlaceType.GROCERY_OR_SUPERMARKET;
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

    public Unit getMeasurementUnit() {
        return measurementUnit;
    }

    public void setMeasurementUnit(Unit measurementUnit) {
        this.measurementUnit = measurementUnit;
    }

    public PlaceType getPreferedLandmark() {
        return preferedLandmark;
    }

    public void setPreferedLandmark(PlaceType preferedLandmark) {
        this.preferedLandmark = preferedLandmark;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String encryptPassword(String pass)
    {
        String inputString = pass;
        //below is the way to get the encoding string by using Base64
        String encodedString = Base64.getEncoder().encodeToString(inputString.getBytes());

        return encodedString;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String decryptPassword(String pass)
    {
        byte[] decodedBytes = Base64.getDecoder().decode(pass);
        String decodedString = new String(decodedBytes);

        return decodedString;
    }

    public static String hidePasswordWithAsterix(String password){
        String asterix = "";
        for (int i = 0; i < password.length(); i++){
            asterix += "*";
        }
        password = asterix;
        return password;
    }
}
