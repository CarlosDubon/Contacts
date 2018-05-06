package com.bonusteam.contacts;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.ArrayList;

public class Contacto implements Parcelable,Comparable<Contacto> {

    private String imagen =null;
    private String name = "No Name";
    private String lastname = " ";
    private ArrayList<String> numbers = new ArrayList<>();
    private String email=" -No Available- ";
    private String address=" -No Available- ";
    private String birth="-No Available- ";
    private boolean isFavorite=false;

    public static String TYPE = "contacto";

    public Contacto() {
    }

    protected Contacto(Parcel in) {
        imagen = in.readString();
        name = in.readString();
        lastname = in.readString();
        numbers = in.createStringArrayList();
        email = in.readString();
        address = in.readString();
        birth = in.readString();
        isFavorite = in.readByte() != 0;
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

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setNumbers(String numbers) {
        this.numbers.add(numbers);
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

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public String getImagen() {
        return imagen;
    }

    public String getName() {
        return name;
    }

    public String getLastname() {
        return lastname;
    }

    public ArrayList<String> getNumbers() {
        return numbers;
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

    public boolean isFavorite() {
        return isFavorite;
    }

    @Override
    public int compareTo(@NonNull Contacto o) {
        return this.getName().compareTo(o.getName());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imagen);
        dest.writeString(name);
        dest.writeString(lastname);
        dest.writeStringList(numbers);
        dest.writeString(email);
        dest.writeString(address);
        dest.writeString(birth);
        dest.writeByte((byte) (isFavorite ? 1 : 0));
    }
}
