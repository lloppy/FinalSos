package io.realworld.saveoursoul;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;


public class Utils {

    public static final int MY_PERMISSIONS_ALL = 0;
    public static final String[] PERMISSIONS = {Manifest.permission.SEND_SMS, Manifest.permission.ACCESS_FINE_LOCATION};
    private static final int PLAY_SERVICES_RESOLUTION_REQ = 240;

    public static boolean hasPermissions(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && PERMISSIONS != null) {
            for (String permission : PERMISSIONS) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED)
                    return false;
            }
        }
        return true;
    }

    public static boolean checkForFirstTimeLaunch(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        boolean firstTimeLaunch = prefs.getBoolean(context.getString(R.string.first_time_launch), true);
        return firstTimeLaunch;
    }

    public static boolean checkForPlayServices(final Activity context) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(context);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                Log.d("debug", "in here");
                Dialog dg = apiAvailability.getErrorDialog(context, resultCode, PLAY_SERVICES_RESOLUTION_REQ, new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        context.finishAffinity();
                    }
                });
                if (dg != null) {
                    Log.d("debug", "after here : " + dg);

                    dg.show();
                }
                return false;
            } else {
                Log.d("debug", "This device is not supported");
                context.finishAffinity();
            }
        }
        return true;
    }

    public static void sendMessage(String message, Context context) {
        SmsManager smsManager = SmsManager.getDefault();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String number = preferences.getString(context.getString(R.string.number), "9*********");
        smsManager.sendTextMessage(number, null, message, null, null);
        Log.d("debug", message);
        Toast.makeText(context, "SOS-сообщение отправлено", Toast.LENGTH_LONG).show();
    }


}
