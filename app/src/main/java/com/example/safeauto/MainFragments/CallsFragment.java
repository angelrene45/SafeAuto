package com.example.safeauto.MainFragments;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.safeauto.MainActivity;
import com.example.safeauto.R;

public class CallsFragment extends Fragment {

    private static View view;
    private ImageView imageCall;


    public CallsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_calls, container, false);

        imageCall = (ImageView)view.findViewById(R.id.imageCall);


        imageCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //comprobamos los permisos del telefono
                if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED){
                    //No se obuvieron los permisos
                    //se piden los permisos al usuario
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.CALL_PHONE},MainActivity.REQUEST_CALL);

                }else{
                    //se obtuvieron los permisos
                    makePhoneCall("4491911320");
                }

            }
        });
        return view;
    }

    private void makePhoneCall(String number) {
        //se obtuvieron los permisos
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" +number));
        startActivity(intent);
    }





}
