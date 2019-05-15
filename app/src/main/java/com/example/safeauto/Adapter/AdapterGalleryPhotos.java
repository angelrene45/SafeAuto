package com.example.safeauto.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.safeauto.Objetos.Photo;
import com.example.safeauto.R;

import java.util.ArrayList;

public class AdapterGalleryPhotos extends RecyclerView.Adapter<AdapterGalleryPhotos.ViewHolderAdapterGallery> implements View.OnClickListener {


    private ArrayList<Photo> listPhotos;
    private View.OnClickListener listener;
    private Context context;

    public AdapterGalleryPhotos(Context context,ArrayList<Photo> listPhotos) {
        this.listPhotos = listPhotos;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolderAdapterGallery onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.gallery_adapter,viewGroup,false);
        view.setOnClickListener(this);
        Context context = viewGroup.getContext();

        return new ViewHolderAdapterGallery(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolderAdapterGallery holder, final int position) {

        Log.i("GalleryFragment","Uri =>" + listPhotos.get(position).getUri());

        Glide.with(context)
                .load(listPhotos.get(position).getUri())
                .apply(new RequestOptions()
                        .centerCrop())
                .into(holder.imageCard);

        holder.tvId.setText(listPhotos.get(position).getUrl());

    }

    @Override
    public int getItemCount() {
        if(listPhotos != null){
            return listPhotos.size();
        }
        return 0;
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        if(listener != null){
            listener.onClick(v);
        }
    }

    public class ViewHolderAdapterGallery extends RecyclerView.ViewHolder {
        ImageView imageCard;
        TextView tvId;

        public ViewHolderAdapterGallery(@NonNull View itemView) {
            super(itemView);

            imageCard = itemView.findViewById(R.id.imageCard);
            tvId = itemView.findViewById(R.id.tvId);
        }
    }
}
