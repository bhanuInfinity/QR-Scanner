package com.infinitylabs.udwan.model.Login;

import java.io.Serializable;

public class LoginResponse implements Serializable {

    private String result;
    private String message;
    private String token;


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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
