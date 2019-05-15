package com.example.safeauto.MainFragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.example.safeauto.MainActivity;
import com.example.safeauto.Objetos.Sensor;
import com.example.safeauto.Objetos.VideoStream;
import com.example.safeauto.R;
import com.example.safeauto.Settings.UserSettings;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class CameraFragment extends Fragment {


    private static final String TAG = "CameraFragment" ;
    private View view;
    private WebView mWebView;
    private String piAddr = "http://192.168.0.101:8081/";
    private String piAddrressPublic = "http://safeauto.ddns.net:8081/";
    private Switch switchStatusVideoStream;
    private Button btnReload;

    //recuperacion de settings del usuario
    Boolean settingStatusVideoStream = false;
    UserSettings userSettings;

    //MacUser guardado en el sharedPrefercences
    String macUser = MainActivity.userDataLocal.getKeyMac();

    DatabaseReference referenceSensors = FirebaseDatabase.getInstance().getReference(Sensor.PATH_DEVICES)
            .child(macUser).child(VideoStream.PATH_VIDEO);


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_camera, container, false);

        //Carga el live Stream
        mWebView = (WebView)view.findViewById(R.id.webview);
        mWebView.loadUrl(piAddrressPublic);

        //Boton Recargar
        btnReload = (Button)view.findViewById(R.id.btnReload);


        //Instancia al switch boton para saber si esta encendida o apagada la alarma
        switchStatusVideoStream = view.findViewById(R.id.statusVideoStream);

        //recuperamos el ultimo status que ingreso el usuario
        userSettings = new UserSettings(Objects.requireNonNull(getContext()));

        switchStatusVideoStream.setChecked(userSettings.getStatusVideoStream());

        getCurrentStatusVideoStreamFirebase();

        //escuchador cuando cambia el estado del switch on/off
        switchStatusVideoStream.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Toast.makeText(getContext(),getString(R.string.statusVideoON), Toast.LENGTH_SHORT).show();
                    referenceSensors.child(VideoStream.FIELD_STATUS).setValue(true);
                    userSettings.setStatusVideoStream(true);
                    Toast.makeText(getContext(), getString(R.string.txt_reload_tab), Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getContext(),getString(R.string.statusVideoOFF), Toast.LENGTH_SHORT).show();
                    referenceSensors.child(VideoStream.FIELD_STATUS).setValue(false);
                    userSettings.setStatusVideoStream(false);
                }
            }
        });



        btnReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWebView.reload();
            }
        });

        return view;
    }


    //obtiene el estatus actual en la base de datos de live stream y lo compara con el settingUusario
    //comparando que este bien y no haya fallas
    private void getCurrentStatusVideoStreamFirebase() {
        referenceSensors.child(Sensor.FIELD_STATUS);

        referenceSensors.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    //objeto de firebase lo parsea en la clase Sensor para obtener status
                    Sensor sensor = dataSnapshot.getValue(Sensor.class);

                    //recuperamos el ultimo status que ingreso el usuario
                    settingStatusVideoStream = userSettings.getStatusAlarm();

                    //si es diferentes el status que se guardo en los settings que en la base de datos de
                    //Firebase cambiamos el switch como esta en firebase y el settings lo actualizamos
                    assert sensor != null; //validacion que el objeto sensor no sea null
                    if(settingStatusVideoStream != sensor.getStatus()){
                        switchStatusVideoStream.setChecked(sensor.getStatus());
                        userSettings.setStatusAlarm(sensor.getStatus());
                    }
                    Log.i(TAG, sensor.getStatus().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(),"Error ver log", Toast.LENGTH_SHORT);
                Log.e(TAG,databaseError.getMessage());
            }
        });
    }



}
