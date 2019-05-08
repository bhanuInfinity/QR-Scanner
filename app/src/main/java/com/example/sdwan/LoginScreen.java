package com.example.sdwan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sdwan.activitis.Dashboard;
import com.example.sdwan.utils.Utils;

import com.example.sdwan.Ui.Activity.ScannerActivity;

public class LoginScreen extends AppCompatActivity implements View.OnClickListener {

    EditText user_name;
    EditText password;
    Button submit;

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
    }

    @Override
    public void onClick(View v) {

        if (user_name.getText().toString().trim().length() > 0 && password.getText().toString().trim().length() > 0) {


            if (Utils.INTANCE.ValidateLogin(user_name.getText().toString().trim(), password.getText().toString().trim())) {
                startActivity(new Intent(this, Dashboard.class));
            } else {
                Toast.makeText(this, "Please enter correct login detail", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Please enter correct login detail", Toast.LENGTH_SHORT).show();
        }
    }

}
