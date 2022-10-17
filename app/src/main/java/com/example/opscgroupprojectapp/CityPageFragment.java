package com.example.opscgroupprojectapp;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 *
 */
public class CityPageFragment extends Fragment {


    View cityView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        cityView = inflater.inflate(R.layout.fragment_city_page, container, false);
        BottomNavigationView bottomNav = cityView.findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        return cityView;
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
/*        else if (itemId == R.id.SignOut_Nav) {
            selectedFragment = new MainActivity();
        }*/


        return true;
    };
}