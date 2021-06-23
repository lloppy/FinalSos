package io.realworld.saveoursoul;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class VoicesActivites extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.voices_activites);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Button tab= (Button) findViewById(R.id.tab);
        tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(VoicesActivites.this,PhoneActivity.class));
            }
        });

        Button tab2= (Button) findViewById(R.id.tab2);
        tab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(VoicesActivites.this,PhoneActivityTwo.class));
            }
        });

        Button tab3= (Button) findViewById(R.id.tab3);
        tab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(VoicesActivites.this,PhoneActivityThree.class));
            }
        });

        Button tab4= (Button) findViewById(R.id.tab4);
        tab4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(VoicesActivites.this,PhoneActivityFour.class));
            }
        });

        Button tab5= (Button) findViewById(R.id.tab5);
        tab5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(VoicesActivites.this,PhoneActivityFive.class));
            }
        });

        Button tab6= (Button) findViewById(R.id.tab6);
        tab6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(VoicesActivites.this,PhoneActivitySix.class));
            }
        });

        Button tab7= (Button) findViewById(R.id.tab7);
        tab7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(VoicesActivites.this,PhoneActivitySeven.class));
            }
        });

    }
}