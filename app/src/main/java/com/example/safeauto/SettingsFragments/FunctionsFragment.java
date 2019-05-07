package com.example.safeauto.SettingsFragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.safeauto.MainActivity;
import com.example.safeauto.R;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FunctionsFragment extends Fragment {


    // Titulos de las opciones
    private String[] listviewTitle;

    //icons
    private int[] listviewImage;

    private View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_functions, container, false);

        //lenado de los titulos
        listviewTitle = new String[]{
                getResources().getString(R.string.functions_alarm_off),
                getResources().getString(R.string.functions_contact),
                getResources().getString(R.string.functions_locate)
        };

        //Llenado de los icons
        listviewImage = new int[]{
                R.drawable.ic_info_black_24dp,
                R.drawable.ic_info_black_24dp,
                R.drawable.ic_info_black_24dp
        };

        List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();

        for (int i = 0; i < listviewTitle.length; i++) {
            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put("listview_title", listviewTitle[i]);
            hm.put("listview_image", Integer.toString(listviewImage[i]));
            aList.add(hm);
        }

        String[] from = {"listview_image", "listview_title"};
        int[] to = {R.id.listview_image, R.id.listview_item_title};

        SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity().getBaseContext(), aList, R.layout.item_list_settings, from, to);
        ListView androidListView = (ListView) view.findViewById(R.id.list_view_functions);
        androidListView.setAdapter(simpleAdapter);

        androidListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:

                        break;
                    case 1:

                        break;
                    case 2:

                        break;
                }
            }
        });



        return view;
    }

}
