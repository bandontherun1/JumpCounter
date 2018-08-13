package com.example.jumper;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.io.IOException;

public class TrainingActivity extends AppCompatActivity {
    Jumper me;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);

        Intent intent = getIntent();
        me = (Jumper) intent.getSerializableExtra("jumperobject");
    }

    protected void onTrainClicked(View v) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.RECORD_AUDIO },
                    10);
        }

        if (me != null) {
            me.mjumpsound = new Jumpsound("/jumperdata");
            try {
                me.mjumpsound.start();
                me.mjumpsound.stop();
            } catch (IOException e) {
                e.printStackTrace();

            }

        }
    }
}
