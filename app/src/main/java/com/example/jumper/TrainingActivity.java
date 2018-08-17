package com.example.jumper;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
import java.util.ArrayList;

public class TrainingActivity extends AppCompatActivity {
    Jumper me;
    final int MY_PERMISSIONS_REQUEST_RECORD_AUDIO=123;
    final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE=124;
    final int AMPLITUDE_THRESHOLD = 30000;
    final int mInterval = 20;

    private boolean inJump = false;
    public ArrayList<Integer> trainedJump = new ArrayList<>();
    private Handler myHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);

        Intent intent = getIntent();
        me = (Jumper) intent.getSerializableExtra("jumperobject");
    }
    protected void onStopClicked(View v) {
        if (me != null) {

            try {
                me.mjumpsound.stop();
                stopRepeatingTask();
            } catch (IOException e) {
                e.printStackTrace();

            }

            Intent intent = new Intent(this, activity_StartJump.class);
          //  intent.putExtra("jumpStartobject", me);
            startActivity(intent);

        }

    }
    protected void onTrainClicked(View v) {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.RECORD_AUDIO)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.RECORD_AUDIO},
                        MY_PERMISSIONS_REQUEST_RECORD_AUDIO);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
        }

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
        }

        if (me != null) {
            Button stopB = findViewById(R.id.stopTrainB);
            Button trainB = findViewById(R.id.trainB);
            trainB.setVisibility(View.INVISIBLE);
            stopB.setVisibility(View.VISIBLE);

            me.mjumpsound = new Jumpsound("/jumperdata");
            try {
                me.mjumpsound.start();
                startRepeatingTask();
            } catch (IOException e) {
                e.printStackTrace();

            }

        }
    }

    void startRepeatingTask() {
        mStatusChecker.run();
    }

    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            try {
                startTraining(me.mjumpsound.myjumpsound);
            } finally {
                // 100% guarantee that this always happens, even if
                // your update method throws an exception
                myHandler.postDelayed(mStatusChecker, mInterval);
            }
        }
    };

    protected void startTraining(MediaRecorder trainer) {
        if (trainer != null) {
            int amplitude = trainer.getMaxAmplitude();
            if (amplitude > AMPLITUDE_THRESHOLD && trainedJump.size() == 0)
                inJump = true;
            else if (amplitude < AMPLITUDE_THRESHOLD)
                inJump = false;

            if (inJump)
                trainedJump.add(amplitude);
        }
    }
    void stopRepeatingTask() {
        myHandler.removeCallbacks(mStatusChecker);
    }
}
