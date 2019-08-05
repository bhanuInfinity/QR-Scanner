package com.infinitylabs.udwan.activitis;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.infinitylabs.udwan.LoginScreen;
import com.infinitylabs.udwan.NetworkClient.NetworkCall;
import com.infinitylabs.udwan.R;
import com.infinitylabs.udwan.adaptor.CpePlanAdaptor;
import com.infinitylabs.udwan.model.dashboard.LicenseResponse;
import com.infinitylabs.udwan.model.dashboard.PlanData;
import com.infinitylabs.udwan.utils.Utils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LicenseListScreen extends AppCompatActivity implements View.OnClickListener {
    private CpePlanAdaptor  mAdapter;
    private Toolbar toolbar;
    private ArrayList<PlanData> planlist;
    ProgressDialog progressDialog;
    private RecyclerView recyclerView;
    LinearLayout error_layout;
    TextView retry_btn;
    ImageView back;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_licenselist);
        init();
        if (Utils.checkInternetConnection(this)) {
            loadingShimmerPlanData();
            CallApiforLicenses();
        } else {
            showInternetError();
        }

    }

    private void init(){
        Toolbar toolbar =  findViewById(R.id.toolbar);
        toolbar.setTitle("Current Licenses");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        recyclerView = findViewById(R.id.recyclerview);
        error_layout =findViewById(R.id.error);
        retry_btn =findViewById(R.id.retry_button);

        retry_btn.setOnClickListener(this);
    }
    private void CallApiforLicenses() {
        Call<LicenseResponse> callapi = NetworkCall.hitNetwork(this).getAllLicenses(Utils.getHeaderMap(this));
        callapi.enqueue(new Callback<LicenseResponse>() {
            @Override
            public void onResponse(Call<LicenseResponse> call, Response<LicenseResponse> response) {
                if (response.code() == 200) {
                    LicenseResponse res = response.body();

                    if (res.getData() != null && res.getData().size() > 0) {
                        Log.e("ERR02", "not exist");
                        setAdaptor(res);
                    } else {
                        Toast.makeText(getApplicationContext(), "No License attached ", Toast.LENGTH_SHORT).show();
                    }
                    hideInternetError();
                } else if (response.code() == 403) {
                    Utils.INTANCE.Logout(getApplicationContext());
                    Intent newIntent = new Intent(LicenseListScreen.this, LoginScreen.class);
                    newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(newIntent);
                }else {
                   showInternetError();
                }


            }

            @Override
            public void onFailure(Call<LicenseResponse> call, Throwable t) {
                 showInternetError();
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
    private  void setAdaptor(LicenseResponse licenseResponse){
        planlist = licenseResponse.getData();
        mAdapter.setValue(planlist);
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    public void showInternetError() {
        if (error_layout != null) {
            recyclerView.setVisibility(View.GONE);
            error_layout.setVisibility(View.VISIBLE);
        }
    }

    public void hideInternetError() {
        if (error_layout != null) {
            recyclerView.setVisibility(View.VISIBLE);
            error_layout.setVisibility(View.GONE);
        }

    }
    private void loadingShimmerPlanData() {
        planlist = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            planlist.add(new PlanData(0, "",0,"",0,"",false,null,"",2,false));
        }
            mAdapter = new CpePlanAdaptor(LicenseListScreen.this,planlist);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(mAdapter);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    @Override
    public void onClick(View v) {
        if(v  == back){
            finish();
        }
        if(v == retry_btn){
            if (Utils.checkInternetConnection(this)) {
                loadingShimmerPlanData();
                CallApiforLicenses();
            } else {
                showInternetError();
            }
        }
    }
}
