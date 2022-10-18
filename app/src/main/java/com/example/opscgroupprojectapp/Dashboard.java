package com.example.opscgroupprojectapp;

import static android.content.Context.LOCATION_SERVICE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.widget.Toast;

import com.example.opscgroupprojectapp.Adapters.CountryListAdapter;
import com.example.opscgroupprojectapp.Adapters.InterfaceRV;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class Dashboard extends Fragment implements InterfaceRV {

    //(Ross, Zhang, Fogarty and Wobbrock, 2018)
    View dashboard;
    public static Double lat = 0.0, lng = 0.0;
    public static Boolean isGpsEnabled = false;
    public GoogleMap mMap;
    LocationManager locationManager;


    @SuppressLint("MissingPermission")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        dashboard = inflater.inflate(R.layout.fragment_dashboard, container, false);

        RecyclerView countryRV = dashboard.findViewById(R.id.dashboardRecyclerView);



        countryRV.setHasFixedSize(true);


        //(Professor Sluiter, 2020).
        //Ensuring the recycler view layout contains 1 item in each row
        RecyclerView.LayoutManager countryRVManager = new StaggeredGridLayoutManager(1, 1);
        countryRV.setLayoutManager(countryRVManager);

        //Retrieving navigation option texts
        String[] allCountries = getResources().getStringArray(R.array.Countries);
        List<String> allCountriesList = new ArrayList<String>();
        Random rand = new Random();

        allCountriesList.addAll(Arrays.asList(allCountries));

        try {
            allCountriesList.remove(allCountriesList.indexOf(MainActivity.currCountryName));
        }catch(Exception e)
        {
            e.printStackTrace();
        }

        allCountriesList.add(0,MainActivity.currCountryName);
        for (int i = 0; i < 5; i++)
        {
            allCountriesList.remove(1+rand.nextInt(allCountriesList.size()-2));
        }


        Log.d("underpants", "onCreateView: " + MainActivity.currCountryName);
        allCountries = allCountriesList.toArray(new String[allCountriesList.size()]);

        //Setting up adapters
        //Destination Options RecyclerView
        RecyclerView.Adapter<CountryListAdapter.OptionViewHolder> countryAdp = new CountryListAdapter(getParentFragmentManager(), this , requireContext(),allCountries);//(Professor Sluiter, 2020).
        countryRV.setAdapter(countryAdp);


        BottomNavigationView bottomNav = dashboard.findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        // as soon as the application opens the first
        // fragment should be shown to the user
        // in this case it is algorithm fragment
        //getParentFragmentManager().beginTransaction().replace(R.id.fragment_container, new SelectTour()).commit();

        return dashboard;
    }



    @Override
    public void onItemClick(int position)
    {
        Toast.makeText(getActivity(),"This is working", Toast.LENGTH_LONG).show();
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener navListener = item -> {
        // By using switch we can easily get
        // the selected fragment
        // by using there id.
        Fragment selectedFragment = null;
        int itemId = item.getItemId();
        if (itemId == R.id.Dashboard_Nav) {
            FragmentManager fm = getParentFragmentManager();
            fm.beginTransaction().setReorderingAllowed(true).replace(R.id.WelcomeFrag, Dashboard.class,null).addToBackStack(null).commit();
        }
        else if (itemId == R.id.Landmark_Nav) {
            FragmentManager fm = getParentFragmentManager();
            fm.beginTransaction().setReorderingAllowed(true).replace(R.id.WelcomeFrag, AddLandmark.class,null).addToBackStack(null).commit();

        }
        else if (itemId == R.id.Camera_Nav) {
            selectedFragment = new AddLandmark();
        }
        else if (itemId == R.id.Settings_Nav) {
            FragmentManager fm = getParentFragmentManager();
            fm.beginTransaction().setReorderingAllowed(true).replace(R.id.WelcomeFrag, SettingsPage.class,null).addToBackStack(null).commit();
        }
        else if (itemId == R.id.SignOut_Nav) {
            Intent intent = new Intent(getActivity().getApplicationContext(), MapsActivity.class);

            startActivity(intent);
        }


        return true;
    };


}