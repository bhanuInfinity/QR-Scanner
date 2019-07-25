package com.infinitylabs.udwan;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        user_name = findViewById(R.id.user_name);
        password = findViewById(R.id.password);
        submit = findViewById(R.id.submit);
        submit.setOnClickListener(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait..");
    }

    @Override
    public void onClick(View v) {

        if (user_name.getText().toString().trim().length() > 0 && password.getText().toString().trim().length() > 0) {
            CallLogin(user_name.getText().toString(), password.getText().toString());
        } else {
            Toast.makeText(this, "Please enter correct login detail", Toast.LENGTH_SHORT).show();
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
                                true,res.getToken());
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
                Toast.makeText(LoginScreen.this, "Something went wrong", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void OpenActivity(LoginResponse loginResponse, boolean flag) {
        if (flag) {
            Intent i = new Intent(LoginScreen.this, Dashboard.class);
            i.putExtra(Constant.LOGINRESPONSE, loginResponse);
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
}
