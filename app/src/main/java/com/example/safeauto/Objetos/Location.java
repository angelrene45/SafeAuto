package com.example.safeauto.Objetos;

public class Location {

    // devices/[mac-arduino]/location
    public static final String PATH_DEVICES = "devices";
    public static final String PATH_SENSORS = "sensors";
    public static final String PATH_LOCATION= "location";
    public static final String FIELD_LATITUDE= "lat";
    public static final String FIELD_LONGITUDE= "lon";
    public static final String FIELD_PRECISION= "pre";

    Double lat;
    Double lon;
    Double prec;

    public Location() {
    }

    public Location(Double lat, Double lon, Double prec) {
        this.lat = lat;
        this.lon = lon;
        this.prec = prec;
    }

    public Double getLatitude() {
        return lat;
    }

    public void setLatitude(Double lat) {
        this.lat = lat;
    }

    public Double getLongitude() {
        return lon;
    }

    public void setLongitude(Double lon) {
        this.lon = lon;
    }

    public Double getPrec() {
        return prec;
    }

    public void setPrec(Double prec) {
        this.prec = prec;
    }

    @Override
    public String toString() {
        return "Location{" +
                "lat=" + lat +
                ", lon=" + lon +
                ", prec=" + prec +
                '}';
    }
}
