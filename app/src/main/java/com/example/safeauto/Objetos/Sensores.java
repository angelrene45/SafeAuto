package com.example.safeauto.Objetos;

public class Sensores {

    public static final String PATH_SENSORS = "sensors";
    public static final String PATH_LOCATION= "location";
    public static final String FIELD_STATUS = "status";
    public static final String FIELD_WEIGTH= "weight";
    public static final String FIELD_IMPACT= "impact";
    public static final String FIELD_LATITUDE= "latitude";
    public static final String FIELD_LONGITUDE= "longitude";

    Boolean status;
    Double weight;
    Double impact;
    Double latitude;
    Double longitud;

    public Sensores() {
    }


    public Sensores(Boolean status, Double weight, Double impact, Double latitude, Double longitud) {
        this.status = status;
        this.weight = weight;
        this.impact = impact;
        this.latitude = latitude;
        this.longitud = longitud;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getImpact() {
        return impact;
    }

    public void setImpact(Double impact) {
        this.impact = impact;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }
}
