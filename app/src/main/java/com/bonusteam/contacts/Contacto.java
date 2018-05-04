package com.bonusteam.contacts;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.io.Serializable;

public class Contacto implements Parcelable,Comparable<Contacto> {

    private Bitmap imagen =null;
    private String name = "No Name";
    private String lastname = " ";
    private String number ="-No Aviable-";
    private String email=" -No Available- ";
    private String address=" -No Available- ";
    private String birth="-No Available- ";
    private boolean isFavorite=false;

    public static String TYPE = "contacto";

    public Contacto() {
    }

    protected Contacto(Parcel in) {
        imagen = in.readParcelable(Bitmap.class.getClassLoader());
        name = in.readString();
        lastname = in.readString();
        number = in.readString();
        email = in.readString();
        address = in.readString();
        birth = in.readString();
        isFavorite = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(imagen, flags);
        dest.writeString(name);
        dest.writeString(lastname);
        dest.writeString(number);
        dest.writeString(email);
        dest.writeString(address);
        dest.writeString(birth);
        dest.writeByte((byte) (isFavorite ? 1 : 0));
    }
    public static final Creator<Contacto> CREATOR = new Creator<Contacto>() {
        @Override
        public Contacto createFromParcel(Parcel in) {
            return new Contacto(in);
        }

        @Override
        public Contacto[] newArray(int size) {
            return new Contacto[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }



    @Override
    public int compareTo(@NonNull Contacto o) {
        return this.getName().compareTo(o.getName());
    }
}
