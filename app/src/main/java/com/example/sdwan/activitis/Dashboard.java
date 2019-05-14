package com.example.sdwan.activitis;

import android.app.ProgressDialog;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Dashboard extends AppCompatActivity implements View.OnClickListener {

    TextView subcription_price;
    TextView subcription_price_200;
    TextView subcription_price_250;
    TextView scan_cpe;
    private IntentIntegrator qrScan;
    ProgressDialog progressDialog;


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

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait..");
    }

    @Override
    public void onClick(View v) {
        if (v == scan_cpe) {
            qrScan.initiateScan();
            //CallApi("123334444");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {
                CallApi(result.getContents().toString());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void CallApi (String val) {
       final String key = val;
       showProgressDialog();
        Call<CpeResponse> callapi = NetworkCall.hitNetwork().getCpeData(val);
        callapi.enqueue(new Callback<CpeResponse>() {
            @Override
            public void onResponse(Call<CpeResponse> call, Response<CpeResponse> response) {
                if(response.code()==200) {
                    CpeResponse res = response.body();
                    if (res.getResponseCode() != null && res.getResponseCode().equalsIgnoreCase("ERR02")) {
                        Log.e("ERR02", "not exist");
                        OpenActivity(res,key,"false");
                    } else {
                        OpenActivity(res,key,"true");
                    }
                }

                hideProgressDialog();

            }

            @Override
            public void onFailure(Call<CpeResponse> call, Throwable t) {
                hideProgressDialog();
                Log.e("error", "error");
            }
        });

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

    public void OpenActivity(CpeResponse cpeResponse,String key,String isExist){
        Intent i = new Intent(Dashboard.this, ActivationScreen.class);
        Bundle bundle =new Bundle();
        bundle.putString("ur_value",String.valueOf(key));
        bundle.putString("isExist", isExist);
        bundle.putSerializable("my_response", cpeResponse);
        i.putExtras(bundle);
        startActivity(i);
    }


}
