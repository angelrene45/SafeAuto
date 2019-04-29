package com.example.safeauto.MainFragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.safeauto.MainActivity;
import com.example.safeauto.Objetos.Location;
import com.example.safeauto.Objetos.Sensor;
import com.example.safeauto.R;
import com.example.safeauto.Settings.UserData;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class GpsFragment extends Fragment implements OnMapReadyCallback {

    public static final String TAG = "GpsFragment";
    public static final float DEFAULT_ZOOM = 14f;
    public static final float MORE_ZOOM = 16f;

    //Instancia del mapa e googleMaps
    GoogleMap mMap;
    //Instancia de el View donde se agregan todos los compenentes de la vista
    private static View view;
    //objeto Location que nos ayuda a obtener la latitud y longitud
    private Location objLocation;

    //Soporte al mapFragment
    SupportMapFragment mapFragment;

    private String macUser = MainActivity.userDataLocal.getKeyMac();

    //Instancia a firebase a en el child location
    //https://safeauto-65aa8.firebaseio.com/devices/[MAC-ARDUINO]/location
    private DatabaseReference referenceGPS = FirebaseDatabase.getInstance().getReference(Location.PATH_DEVICES)
            .child(macUser).child(Location.PATH_LOCATION);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //valida que el fragment no se vuelva a recrear dentro del mismo ya que esto no es deseable en la app
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {

            //inflamos la vista con el layout del fragment gps
            view = inflater.inflate(R.layout.fragment_gps, container, false);

            //recuperamos el fragment del layout para su ejecucion
            mapFragment=(SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.map);

            //si es null el mapa lo inicializamos y creamos la nueva instancia
            if(mapFragment == null) {
                //obtencion de mapa
                FragmentManager manager = getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                //new instance inserta las options en el fragment que tiene el mapa en modo lite
                mapFragment = SupportMapFragment.newInstance();
                transaction.add(R.id.map, mapFragment);
                transaction.commit();
            }

            //metodo asincrono que se ejecuta cuando el mapa esta disponible para ejecutar la logica del metodo onMapReady
            mapFragment.getMapAsync(this);

        } catch (InflateException e) {
            /* map is already there, just return view as it is */
        }
        return view;

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //por defecto le ponemos zoom
        mMap.moveCamera( CameraUpdateFactory.newLatLngZoom(new LatLng(21.879610,-102.295227) , MORE_ZOOM) );

        getLocationFireBase();
    }

    public void getLocationFireBase(){
        Log.i(TAG, "Llenando informacion FRAGMENT UBICACION");
        //consulta a los datos Longitud y Latidud en la base de datos Firebase


        //escuchador que nos avisa cuando cambie la ubicacion del gps de la mac del usuario
        referenceGPS.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    objLocation = dataSnapshot.getValue(Location.class);
                    if(objLocation != null) {
                        Log.i(TAG, "Objeto dataSnapshot => " + objLocation.toString());
                        Log.i(TAG, "Objeto  Location => " + dataSnapshot.toString());
                        moveCamera(new LatLng(objLocation.getLatitude(), objLocation.getLongitude()), MORE_ZOOM, "Chevy 2009");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    //variable market que indica la posicion del automovil
    Marker now;
    //metodo que mueve la camara en el mapa de google
    private void moveCamera(LatLng latLng, float zoom,String title){

        //si ya existe un market lo elimina
        if(now != null){
            now.remove();
        }

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Chevy 2009");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
        now = mMap.addMarker(markerOptions);

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        //si descomentamos hace una animacion
        //mMap.animateCamera(CameraUpdateFactory.zoomTo(zoom));

        now.showInfoWindow();


    }








}
