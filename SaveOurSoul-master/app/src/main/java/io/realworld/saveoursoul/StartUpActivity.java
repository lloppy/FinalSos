package io.realworld.saveoursoul;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by sourabh on 12/6/17.
 */

public class StartUpActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d("debug","In startUpActivity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_activity);
        if(Utils.checkForPlayServices(this)) {

            if (Utils.checkForFirstTimeLaunch(this) || !Utils.hasPermissions(this)) {
                Intent goToWelcome = new Intent(this, WelcomeFirstTime.class);
                Log.d("debug","goin to welcome....." + goToWelcome);
                startActivity(goToWelcome);
                return;
            }
            Intent goToMainActivity = new Intent(this, MainActivity.class);
            startActivity(goToMainActivity);
        }
    }
}
