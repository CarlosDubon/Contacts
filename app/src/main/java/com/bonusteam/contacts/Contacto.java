package com.bonusteam.contacts;

public class Contacto {
    private String name;
    private String number;
    private  String email;
    private String birth;

    public Contacto() {
    }

    public Contacto(String name, String number, String email, String birth) {
        this.name = name;
        this.number = number;
        this.email = email;
        this.birth = birth;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public String getEmail() {
        return email;
    }

    public String getBirth() {
        return birth;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }
}
