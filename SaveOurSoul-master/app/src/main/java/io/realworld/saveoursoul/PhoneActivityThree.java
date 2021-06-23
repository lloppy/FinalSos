package io.realworld.saveoursoul;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class PhoneActivityThree extends AppCompatActivity {



    TextView tvSongDes;
    Button btShowmore;

    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_three);
        tvSongDes = (TextView) findViewById(R.id.tvSongDes3);
        btShowmore = (Button) findViewById(R.id.btShowmore);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);







        ImageButton play = findViewById(R.id.play);
        play.setImageResource(R.drawable.ic_play1);

        final MediaPlayer mediaPlayer ;
        mediaPlayer=MediaPlayer.create(this,R.raw.fenibut3);



        ImageButton playButton= (ImageButton) findViewById(R.id.play);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!mediaPlayer.isPlaying()){ // here mp is object of MediaPlayer class
                    mediaPlayer.start();
                    play.setImageResource(R.drawable.ic_stop);
                    //showNotification("Player State Plying");
                }
                else if(mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                    play.setImageResource(R.drawable.ic_play1);
                    //showNotification("Player State Pause");

                }
            }
        });





        btShowmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (btShowmore.getText().toString().equalsIgnoreCase("Развернуть...")) {
                    tvSongDes.setMaxLines(Integer.MAX_VALUE);//your TextView
                    btShowmore.setText("Свернуть");
                } else {
                    tvSongDes.setMaxLines(3);//your TextView
                    btShowmore.setText("Развернуть...");
                }
            }
        });

    }
}
