package com.example.sdwan.activitis;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sdwan.NetworkClient.NetworkCall;
import com.example.sdwan.R;
import com.example.sdwan.Ui.Activity.AnyActivity;
import com.example.sdwan.model.CpeResponse;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Dashboard extends AppCompatActivity implements View.OnClickListener {

    TextView subcription_price;
    TextView subcription_price_200;
    TextView subcription_price_250;
    TextView scan_cpe;
    private IntentIntegrator qrScan;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboad);
        init();
    }

    private void init() {
        subcription_price = findViewById(R.id.subcription_price);
        subcription_price_200 = findViewById(R.id.subcription_price_200);
        subcription_price_250 = findViewById(R.id.subcription_price_250);
        scan_cpe = findViewById(R.id.scan_cpe);
        scan_cpe.setOnClickListener(this);
        qrScan = new IntentIntegrator(this);
        qrScan.setCaptureActivity(AnyActivity.class);
        qrScan.setOrientationLocked(false);
    }

    @Override
    public void onClick(View v) {
        if (v == scan_cpe) {
            qrScan.initiateScan();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {
                //if qr contains data
                //converting the data to json
                //setting values to textviews
                CallApi(result.getContents().toString());


            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void CallApi(final String val) {
        Call<CpeResponse> callapi = NetworkCall.hitNetwork().getCpeData(val);
        callapi.enqueue(new Callback<CpeResponse>() {
            @Override
            public void onResponse(Call<CpeResponse> call, Response<CpeResponse> response) {
                if(response.code()==200) {
                    CpeResponse res = response.body();
                    if (res.getResponseCode() != null && res.getResponseCode().equalsIgnoreCase("ERR02")) {
                        Log.e("ERR02", "not exist");
                        Intent i = new Intent(Dashboard.this, ActivationScreen.class);
                        i.putExtra("val", val.toString());
                        i.putExtra("response", res);
                        i.putExtra("isExist", false);
                        startActivity(i);
                    } else {
                        Intent i = new Intent(Dashboard.this, ActivationScreen.class);
                        i.putExtra("val", val);
                        i.putExtra("response", res);
                        i.putExtra("isExist", false);
                        startActivity(i);
                    }
                }
            }

            @Override
            public void onFailure(Call<CpeResponse> call, Throwable t) {
                Log.e("error", "error");
            }
        });
    }
}
