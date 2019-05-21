package com.example.sdwan.activitis;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
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
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sdwan.BuildConfig;
import com.example.sdwan.NetworkClient.NetworkCall;
import com.example.sdwan.R;
import com.example.sdwan.model.CpeActivationRequest;
import com.example.sdwan.model.CpeResponse;
import com.example.sdwan.service.LocationReceiver;
import com.example.sdwan.service.LocationUpdatesService;
import com.example.sdwan.utils.Constant;
import com.example.sdwan.utils.Utils;

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
    boolean isUpdate = false;
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 1001;
    private LocationReceiver rcvMReceiver;
    private LocationUpdatesService mLUService;
    private LocationReceiver myReceiver;
    private String latitude="";
    private String longitude = "";

    private boolean mBound = false;


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

        if (isExist.equalsIgnoreCase("true")) {
            selectedPlan = response.getResponseBody().getLicensekey();
            PlanSelectable(selectedPlan);
            layout_200.setEnabled(false);
            layout_250.setEnabled(false);
            layout_100.setEnabled(false);

            activate_cpe.setText("Change Plan");

        } else {

        }
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
                Toast.makeText(context, Utils.INTANCE.getLocationText(location),
                        Toast.LENGTH_SHORT).show();
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
        if (v == activate_cpe) {
            if (activate_cpe.getText().toString().equalsIgnoreCase("change Plan")) {
                layout_200.setEnabled(true);
                layout_250.setEnabled(true);
                layout_100.setEnabled(true);
                activate_cpe.setText("Activate");
                Toast.makeText(this, "You can Select any plan", Toast.LENGTH_SHORT).show();
                choose_and_selected_text.setText("Please choose you plan");
                isUpdate = true;
            } else {
                if (isUpdate) {
                    CallApi(selectedPlan);
                } else {
                    if (selectedPlan.toString().trim().length() > 0) {
                        CallApi(selectedPlan);
                    } else {
                        Toast.makeText(this, "Please Select Plan", Toast.LENGTH_SHORT).show();
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
        cpeActivationRequest.setLatitude(latitude);
        cpeActivationRequest.setLongitude(longitude);
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
                Toast.makeText(ActivationScreen.this, "Something went Wrong", Toast.LENGTH_SHORT).show();
                Log.e("error", "error");
            }
        });
    }


    private void CallupdateApi(String selectedPlan) {

        CpeActivationRequest cpeActivationRequest = new CpeActivationRequest();
        cpeActivationRequest.setSerialnumber(serialNumber);
        cpeActivationRequest.setLicensekey(selectedPlan);
        cpeActivationRequest.setCustid(Constant.USERNAME);
        cpeActivationRequest.setLatitude(latitude);
        cpeActivationRequest.setLongitude(longitude);
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
                Toast.makeText(ActivationScreen.this, "Something went Wrong", Toast.LENGTH_SHORT).show();
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
}
