package com.infinitylabs.udwan.model.Login;

import java.io.Serializable;

public class LoginResponse implements Serializable {


    /**
     * result : Success
     * message : Successfully Loggedin!
     * token : eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6ImFkbWluIiwicGFzc3dvcmQiOiIkMmEkMDgkamlHQ3NCM2xKeW8zbksxb3ZFRlhCT0VML3c5amJMTXRFbVpXTE5JY3hzTTlQSVlpeDNrSG0iLCJ1dWlkIjoiNmRiMGE4bmp5anpoMjk1IiwibmFtZSI6IkFkbWluIiwiaXNBY3RpdmF0ZWQiOiIxIiwiaXNEZWxldGVkIjoiMCIsImNyZWF0ZWRBdCI6IjIwMTktMDctMjZUMTA6NDk6MTcuMDAwWiIsInVwZGF0ZWRBdCI6IjIwMTktMDctMjZUMTA6NDk6MTcuMDAwWiIsInJpZCI6IjZkYjBhOG5qeWp6aDI1OCIsImNpZCI6bnVsbCwiaWF0IjoxNTY0NTczMTMzLCJleHAiOjE1NjUxNzc5MzN9.IPFVj5RbCHfMehSnOPmBVrsyvLjQkSO5WB-muFeleew
     * user : {"rid":"6db0a8njyjzh258","name":"Admin"}
     */

    private String result;
    private String message;
    private String token;
    private UserBean user;

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

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public static class UserBean {
        /**
         * rid : 6db0a8njyjzh258
         * name : Admin
         */

        private String rid;
        private String name;

        public String getRid() {
            return rid;
        }

        public void setRid(String rid) {
            this.rid = rid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
