package com.example.opscgroupprojectapp;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.opscgroupprojectapp.Models.Landmark_Model;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
    Button bc;
    private FirebaseAuth mAuth;
    Uri imageUri;
    StorageReference storageReference;
    String locName;
    Bitmap locImage;
    String locAddress;
    Uri newUri;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        cameraFrag = inflater.inflate(R.layout.fragment_camera, container, false);
        lName = cameraFrag.findViewById(R.id.addlandmark_name_textbox);
        takenPic = cameraFrag.findViewById(R.id.landmarkImageIV);
        imgUserPhoto = cameraFrag.findViewById(R.id.landmarkImageIV);
        bc = cameraFrag.findViewById(R.id.btnConfirm);
        bc.setEnabled(false);
        BottomNavigationView bottomNav = cameraFrag.findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        return cameraFrag;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(!Places.isInitialized())
        {
            Places.initialize(getActivity(),"AIzaSyCLsSnRSj3Wp4ODEHNh-frX__F__ZSpmYw");
        }

        checkPermissions(Manifest.permission.CAMERA, getCheckPermissionCodeCamera);


        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.ADDRESS, Place.Field.NAME, Place.Field.LAT_LNG);
        AutocompleteSupportFragment destLocat = (AutocompleteSupportFragment) getChildFragmentManager().findFragmentById(R.id.landmarkLocationFrag);
        destLocat.setPlaceFields(fields);




        destLocat.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onError(@NonNull Status status) {

            }


            @Override
            public void onPlaceSelected(@NonNull Place place) {
                locName = lName.getText().toString();
                locImage = ((BitmapDrawable)takenPic.getDrawable()).getBitmap();
                StorageReference fileRef = storageReference.child(String.valueOf(System.currentTimeMillis()));
                locAddress = place.getAddress();
                fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                      fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                          @Override
                          public void onSuccess(Uri uri) {
                              newUri = uri;

                          }
                      });
                    }
                }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        bc.setEnabled(true);
                    }
                });


            }
        });

        DatabaseReference userDB = FirebaseDatabase.getInstance().getReference("Users");
        bc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                lm = new Landmark_Model(newUri.toString(), locName, locAddress);
                Log.d("myNewImage", lm.getLandmarkName() + " " + lm.getLandmarkLocation());
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference();

                Log.d("check", "onClick: Button click");
                if(lm != null)
                {
                   userDB.child(mAuth.getUid()).child("FavLandmarks").child(lm.getLandmarkName()).setValue(lm);
                    Log.d("checking", "Value Added");
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
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == getCheckPermissionCodeCamera) {
            if (resultCode == Activity.RESULT_OK) {
                Bitmap bp = (Bitmap) data.getExtras().get("data");
                takenPic.setImageBitmap(bp);
                imageUri = saveImage(bp, getActivity());


            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(getContext(), "Cancelled", Toast.LENGTH_LONG).show();
            }
        }

    }

    public void captureImage()
    {
        /*Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                getCheckPermissionCodeCamera);*/
        Intent myIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Log.d("cImage", "captureImage: works");
        startActivityForResult(myIntent,getCheckPermissionCodeCamera);
        Log.d("cImage2", "captureImage: image taken");
        //ResultLauncher.launch(myIntent);
    }

    private Uri saveImage(Bitmap image, Context context) {
        // Create an image file name(Projects, 2022)
        File imageFolder = new File(context.getCacheDir(),"images");
        Uri uri = null;
        try{
            imageFolder.mkdirs();
            File file = new File(imageFolder,"captured_image.jpg");
            FileOutputStream stream = new FileOutputStream(file);
            //Compress the image taken
            image.compress(Bitmap.CompressFormat.JPEG,100,stream);
            //flush and close the the FileOutputStream
            stream.flush();
            stream.close();
            //Get uri from the FileProvider(Projects, 2022)
            uri = FileProvider.getUriForFile(context.getApplicationContext(),"com.example.opscgroupprojectapp" + ".provider",file);
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }

        //Return uri.
        return uri;
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