package com.example.sdwan.utils;

public enum Utils {
    INTANCE;

    public static final boolean ValidateLogin(String useraname,String passowrd){
        if(useraname.trim().equalsIgnoreCase(Constant.USERNAME)&&passowrd.trim().equalsIgnoreCase(Constant.PASSWORD)){
            return true;
        }else{
            return false;
        }

    }
}
