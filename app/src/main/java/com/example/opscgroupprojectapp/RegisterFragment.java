package com.example.opscgroupprojectapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 *
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment {

    EditText username, uPassword, uEmail, cPass;
    Button registerBtn;
    private FirebaseAuth mAuth;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View register = inflater.inflate(R.layout.fragment_register, container, false);

        username = register.findViewById(R.id.etUsername);
        uPassword = register.findViewById(R.id.etPassword);
        uEmail = register.findViewById(R.id.etGmail);
        cPass = register.findViewById(R.id.ConfirmPassword);
        registerBtn= register.findViewById(R.id.regBtn);

        return register;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
                    public void onClick(View v) {
                        String name = username.getText().toString();
                        String email = uEmail.getText().toString();
                        String password = uPassword.getText().toString();
                        String cPassword = cPass.getText().toString();
                        User_Model user;


                            user = new User_Model(email, password);

                            mAuth = FirebaseAuth.getInstance();
                            mAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword())
                                    .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {

                                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                                DatabaseReference myRef = database.getReference();
                                                Toast.makeText(getActivity(), user.getEmail() + " " + user.getPassword(),
                                                        Toast.LENGTH_SHORT).show();
                                                myRef.child("Users").child(mAuth.getUid()).setValue(user);
                                                Toast.makeText(getActivity(), "Registration Successful.",
                                                        Toast.LENGTH_SHORT).show();
                                                FragmentManager fm = getParentFragmentManager();
                                                fm.beginTransaction().setReorderingAllowed(true).replace(R.id.WelcomeFrag, WelcomeFragment.class,null).addToBackStack(null).commit();

                                            } else {
                                                // If sign in fails, display a message to the user.
                                                Toast.makeText(getActivity(), "Authentication failed.",
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                    }

                });

            }

    }
