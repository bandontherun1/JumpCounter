package com.example.jumper;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class MainActivity extends AppCompatActivity {
    private String myName;
    private String myGender;
    private float myHeight;
    private float myWeight;
    EditText mName;
    EditText mWeight;
    EditText mHeight;
    RadioButton mGender;
    String filename = "jumperProfile1";
    FileOutputStream fos;
    ObjectOutputStream outputStream;
    Jumper me;
    Boolean newUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent().getExtras() != null && getIntent().getExtras().getBoolean("EXIT", false)) {
            finish();
        }

        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mName = (EditText) findViewById(R.id.jumperName);
        mWeight = (EditText) findViewById(R.id.jumperWeight);
        mHeight = (EditText) findViewById(R.id.jumperHeight);
        File file = new File(this.getFilesDir(), filename);
        if (file.exists()) {
            // just display the info
            newUser = false;
            try {
                FileInputStream fis = this.openFileInput(filename);
                ObjectInputStream is = new ObjectInputStream(fis);
                try {
                    Jumper tmp = (Jumper) is.readObject();
                    me = new Jumper(tmp.name, tmp.gender, tmp.height, tmp.weight);
                    mName.setText(me.name);
                    mHeight.setText(me.height+"");
                    mWeight.setText(me.weight+"");
                    if (me.gender.equals("Male")) {
                        mGender = (RadioButton) findViewById(R.id.radioM);
                        mGender.setChecked(true);
                    }
                    else {
                        mGender = (RadioButton) findViewById(R.id.radioF);
                        mGender.setChecked(true);
                    }

                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                is.close();
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        else {
            newUser = true;
            try {
                fos = this.openFileOutput(filename, this.MODE_PRIVATE);
                outputStream = new ObjectOutputStream(fos);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radioM:
                if (checked)
                    myGender = "Male";
                    break;
            case R.id.radioF:
                if (checked)
                    myGender = "Female";
                    break;
        }
    }


    public void onBtnClicked(View v) {

        if (newUser) {

            if (!validateJumperData())
                return;

            me = collectJumperData();
        }

        Intent intent = new Intent(this, TrainingActivity.class);
        intent.putExtra("jumperobject", me);
        startActivity(intent);

    }

    private Jumper collectJumperData() {

        myName = mName.getText().toString().trim();
        myWeight = Float.valueOf(mWeight.getText().toString());
        myHeight = Float.valueOf(mHeight.getText().toString());

        Jumper myJumper = new Jumper(myName, myGender, myHeight, myWeight);
        try {
            outputStream.writeObject(myJumper);
            outputStream.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return myJumper;


    }

    private Boolean validateJumperData() {
        Boolean validation = Boolean.TRUE;
        if (mName.getText().toString().length() == 0) {
            mName.requestFocus();
            mName.setError("please enter a user name");
            validation = Boolean.FALSE;
        }
        if (mWeight.getText().toString().length() == 0) {
            mWeight.requestFocus();
            mWeight.setError("please enter a valid weight");
            validation = Boolean.FALSE;
        }
        if (mHeight.getText().toString().length() == 0) {
            mHeight.requestFocus();
            mHeight.setError("please enter a valid height");
            validation = Boolean.FALSE;
        }

        return validation;
    }

}
