package com.example.jumper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    protected void onBtnClicked() {
        collectJumperData();
        if (validateJumperData())
            createJumper();



    }

    private void collectJumperData() {

    }

    private Boolean validateJumperData() {

        return Boolean.TRUE;
    }

    private int createJumper(){
        return 1;

    }
}
