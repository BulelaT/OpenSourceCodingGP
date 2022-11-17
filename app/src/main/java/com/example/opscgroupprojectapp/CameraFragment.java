package com.example.opscgroupprojectapp;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.opscgroupprojectapp.Models.Landmark_Model;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class CameraFragment extends Fragment {

    View cameraFrag;
    EditText lName;
    Landmark_Model lm;
    ImageView takenPic;
    Bitmap newImage;
    ImageView imgUserPhoto;
    private static final int MY_CAMERA_REQUEST_CODE = 101;
    final int getCheckPermissionCodeCamera = 101;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        cameraFrag = inflater.inflate(R.layout.fragment_camera, container, false);
        lName = cameraFrag.findViewById(R.id.addlandmark_name_textbox);
        takenPic = cameraFrag.findViewById(R.id.landmarkImageIV);
        imgUserPhoto = cameraFrag.findViewById(R.id.landmarkImageIV);
        return cameraFrag;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        checkPermissions(Manifest.permission.CAMERA, getCheckPermissionCodeCamera);


        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.ADDRESS, Place.Field.NAME, Place.Field.LAT_LNG);
        AutocompleteSupportFragment destLocat = (AutocompleteSupportFragment) getChildFragmentManager().findFragmentById(R.id.landmarkLocationFrag);
        destLocat.setPlaceFields(fields);

        String locName = lName.toString();
        Bitmap locImage = ((BitmapDrawable)takenPic.getDrawable()).getBitmap();


        destLocat.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onError(@NonNull Status status) {

            }

            @Override
            public void onPlaceSelected(@NonNull Place place) {
                if(!lName.equals(""))
                {
                    lm = new Landmark_Model(locImage, locName, place.getAddress());
                }

            }
        });
    }

    public void checkPermissions(String perm, int permCode)
    {
        if(ContextCompat.checkSelfPermission(requireContext(), perm) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(getActivity(), new String[]{perm}, permCode);

        }
        else
        {
            captureImage();
        }

    }

    @Override
    public void onRequestPermissionsResult(int reqCode, @NonNull String[] perm, @NonNull int[] grantRes)
    {
        super.onRequestPermissionsResult(reqCode,perm,grantRes);
        if(reqCode == getCheckPermissionCodeCamera)
        {
            if(grantRes.length > 0 && grantRes[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(getActivity(),"Permission Granted", Toast.LENGTH_LONG).show();
                captureImage();
            }
            else
            {
                Toast.makeText(getActivity(),"Permission Denied", Toast.LENGTH_LONG).show();
            }
        }
    }
    /*@Override
    public void onActivityResult(int requestcode, int resultcode, Intent data)
    {
        if(requestcode == getCheckPermissionCodeCamera){
            if(resultcode == Activity.RESULT_OK)
            {
                Bitmap bmp = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream stream = new ByteArrayOutputStream();

                bmp.compress(Bitmap.CompressFormat.PNG,100, stream);
                byte[] byteArray = stream.toByteArray();

                Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray,0,byteArray.length);
                takenPic.setImageBitmap(bitmap);

            }
        }
    }*/

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == getCheckPermissionCodeCamera) {
            if (resultCode == Activity.RESULT_OK) {
                Bitmap bp = (Bitmap) data.getExtras().get("data");
                takenPic.setImageBitmap(bp);
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(getContext(), "Cancelled", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void captureImage()
    {
        Intent myIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(myIntent,getCheckPermissionCodeCamera);
        //ResultLauncher.launch(myIntent);
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
        return true;
    };
}