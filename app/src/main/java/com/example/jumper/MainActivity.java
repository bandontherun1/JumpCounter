package com.example.jumper;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    protected void onBtnClicked(View v) {
        Jumper me = collectJumperData();
        if (validateJumperData())
            createJumper();


        Intent intent = new Intent(this, TrainingActivity.class);
        intent.putExtra("jumperobject", me);
        startActivity(intent);


        /*
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
        */


    }

    private Jumper collectJumperData() {
        EditText mName;
        mName = (EditText) findViewById(R.id.jumperName);
        String myName = mName.getText().toString().trim();
        if (TextUtils.isEmpty(myName)) {
            Toast t = Toast.makeText(this, "Please enter a user name", Toast.LENGTH_SHORT);
            t.show();
            mName.setError("Please enter a user name");
        }
        EditText mGender;
        mGender = (EditText) findViewById(R.id.jumperGender);
        String myGender = mGender.getText().toString();
        EditText mWeight = (EditText) findViewById(R.id.jumperWeight);
        float myWeight = Float.valueOf(mWeight.getText().toString());
        EditText mHeight = (EditText) findViewById(R.id.jumperHeight);
        float myHeight = Float.valueOf(mHeight.getText().toString());
        Jumper myJumper = new Jumper(myName, myGender, myHeight, myWeight);
        return myJumper;


    }

    private Boolean validateJumperData() {

        return Boolean.TRUE;
    }

    private int createJumper(){
        /*
        Jumpsound mysound = new Jumpsound("/");
        try {
            mysound.start();

            mysound.stop();
        } catch (IOException e) {
            e.printStackTrace();

        }
        */
        return 1;

    }
}
