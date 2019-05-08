package com.example.sdwan.activitis;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.sdwan.R;
import com.example.sdwan.model.CpeResponse;

public class ActivationScreen extends AppCompatActivity {

    TextView activation_key;
    TextView choose_and_selected_text;
    String serialNumber="";
    boolean isExist=false;
    CpeResponse response;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activiation);
        serialNumber = getIntent().getExtras().getString("val");
        isExist = getIntent().getExtras().getBoolean("isExist",false);
        response = (CpeResponse)getIntent().getParcelableExtra("response");
        init();
    }

    private void init(){
        activation_key = findViewById(R.id.activation_key);
        choose_and_selected_text = findViewById(R.id.choose_and_selected_text);
    }
}
