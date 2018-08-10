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

    protected Jumper onBtnClicked(View v) {
        Jumper me = collectJumperData();
        if (validateJumperData())
            createJumper();

        return me;


    }

    private Jumper collectJumperData() {
        EditText mName;
        mName = (EditText) findViewById(R.id.jumperName);
        String myName = mName.getText().toString();
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
        return 1;

    }
}
