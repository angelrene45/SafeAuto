package com.example.safeauto.SettingsFragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.safeauto.MainActivity;
import com.example.safeauto.R;
import com.example.safeauto.Settings.UserData;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class MainFragment extends Fragment {

    public static final String TAG = "MainFragment";

    // Titulos de las opciones
    String[] listviewTitle;

    //icons
    int[] listviewImage;

    //Help with handle fragments settings
    Fragment fragment,currentFragment;

    //Data user localmente
    UserData userDataLocal;

    String tag;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        //Recuperamos informacion que tenemos localmente en UserData que es un shared preferences
        userDataLocal = new UserData(Objects.requireNonNull(getContext()));

        //lenado de los titulos
        listviewTitle = new String[]{
                getResources().getString(R.string.settings_profile), getResources().getString(R.string.settings_Car),
                getResources().getString(R.string.settings_notifications), getResources().getString(R.string.settings_functions),
                getResources().getString(R.string.settings_help), getResources().getString(R.string.settings_about),
                getResources().getString(R.string.settings_signout)
        };

        //Llenado de los icons
        listviewImage = new int[]{
                R.drawable.ic_settings_account, R.drawable.ic_settings_car, R.drawable.ic_settings_notifications,
                R.drawable.ic_settings_functions, R.drawable.ic_settings_help, R.drawable.ic_settings_info,
                R.drawable.ic_settings_logout
        };

        List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();

        for (int i = 0; i < 7; i++) {
            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put("listview_title", listviewTitle[i]);
            hm.put("listview_image", Integer.toString(listviewImage[i]));
            aList.add(hm);
        }

        String[] from = {"listview_image", "listview_title"};
        int[] to = {R.id.listview_image, R.id.listview_item_title};

        SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity().getBaseContext(), aList, R.layout.item_list_settings, from, to);
        ListView androidListView = (ListView) view.findViewById(R.id.list_view_settings);
        androidListView.setAdapter(simpleAdapter);

        androidListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        fragment = new ProfileFragment();
                        replaceFragment(fragment);
                        break;
                    case 1:
                        fragment = new CarFragment();
                        replaceFragment(fragment);
                        break;
                    case 2:
                        fragment = new NotificationsFragment();
                        replaceFragment(fragment);
                        break;
                    case 3:
                        fragment = new FunctionsFragment();
                        replaceFragment(fragment);
                        break;
                    case 4:
                        fragment = new HelpFragment();
                        replaceFragment(fragment);
                        break;
                    case 5:
                        fragment = new AboutFragment();
                        replaceFragment(fragment);
                        break;

                    case 6:
                        //cerramos sesion
                        AuthUI.getInstance().signOut(getContext())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                startActivity(new Intent(getContext(),MainActivity.class));
                            }
                        });

                        //limpiamos los datos del usuario
                        onSignedOutCleanup();

                        break;
                }

            }
        });

        return view;
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.content,fragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    private void onSignedOutCleanup() {
        //limpiamos el usuario que cerro sesion
        userDataLocal.cleanDataUser();
    }


}
