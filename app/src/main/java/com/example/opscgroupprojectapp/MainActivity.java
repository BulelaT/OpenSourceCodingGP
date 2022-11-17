package com.example.opscgroupprojectapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.opscgroupprojectapp.Adapters.InterfaceRV;
import com.google.android.gms.common.api.ApiException;
import com.google.maps.GeoApiContext;
import com.google.maps.PlacesApi;
import com.google.maps.model.LatLng;
import com.google.maps.model.PlaceType;
import com.google.maps.model.PlacesSearchResponse;
import com.google.maps.model.RankBy;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LocationListener {

    //(Gerber and Craig, 2015)
    LocationManager locationManager;

    public static String currCountryName;
    public static boolean isLocationOn;
    public static double lati;
    public static double longi;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                && (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                && (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED))
        {
            //This programming statement was adapted from Google Maps Platform:
            //Link: https://developers.google.com/maps/documentation/places/android-sdk/current-place-tutorial
            //Author: Google Developers
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA}, 101);
            //requestPermissions(new String[]{Manifest.permission.CAMERA}, 102);

            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().setReorderingAllowed(true).add(R.id.WelcomeFrag, WelcomeFragment.class,null).commitNow();

        }
    }

    @SuppressLint("MissingPermission")
    private void getLocation() {
            Log.d("Underpants", "permissions works");
            try{
                locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
                isLocationOn = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                if(!isLocationOn)
                {

                }
                else
                {

                    Log.d("Underpants", "else statement");
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, new LocationListener() {
                        @Override
                        public void onLocationChanged(@NonNull Location location) {

                            try{
                                Geocoder currentCountry = new Geocoder(MainActivity.this, Locale.getDefault());
                                List<Address> addresses = currentCountry.getFromLocation(location.getLatitude(), location.getLongitude(),1);
                                String address = addresses.get(0).getCountryName();
                                currCountryName = address;
                                MainActivity.lati = location.getLatitude();
                                MainActivity.longi = location.getLongitude();
                                Log.d("underpants", "onLocationChange: " + currCountryName);

                            }catch(Exception e)
                            {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onProviderEnabled(@NonNull String provider) {

                        }

                        @Override
                        public void onProviderDisabled(@NonNull String provider) {

                        }

                        @Override
                        public void onStatusChanged(String provider, int status, Bundle extras) {

                        }
                    });


                }

            }catch(Exception e)
            {
                e.printStackTrace();
            }


    }

    @Override
    public void onLocationChanged(@NonNull Location location) {


    }

    /*@Override
    public void onLocationChanged(@NonNull List<Location> locations) {
        LocationListener.super.onLocationChanged(locations);
    }*/

    @Override
    public void onFlushComplete(int requestCode) {
        LocationListener.super.onFlushComplete(requestCode);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        LocationListener.super.onStatusChanged(provider, status, extras);
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        LocationListener.super.onProviderEnabled(provider);
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        LocationListener.super.onProviderDisabled(provider);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 101 :
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "Permissions Granted", Toast.LENGTH_SHORT).show();
                    getLocation();
                }
                break;
            default:
                Toast.makeText(this, "Permission Not Granted", Toast.LENGTH_SHORT).show();
        }
    }




}