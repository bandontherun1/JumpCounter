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
import java.util.ArrayList;

public class activity_StartJump extends AppCompatActivity {
    final int AMPLITUDE_THRESHOLD = 20000;
    final int mInterval = 20;

    private ArrayList<Integer> trainedJump;
    private ArrayList<Integer> tempJump = new ArrayList<>();
    private boolean inJump = false;

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
        trainedJump = (ArrayList<Integer>) intent.getSerializableExtra("trainedJump");

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
            int amplitude = myJump.getMaxAmplitude();
//            if (amplitude > max)
//                max = amplitude;
//            if (max > max2)
//                max2 = max;

            // Jump detected; set inJump to true to start saving pattern
            if (amplitude > AMPLITUDE_THRESHOLD && tempJump.size() == 0)
                inJump = true;

            // Jump ended; stop saving pattern, detect if jump matches pattern and clear array
            else if (amplitude < AMPLITUDE_THRESHOLD) {
                inJump = false;
                if (tempJump.size() > 0 && isJump(tempJump)) {
                    count++;
                    counter.setText(count + "");
                }
                tempJump.clear();
            }

            if (inJump)
                tempJump.add(amplitude);

//            if (amplitude > AMPLITUDE_THRESHOLD) {
//                count++;
//                counter.setText(count + "");
//            }
        }

    }

    protected boolean isJump(ArrayList<Integer> jump) {
        System.out.println("Trained: " + trainedJump);
        System.out.println("Test" + jump);
        return false;
    }
}
