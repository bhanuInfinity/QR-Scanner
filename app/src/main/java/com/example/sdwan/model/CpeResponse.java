package com.example.sdwan.model;

import android.os.Parcel;
import android.os.Parcelable;

public class CpeResponse implements Parcelable {

    private String responseCode;
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

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

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

    public static Creator<CpeResponse> getCREATOR() {
        return CREATOR;
    }

    protected CpeResponse(Parcel in) {
        responseCode = in.readString();
        serialnumber = in.readString();
        licensekey = in.readString();
        latitude = in.readString();
        longitude = in.readString();
        custid = in.readString();
        activationdate = in.readString();
        licenseduration = in.readInt();
        speed = in.readString();
        eth = in.readByte() != 0;
        lte = in.readByte() != 0;
        wifi = in.readByte() != 0;
    }

    public static final Creator<CpeResponse> CREATOR = new Creator<CpeResponse>() {
        @Override
        public CpeResponse createFromParcel(Parcel in) {
            return new CpeResponse(in);
        }

        @Override
        public CpeResponse[] newArray(int size) {
            return new CpeResponse[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(serialnumber);
        dest.writeString(licensekey);
        dest.writeString(latitude);
        dest.writeString(longitude);
        dest.writeString(custid);
        dest.writeString(activationdate);
        dest.writeInt(licenseduration);
        dest.writeString(speed);
        dest.writeByte((byte) (eth ? 1 : 0));
        dest.writeByte((byte) (lte ? 1 : 0));
        dest.writeByte((byte) (wifi ? 1 : 0));
    }
}
