package com.example.safeauto.Objetos;

public class VideoStream {

    public static final String PATH_VIDEO = "video";
    public static final String FIELD_STATUS = "status";

    Boolean status;

    public VideoStream() {
    }


    public VideoStream(Boolean status) {
        this.status = status;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "VideoStream{" +
                "status=" + status +
                '}';
    }
}
