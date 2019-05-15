package com.example.safeauto.MainFragments;

import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.safeauto.Adapter.AdapterGalleryPhotos;
import com.example.safeauto.MainActivity;
import com.example.safeauto.Objetos.Photo;
import com.example.safeauto.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;


public class GalleryFragment extends Fragment {

    private static final String TAG = "GalleryFragment" ;
    private View view;
    private RecyclerView recyclerPhotos;
    private ArrayList<Photo> listPhotos;
    private Button btnPhoto;
    private ProgressBar progressPhoto;
    private ImageView imageMain;

    //MacUser guardado en el sharedPrefercences
    String macUser = MainActivity.userDataLocal.getKeyMac();

    DatabaseReference referenceCamera = FirebaseDatabase.getInstance().getReference(Photo.PATH_DEVICES)
            .child(macUser).child(Photo.PATH_CAMERA);

    DatabaseReference referencePhotos = FirebaseDatabase.getInstance().getReference(Photo.PATH_DEVICES)
            .child(macUser).child(Photo.PATH_CAMERA).child(Photo.PATH_PHOTOS);

    DatabaseReference referenceStatus = FirebaseDatabase.getInstance().getReference(Photo.PATH_DEVICES)
            .child(macUser).child(Photo.PATH_CAMERA).child(Photo.PATH_STATUS);

    private Uri urlUri;
    private String urlString;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_gallery, container, false);

        recyclerPhotos = (RecyclerView)view.findViewById(R.id.recyclerPhotos);
        btnPhoto = (Button)view.findViewById(R.id.btnTakePhoto);
        progressPhoto = (ProgressBar)view.findViewById(R.id.progressPhoto);
        imageMain = (ImageView)view.findViewById(R.id.imageMain);

        //Cambia el status a true para que la raspberry capte el cambio y inicie con la captura de la foto
        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressPhoto.setVisibility(View.VISIBLE);

                //Accedemos al path status
                HashMap<String, Object> result = new HashMap<>();
                result.put("status",true);
                referenceCamera.updateChildren(result);

                final ValueEventListener mListener;

                mListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){

                            Log.i(TAG,dataSnapshot.toString());

                            Log.i(TAG,dataSnapshot.getValue().toString());

                            if(dataSnapshot.getValue().toString().equals("") ){

                            }


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                };

                referenceStatus.addValueEventListener(mListener);

            }
        });

        fillRecyclerPhotos();

        return view;
    }

    private void fillRecyclerPhotos() {
        Log.i(TAG,"LLENANDO PHOTOS");

        listPhotos = new ArrayList<>();
        recyclerPhotos.setHasFixedSize(true);

        recyclerPhotos.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        final AdapterGalleryPhotos adapter = new AdapterGalleryPhotos(getContext(),listPhotos);
        recyclerPhotos.setAdapter(adapter);

        //Rellena el recyclerView de todas las fotos
        referencePhotos.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listPhotos.removeAll(listPhotos); //Limpia la lista
                if(dataSnapshot.exists()){
                    for(DataSnapshot child : dataSnapshot.getChildren()){
                        Photo objPhoto = child.getValue(Photo.class);
                        Log.i(TAG,"Objeto =>" + objPhoto.toString());
                        objPhoto.setUri(Uri.parse(objPhoto.getUrl()));
                        listPhotos.add(objPhoto);
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //Muestra la imagen en grande dependiendo de la que se haya seleccionado
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TextView tvId;
                tvId = v.findViewById(R.id.tvId);
                Uri uri = Uri.parse(tvId.getText().toString());

                Log.i(TAG,"URL =>"+tvId.getText().toString());

                Glide.with(getContext())
                        .load(uri)
                        .apply(new RequestOptions()
                                .centerCrop())
                        .into(imageMain);
            }
        });

    }

}
