package com.infinitylabs.udwan.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.preference.PreferenceManager;
import android.util.Log;

import com.infinitylabs.udwan.R;
import com.infinitylabs.udwan.model.Login.LoginRequest;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public enum Utils {
    INTANCE;

    public static final boolean ValidateLogin(String useraname, String passowrd) {
        if (useraname.trim().equalsIgnoreCase(Constant.USERNAME) && passowrd.trim().equalsIgnoreCase(Constant.PASSWORD)) {
            return true;
        } else {
            return false;
        }

    }

    public static final String KEY_REQUESTING_LOCATION_UPDATES = "requesting_locaction_updates";

    /**
     * Returns true if requesting location updates, otherwise returns false.
     *
     * @param context The {@link Context}.
     */
    public static boolean requestingLocationUpdates(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(KEY_REQUESTING_LOCATION_UPDATES, false);
    }

    /**
     * Stores the location updates state in SharedPreferences.
     *
     * @param requestingLocationUpdates The location updates state.
     */
    public static void setRequestingLocationUpdates(Context context, boolean requestingLocationUpdates) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(KEY_REQUESTING_LOCATION_UPDATES, requestingLocationUpdates)
                .apply();
    }

    /**
     * Returns the {@code location} object as a human readable string.
     *
     * @param location The {@link Location}.
     */
    public static String getLocationText(Location location) {
        return location == null ? "Unknown location" :
                "(" + location.getLatitude() + ", " + location.getLongitude() + ")";
    }

    public static String getLocationTitle(Context context) {
        return context.getString(R.string.location_updated,
                DateFormat.getDateTimeInstance().format(new Date()));
    }

    public static void SaveCredential(Context context, String username, String password, boolean islogin,String token) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.USER_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constant.USERNAME, username);
        editor.putString(Constant.PASSWORD, password);
        editor.putBoolean(Constant.IS_LOGGED, islogin);
        editor.putString(Constant.CUST_TOKEN, token);
        editor.commit();
    }

    public static boolean IsLoggedIN(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.USER_PREF_NAME, Context.MODE_PRIVATE);
        if (sharedPreferences.contains(Constant.IS_LOGGED) && sharedPreferences.getBoolean(Constant.IS_LOGGED, false)) {
            return true;
        } else {
            return false;
        }
    }

    public static String getAddressbyLatLon(Context context,double latitude,double longitude){
        Geocoder geocoder;
        List<Address> addresses = null;
        String replaceaddress="";
        geocoder = new Geocoder(context, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException e) {
            e.printStackTrace();
        }

       // String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        if(addresses!=null && addresses.size()>0) {
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();

            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()

            String newaddress = address.replaceAll("Unnamed Road,", "");
            replaceaddress = newaddress.replaceAll("null,", "");
        }else{
           replaceaddress = ""+latitude+"-"+longitude;
        }

        Log.d(Constant.TAG, "getAddress:  address : " + replaceaddress);

        return replaceaddress;
    }
    public static LoginRequest getRequest(Context context) {
        LoginRequest loginRequest = new LoginRequest();
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.USER_PREF_NAME, Context.MODE_PRIVATE);
        loginRequest.setUsername(sharedPreferences.getString(Constant.USERNAME, ""));
        loginRequest.setPassword(sharedPreferences.getString(Constant.PASSWORD, ""));
        return loginRequest;
    }

    public static String getUserToken(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.USER_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(Constant.CUST_TOKEN,"");
    }

    public static void Logout(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.USER_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }

    public static Map<String ,String> getHeaderMap(Context context){
     Map<String,String> map =new HashMap<>();
     map.put(Constant.AUTHORIZATION,"Bearer "+getUserToken(context));
     return map;
    }

}
