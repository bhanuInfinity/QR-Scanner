package com.example.sdwan;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

    private void init(){
        user_name = findViewById(R.id.user_name);
        password = findViewById(R.id.password);
        submit = findViewById(R.id.submit);
    }

    @Override
    public void onClick(View v) {

    }
}
