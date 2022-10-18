package com.example.opscgroupprojectapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.maps.model.PlaceType;
import com.google.maps.model.Unit;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class SettingsPage extends Fragment {

    View settingView;
    RadioGroup measurement, landmarkPreference;
    RadioButton radioButton1, radioButton2;
    Button settingsConfirm;
    private FirebaseAuth mAuth;
    public static Unit measure = Unit.METRIC;
    public static PlaceType pLandmark = PlaceType.BAR;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        settingView = inflater.inflate(R.layout.fragment_settings_page, container, false);
        BottomNavigationView bottomNav = settingView.findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        measurement = settingView.findViewById(R.id.rgMeasurement);
        landmarkPreference = settingView.findViewById(R.id.rgLandmark);
        settingsConfirm = settingView.findViewById(R.id.settingsConfirm);
        mAuth = FirebaseAuth.getInstance();

        measurement.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                // on below line we are getting radio button from our group.
                radioButton1 = settingView.findViewById(checkedId);

                if(radioButton1.getText().equals("Metric"))
                {
                    measure = Unit.METRIC;
                }
                else
                {
                    measure = Unit.IMPERIAL;
                }
            }
        });

        landmarkPreference.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                // on below line we are getting radio button from our group.
                radioButton2 = settingView.findViewById(checkedId);
                if(radioButton2.getText().equals("Supermarket"))
                {
                    pLandmark = PlaceType.GROCERY_OR_SUPERMARKET;
                }
                else if(radioButton2.getText().equals("Bar"))
                {
                    pLandmark = PlaceType.BAR;
                }
                else if(radioButton2.getText().equals("Airport"))
                {
                    pLandmark = PlaceType.AIRPORT;
                }
            }
        });

        FirebaseUser currUser = mAuth.getCurrentUser();
        DatabaseReference userDB = FirebaseDatabase.getInstance().getReference("Users");
        settingsConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userDB.child(currUser.getUid()).child("measurementUnit").setValue(measure).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });

                userDB.child(currUser.getUid()).child("preferedLandmark").setValue(pLandmark).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });

            }
        });

        return settingView;
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