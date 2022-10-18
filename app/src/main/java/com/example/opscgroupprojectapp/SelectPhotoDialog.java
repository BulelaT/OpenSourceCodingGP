package com.example.opscgroupprojectapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.net.URI;

public class SelectPhotoDialog extends AddLandmark{

    private static final String TAG = "SelectPhotoDialog";
    private static final int PICKFILE_REQUEST_CODE = 1234;
    private static final int CAMERA_REQUEST_CODE = 4321;

    public interface OnPhotoSelectedListener {
        void getImagePath(Uri imagePath);
        void getImageBitMap(Bitmap bitmap);
    }

    OnPhotoSelectedListener mOnPhotoSelectedListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_selectphoto, container, false);

        //below code is for memory storage of the photos
        TextView selectPhoto = (TextView) view.findViewById(R.id.dialogChoosePhoto);
        selectPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Accessing phone memory");
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, PICKFILE_REQUEST_CODE);
            }
        });

        //
        TextView takePhoto = (TextView) view.findViewById(R.id.dialogOpenCamera);
        selectPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Starting Camera");
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAMERA_REQUEST_CODE);
            }
        });

        return view;

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //result when selecting a new result from memory
        if(requestCode == PICKFILE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri selectedImageUri = data.getData();
            Log.d(TAG, "onActivityResult: Starting Camera" + selectedImageUri);

            //send the uri to addlandmark fragment
            mOnPhotoSelectedListener.getImagePath(selectedImageUri);

        }

        //result when taking a new photo with camera
        else if(requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Log.d(TAG, "onActivityResult: Finished taking new photo :)");
            Bitmap bitMap;
            bitMap = (Bitmap) data.getExtras().get("data");

            //send the bitmap to the addlandmark fragment and dismiss dialog
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        try
        {
            mOnPhotoSelectedListener = (OnPhotoSelectedListener) getActivity();
        }
        catch (ClassCastException e)
        {
            Log.e(TAG, "onAttach: ClassCastException" +e.getMessage());
        }

        super.onAttach(context);
    }
}
