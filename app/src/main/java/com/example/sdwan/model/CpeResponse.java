package com.example.sdwan.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class CpeResponse implements Serializable {

    private String result;
    private String message;
    private String responseCode;
    private ResponseBodyres responseBody;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public ResponseBodyres getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(ResponseBodyres responseBody) {
        this.responseBody = responseBody;
    }

    public class ResponseBodyres implements Serializable{
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



}
