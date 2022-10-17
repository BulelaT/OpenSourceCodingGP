package com.example.opscgroupprojectapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.opscgroupprojectapp.Models.User_Model;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 *
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment {

    EditText username, password, email, cPass;
    Button regBtn;
    private FirebaseAuth mAuth;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View register = inflater.inflate(R.layout.fragment_register, container, false);
        mAuth = FirebaseAuth.getInstance();
        username = register.findViewById(R.id.etUsername);
        password = register.findViewById(R.id.etPassword);
        email = register.findViewById(R.id.etGmail);
        cPass = register.findViewById(R.id.ConfirmPassword);
        regBtn= register.findViewById(R.id.regBtn);

        

        mAuth.createUserWithEmailAndPassword(email.toString(),password.toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        User_Model newUser = new User_Model(email.toString(),password.toString());

                        FirebaseDatabase.getInstance().getReference("Users")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(newUser);
                    }
                });

        return register;
    }
}