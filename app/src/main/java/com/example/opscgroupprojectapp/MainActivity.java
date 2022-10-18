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

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LocationListener {

    LocationManager locationManager;
    public static String currCountryName;
    public static boolean isLocationOn;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                && (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED))
        {
            //This programming statement was adapted from Google Maps Platform:
            //Link: https://developers.google.com/maps/documentation/places/android-sdk/current-place-tutorial
            //Author: Google Developers
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 101);

            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().setReorderingAllowed(true).add(R.id.WelcomeFrag, WelcomeFragment.class,null).commitNow();


        }



    }

    @SuppressLint("MissingPermission")
    private void getLocation() {
            Toast.makeText(MainActivity.this, "Get location starts up", Toast.LENGTH_LONG).show();
            Log.d("Underpants", "permissions works");
            try{
                locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
                isLocationOn = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                if(!isLocationOn)
                {

                }
                else
                {
                    Toast.makeText(MainActivity.this, "in Else statement", Toast.LENGTH_LONG).show();
                    Log.d("Underpants", "else statement");
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, new LocationListener() {
                        @Override
                        public void onLocationChanged(@NonNull Location location) {
                            Toast.makeText(getApplicationContext(), "Latitude: "+location.getLatitude() +" Longitude: "+ location.getLongitude(), Toast.LENGTH_LONG).show();
                            Toast.makeText(MainActivity.this, "in onLocationChanged method", Toast.LENGTH_LONG).show();
                            try{
                                Geocoder currentCountry = new Geocoder(MainActivity.this, Locale.getDefault());
                                List<Address> addresses = currentCountry.getFromLocation(location.getLatitude(), location.getLongitude(),1);
                                String address = addresses.get(0).getCountryName();
                                currCountryName = address;
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

                Toast.makeText(MainActivity.this, "Something works", Toast.LENGTH_LONG).show();

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
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                    getLocation();
                }
                break;
            default:
                Toast.makeText(this, "Permission Not Granted", Toast.LENGTH_SHORT).show();
        }
    }


}