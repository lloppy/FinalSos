package io.realworld.saveoursoul;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class WelcomeFirstTime extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("debug","in welcome..........");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_layout);
        final Button start = (Button) findViewById(R.id.start_button);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToConfigPage = new Intent(WelcomeFirstTime.this,MyAppConfig.class);
                startActivity(goToConfigPage);
            }
        });
    }
}
