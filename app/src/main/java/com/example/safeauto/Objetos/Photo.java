package com.example.safeauto.Objetos;

import android.net.Uri;

public class Photo {
    public static final String PATH_DEVICES= "devices";
    public static final String PATH_CAMERA= "camera";
    public static final String PATH_STATUS= "status";
    public static final String PATH_PHOTOS = "photos";

    private String url;
    private String id;
    private Uri uri;
    private boolean status;

    public Photo(){}


    public Photo(String url, String id, Uri uri, boolean status) {
        this.url = url;
        this.id = id;
        this.uri = uri;
        this.status = status;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Photo{" +
                "url='" + url + '\'' +
                ", id='" + id + '\'' +
                ", uri=" + uri +
                ", status=" + status +
                '}';
    }
}
