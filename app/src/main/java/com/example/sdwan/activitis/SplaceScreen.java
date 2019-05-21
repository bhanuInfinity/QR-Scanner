package com.example.sdwan.activitis;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.CallLog;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.sdwan.LoginScreen;
import com.example.sdwan.NetworkClient.NetworkCall;
import com.example.sdwan.R;
import com.example.sdwan.model.CpeResponse;
import com.example.sdwan.model.Login.LoginResponse;
import com.example.sdwan.utils.Constant;
import com.example.sdwan.utils.Utils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplaceScreen extends AppCompatActivity {

    ProgressBar progressBar2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splace);
        init();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                CheckedUser();
            }
        }, 3000);
    }

    public void init(){
        progressBar2 = findViewById(R.id.progressBar2);
    }

    public void CheckedUser(){
        if(Utils.INTANCE.IsLoggedIN(this)){
            CallLogin();
        }else{
            progressBar2.setVisibility(View.GONE);
            Intent i = new Intent(SplaceScreen.this, LoginScreen.class);
            startActivity(i);
            finish();

        }
    }

    private void CallLogin() {
        progressBar2.setVisibility(View.VISIBLE);
        Call<LoginResponse> callapi = NetworkCall.hitNetwork().Login(Utils.INTANCE.getRequest(this));
        callapi.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                progressBar2.setVisibility(View.GONE);
                if(response.code()==200) {
                    LoginResponse res = response.body();
                    if (res.getResult() != null && res.getResult().equalsIgnoreCase("Success")) {
                       OpenActivity(res,true);
                    } else {
                       OpenActivity(res,false);
                    }
                }else{
                   RetryDialog(SplaceScreen.this);
                }

            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                progressBar2.setVisibility(View.GONE);
                Log.e("error", "error");
                RetryDialog(SplaceScreen.this);
            }
        });

    }

    private void OpenActivity(LoginResponse loginResponse,boolean logged){
        if(logged){
            Intent i = new Intent(SplaceScreen.this, Dashboard.class);
            i.putExtra(Constant.LOGINRESPONSE,loginResponse);
            startActivity(i);
            finish();
        }else{

        }

    }

    public  void RetryDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("ERROR !!");
        builder.setMessage("Sorry there was an error getting data from the Server.\nPlease try again!");
        AlertDialog dialog = builder.create();
        builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                CallLogin();
            }
        });

        dialog.show();


    }
}
