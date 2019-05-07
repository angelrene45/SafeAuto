package com.example.safeauto.SettingsFragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.safeauto.R;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashSet;
import java.util.Set;


public class NotificationsFragment extends Fragment implements View.OnClickListener{

    /*Este fragment es para suscrirse a topicos o temas en la aplicacion en este caso
     el usuario escogera que tipo de notificacion quiere que le lleguen al celuar*/

    private static final String TAG = "NotificationsFragment";
    private  static final String SP_TOPICS = "sharedPreferencesTopics";
    private Spinner spTopics;
    private TextView tvTopics;
    private Button btnSuscribe;
    private Button btnUnSuscribe;
    private static View view;

    private Set<String> mTopicsSet;
    private SharedPreferences mSharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_notifications, container, false);

        spTopics = (Spinner)view.findViewById(R.id.spTopics);
        tvTopics = (TextView) view.findViewById(R.id.tvTopics);
        btnSuscribe = (Button) view.findViewById(R.id.btnSuscribe);
        btnUnSuscribe = (Button) view.findViewById(R.id.btnUnSuscribe);

        btnSuscribe.setOnClickListener(this);
        btnUnSuscribe.setOnClickListener(this);

        configSharedPreferences();


        return view;
    }

    private void configSharedPreferences() {
        mSharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);

        mTopicsSet = mSharedPreferences.getStringSet(SP_TOPICS,new HashSet<String>());

        showTopics();
    }

    private void showTopics() {
        tvTopics.setText(mTopicsSet.toString());
    }


    @Override
    public void onClick(View v) {

        //obtiene el topico seleccionado del usuario
        String topic = getResources().getStringArray(R.array.TopicsValues)[spTopics.getSelectedItemPosition()];


        switch (v.getId()){
            case R.id.btnSuscribe:
                if (!mTopicsSet.contains(topic)) { //en caso que no contenga el topico pasa a suscribirse
                    FirebaseMessaging.getInstance().subscribeToTopic(topic);
                    mTopicsSet.add(topic);
                    saveSharedPreferences();
                    Toast.makeText(getContext(), "suscribe", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.btnUnSuscribe:
                if (mTopicsSet.contains(topic)){ //en caso que contenga el topico pasa a desuscribirse
                    FirebaseMessaging.getInstance().unsubscribeFromTopic(topic);
                    mTopicsSet.remove(topic);
                    saveSharedPreferences();
                    Toast.makeText(getContext(), "unsuscribe", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void saveSharedPreferences() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.clear(); //prevenimos datos corruptos
        editor.putStringSet(SP_TOPICS,mTopicsSet); //agregamos el topico agregado
        editor.apply(); //ejecutamos los cambios

        //refrescamos el texview
        showTopics();

    }
}
