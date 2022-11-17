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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.opscgroupprojectapp.Adapters.InterfaceRV;
import com.example.opscgroupprojectapp.Adapters.LandmarkAdapter;
import com.example.opscgroupprojectapp.Models.Landmark_Model;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.

 * create an instance of this fragment.
 */

//(Zapata, 2016)
public class AddLandmark extends Fragment implements InterfaceRV {
    View landmarkView;
    Button imageButton;
    RecyclerView itemRV;
    FirebaseAuth mAuth;
    DatabaseReference dbr;
    ArrayList<Landmark_Model> myLMList = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mAuth = FirebaseAuth.getInstance();
        dbr = FirebaseDatabase.getInstance().getReference("Users");
        landmarkView = inflater.inflate(R.layout.fragment_add_landmark, container, false);

        itemRV = landmarkView.findViewById(R.id.rvItem);

        dbr.child(mAuth.getUid()).child("FavLandmarks").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                myLMList.clear();
                for (DataSnapshot snap: snapshot.getChildren()) {

                    myLMList.add(snap.getValue(Landmark_Model.class));
                    Log.d("loadedItem", snap.getValue(Landmark_Model.class).getLandmarkName());

                }
                initializeRV();
            }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        BottomNavigationView bottomNav = landmarkView.findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);





        return landmarkView;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



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

    public void initializeRV()
    {
        LandmarkAdapter myAdapter = new LandmarkAdapter(getParentFragmentManager(), this , requireContext(),myLMList);
        LinearLayoutManager llm = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false);
        itemRV.setLayoutManager(llm);
        itemRV.setAdapter(myAdapter);
    }


    @Override
    public void onItemClick(int position)
    {
        Toast.makeText(getActivity(),"This is working", Toast.LENGTH_LONG).show();
    }
}