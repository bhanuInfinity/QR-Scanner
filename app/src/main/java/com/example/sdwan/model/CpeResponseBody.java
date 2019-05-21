package com.example.sdwan.model;

import java.io.Serializable;

public class CpeResponseBody implements Serializable {
    private String serialnumber;
    private String licensekey;
    private String latitude;
    private String longitude;
    private String custid;
    private String activationdate;
    private int licenseduration;
    private String speed;
    private boolean eth;
    private boolean lte;
    private boolean wifi;


    public String getSerialnumber() {
        return serialnumber;
    }

    public void setSerialnumber(String serialnumber) {
        this.serialnumber = serialnumber;
    }

    public String getLicensekey() {
        return licensekey;
    }

    public void setLicensekey(String licensekey) {
        this.licensekey = licensekey;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getCustid() {
        return custid;
    }

    public void setCustid(String custid) {
        this.custid = custid;
    }

    public String getActivationdate() {
        return activationdate;
    }

    public void setActivationdate(String activationdate) {
        this.activationdate = activationdate;
    }

    public int getLicenseduration() {
        return licenseduration;
    }

    public void setLicenseduration(int licenseduration) {
        this.licenseduration = licenseduration;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public boolean isEth() {
        return eth;
    }

    public void setEth(boolean eth) {
        this.eth = eth;
    }

    public boolean isLte() {
        return lte;
    }

    public void setLte(boolean lte) {
        this.lte = lte;
    }

    public boolean isWifi() {
        return wifi;
    }

    public void setWifi(boolean wifi) {
        this.wifi = wifi;
    }
}