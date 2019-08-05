package com.infinitylabs.udwan;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.infinitylabs.udwan.NetworkClient.NetworkCall;
import com.infinitylabs.udwan.activitis.Dashboard;
import com.infinitylabs.udwan.model.Login.LoginRequest;
import com.infinitylabs.udwan.model.Login.LoginResponse;
import com.infinitylabs.udwan.utils.Constant;
import com.infinitylabs.udwan.utils.Utils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginScreen extends AppCompatActivity implements View.OnClickListener {

    EditText user_name;
    EditText password;
    Button submit;
    ProgressDialog progressDialog;
    TextView  term_and_condition;
RelativeLayout main_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        user_name = findViewById(R.id.user_name);
        password = findViewById(R.id.password);
        term_and_condition = findViewById(R.id.term_and_condition);
        submit = findViewById(R.id.submit);
        submit.setOnClickListener(this);
        main_layout = findViewById(R.id.main_layout);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait..");
        term_and_condition.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if(v==term_and_condition){
           Utils.term_And_Condition(this);
        }

        if( v == submit) {
            if (user_name.getText().toString().trim().length() > 0 && password.getText().toString().trim().length() > 0) {
                CallLogin(user_name.getText().toString(), password.getText().toString());
            } else {
                Toast.makeText(this, "Please enter correct login detail", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void CallLogin(final String username, String Pass) {
        showProgressDialog();
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(username);
        loginRequest.setPassword(Pass);
        Call<LoginResponse> callapi = NetworkCall.hitNetwork(this).Login(loginRequest);
        callapi.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                hideProgressDialog();
                if (response.code() == 200) {
                    LoginResponse res = response.body();
                    if (res.getResult() != null && res.getResult().equalsIgnoreCase("Success")) {
                        Utils.INTANCE.SaveCredential(LoginScreen.this,user_name.getText().toString(),password.getText().toString(),
                                true,res.getToken(),res.getUser().getName());
                        OpenActivity(res, true);
                    } else {
                        OpenActivity(res, false);
                    }
                } else {
                    Toast.makeText(LoginScreen.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {

                Log.e("error", "error");
                hideProgressDialog();
                snackBar();

            }
        });

    }

    private void OpenActivity(LoginResponse loginResponse, boolean flag) {
        if (flag) {
            Intent i = new Intent(LoginScreen.this, Dashboard.class);
            startActivity(i);
            finish();
        } else {
            Toast.makeText(LoginScreen.this, "Please enter Valid Credential", Toast.LENGTH_SHORT).show();
        }

    }

    private void showProgressDialog() {
        if (progressDialog != null) {
            progressDialog.show();
        }
    }

    private void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private void snackBar(){
        Snackbar snackbar = Snackbar
                .make(main_layout, "Oops! no internet connection!", Snackbar.LENGTH_INDEFINITE)
                .setAction("Dissmiss", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
        snackbar.show();
    }
}
