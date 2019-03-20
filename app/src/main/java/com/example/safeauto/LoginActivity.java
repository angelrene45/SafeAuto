package com.example.safeauto;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.example.safeauto.LoginFragments.OptionsLoginFragment;
import com.example.safeauto.LoginFragments.SignInFragment;
import com.example.safeauto.LoginFragments.SignUpFragment;
import com.example.safeauto.MainActivity;
import com.example.safeauto.R;

public class LoginActivity extends AppCompatActivity {

    Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /*Linea que nos ayuda a que el background ocupe toda la pantalla incluyendo statusbar  */
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        fragment = new OptionsLoginFragment();
        replaceFragment(fragment);

        startActivity(new Intent(this, MainActivity.class));
    }


    private void replaceFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.content_login, fragment);
        ft.commit();
    }
}
