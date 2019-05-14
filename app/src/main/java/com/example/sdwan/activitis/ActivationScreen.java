package com.example.sdwan.activitis;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sdwan.NetworkClient.NetworkCall;
import com.example.sdwan.R;
import com.example.sdwan.model.CpeActivationRequest;
import com.example.sdwan.model.CpeResponse;
import com.example.sdwan.utils.Constant;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivationScreen extends AppCompatActivity implements View.OnClickListener {

    TextView activation_key;
    TextView choose_and_selected_text;
    String serialNumber = "";
    String isExist;
    TextView activate_cpe;
    CpeResponse response;
    RelativeLayout layout_100;
    TextView subcription_plan;
    TextView et_100;
    TextView subcription_price;

    RelativeLayout layout_200;
    TextView subcription_plan_200;
    TextView et_200;
    TextView subcription_price_200;

    RelativeLayout layout_250;
    TextView subcription_plan_250;
    TextView et_250;
    TextView subcription_price_250;

    boolean is100Selected = false;
    boolean is200Selected = false;
    boolean is250selected = false;
    String selectedPlan = "";
    ProgressDialog progressDialog;
    boolean isUpdate =false;


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        serialNumber = intent.getExtras().getString("ur_value");
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activiation);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            serialNumber = bundle.getString("ur_value");
            isExist = bundle.getString("isExist");
            response = (CpeResponse) getIntent().getSerializableExtra("my_response");
        }
        init();
    }

    private void init() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait..");
        activation_key = findViewById(R.id.activation_key);
        choose_and_selected_text = findViewById(R.id.choose_and_selected_text);
        activate_cpe = findViewById(R.id.activate_cpe);

        layout_100 = findViewById(R.id.layout_100);
        subcription_plan = findViewById(R.id.subcription_plan);
        et_100 = findViewById(R.id.et_100);
        subcription_price = findViewById(R.id.subcription_price);

        layout_200 = findViewById(R.id.layout_200);
        subcription_plan_200 = findViewById(R.id.subcription_plan_200);
        et_200 = findViewById(R.id.et_200);
        subcription_price_200 = findViewById(R.id.subcription_price_200);

        layout_250 = findViewById(R.id.layout_250);
        subcription_plan_250 = findViewById(R.id.subcription_plan_250);
        et_250 = findViewById(R.id.et_250);
        subcription_price_250 = findViewById(R.id.subcription_price_250);


        activate_cpe.setOnClickListener(this);
        layout_100.setOnClickListener(this);
        layout_200.setOnClickListener(this);
        layout_250.setOnClickListener(this);
        if (isExist.equalsIgnoreCase("false")) {
            choose_and_selected_text.setText("Please choose you plan");
        } else {
            choose_and_selected_text.setText("Selected Plan");
        }
        activation_key.setText(serialNumber);

        if(isExist.equalsIgnoreCase("true")){
            selectedPlan = response.getResponseBody().getLicensekey();
            PlanSelectable(selectedPlan);
            layout_200.setEnabled(false);
            layout_250.setEnabled(false);
            layout_100.setEnabled(false);

            activate_cpe.setText("Change Plan");

        } else{

        }
    }

    @Override
    public void onClick(View v) {
        if (v == activate_cpe) {
            if(activate_cpe.getText().toString().equalsIgnoreCase("change Plan")){
                layout_200.setEnabled(true);
                layout_250.setEnabled(true);
                layout_100.setEnabled(true);
                activate_cpe.setText("Activate");
                Toast.makeText(this,"You can Select any plan",Toast.LENGTH_SHORT).show();
                choose_and_selected_text.setText("Please choose you plan");
                isUpdate =true;
            }else
            {
                if(isUpdate){
                    CallupdateApi(selectedPlan);
                }else{
                    if(selectedPlan.toString().trim().length()>0) {
                        CallApi(selectedPlan);
                    }else{
                        Toast.makeText(this,"Please Select Plan",Toast.LENGTH_SHORT).show();
                    }
                }

            }

        }
        if (v == layout_100) {
            selectedPlan = "01-20M-ET-LT";
            PlanSelectable(selectedPlan);
        }

        if (v == layout_200) {
            selectedPlan = "03-20M-ET-LT";
            PlanSelectable(selectedPlan);
        }
        if (v == layout_250) {
            selectedPlan = "04-20M-ET-W";
            PlanSelectable(selectedPlan);
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

    public void PlanSelectable(String value) {
        if (value != null) {

        if (value.equalsIgnoreCase("01-20M-ET-LT")) {
            // Selectable
            layout_100.setBackgroundColor(getResources().getColor(R.color.color_FFC107));
            subcription_plan.setTextColor(getResources().getColor(R.color.white));
            et_100.setTextColor(getResources().getColor(R.color.white));
            subcription_price.setTextColor(getResources().getColor(R.color.color_black));
            subcription_price.setBackgroundColor(getResources().getColor(R.color.white));

            layout_200.setBackgroundColor(getResources().getColor(R.color.white));
            subcription_plan_200.setTextColor(getResources().getColor(R.color.btn_color));
            et_200.setTextColor(getResources().getColor(R.color.color_black));
            subcription_price_200.setTextColor(getResources().getColor(R.color.white));
            subcription_price_200.setBackgroundColor(getResources().getColor(R.color.color_FFC107));


            layout_250.setBackgroundColor(getResources().getColor(R.color.white));
            subcription_plan_250.setTextColor(getResources().getColor(R.color.btn_color));
            et_250.setTextColor(getResources().getColor(R.color.color_black));
            subcription_price_250.setTextColor(getResources().getColor(R.color.white));
            subcription_price_250.setBackgroundColor(getResources().getColor(R.color.color_FFC107));


        } else if (value.equalsIgnoreCase("03-20M-ET-LT")) {
            layout_200.setBackgroundColor(getResources().getColor(R.color.color_FFC107));
            subcription_plan_200.setTextColor(getResources().getColor(R.color.white));
            et_200.setTextColor(getResources().getColor(R.color.white));
            subcription_price_200.setTextColor(getResources().getColor(R.color.color_black));
            subcription_price_200.setBackgroundColor(getResources().getColor(R.color.white));

            layout_100.setBackgroundColor(getResources().getColor(R.color.white));
            subcription_plan.setTextColor(getResources().getColor(R.color.btn_color));
            et_100.setTextColor(getResources().getColor(R.color.color_black));
            subcription_price.setTextColor(getResources().getColor(R.color.white));
            subcription_price.setBackgroundColor(getResources().getColor(R.color.color_FFC107));


            layout_250.setBackgroundColor(getResources().getColor(R.color.white));
            subcription_plan_250.setTextColor(getResources().getColor(R.color.btn_color));
            et_250.setTextColor(getResources().getColor(R.color.color_black));
            subcription_price_250.setTextColor(getResources().getColor(R.color.white));
            subcription_price_250.setBackgroundColor(getResources().getColor(R.color.color_FFC107));


        } else {
            layout_250.setBackgroundColor(getResources().getColor(R.color.color_FFC107));
            subcription_plan_250.setTextColor(getResources().getColor(R.color.white));
            et_250.setTextColor(getResources().getColor(R.color.white));
            subcription_price_250.setTextColor(getResources().getColor(R.color.color_black));
            subcription_price_250.setBackgroundColor(getResources().getColor(R.color.white));

            layout_200.setBackgroundColor(getResources().getColor(R.color.white));
            subcription_plan_200.setTextColor(getResources().getColor(R.color.btn_color));
            et_200.setTextColor(getResources().getColor(R.color.color_black));
            subcription_price_200.setTextColor(getResources().getColor(R.color.white));
            subcription_price_200.setBackgroundColor(getResources().getColor(R.color.color_FFC107));

            layout_100.setBackgroundColor(getResources().getColor(R.color.white));
            subcription_plan.setTextColor(getResources().getColor(R.color.btn_color));
            et_100.setTextColor(getResources().getColor(R.color.color_black));
            subcription_price.setTextColor(getResources().getColor(R.color.white));
            subcription_price.setBackgroundColor(getResources().getColor(R.color.color_FFC107));
        }
    }
    }

    private void CallApi(String selectedPlan) {

        CpeActivationRequest cpeActivationRequest = new CpeActivationRequest();
        cpeActivationRequest.setSerialnumber(serialNumber);
        cpeActivationRequest.setLicensekey(selectedPlan);
        cpeActivationRequest.setCustid(Constant.USERNAME);
        cpeActivationRequest.setLatitude("");
        cpeActivationRequest.setLongitude("");
        showProgressDialog();
        Call<CpeResponse> callapi = NetworkCall.hitNetwork().PostActivation(cpeActivationRequest);
        callapi.enqueue(new Callback<CpeResponse>() {
            @Override
            public void onResponse(Call<CpeResponse> call, Response<CpeResponse> response) {
                if (response.code() == 200) {
                    CpeResponse res = response.body();
                }
                hideProgressDialog();
                finish();
            }

            @Override
            public void onFailure(Call<CpeResponse> call, Throwable t) {
                Toast.makeText(ActivationScreen.this,"Something went Wrong",Toast.LENGTH_SHORT).show();
                Log.e("error", "error");
            }
        });
    }


    private void CallupdateApi(String selectedPlan) {

        CpeActivationRequest cpeActivationRequest = new CpeActivationRequest();
        cpeActivationRequest.setSerialnumber(serialNumber);
        cpeActivationRequest.setLicensekey(selectedPlan);
        cpeActivationRequest.setCustid(Constant.USERNAME);
        cpeActivationRequest.setLatitude("");
        cpeActivationRequest.setLongitude("");
        showProgressDialog();
        Call<CpeResponse> callapi = NetworkCall.hitNetwork().updateActivation(cpeActivationRequest);
        callapi.enqueue(new Callback<CpeResponse>() {
            @Override
            public void onResponse(Call<CpeResponse> call, Response<CpeResponse> response) {
                if (response.code() == 200) {
                    CpeResponse res = response.body();
                }
                hideProgressDialog();
                finish();
            }

            @Override
            public void onFailure(Call<CpeResponse> call, Throwable t) {
                Toast.makeText(ActivationScreen.this,"Something went Wrong",Toast.LENGTH_SHORT).show();
                Log.e("error", "error");
            }
        });
    }
}
