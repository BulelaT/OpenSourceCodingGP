package com.example.opscgroupprojectapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * A simple {@link Fragment} subclass.
 *
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment {

    EditText username, password, email, cPass;
    Button regBtn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View register = inflater.inflate(R.layout.fragment_register, container, false);

        username = register.findViewById(R.id.etUsername);
        password = register.findViewById(R.id.etPassword);
        email = register.findViewById(R.id.etGmail);
        cPass = register.findViewById(R.id.ConfirmPassword);
        regBtn= register.findViewById(R.id.regBtn);

        return register;
    }
}