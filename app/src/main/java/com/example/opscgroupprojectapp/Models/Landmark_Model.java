package com.example.opscgroupprojectapp.Models;

import android.graphics.Bitmap;

// This class is the landmark  model()
public class Landmark_Model {

    // Declaration of variables ()
    private String LandmarkPicture;
    private String LandmarkName;
    private String LandmarkType;
    private String LandmarkLocation;

    public Landmark_Model()
    {

    }
    // The code below is a constructor that will take four arguments ()
    public Landmark_Model(String landmarkPicture, String landmarkName, String landmarkType, String landmarkLocation) {
        LandmarkPicture = landmarkPicture;
        LandmarkName = landmarkName;
        LandmarkType = landmarkType;
        LandmarkLocation = landmarkLocation;
    }

    public Landmark_Model(String landmarkPicture, String landmarkName, String landmarkLocation) {
        LandmarkPicture = landmarkPicture;
        LandmarkName = landmarkName;
        LandmarkLocation = landmarkLocation;
    }

    // The following are setters and getters ()
    // get landmark picture method ()
    public String getLandmarkPicture() {
        return LandmarkPicture;
    }

    // set landmark picture method ()
    public void setLandmarkPicture(String landmarkPicture) {
        LandmarkPicture = landmarkPicture;
    }

    // get landmark name method ()
    public String getLandmarkName() {
        return LandmarkName;
    }

    // set landmark name method ()
    public void setLandmarkName(String landmarkName) {
        LandmarkName = landmarkName;
    }

    // get landmark type method ()
    public String getLandmarkType() {
        return LandmarkType;
    }

    // set landmark type method ()
    public void setLandmarkType(String landmarkType) {
        LandmarkType = landmarkType;
    }

    // get landmark location method ()
    public String getLandmarkLocation() {
        return LandmarkLocation;
    }

    // set landmark location method ()
    public void setLandmarkLocation(String landmarkLocation) {
        LandmarkLocation = landmarkLocation;
    }
}
