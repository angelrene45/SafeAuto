package com.example.safeauto.SettingsFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.safeauto.MainActivity;
import com.example.safeauto.R;
import com.example.safeauto.Settings.UserData;

import java.util.Objects;

public class ProfileFragment extends Fragment {

    private static final String PROVEEDOR_DESCONOCIDO = "Proveedor desconocido" ;
    private static final String PASSWORD_FIREBASE = "password" ; //indica quue se inicio sesion con correo y contraseña
    private static final String FACEBOOK  = "facebook.com"; //indica quue se inicio sesion con Facebook

    private static View view;

    //Componentes de la vista
    private TextView tvName,tvEmail,tvPhone,tvProvider;
    private ImageButton imageButtonProfile;

    //Data user localmente
    UserData userDataLocal;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);

        tvName  = (TextView)view.findViewById(R.id.tvName);
        tvEmail = (TextView)view.findViewById(R.id.tvEmail);
        tvPhone = (TextView)view.findViewById(R.id.tvPhone);
        tvPhone = (TextView)view.findViewById(R.id.tvPhone);
        tvProvider = (TextView)view.findViewById(R.id.tvProvider);
        imageButtonProfile = (ImageButton)view.findViewById(R.id.imageProfile);

        //Recuperamos informacion que tenemos localmente en UserData que es un shared preferences
        userDataLocal = new UserData(Objects.requireNonNull(getContext()));
        tvName.setText(userDataLocal.getName());
        tvEmail.setText(userDataLocal.getEmail());
        tvPhone.setText(userDataLocal.getPhone());

        //Rellenamos el imageView con el proveedor
        int drawableRes;
        switch (userDataLocal.getProvider()){
            case PASSWORD_FIREBASE:
                drawableRes = R.drawable.ic_firebase;
                break;

            case FACEBOOK:
                drawableRes = R.drawable.ic_facebook_box;
                break;

            default:
                drawableRes = R.drawable.ic_block_helper;
                break;
        }

        tvProvider.setCompoundDrawablesWithIntrinsicBounds( drawableRes, 0, 0, 0);
        tvProvider.setText(userDataLocal.getProvider());

        //objeto requestOptions para el cache y la imagen
        RequestOptions requestOptions = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop();
        //muestra la imagen de perfil con GLADE
        Glide.with(getContext())
                .load(userDataLocal.getPhoto())
                .apply(requestOptions)
                .placeholder(R.drawable.ic_account)
                .into(imageButtonProfile);


        return view;
    }




}
