package com.example.safeauto;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.safeauto.MainFragments.CallsFragment;
import com.example.safeauto.MainFragments.CameraFragment;
import com.example.safeauto.MainFragments.GalleryFragment;
import com.example.safeauto.MainFragments.GpsFragment;
import com.example.safeauto.MainFragments.HomeFragment;
import com.example.safeauto.Objetos.Car;
import com.example.safeauto.Objetos.Location;
import com.example.safeauto.Objetos.Usuario;
import com.example.safeauto.Settings.SharedPreferencesManager;
import com.example.safeauto.Settings.UserData;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_CALL = 302 ;
    private static final String PROVEEDOR_DESCONOCIDO = "Proveedor desconocido" ;
    public static  final String TAG = "MainActivity";
    //Temas de las notificaciones
    private static final String SP_TOPICS = "sharedPreferencesTopics";
    private Set<String> mTopicsSet;
    private SharedPreferences mSharedPreferences;

    private TextView mTextMessage;
    private Fragment fragment;
    private CircleImageView imageProfilePicture;

    //codigo para FirebaseIU (Authentification)
    public static final int RC_SING_IN = 123;
    public static final int RC_DATA_PERSONAL = 948;
    //Instancias para la autentificacion de usuario en Firebase
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    //instancia a firebase
    private DatabaseReference referenceUser = FirebaseDatabase.getInstance().getReference(Usuario.PATH_USER);

    //Recuperacion de UserData que es un sharedPreferences
    //es estatico para poder usaarlo en cualquier Activity de la aplicacion y obtener los datos rapidamente
    public static UserData userDataLocal;

    //manejo de tokens
    private ArrayList<String> listTokens;

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

        toolbar.setLogo(R.drawable.ic_car);

        //config SharedPreferences
        configSharedPreferences();
        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //ImageProfilepICTURE
        imageProfilePicture = (CircleImageView) findViewById(R.id.imageViewProfile);

        imageProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),SettingsActivity.class));
            }
        });

        //manejo de token
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        String refreshToken = task.getResult().getToken();
                        Log.d("tokenId",task.getResult().getToken());
                    }
                });


        //obitiene la instancia a FirebaseAuth
        mFirebaseAuth = FirebaseAuth.getInstance();

        //se ejecutara cada vez que se inicie o cierre sesion
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                //usuario logueado
                if(user!=null){
                    //verificamos que tengamos informacion del mac de arduino
                    verifyUser(user);

                    //Guardamos la informacion del usuario localmente para un rapido acceso a la informacion
                    onSetDataUser(user);

                    //objeto requestOptions para el cache y la imagen
                    RequestOptions requestOptions = new RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .centerCrop();
                    //muestra la imagen de perfil con GLADE
                    Glide.with(MainActivity.this)
                            .load(user.getPhotoUrl())
                            .apply(requestOptions)
                            .placeholder(R.drawable.ic_account_circle)
                            .into(imageProfilePicture);

                    //Mostramos el fragment Home
                    fragment = new HomeFragment();
                    replaceFragment(fragment);

                }else{//no tiene sesion activa

                    //Agregamos el provedoor facebook
                    AuthUI.IdpConfig facebookIdp = new AuthUI.IdpConfig.FacebookBuilder()
                            .setPermissions(Arrays.asList("user_friends","user_gender"))
                            .build();

                    //inicializar los proveedores para iniciar sesion y recibimos respuesta en onActivityResult
                    startActivityForResult(AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setIsSmartLockEnabled(false)
                            .setTosAndPrivacyPolicyUrls(
                                    "https://example.com/terms.html",
                                    "https://example.com/privacy.html")
                            .setAvailableProviders(Arrays.asList(new AuthUI.IdpConfig.EmailBuilder().build(),facebookIdp))
                            .setTheme(R.style.AuthUITheme)
                            .build(),RC_SING_IN);
                }
            }
        };

        //nos da la clave hash que nos pide facebook para usar su login
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.example.safeauto",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

        //pedimos permisos de las llamadas telefonicas que se utilizara en CallFragment
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.CALL_PHONE},REQUEST_CALL);

        listTokens = new ArrayList<>();
    }

    private void onSetDataUser(final FirebaseUser user) {
        //Tiene iniciada la sesion guardamos la informacion del usuario en un preferences o settings

        userDataLocal = new UserData(getApplicationContext());

        //Datos del usuario
        referenceUser.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Usuario objUsuario = dataSnapshot.getValue(Usuario.class);
                    Log.i(TAG,dataSnapshot.toString());
                    assert objUsuario != null;
                    //almacenamos los datos del usuario de forma local en un shared Preferences
                    userDataLocal.setUserData(
                            objUsuario.getName(),objUsuario.getEmail(),
                            objUsuario.getPhotoUrl(),objUsuario.getPhoneNumber(),
                            objUsuario.getTypeUser(),
                            objUsuario.getProvider()
                            );

                    //Datos del automovil
                    Car objCar = dataSnapshot.child(Car.PATH_CAR).getValue(Car.class);
                    Log.i(TAG, "Objeto Carro => " + objCar.toString());
                    assert objCar != null;
                    //almacenamos los datos del usuario de forma local en un shared Preferences
                    userDataLocal.setCarData(
                            objCar.getModel(), objCar.getPlaque(),
                            objCar.getMacArduino()
                    );
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //recibimos respuesta del servidor del login con FirebaseUI
        if(requestCode == RC_SING_IN){
            if(resultCode == Activity.RESULT_OK){
                /*
                todo salio bien
                Ya aqui ya tenemos iniciada la sesion
                solicitamoslosDatosPersonales como mac de arduino, telefono
                */
                FirebaseUser user = mFirebaseAuth.getCurrentUser();
                //verificamos si es su primer ingreso para solicitar los demas datos pendientes
                assert user != null;
                verifyUser(user);
                //Toast.makeText(getApplicationContext(),getString(R.string.toast_welcome_user),Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getApplicationContext(),getString(R.string.toast_failed_firebaseUI),Toast.LENGTH_SHORT).show();
            }
        }

        //Registro de datos personales del usuario ya logueado en Firebase
        if(requestCode == RC_DATA_PERSONAL){
            if(resultCode == Activity.RESULT_OK){
                //Se obtuvieron los datos del usuario correctamente
                FirebaseUser user = mFirebaseAuth.getCurrentUser();
                Toast.makeText(getApplicationContext(),getString(R.string.toast_new_user),Toast.LENGTH_SHORT).show();
            }else{
                //Con esto obligamos a que el usuario llene los datos forsozamente
                Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(),LoginDataActivity.class);
                startActivityForResult(intent,RC_DATA_PERSONAL);
            }
        }
    }

    private void verifyUser(final FirebaseUser user) {

        //comprobamos si existe el usuario
        referenceUser.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    //existe usuario
                    Usuario objetoUsuario = dataSnapshot.getValue(Usuario.class);
                    objetoUsuario.setUid(user.getUid());
                    comprobarTokensUser(objetoUsuario);
                    //Toast.makeText(getApplicationContext(), "Existe", Toast.LENGTH_SHORT).show();
                }else{
                    //mandamos la informacion basica del usuario(id,email,nombre)
                    Intent intent = new Intent(getApplicationContext(),LoginDataActivity.class);
                    intent.putExtra("uid",user.getUid());
                    intent.putExtra("name",user.getDisplayName());
                    intent.putExtra("email",user.getEmail());
                    if(user.getPhotoUrl() != null) {
                        intent.putExtra("photourl", user.getPhotoUrl().toString() );
                    }else{
                        intent.putExtra("photourl", "");
                    }
                    intent.putExtra("provider",user.getProviders() != null?
                            user.getProviders().get(0) : PROVEEDOR_DESCONOCIDO);
                    startActivityForResult(intent,RC_DATA_PERSONAL);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    /* Metodo que comprueba los tokens del usuario */
    private void comprobarTokensUser(Usuario objUsuario) {
        int aux=0;
        String tokenDevice = SharedPreferencesManager.getInstance(getApplicationContext()).getToken();
        ArrayList<String> tokensUsuario = objUsuario.getRegistrationTokens();

        Log.i(TAG, "size " + tokensUsuario.size());
        for(int i=0;i<tokensUsuario.size();i++){

            Log.i("token",tokensUsuario.get(i));

            if (tokensUsuario.get(i).equals(tokenDevice)) {
                //no es necesario actualizar la lista de token
                aux=-1;
            }

        }

        if(aux == -1) {
            Log.i(TAG,"No hay nuevo token no actualiza");
        }else {
            Log.i(TAG,"Actualiza nuevo token");
            tokensUsuario.add(tokenDevice); //añade el nuevo token
            referenceUser.child(objUsuario.getUid()).child(Usuario.CAMPO_REGISTRATION_TOKENS).
                    setValue(tokensUsuario);
        }
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == REQUEST_CALL ){
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //todo bien
                //tenemos los permisos
            }else{
                Toast.makeText(getApplicationContext(), getString(R.string.toast_failed_permission_call), Toast.LENGTH_SHORT).show();
            }
        }
    }



    @Override
    public void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mAuthStateListener != null){
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
    }
}
