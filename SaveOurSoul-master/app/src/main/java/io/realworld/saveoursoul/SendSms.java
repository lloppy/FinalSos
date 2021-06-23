package io.realworld.saveoursoul;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.EditText;

import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;


public class SendSms extends IntentService implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private FusedLocationProviderApi fusedLocationProviderApi = LocationServices.FusedLocationApi;
    private LocationRequest locationRequest;
    private GoogleApiClient googleApiClient;
    private int count = 0;
    private float accuracy;

    private boolean flag = false;
    public SendSms() {
        super("send-sms");
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(SendSms.this);
        accuracy = sharedPreferences.getFloat(getString(R.string.accuracy),200.0f);
        this.locationRequest = intent.getParcelableExtra("locationRequest");

        if(this.locationRequest == null){
            Log.d("debug","yay");
            return;
        }
        Log.d("debug","background service started");
        setLocationClient();
    }

    private void setLocationClient() {
        Log.d("debug","building google api client");

        this.googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        googleApiClient.connect();
        Log.d("debug","trying to connect");
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d("debug","Connected...........");
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        //Empty
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        //Empty
    }

    private void startLocationUpdates(){
        try {
            this.fusedLocationProviderApi.requestLocationUpdates(googleApiClient, locationRequest, locationListener);
            Log.d("debug","location update started............");
        }catch (SecurityException exc){
            exc.printStackTrace();
        }
    }

    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            /*if(flag){
                Log.d("debug","Service end");
                googleApiClient.disconnect();
            }*/
            Log.d("debug","got location with accuracy" + location.getAccuracy());
            count++;
            if(count > 5){
                Utils.sendMessage("Could not find location within " + accuracy + " in 5 tries " +
                        "last know location is " + location.getLatitude() + ":" + location.getLongitude() + "with accuracy of " +
                location.getAccuracy() + " mts.",SendSms.this);
                Log.d("debug","Location not found withing accuracy");
                flag = true;
                fusedLocationProviderApi.removeLocationUpdates(googleApiClient,this);
                Log.d("debug","Stopping service");
                SendSms.this.stopSelf();
                return;
            }
            if(location.getAccuracy() < accuracy && location.hasAccuracy()){
                /*try{
                    Thread.sleep(5000);
                }catch (InterruptedException exc){
                    exc.printStackTrace();
                }*/

                Utils.sendMessage ("SOS! Pomogi mne: " + " http://maps.google.com?q=" + location.getLatitude() + "," + location.getLongitude() + " Tochnost: " +
                       location.getAccuracy() + " mts.",SendSms.this);
                flag = true;
                fusedLocationProviderApi.removeLocationUpdates(googleApiClient,this);
                Log.d("debug","Stopping service");
                SendSms.this.stopSelf();
            }
        }
    };

}
