package com.example.opscgroupprojectapp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * A simple {@link Fragment} subclass.

 * create an instance of this fragment.
 */

//(Zapata, 2016)
public class AddLandmark extends Fragment {
    View landmarkView;
    Button imageButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        landmarkView = inflater.inflate(R.layout.fragment_add_landmark, container, false);
        BottomNavigationView bottomNav = landmarkView.findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        return landmarkView;
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
            FragmentManager fm = getParentFragmentManager();
            fm.beginTransaction().setReorderingAllowed(true).replace(R.id.WelcomeFrag, CameraFragment.class,null).addToBackStack(null).commit();
        }
        else if (itemId == R.id.Settings_Nav) {
            FragmentManager fm = getParentFragmentManager();
            fm.beginTransaction().setReorderingAllowed(true).replace(R.id.WelcomeFrag, SettingsPage.class,null).addToBackStack(null).commit();
        }
/*        else if (itemId == R.id.SignOut_Nav) {
            selectedFragment = new MainActivity();
        }*/


        return true;
    };

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private void verifyPermissions()
    {
        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};

        if(ContextCompat.checkSelfPermission(getContext(),permissions[0]) == PackageManager.PERMISSION_GRANTED
        && ContextCompat.checkSelfPermission(getContext(),permissions[1]) == PackageManager.PERMISSION_GRANTED
        && ContextCompat.checkSelfPermission(getContext(),permissions[2]) == PackageManager.PERMISSION_GRANTED)
        {

        }
        else
        {

        }
    }

    /*@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions, @NonNull int[] grantResults)
    {
        verifyPermissions();
    }*/
}