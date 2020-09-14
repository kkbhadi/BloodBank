package com.bloodbankapp.models;

import java.io.Serializable;

public class Doner implements Serializable {
    public Doner() {
        //to send data in recyclerView
    }

    private String name;
    private String donerId;
    private String email;
    private Long phone;
    private String city;
    private String address;
    private String bloodGroup = "";
    private boolean isDoner = false;

    public boolean isDoner() {
        return isDoner;
    }

    public void setDoner(boolean doner) {
        isDoner = doner;
    }

    public String getDonerId() {
        return donerId;
    }

    public void setDonerId(String donerId) {
        this.donerId = donerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getPhone() {
        return phone;
    }

    public void setPhone(Long phone) {
        this.phone = phone;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

}