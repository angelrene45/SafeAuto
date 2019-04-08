package com.example.safeauto.Objetos;

public class Car {

    public static final String PATH_CAR = "car";
    private  String Model;
    private String Plaque;
    private String macArduino;

    public Car() {

    }

    public Car(String model, String plaque, String macArduino) {
        Model = model;
        Plaque = plaque;
        this.macArduino = macArduino;
    }

    public String getModel() {
        return Model;
    }

    public void setModel(String model) {
        Model = model;
    }

    public String getPlaque() {
        return Plaque;
    }

    public void setPlaque(String plaque) {
        Plaque = plaque;
    }

    public String getMacArduino() {
        return macArduino;
    }

    public void setMacArduino(String macArduino) {
        this.macArduino = macArduino;
    }
}
