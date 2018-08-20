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
    private int leadingAmplitude = 0;
    private int AMPLITUDE_THRESHOLD_HIGH = 0;
    private int AMPLITUDE_THRESHOLD_LOW = 0;
    private int AMPLITUDE_OFFSET = 0;
    final int mInterval = 20;
    
    private ArrayList<Integer> tempJump = new ArrayList<>();
    private boolean inJump = false;

    int count = 0;
    private Handler myHandler = new Handler();
    Jumpsound myJump = new Jumpsound("/dev/null");
    Jumper myjumper;
    TextView counter;
    Button jumpStartB;
    Button jumpStopB;
    Boolean amIRunning = true;
    Button resumeJumpB;
    Button pauseJumpB;
    Button restartB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__start_jump);
        Intent intent = getIntent();
        AMPLITUDE_THRESHOLD_HIGH = (int)((double)intent.getSerializableExtra("AMPLITUDE_THRESHOLD_HIGH") * .6);
        AMPLITUDE_OFFSET = (int) (AMPLITUDE_THRESHOLD_HIGH * .2);
        AMPLITUDE_THRESHOLD_LOW = (int) (AMPLITUDE_THRESHOLD_HIGH * .2);
        counter = findViewById(R.id.count);
        jumpStartB = findViewById(R.id.startJumpB);
        jumpStopB = findViewById(R.id.stopJumpB);
        counter.setTextSize(120);
        counter.setGravity(View.TEXT_ALIGNMENT_GRAVITY);
        counter.setText("0");
        pauseJumpB = findViewById(R.id.pauseJumpB);
        resumeJumpB = findViewById(R.id.resumeJumpB);
        restartB = findViewById(R.id.restartB);

    }

    protected void onJumpStartClicked(View v) {
        // open /dev/null for not saving the mic data

        jumpStartB.setVisibility(View.INVISIBLE);
        jumpStopB.setVisibility(View.VISIBLE);
        pauseJumpB.setVisibility(View.VISIBLE);
        restartB.setVisibility(View.VISIBLE);

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

    protected void onRestartClicked(View v) {
        count = 0;
        counter.setText("0");
    }

    protected void onPauseJumpClicked(View v) {

        pauseJumpB.setVisibility(View.INVISIBLE);
        resumeJumpB.setVisibility(View.VISIBLE);
        amIRunning = false;
    }

    protected void onResumeJumpClicked(View v) {
        pauseJumpB.setVisibility(View.VISIBLE);
        resumeJumpB.setVisibility(View.INVISIBLE);
        amIRunning = true;
    }

    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            try {
                if (amIRunning)
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

            // Jump detected; set inJump to true to start saving pattern
            // tempJump.size condition allow only one capture of the sound
            if (amplitude > AMPLITUDE_THRESHOLD_HIGH && tempJump.size() == 0) {
                inJump = true;
            }
            // Jump ended; stop saving pattern
            else if (amplitude < AMPLITUDE_THRESHOLD_LOW) {
                if (inJump) {
                    // capture the fading off amplitude
                    count++;
                    counter.setText(count + "");
                    tempJump.add(amplitude);
                    System.out.println(count);
                    System.out.println(tempJump);
                    System.out.println("fading off sound Jump size = " + tempJump.size());

                    inJump = false;
                    tempJump.clear();
                }

            }

            // Save amplitude point to arraylist
            if (inJump) {
                //  System.out.println(">>>> " + amplitude);
                if (amplitude > leadingAmplitude && amplitude - leadingAmplitude > AMPLITUDE_OFFSET) { // it is probably a new sound
                    if (amplitude > AMPLITUDE_THRESHOLD_HIGH) { // record the new sound
                        if (tempJump.size() > 1) { // print the leading jump sound
                            count++; // as the previous sound will not tampering off
                            counter.setText(count + "");

                            System.out.println(count);
                            System.out.println(tempJump);
                            System.out.println("competing sounds Jump size = " + tempJump.size());

                            tempJump.clear();
                        }

//                        inJump = true; // in a new jump
                        tempJump.add(amplitude);
                        leadingAmplitude = amplitude;
                    }

                } else {
                    tempJump.add(amplitude);
                    leadingAmplitude = amplitude;
                }
            }
        }

    }
}
