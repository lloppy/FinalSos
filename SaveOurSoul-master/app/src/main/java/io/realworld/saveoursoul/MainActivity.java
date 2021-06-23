package io.realworld.saveoursoul;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient googleApiClient;
    private FusedLocationProviderApi fusedLocationProviderApi = LocationServices.FusedLocationApi;
    private LocationManager locationManager;
    private LocationRequest locationRequest;
    private LocationSettingsRequest.Builder locationSetReqBuilder;
    private static final long INTERVAL = 100 * 1;
    private static final long FASTEST_INTERVAL = 100 * 1;
    private final int REQ_CODE = 245;
    private boolean locationSettingsEnable = false;
    /*
    private boolean checkForFirstTimeLaunch() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        boolean firstTimeLaunch = prefs.getBoolean(getString(R.string.first_time_launch), true);
        return firstTimeLaunch;
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);

        Drawable d = getResources().getDrawable(R.drawable.shester1);
        myToolbar.setOverflowIcon(d);

        setSupportActionBar(myToolbar);

        //setting first time launch flag to false
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        SharedPreferences.Editor edit = prefs.edit();
        edit.putBoolean(getString(R.string.first_time_launch),Boolean.FALSE);
        edit.commit();





        createLocationRequest();
        setLocationClient();
        if (!checkifLocationisEnable()) {
            checkLocationSettings();
        }

        ImageButton phone= (ImageButton) findViewById(R.id.phone);
        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,VoicesActivites.class));
            }
        });


        Button sendSOS = (Button) findViewById(R.id.send_sos);
        sendSOS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("debug", "location settings enable :" + locationSettingsEnable);

                Log.d("debug", "[After]location settings enable :" + locationSettingsEnable);

                Intent service = new Intent(MainActivity.this, SendSms.class);
                Log.d("debug", "Starting background service");
                service.putExtra("locationRequest", locationRequest);
                MainActivity.this.startService(service);

            }
        });

    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent goToConfigPage = new Intent(this, MyAppConfig.class);
                startActivity(goToConfigPage);
                return true;
            default:
                Log.d("debug", "dont know");
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return true;
    }

    private void createLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


    }

    private boolean checkifLocationisEnable() {
        locationManager = (LocationManager) getBaseContext().getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
            return true;
        else
            return false;
    }

    private void checkLocationSettings() {
        Log.d("debug", "in checkLocationSettings");
        locationSetReqBuilder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        locationSetReqBuilder.setAlwaysShow(true);

        final PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, locationSetReqBuilder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
                final Status status = locationSettingsResult.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        Log.d("debug", "All locations settings satisfied");
                        locationSettingsEnable = true;
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.d("debug", "Location settings are not satisfied, Show the user a dialog to upgrade locations settings");
                        try {
                            status.startResolutionForResult(MainActivity.this, REQ_CODE);
                        } catch (IntentSender.SendIntentException exc) {
                            exc.printStackTrace();
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.d("debug", "Location settings change unavailable");
                        finishAffinity();
                }
            }
        });

        Log.d("debug", "Builder formerd");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("debug", "in onActivityResult");
        final LocationSettingsStates states = LocationSettingsStates.fromIntent(data);
        switch (requestCode) {
            case REQ_CODE:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Log.d("debug", "Location settings change successful");
                        locationSettingsEnable = true;
                        break;
                    case Activity.RESULT_CANCELED:
                        Log.d("debug", "Location settings cancelled");
                        break;
                    default:
                        break;
                }
                break;
        }
    }

    private void setLocationClient() {
        Log.d("debug", "building google api client");

        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d("debug", "main googleapi client connected");
    }

    @Override
    public void onConnectionSuspended(int i) {
        //Empty
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        //Empty
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }
}
