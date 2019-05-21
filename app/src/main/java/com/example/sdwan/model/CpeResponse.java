package com.example.sdwan.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class CpeResponse implements Serializable {

    private String result;
    private String message;
    private String responseCode;
    private CpeResponseBody responseBody;

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

    public CpeResponseBody getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(CpeResponseBody responseBody) {
        this.responseBody = responseBody;
    }
}
