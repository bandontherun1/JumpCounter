package com.example.jumper;

import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

public class activity_StartJump extends AppCompatActivity {
    final int AMPLITUTE_THRESHOLD = 30000;
    final int mInterval = 200;

    int count = 0;
    private Handler myHandler = new Handler();
    Jumpsound myJump = new Jumpsound("/dev/null");
    Jumper myjumper;
    int max = 0;
    int max2 = 0;
    TextView counter;
    Button jumpStartB;
    Button jumpStopB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__start_jump);
        Intent intent = getIntent();
     //   myjumper = (Jumper) intent.getSerializableExtra("jumpStartobject");

    }

    protected void onJumpStartClicked(View v) {
        // open /dev/null for not saving the mic data
        counter = findViewById(R.id.count);
        jumpStartB = findViewById(R.id.startJumpB);
        jumpStopB = findViewById(R.id.stopJumpB);

        jumpStartB.setVisibility(View.INVISIBLE);
        jumpStopB.setVisibility(View.VISIBLE);

        if (myJump != null) {
            try {
                myJump.start();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // startCounting(myJump.myjumpsound);
            startRepeatingTask();

        }

    }

    protected void onJumpStopClicked(View v) {
        jumpStopB.setVisibility(View.INVISIBLE);
        stopRepeatingTask();
        counter.setText(count+"");

    }

    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            try {
                startCounting(myJump.myjumpsound);
            } finally {
                // 100% guarantee that this always happens, even if
                // your update method throws an exception
                myHandler.postDelayed(mStatusChecker, mInterval);
            }
        }
    };

    void startRepeatingTask() {
        mStatusChecker.run();
    }

    void stopRepeatingTask() {
        myHandler.removeCallbacks(mStatusChecker);
    }
    protected void startCounting(MediaRecorder myJump) {

        if (myJump != null) {
            int amplitute = myJump.getMaxAmplitude();
            if (amplitute > max)
                max = amplitute;
            if (max > max2)
                max2 = max;


            if (amplitute > AMPLITUTE_THRESHOLD) {
                count++;
                counter.setText(count+"");
            }
        }

    }
}
