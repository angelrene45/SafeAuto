package com.example.safeauto.SettingsFragments;


import android.os.Bundle;
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

import com.example.safeauto.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainFragment extends Fragment {

    public static final String TAG = "MainFragment";

    // Array of strings for ListView Title
    String[] listviewTitle = new String[]{
            "Perfil", "Vehiculo", "Notificaciones", "Funciones",
            "Ayuda", "Acerca de","Cerrar sesión"
    };

    //icons
    int[] listviewImage = new int[]{
            R.drawable.ic_settings_account, R.drawable.ic_settings_car, R.drawable.ic_settings_notifications,
            R.drawable.ic_settings_functions, R.drawable.ic_settings_help, R.drawable.ic_settings_info,
            R.drawable.ic_settings_logout
    };

    //Help with handle fragments settings
    Fragment fragment,currentFragment;

    String tag;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

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


}
