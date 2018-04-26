package com.bonusteam.contacts;

public class Contacto {

    private String imagen;
    private String name;
    private String lastname;
    private String number;
    private String email;
    private String address;
    private String birth;

    public Contacto(String imagen, String name, String lastname, String number, String email, String address, String birth) {
        this.imagen = imagen;
        this.name = name;
        this.lastname = lastname;
        this.number = number;
        this.email = email;
        this.address = address;
        this.birth = birth;
    }

    public void setImagen(String imagen) {
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

    public String getImagen() {
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
