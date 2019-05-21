package com.example.sdwan.model.Login;

import java.io.Serializable;

public class LoginResponse implements Serializable {

    private String result;
    private String message;
    private String responseCode;
    private LoginResponseBody responseBody;

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

    public LoginResponseBody getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(LoginResponseBody responseBody) {
        this.responseBody = responseBody;
    }


}
