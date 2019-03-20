package com.example.safeauto;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.safeauto.MainFragments.CallsFragment;
import com.example.safeauto.MainFragments.CameraFragment;
import com.example.safeauto.MainFragments.GalleryFragment;
import com.example.safeauto.MainFragments.GpsFragment;
import com.example.safeauto.MainFragments.HomeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    public static  final String TAG = "MainActivity";
    //Temas de las notificaciones
    private static final String SP_TOPICS = "sharedPreferencesTopics";
    private Set<String> mTopicsSet;
    private SharedPreferences mSharedPreferences;

    private TextView mTextMessage;

    private Fragment fragment;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragment = new HomeFragment();
                    replaceFragment(fragment);
                    return true;
                case R.id.navigation_gps:
                    fragment = new GpsFragment();
                    replaceFragment(fragment);
                    return true;
                case R.id.navigation_calls:
                    fragment = new CallsFragment();
                    replaceFragment(fragment);
                    return true;
                case R.id.navigation_camera:
                    fragment = new CameraFragment();
                    replaceFragment(fragment);
                    return true;
                case R.id.navigation_gallery:
                    fragment = new GalleryFragment();
                    replaceFragment(fragment);
                    return true;
            }
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Configuracion del toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle(" " +getString(R.string.app_name));
        toolbar.setLogo(R.drawable.ic_car);


        //config SharedPreferences
        configSharedPreferences();
        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //manejo de token
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        String refreshToken = task.getResult().getToken();
                        Log.d("tokenId",task.getResult().getToken());
                    }
                });

        //Mostramos el fragment Home
        fragment = new HomeFragment();
        replaceFragment(fragment);
    }


    //investigar como se hace con java para la recuperacion de estados de un fragment
    /*https://medium.com/orbismobile/navegando-entre-fragmentos-sin-perder-su-estado-android-febc7c2b16a7  */
    private void replaceFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.container,fragment);
        //ft.addToBackStack(null);
        ft.commit();
    }


    private void configSharedPreferences() {

        mSharedPreferences = getPreferences(Context.MODE_PRIVATE);
        //extraemos la collecion set y en caso de no existir ponemos una instancia que este vacia
        mTopicsSet = mSharedPreferences.getStringSet(SP_TOPICS,new HashSet<String>());
        showTopics();

    }

    private void showTopics() {

    }

    //infla el menu con la opcion de account en el toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_option, menu);
        return true;
    }

    //evento donde se presiona la opcion account en el toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(getApplicationContext(),SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
