package com.example.sdwan.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.location.Location;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.example.sdwan.R;
import com.example.sdwan.model.Login.LoginRequest;

import java.text.DateFormat;
import java.util.Date;

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

    public static void SaveCredential(Context context, String username, String password, boolean islogin) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.USER_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constant.USERNAME, username);
        editor.putString(Constant.PASSWORD, password);
        editor.putBoolean(Constant.IS_LOGGED, islogin);
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

    public static LoginRequest getRequest(Context context) {
        LoginRequest loginRequest = new LoginRequest();
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.USER_PREF_NAME, Context.MODE_PRIVATE);
        loginRequest.setUsername(sharedPreferences.getString(Constant.USERNAME, ""));
        loginRequest.setPassword(sharedPreferences.getString(Constant.PASSWORD, ""));
        return loginRequest;

    }



}
