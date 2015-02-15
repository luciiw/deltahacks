package com.example.fannydengdeng.showervolume;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

    private Button showerMode;
    private Button streetMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showerMode = (Button) findViewById(R.id.shower_btn);
        streetMode = (Button) findViewById(R.id.street_btn);

        showerMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                AudioManager manager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
                if(manager.isMusicActive()){
                    Intent goTo = new Intent(MainActivity.this, Play.class);
                    goTo.putExtra("mode", "shower");
                    startActivity(goTo);
                } else {
                    Toast.makeText(getApplicationContext(), "Please play your music", Toast.LENGTH_SHORT).show();
                }

            }
        });

        streetMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                AudioManager manager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
                if(manager.isMusicActive()){
                    Intent goTo = new Intent(MainActivity.this, Play.class);
                    goTo.putExtra("mode", "street");
                    startActivity(goTo);
                } else {
                    Toast.makeText(getApplicationContext(), "Please play your music", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
