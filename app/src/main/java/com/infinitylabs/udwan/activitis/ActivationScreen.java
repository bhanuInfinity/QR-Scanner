package com.infinitylabs.udwan.activitis;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.infinitylabs.udwan.BuildConfig;
import com.infinitylabs.udwan.LoginScreen;
import com.infinitylabs.udwan.NetworkClient.NetworkCall;
import com.infinitylabs.udwan.R;
import com.infinitylabs.udwan.adaptor.CpePlanAdaptor;
import com.infinitylabs.udwan.listner.RecyclerTouchListener;
import com.infinitylabs.udwan.model.CpeActivationRequest;
import com.infinitylabs.udwan.model.CpeResponse;

import com.infinitylabs.udwan.model.dashboard.LicenseResponse;
import com.infinitylabs.udwan.model.dashboard.PlanData;
import com.infinitylabs.udwan.service.LocationUpdatesService;
import com.infinitylabs.udwan.utils.Utils;

import java.util.ArrayList;
import java.util.Locale;

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
    ArrayList<PlanData> planList;


    String selectedPlan = "";
    ProgressDialog progressDialog;
    boolean isUpdate = false;
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 1001;
    private LocationReceiver rcvMReceiver;
    private LocationUpdatesService mLUService;
    private LocationReceiver myReceiver;
    private String latitude="";
    private String longitude = "";
    private RecyclerView recyclerView;
    private boolean mBound = false;
    private CpePlanAdaptor mAdapter;
    private int lastPosition =-1;

    private String lid="";
    Call<CpeResponse> callapi;
    Geocoder geocoder;
    LinearLayout error_layout;
    RelativeLayout main_layout;
    ImageView back;


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        serialNumber = intent.getExtras().getString("ur_value");
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activiation);
        if (!checkPermission()) {
            requestPermissions();
        }
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            serialNumber = bundle.getString("ur_value");
            isExist = bundle.getString("isExist");
            lid =  getIntent().getStringExtra("lid");
           // planList = (ArrayList<PlanData>) getIntent().getSerializableExtra("planlist");
        }
        init();
        if (Utils.checkInternetConnection(this)) {
            loadingShimmerPlanData();
            CallApiforLicenses();
        } else {
            showInternetError();
        }
    }

    private void init() {

        Toolbar toolbar =  findViewById(R.id.toolbar);
        toolbar.setTitle("Choose license");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        geocoder = new Geocoder(this, Locale.getDefault());
        recyclerView = findViewById(R.id.recyclerview);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait..");
        activation_key = findViewById(R.id.activation_key);
        choose_and_selected_text = findViewById(R.id.choose_and_selected_text);
        activate_cpe = findViewById(R.id.activate_cpe);
        activate_cpe.setOnClickListener(this);
        activation_key.setText(serialNumber);
        main_layout = findViewById(R.id.main_layout);
        error_layout = findViewById(R.id.error);





        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this,
                recyclerView, new CpePlanAdaptor.ClickListener() {
            @Override
            public void onClick(View view, final int position) {

                    if (lastPosition >= 0) {
                        planList.get(lastPosition).setSelected(false);
                    }

                    planList.get(position).setSelected(true);
                    mAdapter.setValue(planList);

                    lastPosition = position;

                    display();
            }

            @Override
            public void onLongClick(View view, int position) {
                Toast.makeText(ActivationScreen.this, "Long press on position :"+position,
                        Toast.LENGTH_LONG).show();
            }
        }));

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
                    Intent newIntent = new Intent(ActivationScreen.this, LoginScreen.class);
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
    private  void setAdaptor(LicenseResponse licenseResponse){
        Log.e("setAdaptor","setAdaptor");
        planList = licenseResponse.getData();
        if(isExist.equalsIgnoreCase("true")){
           // activate_cpe.setText("Change Plan");
            for (int i= 0;i<planList.size();i++){
                if(planList.get(i).getLid().equalsIgnoreCase(lid)){
                    planList.get(i).setSelected(true);
                    planList.get(i).setFromServer(true);
                    lastPosition = i;
                }
            }
            isUpdate=true;
        }
        display();
        mAdapter.setValue(planList);
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    public void showInternetError() {
        if (error_layout != null) {
            recyclerView.setVisibility(View.GONE);
            error_layout.setVisibility(View.VISIBLE);
            main_layout.setVisibility(View.GONE);
            activate_cpe.setVisibility(View.GONE);
        }
    }

    public void hideInternetError() {
        if (error_layout != null) {
            recyclerView.setVisibility(View.VISIBLE);
            error_layout.setVisibility(View.GONE);
            main_layout.setVisibility(View.VISIBLE);
            activate_cpe.setVisibility(View.VISIBLE);
        }

    }
    private void loadingShimmerPlanData() {
        planList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            planList.add(new PlanData(0, "",0,"",0,"",false,null,"",2,false));
        }
        mAdapter = new CpePlanAdaptor(ActivationScreen.this,planList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

    }
    private boolean checkPermission() {
        return PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
    }

    private void requestPermissions() {
        boolean shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (shouldProvideRationale) {
            Snackbar.make(
                    findViewById(R.id.activity_main),
                    R.string.permission_rationale,
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            ActivityCompat.requestPermissions(ActivationScreen.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    REQUEST_PERMISSIONS_REQUEST_CODE);
                        }
                    })
                    .show();
        } else
            ActivityCompat.requestPermissions(ActivationScreen.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(myReceiver);
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (checkPermission()) {
            if (mLUService == null)
                mLUService = new LocationUpdatesService();

            Utils.INTANCE.requestingLocationUpdates(this);
            bindService(new Intent(this, LocationUpdatesService.class), mServiceConection,
                    Context.BIND_AUTO_CREATE);

        }
        myReceiver = new LocationReceiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(myReceiver,
                new IntentFilter(LocationUpdatesService.ACTION_BROADCAST));
    }

    @Override
    protected void onStop() {
        if (mBound) {

            unbindService(mServiceConection);
            mBound = false;
        }
        mLUService.removeLocationUpdates();
        super.onStop();
    }

    public class LocationReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Location location = intent.getParcelableExtra(LocationUpdatesService.EXTRA_LOCATION);
            if (location != null) {
                latitude = String.valueOf(location.getLatitude());
                longitude = String.valueOf(location.getLongitude());
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSIONS_REQUEST_CODE:
                if (grantResults.length <= 0) {
                    // Permission was not granted.
                } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission was granted.
                    if (mLUService == null)
                        mLUService = new LocationUpdatesService();
//                    mLUService.requestLocationUpdates();
                } else {
                    // Permission denied.
                    //changeButtonsState(false);
                    Snackbar.make(
                            findViewById(R.id.activity_main),
                            R.string.permission_denied_explanation,
                            Snackbar.LENGTH_INDEFINITE)
                            .setAction(R.string.settings, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    // Build intent that displays the App settings screen.
                                    Intent intent = new Intent();
                                    intent.setAction(
                                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri = Uri.fromParts("package",
                                            BuildConfig.APPLICATION_ID, null);
                                    intent.setData(uri);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }
                            })
                            .show();
                }
        }
    }


    @Override
    public void onClick(View v) {

        if(v == back){
            finish();
        }
        if (v == activate_cpe) {
            if (activate_cpe.getText().toString().equalsIgnoreCase("change Plan")) {
                choose_and_selected_text.setText("Please choose you plan");
                isUpdate = true;
                activate_cpe.setText("Activate");
            } else {
                if (isUpdate) {
                    if(!lid.equalsIgnoreCase(planList.get(lastPosition).getLid())){
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                        alertDialogBuilder.setMessage("Are you sure you wanted to change license");
                                alertDialogBuilder.setPositiveButton("yes",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface arg0, int arg1) {
                                                CallApi(selectedPlan);
                                            }
                                        });

                        alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                    }

                } else {
                    if (lastPosition>=0) {
                        CallApi(selectedPlan);
                    } else {
                        Toast.makeText(this, "Please Select Plan", Toast.LENGTH_SHORT).show();
                    }
                }

            }

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
        }
    }

    private void CallApi(String selectedPlan) {
        CpeActivationRequest cpeActivationRequest = new CpeActivationRequest();
        cpeActivationRequest.setSerialNumber(serialNumber);
        cpeActivationRequest.setLid(planList.get(lastPosition).getLid());
        cpeActivationRequest.setLatitude(latitude);
        cpeActivationRequest.setLongitude(longitude);
        cpeActivationRequest.setName(Utils.getAddressbyLatLon(this,Double.valueOf(latitude),Double.valueOf(longitude)));
        showProgressDialog();
        if(isExist.equalsIgnoreCase("true")){
             callapi = NetworkCall.hitNetwork(this).UpdateActivation(Utils.INTANCE.getHeaderMap(this),cpeActivationRequest);
        }else{
             callapi = NetworkCall.hitNetwork(this).PostActivation(Utils.INTANCE.getHeaderMap(this),cpeActivationRequest);
        }

        callapi.enqueue(new Callback<CpeResponse>() {
            @Override
            public void onResponse(Call<CpeResponse> call, Response<CpeResponse> response) {
                hideProgressDialog();
                if (response.code() == 200) {
                    CpeResponse res = response.body();
                    ShowDialog(res.getMessage());
                }else if(response.code() == 403){
                    Utils.INTANCE.Logout(getApplicationContext());
                    Intent newIntent = new Intent(ActivationScreen.this, LoginScreen.class);
                    newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(newIntent);
                }else{
                    Toast.makeText(ActivationScreen.this,"Something went Wrong Please try again",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<CpeResponse> call, Throwable t) {
                hideProgressDialog();
                Toast.makeText(ActivationScreen.this, "Something went Wrong Please try again", Toast.LENGTH_SHORT).show();
                Log.e("error", "error");

            }
        });
    }

    private final ServiceConnection mServiceConection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mLUService = ((LocationUpdatesService.LocationBinder) service).getLocationUpdateService();
            mLUService.requestLocationUpdates();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mLUService = null;
            mBound = false;
        }
    };



    private void ShowDialog(String msg){
        AlertDialog.Builder alertDialog =new AlertDialog.Builder(this);
        alertDialog.setMessage(msg);
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        alertDialog.show();
    }
  private void display(){
         if(planList.size()>0 &&lastPosition>0) {
                 if (lid.equalsIgnoreCase(planList.get(lastPosition).getLid())) {
                     activate_cpe.setEnabled(false);
                     activate_cpe.setBackgroundColor(getResources().getColor(R.color.view_bg));
                 } else {
                     activate_cpe.setEnabled(true);
                     activate_cpe.setBackgroundColor(getResources().getColor(R.color.btn_color));
                 }
         }
  }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
