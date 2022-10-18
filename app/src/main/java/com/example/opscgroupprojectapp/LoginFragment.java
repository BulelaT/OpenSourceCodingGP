package com.example.opscgroupprojectapp;

import android.Manifest;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment{

    View login;
    EditText uEmail, uPassword;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        login = inflater.inflate(R.layout.fragment_login, container, false);
        uEmail = login.findViewById(R.id.etLoginEmail);
        uPassword = login.findViewById(R.id.etLoginPassword);
        Button btnLogin = login.findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signInWithEmailAndPassword(uEmail.getText().toString(), uPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            FirebaseUser fbu = mAuth.getCurrentUser();
                            FragmentManager fm = getParentFragmentManager();
                            fm.beginTransaction().setReorderingAllowed(true).replace(R.id.WelcomeFrag, Dashboard.class,null).addToBackStack(null).commit();

                        }
                    }
                });
            }
        });

        return login;
    }


}