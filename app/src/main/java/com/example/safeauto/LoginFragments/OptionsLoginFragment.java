package com.example.safeauto.LoginFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.safeauto.R;


public class OptionsLoginFragment extends Fragment {

    private Button btnSingIn, btnSingUp;
    private Fragment fragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_options_login, container, false);
        btnSingIn = view.findViewById(R.id.button_signIn);
        btnSingUp = view.findViewById(R.id.button_signUp);

        btnSingIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new SignInFragment();
                replaceFragment(fragment);
            }
        });

        btnSingUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new SignUpFragment();
                replaceFragment(fragment);
            }
        });
        return view ;
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.content_login, fragment);
        ft.addToBackStack(null);
        ft.commit();
    }



}
