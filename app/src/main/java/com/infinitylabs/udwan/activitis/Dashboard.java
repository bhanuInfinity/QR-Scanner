package com.infinitylabs.udwan.activitis;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.infinitylabs.udwan.LoginScreen;
import com.infinitylabs.udwan.NetworkClient.NetworkCall;
import com.infinitylabs.udwan.R;
import com.infinitylabs.udwan.Ui.Activity.AnyActivity;
import com.infinitylabs.udwan.adaptor.CpePlanAdaptor;
import com.infinitylabs.udwan.model.CpeResponse;

import com.infinitylabs.udwan.model.dashboard.PlanData;
import com.infinitylabs.udwan.utils.Utils;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Dashboard extends AppCompatActivity implements View.OnClickListener {

    TextView subcription_price;
    TextView subcription_price_200;
    TextView subcription_price_250;

    private IntentIntegrator qrScan;
    ProgressDialog progressDialog;
    private RecyclerView recyclerView;
    private CpePlanAdaptor  mAdapter;
    private Toolbar toolbar;
    private ArrayList<PlanData> planlist;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboad);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        init();
        CallApiforLicenses();
    }

    private void init() {
        recyclerView = findViewById(R.id.recyclerview);

        qrScan = new IntentIntegrator(this);
        qrScan.setCaptureActivity(AnyActivity.class);
        qrScan.setOrientationLocked(false);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait..");



    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {
                //Toast.makeText(this, "Result :"+result.getContents().toString(), Toast.LENGTH_LONG).show();
               CallApi(result.getContents().toString());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void CallApi (String val) {
       final String key = val;
        showProgressDialog();
        Call<CpeResponse> callapi = NetworkCall.hitNetwork(this).getCpeData(Utils.getHeaderMap(this),val);
        callapi.enqueue(new Callback<CpeResponse>() {
            @Override
            public void onResponse(Call<CpeResponse> call, Response<CpeResponse> response) {
                if(response.code()==200) {
                    CpeResponse res = response.body();
                    if (res.getData() != null) {
                        Log.e("ERR02", "not exist");
                        OpenActivity(res.getData().getLid(),key,"true",planlist);
                    } else {
                        OpenActivity("",key,"false",planlist);
                    }
                }else if(response.code() == 403){
                    Utils.INTANCE.Logout(getApplicationContext());
                    Intent newIntent = new Intent(Dashboard.this, LoginScreen.class);
                    newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(newIntent);
                }else{
                    Toast.makeText(Dashboard.this,"Something went wrong",Toast.LENGTH_SHORT).show();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_dashboard_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                qrScan.initiateScan();
                return true;
            case R.id.action_logout:
                Utils.INTANCE.Logout(this);
                Intent newIntent = new Intent(Dashboard.this, LoginScreen.class);
                newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(newIntent);
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void CallApiforLicenses() {

        showProgressDialog();
        Call<com.infinitylabs.udwan.model.dashboard.Response> callapi = NetworkCall.hitNetwork(this).getAllLicenses(Utils.getHeaderMap(this));
        callapi.enqueue(new Callback<com.infinitylabs.udwan.model.dashboard.Response>() {
            @Override
            public void onResponse(Call<com.infinitylabs.udwan.model.dashboard.Response> call, Response<com.infinitylabs.udwan.model.dashboard.Response> response) {
                if (response.code() == 200) {
                    com.infinitylabs.udwan.model.dashboard.Response res = response.body();

                    if (res.getData() != null && res.getData().size() > 0) {
                        Log.e("ERR02", "not exist");
                        setAdaptor(res);
                    } else {
                        Toast.makeText(getApplicationContext(), "No License attached ", Toast.LENGTH_SHORT).show();
                    }
                } else if (response.code() == 403) {
                    Utils.INTANCE.Logout(getApplicationContext());
                    Intent newIntent = new Intent(Dashboard.this, LoginScreen.class);
                    newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(newIntent);
                }

                hideProgressDialog();

            }

            @Override
            public void onFailure(Call<com.infinitylabs.udwan.model.dashboard.Response> call, Throwable t) {
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

    public void OpenActivity(String lid, String key, String isExist, ArrayList<PlanData> planlist){
        Intent i = new Intent(Dashboard.this, ActivationScreen.class);
        Bundle bundle =new Bundle();
        bundle.putString("ur_value",String.valueOf(key));
        bundle.putString("isExist", isExist);
        bundle.putString("lid", lid);
        bundle.putSerializable("planlist", planlist);
        i.putExtras(bundle);
        startActivity(i);
    }

    private  void setAdaptor(com.infinitylabs.udwan.model.dashboard.Response response){
        mAdapter = new CpePlanAdaptor(this);
        planlist = response.getData();
        mAdapter.setValue(planlist);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }


}
