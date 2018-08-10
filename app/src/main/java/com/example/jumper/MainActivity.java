package com.example.jumper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    protected void onBtnClicked(View v) {
        collectJumperData();
        if (validateJumperData())
            createJumper();



    }

    private void collectJumperData() {
        EditText mName;
        mName = (EditText) findViewById(R.id.jumperName);
        String myName = mName.getText().toString();
        EditText mGender;
        mGender = (EditText) findViewById(R.id.gender);
        String myGender = mGender.getText().toString();
        EditText mWeight = (EditText) findViewById(R.id.jumperWeight);
        float myWeight = Float.valueOf(mWeight.getText().toString());
        EditText mHeight = (EditText) findViewById(R.id.jumperHeight);
        float myHeight = Float.valueOf(mHeight.getText().toString());


    }

    private Boolean validateJumperData() {

        return Boolean.TRUE;
    }

    private int createJumper(){
        return 1;

    }
}
