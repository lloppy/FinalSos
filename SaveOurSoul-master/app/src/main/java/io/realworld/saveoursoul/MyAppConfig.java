package io.realworld.saveoursoul;
import androidx.appcompat.widget.Toolbar;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class MyAppConfig extends AppCompatActivity {

    EditText contactNum = null;
    Button confirmButton = null;
    EditText accuracy = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_app_config_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);




        contactNum = (EditText) findViewById(R.id.contact_number);
        confirmButton = (Button) findViewById(R.id.confirm_button);
        accuracy = (EditText) findViewById(R.id.accuracy);
        setCurrentSettings();
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkIfValidEnteredContactNumberAndStore()) {
                    permissionUtil();

                }
            }
        });

    }

    private void setCurrentSettings(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String number = preferences.getString(getString(R.string.number),"0000000000");
        float acc = preferences.getFloat(getString(R.string.accuracy),200.0f);
        contactNum.setHint(number);
        accuracy.setHint(String.valueOf(acc));

    }

    private boolean checkIfValidEnteredContactNumberAndStore() {
        String number = contactNum.getText().toString();
        if (number.length() != 10) {
            Toast.makeText(this, "Длина номера - 10 знаков.", Toast.LENGTH_SHORT).show();
            return false;
        }
        float acc;
        if(accuracy.getText().toString().trim().isEmpty()){
            Toast.makeText(this, "Установка точности по умолчанию", Toast.LENGTH_SHORT).show();
            acc = 200.0f;
        }else{
            acc = Float.parseFloat(accuracy.getText().toString());
        }

        Log.d("debug", "Set accuracy is " + acc);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        SharedPreferences.Editor edit = prefs.edit();
        edit.putString(getString(R.string.number), number);
        edit.putFloat(getString(R.string.accuracy), acc);
        edit.commit();
        return true;
    }

    private void permissionUtil() {
        if (!Utils.hasPermissions(this)) {
            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(this);
            }
            builder.setTitle("Разрешения предоставлены")
                    .setMessage("Permission to send message and access location required for functioning of this app." +
                            "Cancelling will close the app.")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(MyAppConfig.this, Utils.PERMISSIONS, Utils.MY_PERMISSIONS_ALL);
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            MyAppConfig.this.finishAffinity();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .show();

        } else {
            Intent goToMainPage = new Intent(this, MainActivity.class);
            startActivity(goToMainPage);
            Toast.makeText(getBaseContext(), "Номер сохранён", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Utils.MY_PERMISSIONS_ALL:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    Intent goToMainPage = new Intent(MyAppConfig.this, MainActivity.class);
                    startActivity(goToMainPage);
                }


        }
    }
}
