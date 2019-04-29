package com.example.safeauto.Objetos;

public class Sensor {

    // devices/[mac-arduino]/sensors
    public static final String PATH_DEVICES = "devices";
    public static final String PATH_SENSORS = "sensors";
    public static final String FIELD_STATUS = "status";
    public static final String FIELD_WEIGTH= "weight";
    public static final String FIELD_IMPACT= "impact";

    Boolean status;
    Double weight;
    Double impact;


    public Sensor() {
    }


    public Sensor(Boolean status, Double weight, Double impact) {
        this.status = status;
        this.weight = weight;
        this.impact = impact;
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

    @Override
    public String toString() {
        return "Sensor{" +
                "status=" + status +
                ", weight=" + weight +
                ", impact=" + impact +
                '}';
    }
}
