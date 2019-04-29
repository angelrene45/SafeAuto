package com.example.safeauto;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.safeauto.Objetos.Car;
import com.example.safeauto.Objetos.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginDataActivity extends AppCompatActivity {

    private static final String TAG = "LoginDataActivity" ;
    private EditText etPhone,etCarModel,etPlaques,etMac;
    private Button btnCreateAccoutn;

    //instancia a firebase
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference(Usuario.PATH_USER);

    //Objetos
    Usuario objUser = new Usuario();
    Car objCar = new Car();

    //Variables
    private String uid ;
    private String name ;
    private String email ;
    private String photourl;
    private String provider ;
    private String phone;
    private String carModel;
    private String plaques;
    private String mac;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_data);

        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");
        name = intent.getStringExtra("name");
        email = intent.getStringExtra("email");
        photourl = intent.getStringExtra("photourl");
        provider = intent.getStringExtra("provider");

        Log.i(TAG, "id =>" + uid + " email => " + email + " photoUrl => " + photourl+
                " provider => " + provider);

        btnCreateAccoutn = (Button)findViewById(R.id.button_create_account);
        etPhone = (EditText)findViewById(R.id.et_phone);
        etCarModel = (EditText)findViewById(R.id.et_car_model);
        etPlaques = (EditText)findViewById(R.id.et_plaque);
        etMac = (EditText)findViewById(R.id.et_mac_arduino);


        //VALIDACIONES QUE OBTENGAMOS INFORMACION REAL


        //SUBIR A LA BASE DE DATOS TODO
        btnCreateAccoutn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG,"url =>" + photourl);
                phone = etPhone.getText().toString();
                carModel = etCarModel.getText().toString();
                plaques= etPlaques.getText().toString();
                mac  = etMac.getText().toString();

                addDataUserDataBase(uid,name,email,photourl,provider,phone,carModel,plaques,mac);
            }
        });

        //validacion para comprobar el estado del usuario ya que regresaba a la activity aunque
        //ya estubiera registrado
        comprobarUsuario();


    }

    private void comprobarUsuario() {
        reference.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Intent returnIntent = new Intent();
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void addDataUserDataBase(final String uid,String name, String email,String photoUrl,
                                     String provider,String phone,String carModel,String plaques,
                                     String mac){

        objUser.setUid(uid);
        objUser.setName(name);
        objUser.setEmail(email);
        objUser.setPhotoUrl(photoUrl);
        objUser.setProvider(provider);
        objUser.setPhoneNumber(phone);
        objCar.setModel(carModel);
        objCar.setPlaque(plaques);
        objCar.setMacArduino(mac);


        //Ahora lo subimos ala base de datos toda la informacion
        reference.child(uid).setValue(objUser).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                reference.child(uid).child(Car.PATH_CAR).setValue(objCar).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Intent returnIntent = new Intent();
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Intent returnIntent = new Intent();
                        setResult(Activity.RESULT_CANCELED, returnIntent);
                        finish();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_CANCELED, returnIntent);
                finish();
            }
        });

    }
}
