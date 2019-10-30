package com.example.trippers;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;


public class launching_fragment extends Fragment {

    TextView aboutApp,register,login;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=  inflater.inflate(R.layout.fragment_launching_fragment, container, false);

        aboutApp= (TextView) view.findViewById(R.id.aboutApp);
        register= (TextView) view.findViewById(R.id.register);
        login= (TextView) view.findViewById(R.id.login);

        aboutApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), com.example.trippers.aboutApp.class);
                startActivity(intent);

            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Shubham_", "Register!");
                main_fragment fragment= new main_fragment();
                FragmentManager fm= getFragmentManager();
                fm.beginTransaction().replace(R.id.fragment,fragment).commit();


            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Shubham_", "Login!");
                com.example.trippers.login fragment= new login();
                FragmentManager fm= getFragmentManager();
                fm.beginTransaction().replace(R.id.fragment,fragment).commit();
            }
        });

        return view;
    }
}
