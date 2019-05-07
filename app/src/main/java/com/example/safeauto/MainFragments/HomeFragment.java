package com.example.safeauto.MainFragments;


import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.example.safeauto.MainActivity;
import com.example.safeauto.Objetos.Location;
import com.example.safeauto.Objetos.Sensor;
import com.example.safeauto.R;
import com.example.safeauto.Settings.UserSettings;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import pl.pawelkleczkowski.customgauge.CustomGauge;

public class HomeFragment extends Fragment {

    public static final String TAG = "HomeFragment";
    public static final int GAUGE_FULL = 270;
    public static final int GAUGE_LOW = 0;
    private Switch switchStatusAlarma;

    //recuperacion de settings del usuario
    Boolean settingStatusAlarm = false;
    UserSettings userSettings;


    //MacUser guardado en el sharedPrefercences
    String macUser = MainActivity.userDataLocal.getKeyMac();

    //instancia a firebase de los sensores
    //https://safeauto-65aa8.firebaseio.com/devices/[MAC-ARDUINO]/sensors
    DatabaseReference referenceSensors = FirebaseDatabase.getInstance().getReference(Sensor.PATH_DEVICES)
            .child(macUser).child(Sensor.PATH_SENSORS);

    //instancia a clase  sensores
    Sensor objSensor;

    //instanmcia View
    private static View view;

    //Medidores
    private CustomGauge gaugeImpact;
    private CustomGauge gaugeWeight;

    //componentes View
    ImageView imageViewLogo;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.fragment_home, container, false);

            Log.i(TAG,macUser);

            //instancia a medidores
            gaugeImpact = view.findViewById(R.id.gaugeImpact);
            gaugeWeight = view.findViewById(R.id.gaugeWeigth);

            //instancia componentes vista
            imageViewLogo = view.findViewById(R.id.logoSafeAuto);


            listenerSensors();

            //instancia al objeto sensores
            objSensor = new Sensor();

            //Instancia al switch boton para saber si esta encendida o apagada la alarma
            switchStatusAlarma = view.findViewById(R.id.statusAlarm);

            //recuperamos el ultimo status que ingreso el usuario
            userSettings = new UserSettings(Objects.requireNonNull(getContext()));

            switchStatusAlarma.setChecked(userSettings.getStatusAlarm());

            getCurrentStatusAlarmFirebase();

            //escuchador cuando cambia el estado del switch on/off
            switchStatusAlarma.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        Toast.makeText(getContext(),getString(R.string.statusON), Toast.LENGTH_SHORT).show();
                        referenceSensors.child(Sensor.FIELD_STATUS).setValue(true);
                        userSettings.setStatusAlarm(true);
                    }else{
                        Toast.makeText(getContext(),getString(R.string.statusOFF), Toast.LENGTH_SHORT).show();
                        referenceSensors.child(Sensor.FIELD_STATUS).setValue(false);
                        userSettings.setStatusAlarm(false);
                    }
                }
            });
        } catch (InflateException e) {

        }
        return view;


    }

    //maximo es 270
    private void listenerSensors() {

        referenceSensors.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    Log.i(TAG,dataSnapshot.toString());
                    Sensor objSensor = dataSnapshot.getValue(Sensor.class);

                    if(objSensor != null) {
                        Log.i(TAG,objSensor.toString());

                        if (objSensor.getImpact() == 1) {
                            gaugeImpact.setValue(GAUGE_FULL);


                        } else if (objSensor.getImpact() == 0) {
                            gaugeImpact.setValue(GAUGE_LOW);
                        }

                        if (objSensor.getWeight() == 1) {
                            gaugeWeight.setValue(GAUGE_LOW);

                        } else if (objSensor.getImpact() == 0) {
                            gaugeWeight.setValue(GAUGE_FULL);

                        }

                        Log.i(TAG, dataSnapshot.toString());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //obtiene el estatus actual en la base de datos de la alarma y lo compara con el settingUusario
    //comparando que este bien y no haya fallas
    private void getCurrentStatusAlarmFirebase() {
        referenceSensors.child(Sensor.FIELD_STATUS);

        referenceSensors.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    //objeto de firebase lo parsea en la clase Sensor para obtener status
                    Sensor sensor = dataSnapshot.getValue(Sensor.class);

                    //recuperamos el ultimo status que ingreso el usuario
                    settingStatusAlarm = userSettings.getStatusAlarm();

                    //si es diferentes el status que se guardo en los settings que en la base de datos de
                    //Firebase cambiamos el switch como esta en firebase y el settings lo actualizamos
                    assert sensor != null; //validacion que el objeto sensor no sea null
                    if(settingStatusAlarm != sensor.getStatus()){
                        switchStatusAlarma.setChecked(sensor.getStatus());
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

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        //Orientacion actual portrait o landscape
        int currentOrientation = getResources().getConfiguration().orientation;
        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            imageViewLogo.setVisibility(View.GONE);
        }
        else {
            imageViewLogo.setVisibility(View.VISIBLE);
        }
    }
}
