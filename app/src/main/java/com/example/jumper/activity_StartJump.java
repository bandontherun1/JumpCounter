package com.example.jumper;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaRecorder;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
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
    private boolean enableDebug = false;
    
    private ArrayList<Integer> tempJump = new ArrayList<>();
    private boolean inJump = false;

    int count = 0;
    private Handler myHandler = new Handler();
    float myweight;
    Jumpsound myJump = new Jumpsound("/dev/null");
    TextView counter;
    Button jumpStartB;
    Button jumpStopB;
    Boolean amIRunning = true;
    Button resumeJumpB;
    Button pauseJumpB;
    Button restartB;
    TextView sw;
    private Handler stopWatchHandler;
    private boolean swStarted;
    private long startTime;
    private long seconds;
    final double caloriesPerMin = 0.076; // per livingstrong.com


    private final Runnable stopWatchRunnable = new Runnable() {
        @Override
        public void run() {
            if (swStarted) {
                seconds = (System.currentTimeMillis() - startTime) / 1000;
                if (seconds != 0)
                    sw.setText(String.format("%02d:%02d  %3d", seconds / 60, seconds % 60, count*60/seconds));
                stopWatchHandler.postDelayed(stopWatchRunnable, 1000L);
            }
        }

    };




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__start_jump);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Intent intent = getIntent();
      //  AMPLITUDE_THRESHOLD_HIGH = (int)((double)intent.getSerializableExtra("AMPLITUDE_THRESHOLD_HIGH") * .6);
        AMPLITUDE_THRESHOLD_HIGH = (int)((double)intent.getSerializableExtra("AMPLITUDE_THRESHOLD_HIGH"));
        myweight = (float) intent.getSerializableExtra("JumperWeight");
        AMPLITUDE_OFFSET = (int) (AMPLITUDE_THRESHOLD_HIGH * .2);
        AMPLITUDE_THRESHOLD_LOW = (int) (AMPLITUDE_THRESHOLD_HIGH * .2);
        counter = findViewById(R.id.count);
        jumpStartB = findViewById(R.id.startJumpB);
        jumpStopB = findViewById(R.id.stopJumpB);
        counter.setTextSize(80);
        counter.setGravity(View.TEXT_ALIGNMENT_GRAVITY);
        counter.setText("0");
        pauseJumpB = findViewById(R.id.pauseJumpB);
        resumeJumpB = findViewById(R.id.resumeJumpB);
        restartB = findViewById(R.id.restartB);
        sw = findViewById(R.id.stopWatch);
        sw.setTextSize(80);
        sw.setGravity(View.TEXT_ALIGNMENT_GRAVITY);
        stopWatchHandler = new Handler();

    }

    public void onJumpStartClicked(View v) {
        jumpStartB.setVisibility(View.INVISIBLE);
        jumpStopB.setVisibility(View.VISIBLE);
        pauseJumpB.setVisibility(View.VISIBLE);
        restartB.setVisibility(View.VISIBLE);
        startTime = System.currentTimeMillis();
        sw.setVisibility(View.VISIBLE);

        if (myJump != null) {
            try {
                myJump.start();
                swStarted = true;
                stopWatchHandler.postDelayed(stopWatchRunnable, 1000L);
            } catch (IOException e) {
                e.printStackTrace();
            }

            startRepeatingTask();
        }

    }

    public void onJumpStopClicked(View v) {
        stopRepeatingTask();
        getWindow().clearFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        jumpStopB.setVisibility(View.INVISIBLE);
        pauseJumpB.setVisibility(View.INVISIBLE);
        restartB.setVisibility(View.INVISIBLE);
        resumeJumpB.setVisibility(View.INVISIBLE);
        stopWatchHandler.removeCallbacks(stopWatchRunnable);
        swStarted = false;
        sw.setVisibility(View.INVISIBLE);

        try {
            Thread.sleep(8000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.finishAffinity();
        super.finishAffinity();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("EXIT", true);
        startActivity(intent);

    }

    public void onRestartClicked(View v) {
        count = 0;
        counter.setText("0");
        startTime = System.currentTimeMillis();
        //sw.setText("00:00    0");
        sw.setText(R.string.stopWatchZero);
    }

    public void onPauseJumpClicked(View v) {
        pauseJumpB.setVisibility(View.INVISIBLE);
        resumeJumpB.setVisibility(View.VISIBLE);
        // display calories
        if (seconds != 0) {
            counter.setText(String.format("%.02f Cal.", caloriesPerMin * myweight * seconds/60));
        }

        amIRunning = false;
    }

    public void onResumeJumpClicked(View v) {
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
                    if (enableDebug) {
                        System.out.println(count);
                        System.out.println(tempJump);
                        System.out.println("fading off sound Jump size = " + tempJump.size());
                    }

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
                            if (enableDebug) {
                                System.out.println(count);
                                System.out.println(tempJump);
                                System.out.println("competing sounds Jump size = " + tempJump.size());
                            }

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
