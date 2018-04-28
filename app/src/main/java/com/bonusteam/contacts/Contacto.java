package com.bonusteam.contacts;

import android.graphics.Bitmap;

public class Contacto {

    private Bitmap imagen ;
    private String name = " ";
    private String lastname = " ";
    private String number =" ";
    private String email=" ";
    private String address=" ";
    private String birth=" ";
    private boolean isFavorite=false;

    public Contacto() {
    }

    public Contacto(Bitmap imagen, String name, String lastname, String number, String email, String address, String birth, boolean isFavorite) {
        this.imagen = imagen;
        this.name = name;
        this.lastname = lastname;
        this.number = number;
        this.email = email;
        this.address = address;
        this.birth = birth;
        this.isFavorite = isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setImagen(Bitmap imagen) {
        this.imagen = imagen;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public Bitmap getImagen() {
        return imagen;
    }

    public String getName() {
        return name;
    }

    public String getLastname() {
        return lastname;
    }

    public String getNumber() {
        return number;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getBirth() {
        return birth;
    }
}
