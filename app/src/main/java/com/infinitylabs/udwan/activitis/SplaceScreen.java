package com.infinitylabs.udwan.activitis;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.infinitylabs.udwan.LoginScreen;
import com.infinitylabs.udwan.NetworkClient.NetworkCall;
import com.infinitylabs.udwan.R;
import com.infinitylabs.udwan.model.Login.LoginResponse;
import com.infinitylabs.udwan.utils.Constant;
import com.infinitylabs.udwan.utils.Utils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplaceScreen extends AppCompatActivity implements View.OnClickListener {

    ProgressBar progressBar2;
    LinearLayout error_layout;
    RelativeLayout maine_layout;
    TextView retry_button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splace);
        init();
        SPlaceAnimation();
    }

    public void init(){
        progressBar2 = findViewById(R.id.progressBar2);
        error_layout = findViewById(R.id.error);
        maine_layout =findViewById(R.id.main_layout);
        retry_button =findViewById(R.id.retry_button);
        retry_button.setOnClickListener(this);
    }

    public void CheckedUser(){
          progressBar2.setVisibility(View.GONE);
        if(Utils.checkInternetConnection(this)) {

            if (Utils.INTANCE.IsLoggedIN(this)) {
                Intent i = new Intent(SplaceScreen.this, Dashboard.class);
                startActivity(i);
                finish();
            } else {
                Intent i = new Intent(SplaceScreen.this, LoginScreen.class);
                startActivity(i);
                finish();

            }
        }else{
            maine_layout.setVisibility(View.GONE);
            error_layout.setVisibility(View.VISIBLE);
        }
    }

    private void CallLogin() {
        progressBar2.setVisibility(View.VISIBLE);
        Call<LoginResponse> callapi = NetworkCall.hitNetwork(this).Login(Utils.INTANCE.getRequest(this));
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

    public void RetryDialog(Context context) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("ERROR !!");
        builder.setMessage("Sorry there was an error getting data from server");
        builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                CallLogin();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                finish();
            }
        });
        final AlertDialog dialog = builder.create();
        dialog.show();

    }
private void SPlaceAnimation(){
        error_layout.setVisibility(View.GONE);
        maine_layout.setVisibility(View.VISIBLE);
        progressBar2.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {

        @Override
        public void run() {
            CheckedUser();
        }
    }, 3000);
}
    @Override
    public void onClick(View v) {
        if(v == retry_button){
            SPlaceAnimation();
        }
    }
}
