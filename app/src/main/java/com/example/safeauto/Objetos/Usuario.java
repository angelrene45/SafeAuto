package com.example.safeauto.Objetos;

import java.util.ArrayList;

public class Usuario {
    public static final String PATH_USER = "users";

    public static final int TYPE_USER_ADMIN = 0;
    public static final int TYPE_USER_CLIENT = 1;

    public static final String CAMPO_UID = "uid";
    public static final String CAMPO_EMAIL = "email";
    public static final String CAMPO_NAME = "name";
    public static final String CAMPO_PHOTO_URL = "photoUrl";
    public static final String CAMPO_PHONE_NUMBER = "phoneNumber";
    public static final String CAMPO_TYPE_USER = "typeUser";
    public static final String CAMPO_TYPE_MAC_ARDUINO = "macArduino";
    public static final String CAMPO_TYPE_PROVIDER = "provider";
    public static final String CAMPO_REGISTRATION_TOKENS = "registrationTokens";

    private String uid;
    private String email;
    private String name;
    private String photoUrl;
    private String phoneNumber;
    private String typeUser;
    private String macArduino;
    private String provider;
    private ArrayList<String> registrationTokens;


    public Usuario() {

    }

    public Usuario(String uid, String email, String name, String photoUrl, String phoneNumber, String typeUser, String macArduino, String provider, ArrayList<String> registrationTokens) {
        this.uid = uid;
        this.email = email;
        this.name = name;
        this.photoUrl = photoUrl;
        this.phoneNumber = phoneNumber;
        this.typeUser = typeUser;
        this.macArduino = macArduino;
        this.provider = provider;
        this.registrationTokens = registrationTokens;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getTypeUser() {
        return typeUser;
    }

    public void setTypeUser(String typeUser) {
        this.typeUser = typeUser;
    }

    public String getMacArduino() {
        return macArduino;
    }

    public void setMacArduino(String macArduino) {
        this.macArduino = macArduino;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public ArrayList<String> getRegistrationTokens() {
        return registrationTokens;
    }

    public void setRegistrationTokens(ArrayList<String> registrationTokens) {
        this.registrationTokens = registrationTokens;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "uid='" + uid + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", photoUrl='" + photoUrl + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", typeUser='" + typeUser + '\'' +
                ", macArduino='" + macArduino + '\'' +
                ", provider='" + provider + '\'' +
                ", registrationTokens=" + registrationTokens +
                '}';
    }
}