package com.infinitylabs.udwan.activitis;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.infinitylabs.udwan.LoginScreen;
import com.infinitylabs.udwan.NetworkClient.NetworkCall;
import com.infinitylabs.udwan.R;
import com.infinitylabs.udwan.Ui.Activity.AnyActivity;
import com.infinitylabs.udwan.adaptor.CpePlanAdaptor;
import com.infinitylabs.udwan.customeviews.TextViewJosifinRegular;
import com.infinitylabs.udwan.model.CpeResponse;

import com.infinitylabs.udwan.model.dashboard.DashBoardResponse;
import com.infinitylabs.udwan.model.dashboard.LicenseResponse;
import com.infinitylabs.udwan.model.dashboard.PlanData;
import com.infinitylabs.udwan.utils.Utils;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Dashboard extends AppCompatActivity implements View.OnClickListener {


    private IntentIntegrator qrScan;
    ProgressDialog progressDialog;
    private RecyclerView recyclerView;
    private CpePlanAdaptor  mAdapter;
    private Toolbar toolbar;
    private ArrayList<PlanData> planlist;
    private TextView active_device;
    private TextView inactive_device;
    private TextView license_avail;
    private TextView license_used;
    public FloatingActionButton fab_add;
    TextViewJosifinRegular name;
    ImageView back;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboad);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        init();
        CallApiforDashboard();
    }

    private void init() {
        active_device = findViewById(R.id.active_device);
        inactive_device = findViewById(R.id.inactive_device);
        license_avail = findViewById(R.id.license_avail);
        license_used = findViewById(R.id.license_used);
        fab_add = (FloatingActionButton)findViewById(R.id.fab_add);
        name = findViewById(R.id.name);
        fab_add.setOnClickListener(this);
        license_avail.setOnClickListener(this);
        back =findViewById(R.id.back);
        back.setVisibility(View.GONE);
        //recyclerView = findViewById(R.id.recyclerview);
        qrScan = new IntentIntegrator(this);
        qrScan.setCaptureActivity(AnyActivity.class);
        qrScan.setOrientationLocked(false);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait..");
        progressDialog.setCancelable(false);
        name.setText("Hello "+Utils.getUserName(this));


    }

    @Override
    public void onClick(View v) {
            if(v  == fab_add){
                qrScan.initiateScan();
            }
            if(v == license_avail){
                Intent intent = new Intent(this,LicenseListScreen.class);
                startActivity(intent);
            }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
               // Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
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
        Call<CpeResponse> callapi = NetworkCall.hitNetwork(this).getCpeData(Utils.getHeaderMap(this),val);
        callapi.enqueue(new Callback<CpeResponse>() {
            @Override
            public void onResponse(Call<CpeResponse> call, Response<CpeResponse> response) {
                if(response.code()==200) {
                    CpeResponse res = response.body();
                    if(res.getResult().equalsIgnoreCase("failed")){
                        showErrorDialog();
                    }else {
                        if (res.getData() != null) {
                            Log.e("ERR02", "not exist");
                            OpenActivity(res.getData().getLid(), key, "true");
                        } else {
                            OpenActivity("", key, "false");
                        }
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

    private void CallApiforDashboard() {
        showProgressDialog();
        Call<DashBoardResponse> callapi = NetworkCall.hitNetwork(this).getDashboardCount(Utils.getHeaderMap(this));
        callapi.enqueue(new Callback<DashBoardResponse>() {
            @Override
            public void onResponse(Call<DashBoardResponse> call, Response<DashBoardResponse> response) {
                if(response.code()==200) {
                    DashBoardResponse res = response.body();
                     setValueOnViews(res);
                }else if(response.code() == 403 || response.code() == 401){
                    Utils.INTANCE.Logout(getApplicationContext());
                    Intent newIntent = new Intent(Dashboard.this, LoginScreen.class);
                    newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(newIntent);
                    finish();
                }else{
                    Toast.makeText(Dashboard.this,"Something went wrong",Toast.LENGTH_SHORT).show();
                }

                hideProgressDialog();

            }

            @Override
            public void onFailure(Call<DashBoardResponse> call, Throwable t) {
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

    private void setValueOnViews(DashBoardResponse valueOnViews){
        active_device.setText(""+valueOnViews.getData().getTotal_active_devices()+" up");
        inactive_device.setText(""+valueOnViews.getData().getTotal_inactive_devices()+" down");
        license_avail.setText(""+valueOnViews.getData().getTotal_active_licenses()+" Available");
        license_used.setText(""+valueOnViews.getData().getTotal_inactive_licenses()+ " Inactive");
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

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

    public void OpenActivity(String lid, String key, String isExist){
        Intent i = new Intent(Dashboard.this, ActivationScreen.class);
        Bundle bundle =new Bundle();
        bundle.putString("ur_value",String.valueOf(key));
        bundle.putString("isExist", isExist);
        bundle.putString("lid", lid);

        i.putExtras(bundle);
        startActivity(i);
    }

private void showErrorDialog(){
    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
    alertDialogBuilder.setMessage("Unknow Device");
    alertDialogBuilder.setPositiveButton("OK",
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                   arg0.dismiss();
                }
            });


    AlertDialog alertDialog = alertDialogBuilder.create();
    alertDialog.show();
}

}
