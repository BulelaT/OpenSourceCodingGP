package com.example.opscgroupprojectapp;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

/**
 * A simple {@link Fragment} subclass.

 * create an instance of this fragment.
 */
public class AddLandmark extends Fragment {

    View landmarkView;
    ImageView picture;


    // Declaration of variables (The IIE, 2022)
    private final int STORAGE_PERMISSION_CODE = 100;
    private final int CAMERA_PERMISSION_CODE = 101;

    Bitmap imageBMP;
    Uri imgUri;


    private DatabaseReference dbRef;
    private FirebaseStorage fbStorage;
    private StorageReference storRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        landmarkView = inflater.inflate(R.layout.fragment_add_landmark, container, false);
        BottomNavigationView bottomNav = landmarkView.findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        //database setup
        dbRef = FirebaseDatabase.getInstance("").getReference();
        fbStorage = FirebaseStorage.getInstance();
        storRef = fbStorage.getReference();

        //
        picture = (ImageView) landmarkView.findViewById(R.id.ImageItemPic);


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
            fm.beginTransaction().setReorderingAllowed(true).replace(R.id.WelcomeFrag, DisplayLandFragment.class,null).addToBackStack(null).commit();

        }
        else if (itemId == R.id.Camera_Nav) {
            FragmentManager fm = getParentFragmentManager();
            fm.beginTransaction().setReorderingAllowed(true).replace(R.id.WelcomeFrag, AddLandmark.class,null).addToBackStack(null).commit();
        }
        else if (itemId == R.id.Settings_Nav) {
            FragmentManager fm = getParentFragmentManager();
            fm.beginTransaction().setReorderingAllowed(true).replace(R.id.WelcomeFrag, SettingsPage.class,null).addToBackStack(null).commit();
        }
        else if (itemId == R.id.SignOut_Nav) {
            FragmentManager fm = getParentFragmentManager();
            fm.beginTransaction().setReorderingAllowed(true).replace(R.id.WelcomeFrag, WelcomeFragment.class,null).addToBackStack(null).commit();
        }


        return true;
    };

    //AR Launcher (The IIE, 2022)
    ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            Bundle imgBundle = result.getData().getExtras();
            if (imgBundle != null){
                imageBMP = (Bitmap) imgBundle.get("data");
                picture.setImageBitmap(imageBMP);

                //gets timestamp at image creation, for use as image ID in database (Android Developers, 2022)
                Long timestamp = System.currentTimeMillis()/1000;
                String imgID = "IMG" + timestamp.toString();

                ByteArrayOutputStream boas = new ByteArrayOutputStream();
                imageBMP.compress(Bitmap.CompressFormat.JPEG, 100, boas);
                byte[] imgData = boas.toByteArray();
                String imgPath = MediaStore.Images.Media.insertImage(getContentResolver(), imageBMP, imgID, null);
                imgUri = Uri.parse(imgPath);

                uploadImg(imgID);
            }
        }
    });

    private void uploadImg(String imgid) {
        StorageReference imgRef = storRef.child("images/" + imgid);

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Uploading image");
        pd.show();

        imgRef.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                landmarkView = Toast.makeText(AddLandmark.this, "Image uploaded", Toast.LENGTH_SHORT).show();
                pd.dismiss();
                imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        imgUri = uri;
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddLandmark.this, "Image upload failed", Toast.LENGTH_LONG).show();
                pd.dismiss();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progress = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                pd.setMessage("Percentage: " + (int) progress + "%");
            }
        });
    }

    public void onClick(View view) {
        if (view.getId()==R.id.ImageItemPic) {
            //checkPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE);
            checkPermissions(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE);
        }else{
        }
    }

    // The code below checks for permission before the user can choose an image (The IIE, 2022)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(Add_Item_Page.this, "Camera permisison granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(Add_Item_Page.this, "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(Add_Item_Page.this, "Storage permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(Add_Item_Page.this, "Storage permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // The code below checks for permission before the user can choose an image (The IIE, 2022)
    public void checkPermissions(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Add_Item_Page.this, new String[]{permission}, requestCode);
        } else {
            Toast.makeText(Add_Item_Page.this, "Permission already granted", Toast.LENGTH_SHORT).show();
            ImageHandler();
        }
    }

    // The code below ensures a user can add an image (The IIE, 2022)
    public void ImageHandler(){
        Toast.makeText(Add_Item_Page.this, "Camera permission granted", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        resultLauncher.launch(i);
    }


}