package com.example.jumper;/*
 * com.example.jumper.Jumper class
 * com.example.jumper.Jumper is created when user input height and weight data and it should persist app relaunch
 */

import com.example.jumper.Jumpsound;

import java.io.Serializable;
import java.util.Date;

class Jumper implements Serializable {
    String name;
    String gender;
    float height; // jumper's height
    float weight; // jumper's weight

    float jumpingspeed; // jumps per minute
    float caloriespermin;
    float caloriestotal; // = jumpingspeed x caloriespermin
    int   totaljumps;

    Jumpsound mjumpsound; // recorded jumpsound data
    Date  startTime; // when jumper starts counting
    Date  endTime; // when jumper stops counting

    public Jumper(String name, String gender, float height, float weight) {
        this.name = name;
        this.gender = gender;
        this.height = height;
        this.weight = weight;
    }


    void set(float caloriesunit) {
        this.caloriespermin = caloriesunit;
    }





}
