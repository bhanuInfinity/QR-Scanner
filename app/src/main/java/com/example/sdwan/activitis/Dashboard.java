package com.example.sdwan.activitis;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.sdwan.R;

public class Dashboard extends AppCompatActivity implements View.OnClickListener {

    TextView subcription_price;
    TextView subcription_price_200;
    TextView subcription_price_250;
    TextView scan_cpe;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboad);
        init();
    }
    private void init(){
        subcription_price = findViewById(R.id.subcription_price);
        subcription_price_200 = findViewById(R.id.subcription_price_200);
        subcription_price_250 = findViewById(R.id.subcription_price_250);
        scan_cpe = findViewById(R.id.scan_cpe);
        scan_cpe.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == scan_cpe){

        }
    }
}
