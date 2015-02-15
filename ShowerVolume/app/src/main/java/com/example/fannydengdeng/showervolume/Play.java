package com.example.fannydengdeng.showervolume;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Play extends Activity {

   private Button back;
    String mode;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playing);
        back = (Button) findViewById(R.id.back_btn);

        mode = getIntent().getStringExtra("mode");
        Intent background = new Intent (Play.this, Background.class);
        background.putExtra("mode", mode);
        startService(background);
    }

    public void backButtonClicked (View view) {
        finish();
    }

}

